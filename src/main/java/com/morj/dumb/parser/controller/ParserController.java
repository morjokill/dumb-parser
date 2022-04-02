package com.morj.dumb.parser.controller;

import com.morj.dumb.parser.client.SimpleClient;
import com.morj.dumb.parser.dto.WordFoundDto;
import com.morj.dumb.parser.parser.PageParser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ParserController {
    //todo: get the content of the html NODE
    private final SimpleClient simpleClient;
    private final PageParser parser;

    @GetMapping("/page")
    public String page(@RequestParam("url") String url) {
        return simpleClient.getPage(url);
    }

    @GetMapping("/content")
    public String content(@RequestParam("url") String url) {
        return parser.getPageContent(url);
    }

    @GetMapping("/sort")
    public String sort(@RequestParam("url") String url) {
        return parser.sort(url);
    }

    @GetMapping("/group")
    public List<Map.Entry<Character, Long>> group(@RequestParam("url") String url) {
        return parser.groupBySymbol(url);
    }

    @GetMapping("/find")
    public WordFoundDto find(@RequestParam("url") String url,
                             @RequestParam("word") String word) {
        return parser.findWord(url, word);
    }
}
