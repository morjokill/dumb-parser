package com.morj.dumb.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WordFoundDto {
    private String word;
    private long count;
    private List<String> entries;

    public static WordFoundDto empty(String word) {
        return new WordFoundDto(word, 0, null);
    }
}
