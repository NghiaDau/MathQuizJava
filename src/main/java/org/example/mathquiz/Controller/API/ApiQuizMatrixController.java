package org.example.mathquiz.Controller.API;

import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Entities.Result;
import org.example.mathquiz.Service.QuizMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizmatrices")
public class ApiQuizMatrixController {
   @Autowired
   private QuizMatrixService quizMatrixService;
   @GetMapping("/newest")
   public List<Object[]> getNewestQuizMatrix() {
      List<Object[]> ls = quizMatrixService.findNewestQuizMatrix().stream().limit(10).toList();
      return ls;
   }
   @GetMapping("/all/{id}")
   public List<Object[]> findQuizMatrixResultByQuizMatrixId(@PathVariable String id) {
      List<Object[]> lr = quizMatrixService.findQuizMatrixResultByQuizMatrixId(id).stream().limit(5).toList();
      return lr;
   }
   @GetMapping("/user/{id}")
   public List<Object[]> findQuizMatrixResultByQuizMatrixIdAndUserName(@PathVariable String id) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String username;
      if (principal instanceof UserDetails) {
         username = ((UserDetails) principal).getUsername();
      } else {
         username = principal.toString();
      }
      List<Object[]> lr = quizMatrixService.findQuizMatrixResultByQuizMatrixIdAndUserName(id,username).stream().limit(5).toList();
      return lr;
   }
}
