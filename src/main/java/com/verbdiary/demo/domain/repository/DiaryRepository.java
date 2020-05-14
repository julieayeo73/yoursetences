package com.verbdiary.demo.domain.repository;

import com.verbdiary.demo.domain.entity.Diary;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public interface DiaryRepository extends JpaRepository<Diary, Long>  {
    Diary findByEmailAndCode(String email, String Code);
}