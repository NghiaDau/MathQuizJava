package org.example.mathquiz.Controller;

import jakarta.validation.Valid;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.RequesEntities.RequesUpdateUser;
import org.example.mathquiz.RequesEntities.RequesUser;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user")
    public String displayAllUser(Model model){

        List<User> users = userService.getAllUser();
        System.out.println(users);
        model.addAttribute("list_user",users);
        return "user/index";
    }

    @GetMapping("/user/new")
    public String showFormAdd(Model model){
        model.addAttribute("user",new RequesUser());
        return "user/new";
    }


    @PostMapping("/user/add_new")
    public String addNewUser(@ModelAttribute("user") RequesUser requesUser,
                             Model model,
                             @RequestParam("photo") MultipartFile multipartFile){
        User use= userService.addNewUser(requesUser,multipartFile);
        return "redirect:/user";
    }

    @GetMapping("/user/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user/edit";
    }
    @PostMapping("/user/save_change")
    public String saveChange(RequesUpdateUser requesUpdateUser,@RequestParam("photo") MultipartFile multipartFile) {
        userService.UpdateUser(requesUpdateUser,multipartFile);
        return "redirect:/user";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user",new RequesUser());
        return "user/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RequesUser requesUser, Model model) {
        userService.save(requesUser);
        userService.setDefaultRole(requesUser.getUserName());
        return "redirect:/login";
    }

    @GetMapping("/user/profile")
    public String profile(Model model){

        return "/user/profile";
    }
}
