package com.verbdiary.demo.service;

import com.verbdiary.demo.domain.entity.CheckList;
import com.verbdiary.demo.domain.entity.Diary;
import com.verbdiary.demo.domain.repository.CheckListRepository;
import com.verbdiary.demo.domain.repository.DiaryRepository;
import com.verbdiary.demo.dto.CheckListDto;
import com.verbdiary.demo.dto.DiaryDto;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class DiaryService {
    private DiaryRepository diaryRepository;
    private CheckListRepository checkListRepository;
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "remind.your.idea@gmail.com";

    @Transactional
    public Long saveDiary(DiaryDto diaryDto) {
        String codeS = RandomStringUtils.randomAlphanumeric(5);
        System.out.println(codeS);
        diaryDto.setCode(codeS);
        
        return diaryRepository.save(diaryDto.toEntity()).getId();
    }

    @Transactional
    public DiaryDto getDiary(Long id) {
        Optional<Diary> diaryEntityWrapper = diaryRepository.findById(id);

        Diary diaryEntity = diaryEntityWrapper.get();

        DiaryDto diaryDTO = DiaryDto.builder().id(diaryEntity.getId())
        .email(diaryEntity.getEmail())
        .content(diaryEntity.getContent())
        .code(diaryEntity.getCode())
        .createdDate(diaryEntity.getCreatedDate())
        .checkList(diaryEntity.getCheckList())
        .build();

        return diaryDTO;
    }

    public void sendMail(DiaryDto dDto, String checkedSt) throws ParseException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dDto.getEmail());
        message.setFrom(FROM_ADDRESS);
        message.setSubject("과거로부터 온 당신의 문장\u2709");
        message.setText( dDto.getCreatedDate() + "의 당신이 지금의 당신에게 \r\n\r\n"
                        + "           \u2728 \r\n\r\n"
                        + checkedSt + "\r\n"
                        + "           \u2728 \r\n\r\n"
                        + "언제든 적고, 리마인드 하세요.\r\n"
                        + "과거의 당신이 지금의 당신을 찾아옵니다.\r\n\r\n"
                        + " ------------------------------------ \r\n\r\n  " 
                        + "확인 및 삭제를 위해서는 입력했던 이메일과 코드가 필요합니다. "
                        + "Code : " + dDto.getCode()  + "\r\n");
                        

        mailSender.send(message);   
    }

    @Transactional
    public DiaryDto searchDiary(String email, String code) {

        Diary dEtt = diaryRepository.findByEmailAndCode(email, code);
        DiaryDto dDto = new DiaryDto();

        if(dEtt!=null){ dDto = this.convertEntityToDto(dEtt); 
            System.out.println("dto is here" + dDto.toString());
        }

        System.out.println("dto 1" + dDto.toString());
        System.out.println("dto 2" + dDto.toString() + " "  + dDto.getId()  +  " " + dDto.getContent() );

        return dDto; 
    }

    @Transactional
    public List<CheckListDto> searchCheckList(DiaryDto diary) {
        List<CheckList> checkLists = checkListRepository.findByDiary(diary.toEntity());
        List<CheckListDto> checkListDtos = new ArrayList<CheckListDto>();

        for(CheckList cl : checkLists){
            
            checkListDtos.add(
            CheckListDto.builder()
            .diary(diary.toEntity())
            .sentence(cl.getSentence())
            .hasChecked(cl.getHasChecked())
            .id(cl.getId())
            .build());

            System.out.print("getcheckList start" + cl.getSentence());
        }
        
        return checkListDtos;
    }
    

    private DiaryDto convertEntityToDto(Diary diaryEntity) {
        return DiaryDto.builder()
                .id(diaryEntity.getId())
                .email(diaryEntity.getEmail())
                .content(diaryEntity.getContent())
                .code(diaryEntity.getCode())
                .createdDate(diaryEntity.getCreatedDate())
                .checkList(diaryEntity.getCheckList())
                .build();
    }

    private CheckListDto checkListEntityToDto(CheckList checkList) {
        return CheckListDto.builder()
                .id(checkList.getId())
                .hasChecked(checkList.getHasChecked())
                .sentence(checkList.getSentence())
                .diary(checkList.getDiary())
                .build();
    }


    @Transactional
    public void saveCheckList(Long id, CheckListDto clDto) {
        Optional<Diary> diaryEntityWrapper = diaryRepository.findById(id);
        Diary diary = diaryEntityWrapper.get();
        System.out.println( "savechecklist diary ID "+ id);
        System.out.println(diary);
        clDto.setDiary(diary);
        checkListRepository.save(clDto.toEntity());
        
    }

    @Transactional
    public void doneCheckList(Long id, String checkedList, String notCheckedList) {

        List<String> ckList = Arrays.asList(checkedList.split("[,]"));
        List<String> nckList = Arrays.asList(notCheckedList.split("[,]"));

        if(!checkedList.equals("")) {
            //1. update checked list
            for(String s : ckList) {
                Long ckId = Long.parseLong(s);
                Optional<CheckList> entityWrapper = checkListRepository.findById(ckId);
                CheckList checkList = entityWrapper.get();
                CheckListDto ckDto = this.checkListEntityToDto(checkList);
                ckDto.setHasChecked(true);
                checkListRepository.save(ckDto.toEntity());
            }
        }
        if(!notCheckedList.equals("")) {   
            for(String s : nckList){
                Long ckId = Long.parseLong(s);
                Optional<CheckList> entityWrapper = checkListRepository.findById(ckId);
                CheckList checkList = entityWrapper.get();
                CheckListDto ckDto = this.checkListEntityToDto(checkList);
                ckDto.setHasChecked(false);
                checkListRepository.save(ckDto.toEntity());
            }
        }
    }

    @Transactional
    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }

}