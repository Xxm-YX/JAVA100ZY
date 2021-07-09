package com.example.demo.lock.lockscope;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class Interesting {

    volatile int a = 1;
    volatile int b = 1;

    public synchronized void add(){
        log.info("add start");
        for (int i = 0; i < 10000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

    public void compare(){
        log.info("compare start");
        for (int i = 0; i < 10000; i++) {
            //a始终等于b吗？  
            if (a < b) {
                log.info("a:{},b:{},{}", a, b, a > b);
            //最后的a>b应该始终是false吗？
            }
        }
        log.info("compare done");
    }
    
    public synchronized void compareRight(){
        log.info("compare start");
        for (int i = 0; i < 1000000; i++) {
            Assert.isTrue(a == b,"不等于");
            if(a < b){//这里其实是分为三步，加载a，加载b，比较
                log.info("a:{},b:{},{}", a, b, a > b);
            }
        }
        log.info("compare done");
    }

    public static void main(String[] args) {
            Interesting interesting = new Interesting();
            new Thread(() -> interesting.add()).start();
            new Thread(() -> interesting.compareRight()).start();
    }
}
