package com.verbdiary.demo.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "checkList")
public class CheckList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String sentence;

    @Column
    //@ColumnDefault("0") 
    private Boolean hasChecked;

    @ManyToOne(optional = false)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Builder
    public CheckList(Long id, Boolean hasChecked, String sentence, Diary diary){
        this.id = id;
        this.hasChecked = hasChecked;
        this.sentence = sentence;
        this.diary = diary;
    }
}