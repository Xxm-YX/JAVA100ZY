package com.example.demo.lock.deadlock;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("deadlock")
@Slf4j
public class DeadLockController {

    private ConcurrentHashMap<String,Item> items = new ConcurrentHashMap<String, Item>();

    public DeadLockController(){
        IntStream.range(0,10).forEach(i -> items.put("item" + i , new Item("item" + i)));
    }

    private List<Item> createCart() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(items.size()))
                .map(name -> items.get(name)).collect(Collectors.toList());
    }

    private boolean createOrder(List<Item> order){
        //存放所有获得的锁
        List<ReentrantLock> locks = new ArrayList<>();

        for (Item item : order) {
            try{
                //获得锁10秒超时
                if(item.lock.tryLock(10, TimeUnit.SECONDS)){
                    locks.add(item.lock);
                }else {
                    locks.forEach(ReentrantLock::unlock);
                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //锁全部拿到之后执行扣减库存业务逻辑
        try{
            order.forEach(item -> item.remaining--);
        }finally {
            locks.forEach(ReentrantLock::unlock);
        }
        return true;
    }

    @GetMapping("wrong")
    public Long wrong() {
        long begin = System.currentTimeMillis();
        //并发进行100次下单操作，统计成功次数
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart();
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
        return success;
    }

    @GetMapping("right")
    public Long right(){
        long begin = System.currentTimeMillis();
        Long success = IntStream.rangeClosed(1,100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart().stream()
                            .sorted(Comparator.comparing(Item::getName))
                            .collect(Collectors.toList());
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
        return success;
    }
}

@Data
@RequiredArgsConstructor
class Item{
    final String name;//商品名
    int remaining = 1000;//库存剩余
    @ToString.Exclude   //ToString不包含这个字段
    ReentrantLock lock = new ReentrantLock();
}
