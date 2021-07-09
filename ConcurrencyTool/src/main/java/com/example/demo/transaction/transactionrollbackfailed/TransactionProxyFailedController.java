//package com.example.demo.transaction.transactionrollbackfailed;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("transactionrollbackfailed")
//@Slf4j
//public class TransactionProxyFailedController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("wrong1")
//    public void wrong1(@RequestParam("name")String name){
//        userService.createUserWrong1(name);
//    }
//
//    @GetMapping("wrong2")
//    public void wrong2(@RequestParam("name")String name) throws IOException {
//        userService.createUserWrong2(name);
//    }
//
//    @GetMapping("right1")
//    public void right1(@RequestParam("name")String name){
//        userService.createUserRight(name);
//    }
//
//    @GetMapping("right2")
//    public void right2(@RequestParam("name")String name) throws IOException {
//        userService.createUserRight2(name);
//    }
////
////    @GetMapping("wrong3")
////    public int wrong3(@RequestParam("name")String name){
////        return userService.createUserWrong3(name);
////    }
//}
