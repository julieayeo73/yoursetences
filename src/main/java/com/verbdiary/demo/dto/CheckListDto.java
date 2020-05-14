package com.verbdiary.demo.dto;

import java.util.List;

import com.verbdiary.demo.domain.entity.CheckList;
import com.verbdiary.demo.domain.entity.Diary;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CheckListDto {

    private Long id;
    private Boolean hasChecked;
    private String sentence;
    private Diary diary;
    private List<String> checked;
    private List<CheckList> checkLists;

    public CheckList toEntity(){
        CheckList checkList = CheckList.builder()
                .id(id)
                .hasChecked(hasChecked)
                .sentence(sentence)
                .diary(diary)
                .build();
        return checkList;

        
    }

    @Builder
    public CheckListDto(Long id, Boolean hasChecked, String sentence, Diary diary) {
        this.id = id;
        this.hasChecked = hasChecked;
        this.sentence = sentence;
        this.diary = diary;
    }
}