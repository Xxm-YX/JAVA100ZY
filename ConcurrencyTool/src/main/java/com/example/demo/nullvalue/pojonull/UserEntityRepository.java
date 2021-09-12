package com.example.demo.nullvalue.pojonull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}