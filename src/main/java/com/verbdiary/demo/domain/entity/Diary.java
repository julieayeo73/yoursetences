package com.verbdiary.demo.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "diary")
public class Diary {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column (nullable=false, unique=true)
    private String code;

    @Column
    private String createdDate;
    
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	private List<CheckList> checkList;

    @Builder
    public Diary(Long id, String email, String createdDate, String content, String code, List<CheckList> checkList) {
        this.id = id;
        this.email = email;
        this.content = content;
        this.code = code;
        this.createdDate = createdDate;
        this.checkList = checkList;
    }
}