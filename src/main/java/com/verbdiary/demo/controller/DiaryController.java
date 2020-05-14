package com.verbdiary.demo.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.verbdiary.demo.dto.*;
import com.verbdiary.demo.service.DiaryService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class DiaryController {
    private DiaryService diaryService;

    @GetMapping("/search")
    public String list() {
        return "diary/search.html";
    }

    @GetMapping("/write")
    public String write() {
        return "diary/write.html";
    }

    @PostMapping("/write")
    public String write(RedirectAttributes ra, DiaryDto diaryDto) {
        ra.addFlashAttribute("diaryDto", diaryDto);
        return "redirect:/checkList/";
    }

    @PostMapping("/diary/save")
    public String saveAll(DiaryDto dDto, CheckListDto checkListDto) throws ParseException {
        String[] sArrays = checkListDto.getChecked().toArray(new String[checkListDto.getChecked().size()]);
        String checkedSt = "";
        
        //save diary First
        Long diaryId = diaryService.saveDiary(dDto);

        //save sentences
        for(String s : sArrays){
            System.out.println(s);
            checkedSt += "# " + s + ". \r\n";
            CheckListDto ck = new CheckListDto();
            ck.setHasChecked(false);
            ck.setSentence(s);
            diaryService.saveCheckList(diaryId, ck);
        }

        //send Email
        diaryService.sendMail(dDto, checkedSt);
        
       return "redirect:/search"; 
    }


    //search
    @PostMapping("/diary/read")
    public String search(String email, String code, Model model) {
        DiaryDto dDto = diaryService.searchDiary(email, code);
        if(dDto.getId()!=null) {
            System.out.println(dDto.toString());
            List<CheckListDto> ckDto = new ArrayList<CheckListDto>();
            ckDto = diaryService.searchCheckList(dDto);
            model.addAttribute("diaryDto", dDto);
            model.addAttribute("checkList", ckDto);
            return "diary/read.html"; 
        } else  {
            System.out.println("Nothing");
            model.addAttribute("diaryDto", "");
            model.addAttribute("checkList", "");
            return "diary/read.html";
        }
    } 

    @GetMapping("/checkList/")
    public String getCheckList(@ModelAttribute DiaryDto dDto, Model model) {
        CheckListDto checkListDto = new CheckListDto();
        //1. enter
        String teststr = dDto.getContent().replaceAll("(\r|\n|\r\n|\n\r)", "");
        //2. question mark and exclamation mark
        teststr = teststr.replaceAll("[?]", ".");
        teststr = teststr.replaceAll("[!]", ".");
        //3. spilt depending  on .
        List<String> allCheckList = new ArrayList<>(Arrays.asList(teststr.split("[.]")));
        List<String> checkList = new ArrayList<String>();
        //4. remove blank
        for(Iterator<String> it = allCheckList.iterator() ; it.hasNext() ; ) {
            String value = it.next();
            if(value.length()==0) {
              it.remove();
            }
          }

        System.out.println("START" + allCheckList);
        model.addAttribute("diaryDto", dDto);
        model.addAttribute("allCheckList", allCheckList);
        model.addAttribute("checkList", checkListDto);
        model.addAttribute("checked", checkList);
        
        return "diary/checkList.html";
    }

    //done
    @PutMapping("/diary/done/{no}")
    public String checkListDone(@PathVariable("no") Long no,  
    @RequestParam("checkedList")String checkedList,
    @RequestParam("notCheckedList")String notCheckedList) {
        System.out.println(checkedList);
        System.out.println(notCheckedList);
        diaryService.doneCheckList(no, checkedList, notCheckedList);
    return "redirect:/search"; 
    }

    @DeleteMapping("/diary/delete/{no}")
    public String delete(@PathVariable("no") Long no) {
        diaryService.deleteDiary(no);
        return "redirect:/search";
    }

}
