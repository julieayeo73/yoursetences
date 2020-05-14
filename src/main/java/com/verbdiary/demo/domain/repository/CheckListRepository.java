package com.verbdiary.demo.domain.repository;

import java.util.List;

import com.verbdiary.demo.domain.entity.CheckList;
import com.verbdiary.demo.domain.entity.Diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

@EnableJpaAuditing
@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long>  {
    List<CheckList> findByDiary(Diary diary);
}