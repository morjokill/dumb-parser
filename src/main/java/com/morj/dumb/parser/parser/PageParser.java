package com.morj.dumb.parser.parser;

import com.morj.dumb.parser.client.SimpleClient;
import com.morj.dumb.parser.dto.WordFoundDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageParser {
    private final SimpleClient simpleClient;

    public String sort(String url) {
        String pageContent = getPageContent(url);
        char[] pageArray = pageContent.toCharArray();
        Arrays.sort(pageArray);
        return new String(pageArray);
    }

    public List<Map.Entry<Character, Long>> groupBySymbol(String url) {
        String pageContent = getPageContent(url);
        return pageContent.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(o -> o, HashMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((entry, entry1) -> Long.compare(entry1.getValue(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public WordFoundDto findWord(String url, String word) {
        if (word == null || word.length() == 0) {
            return WordFoundDto.empty(word);
        }
        String pageContent = getPageContent(url);
        word = word.strip();
        if (!pageContent.contains(word)) {
            return WordFoundDto.empty(word);
        }
        StringBuilder builder = new StringBuilder(pageContent);
        long count = 0;
        List<String> entries = new ArrayList<>();
        int index;
        while ((index = builder.indexOf(word)) != -1) {
            count++;
            entries.add(getEntry(builder, word, index));
            builder.replace(index, index + word.length(), "");
        }
        return new WordFoundDto(word, count, entries);
    }

    public String getTag(String url, String tag) {
        Document document = getPageDocument(url);
        Elements elements = document.getElementsByTag(tag);
        String result = "";
        if (elements.size() > 0) {
            return elements.text();
        }
        return result;
    }

    @SneakyThrows
    public String getPageContent(String url) {
        Document document = getPageDocument(url);
        return document
                .body()
                .text();
    }

    private Document getPageDocument(String url) {
        String page = simpleClient.getPage(url);
        return Jsoup.parse(page, url);
    }

    private static String getEntry(StringBuilder builder, String word, int index) {
        int start = 0;
        int end = builder.length();
        if (index > 10) {
            start = index - 10;
        }
        if (builder.length() - index - word.length() > 10) {
            end = index + word.length() + 10;
        }
        return builder.substring(start, end);
    }
}
