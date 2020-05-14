package com.verbdiary.demo.dto;

import java.util.List;

import com.verbdiary.demo.domain.entity.CheckList;
import com.verbdiary.demo.domain.entity.Diary;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryDto {

    private Long id;
    private String email;
    private String content;
    private String code;
    private String createdDate;
    private List<CheckList> checkList;

    public Diary toEntity(){
        Diary diary = Diary.builder()
                .id(id)
                .email(email)
                .content(content)
                .code(code)
                .createdDate(createdDate)
                .checkList(checkList)
                .build();
        return diary;
    }

    @Builder
    public DiaryDto(Long id, String sentences, String email, String createdDate, String content, String code, List<CheckList> checkList) {
        this.id = id;
        this.email = email;
        this.content = content;
        this.code = code;
        this.createdDate = createdDate;
        this.checkList = checkList;
    }
}