package org.example.mathquiz.Controller.API;

import org.example.mathquiz.Entities.Result;
import org.example.mathquiz.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ApiResultController {
    @Autowired
    private ResultService resultService;

    @GetMapping("/resultbyexamid/{examId}")
    public List<Result> findAndOrdertResultByExamId(@RequestParam String examId) {
        List<Result> lr = resultService.findAndOrderResultByExamId(examId).stream().limit(5).toList();
        return lr;
    }

    @GetMapping("/resultbyexamidandusername/{examId}")
    public List<Result> findAndOrdertResultByExamIdAndUsername(@PathVariable String examId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        List<Result> lr = resultService.findAndOrderResultByExamIdAndUserName(examId,username).stream().limit(5).toList();
        return lr;
    }
}
