package com.example.demo.transaction.transactionproxyfailed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService self;


    //一个公共方法供Controller调用，内部调用事务性的私有方法
    public int createUserWrong1(String name){
        try{
            this.createUserPrivate(new UserEntity(name));
        }catch (Exception ex){
            log.error("create user failed because {}", ex.getMessage());
        }
        return userRepository.findByName(name).size();
    }

    //标记了Transactional的private方法
    @Transactional(rollbackFor = Exception.class)
    private void createUserPrivate(UserEntity entity){
        userRepository.save(entity);
        if(entity.getName().contains("test")) {
            throw new RuntimeException("invalid username!");
        }
    }

    //自调用
//    @Transactional(rollbackFor = Exception.class)
    public int createUserWrong2(String name) {
//        try {
            self.createUserPublic(new UserEntity(name));
//        } /*catch (Exception ex) {
//            log.error("create user failed because {}", ex.getMessage());
//        }*/
        return userRepository.findByName(name).size();
    }

    //可以传播出异常
    @Transactional(rollbackFor = Exception.class)
    public void createUserPublic(UserEntity entity) {
        userRepository.save(entity);
        if (entity.getName().contains("test")) {
            throw new RuntimeException("invalid username!");
        }
    }

    //重新注入自己
    public int createUserRight(String name){
        try{
            self.createUserPublic(new UserEntity(name));
        }catch (Exception ex){
            log.error("create user failed because {}", ex.getMessage());
        }
        return userRepository.findByName(name).size();
    }

    //不抛出异常，不生效
    @Transactional(rollbackFor = Exception.class)
    public int createUserWrong3(String name) {
        try {
            this.createUserPublic(new UserEntity(name));
        } catch (Exception ex) {
            log.error("create user failed because {}", ex.getMessage());
        }
        return userRepository.findByName(name).size();
    }

    //根据用户名查询用户数
    public int getUserCount(String name){
        return userRepository.findByName(name).size();
    }


}
