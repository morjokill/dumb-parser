package com.morj.dumb.parser.client;

import com.morj.dumb.parser.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SimpleClient {
    private static final String defaultUserAgent = "Mozilla/5.0 (X11; Linux x86_64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36";

    private final HttpClient httpClient;
    private final RequestConfig config;
    private final List<String> userAgents;
    private final RedisClient redisClient;

    public String getPage(String url) {
        String uri = formatUri(url);
        String cached = redisClient.get(uri);
        if (cached != null) {
            return cached;
        }

        String page = readInputStream(getRawPage(url));
        redisClient.set(uri, page);
        return page;
    }

    @SneakyThrows
    public InputStream getRawPage(String url) {
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", getRandomUserAgent(userAgents));
        request.setConfig(config);
        return httpClient
                .execute(request)
                .getEntity()
                .getContent();
    }

    @SneakyThrows
    private static String readInputStream(InputStream inputStream) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    private static String getRandomUserAgent(List<String> userAgents) {
        if (!CollectionUtils.isEmpty(userAgents)) {
            return userAgents.get(new Random().nextInt(userAgents.size()));
        }
        return defaultUserAgent;
    }

    @SneakyThrows
    private static String formatUri(String url) {
        if (url == null || url.length() == 0) {
            throw new IllegalStateException("Fill in the URL string");
        }
        url = url.strip()
                .toLowerCase();
        if (!url.contains("https://") && !url.contains("http://")) {
            return "https://" + url;
        }
        return url;
    }
}
