package com.example.demo.io.filestreamoperationneedclose;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class CommonMistakesApplication {

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        String payload = IntStream.rangeClosed(1,1000)
                .mapToObj(__ -> "a")
                .collect(Collectors.joining("")) + UUID.randomUUID().toString();


    }

}
