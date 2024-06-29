package org.example.mathquiz.Controller;

import org.example.mathquiz.Entities.User;
import org.example.mathquiz.RequesEntities.RequesUpdateUser;
import org.example.mathquiz.RequesEntities.RequesUser;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/new")
    public String showFormAdd(Model model){
        model.addAttribute("user",new RequesUser());
        return "user/new";
    }


    @PostMapping("/add_new")
    public String addNewUser(@ModelAttribute("user") RequesUser requesUser,
                             Model model,
                             @RequestParam("photo") MultipartFile multipartFile){
        User use= userService.addNewUser(requesUser,multipartFile);
        return "redirect:/user";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user/edit";
    }
    @PostMapping("/save_change")
    public String saveChange(RequesUpdateUser requesUpdateUser,@RequestParam("photo") MultipartFile multipartFile) {
        userService.UpdateUser(requesUpdateUser,multipartFile);
        return "redirect:/user";
    }
}
