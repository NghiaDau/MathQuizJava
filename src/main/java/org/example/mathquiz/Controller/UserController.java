package org.example.mathquiz.Controller;

import org.example.mathquiz.Entities.User;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("")
    public String displayAllUser(Model model){

        List<User> users = userService.getAllUser();
        System.out.println(users);
        model.addAttribute("list_user",users);
        return "user/index";
    }
}
