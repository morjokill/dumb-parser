package com.morj.dumb.parser.config;

import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Configuration
public class ClientConfig {
    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder
                .create()
                .build();
    }

    @Bean
    public RequestConfig requestConfig(@Value("${client.timeout.connect}") int connectTimeout,
                                       @Value("${client.timeout.request}") int requestTimeout) {
        return RequestConfig
                .custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(connectTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .build();
    }

    @Bean
    public JedisPooled jedisPooled(@Value("${spring.redis.host}") String host,
                                   @Value("${spring.redis.port}") int port) {
        return new JedisPooled(host, port);
    }

    @Bean
    @SneakyThrows
    public List<String> userAgents(@Value("${user.agents.file}") String path) {
        return Files.readAllLines(Path.of(path));
    }
}
