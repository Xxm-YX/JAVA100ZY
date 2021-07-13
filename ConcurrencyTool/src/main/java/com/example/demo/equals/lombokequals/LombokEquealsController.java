package com.example.demo.equals.lombokequals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("lombokequals")
public class LombokEquealsController {

    @Data
    class Person {
        @EqualsAndHashCode.Exclude
        private String name;
        private String identity;

        public Person(String name, String identity) {
            this.name = name;
            this.identity = identity;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class Employee extends Person {

        private String company;

        public Employee(String name, String identity, String company) {
            super(name, identity);
            this.company = company;
        }
    }
}
