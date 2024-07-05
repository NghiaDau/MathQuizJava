package org.example.mathquiz.Controller;

import jakarta.validation.Valid;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.RequesEntities.RequesUpdateUser;
import org.example.mathquiz.RequesEntities.RequesUser;
import org.example.mathquiz.RequesEntities.RequestChangePassUser;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                             @RequestParam("photo") MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes){
        User use= userService.addNewUser(requesUser,multipartFile);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm tài khoản thành công");
        return "redirect:/user";
    }

    @GetMapping("/user/edit/{id}")
    public String edit(@PathVariable String id, Model model,RedirectAttributes redirectAttributes) {
        model.addAttribute("user", userService.findById(id));
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
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

    @GetMapping("/user/profile/{id}")
    public String profile(Model model,@PathVariable String id){
        model.addAttribute("user", userService.findById(id));
        return "/user/profile";
    }
    @PostMapping("/user/save_profile")
    public String saveProfile(RequesUpdateUser requesUpdateUser,
                              @RequestParam("photo") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes) {
        User user1 = userService.UpdateUser(requesUpdateUser,multipartFile);
        UserDetails userDetails = userService.loadUserByUsername(user1.getUsername());
        userService.updatePrincipal(user1);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        return "redirect:/user/profile/"+user1.getId();
    }

    @GetMapping("/user/change_password")
    public String ChangePassword(@AuthenticationPrincipal User user, Model model) {
        RequestChangePassUser requestChangePassUser = new RequestChangePassUser();
        requestChangePassUser.setId(user.getId());
        model.addAttribute("user", requestChangePassUser);
        return "/user/change_password";
    }

    @PostMapping("/user/save_change_password")
    public String ChangePassword_Submit(@AuthenticationPrincipal User user,
                                        @ModelAttribute("user") RequestChangePassUser requestChangePassUser,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        try {
            if(userService.checkPass(user, requestChangePassUser.getOldPassword())){
                userService.ChangePassword(user,requestChangePassUser.getNewPassword());
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
                return "redirect:/user";
            }
            else{
                redirectAttributes.addFlashAttribute("errormessage"," Mật khẩu cũ không hợp lệ");
                return "redirect:/user/change_password";
            }
        } catch (Exception e) {
            return "redirect:/user/change_password";
        }
    }

    @GetMapping("/forgotpassword")
    public String ForgotPassword() {
        return "/user/forgot_password";
    }

    @PostMapping("/forgot_password")
    public String ForgotPassword_Submit(@RequestParam("email") String email,RedirectAttributes redirectAttributes) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            user = userService.createTokenResetPassword(user);
            String token = user.getResetPasswordToken();
            String URL = "http://localhost:8080/reset_password?token=" + token;
            userService.SendMail(user.getEmail(), URL,user.getUsername());
            redirectAttributes.addFlashAttribute("message","Vui lòng kiểm tra email để thay đổi mật khẩu!");
            return "redirect:/forgotpassword";
        }
        redirectAttributes.addFlashAttribute("message","Email không tồn tại");
        return "redirect:/forgotpassword";
    }

    @GetMapping("/reset_password")
    public String ResetPassword(@Param("token") String token, Model model,RedirectAttributes redirectAttributes) {
        User user = userService.findUserByResetPasswordToken(token);
        if (user == null) {
            return "redirect:/forgotpassword";
        } else {
            if (user.getResetPasswordTokenExpired().getTime() > System.currentTimeMillis()) {
                model.addAttribute("token", token);
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
                return "/user/reset_password";
            } else {
                return "/user/fail_token";
            }

        }
    }

    @PostMapping("/reset_password")
    public String ResetPassword_Submit(@RequestParam("token") String token, @RequestParam("password") String password) {
        User user = userService.findUserByResetPasswordToken(token);
        if (user == null) {
            return "redirect:/forgotpassword";
        } else {
            userService.updateResetPasswordToken(user, password);
            return "redirect:/login";
        }
    }


}
