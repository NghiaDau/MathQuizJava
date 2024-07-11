package org.example.mathquiz.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.CustomOAuth2User;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.RequesEntities.RequestUpdateUser;
import org.example.mathquiz.RequesEntities.RequestUser;
import org.example.mathquiz.RequesEntities.RequestChangePassUser;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {
    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Value("${recaptcha.url}")
    private String recaptchaServerURL;

    @Bean
    public static RestTemplate restTemplate(RestTemplateBuilder bulider) {
        return bulider.build();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/user")
    public String displayAllUser(Model model) {

        List<User> users = userService.getAllUser();
        System.out.println(users);
        model.addAttribute("list_user", users);
        return "user/index";
    }

    @GetMapping("/user/new")
    public String showFormAdd(Model model) {
        model.addAttribute("user", new RequestUser());
        return "user/new";
    }


    @PostMapping("/user/add_new")
    public String addNewUser(@ModelAttribute("user") RequestUser requesUser, Model model, @RequestParam("photo") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
        User use = userService.addNewUser(requesUser, multipartFile);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm tài khoản thành công");
        return "redirect:/user";
    }

    @GetMapping("/user/edit/{id}")
    public String edit(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("user", userService.findById(id));
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        return "user/edit";
    }

    @PostMapping("/user/save_change")
    public String saveChange(RequestUpdateUser requestUpdateUser, @RequestParam("photo") MultipartFile multipartFile) {
        userService.UpdateUser(requestUpdateUser, multipartFile);
        return "redirect:/user";

    }
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new RequestUser());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RequestUser requestUser, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String gRecaptchaReponse = request.getParameter("g-recaptcha-response");

        if(!verifyReCAPCHA(gRecaptchaReponse)) {
            response.reset();
            String errorMessage1 = "Please Verify captcha response";
            model.addAttribute("errorsVerify", errorMessage1);
            return "user/register";
        }
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "user/register";
        }
        userService.save(requestUser);
        userService.setDefaultRole(requestUser.getUserName());
        return "redirect:/login";
    }

    @GetMapping("/user/profile")
    public String profile(@AuthenticationPrincipal User user1,
                          @AuthenticationPrincipal CustomOAuth2User user2,
                          Model model,
                          HttpSession session) {
        User user;
        if (user1==null)
            user = userService.findByEmail(user2.getEmail());
        else
            user = userService.findById(user1.getId());
        model.addAttribute("user", user);
//        session.setAttribute("user", user);
        return "/user/profile";
    }

    @PostMapping("/user/save_profile")
    public String saveProfile(@Valid RequestUpdateUser requestUpdateUser,
                              BindingResult bindingResult,
                              @RequestParam("photo") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("userLogin");
        if (bindingResult.hasErrors() ) {
            List<String> customErrors = new ArrayList<>();
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            if (!user.getPhoneNumber().equals(requestUpdateUser.getPhoneNumber())){
                User tmpUser = userService.findByPhone(requestUpdateUser.getPhoneNumber());
                if (tmpUser != null && !tmpUser.getId().equals(user.getId())){
                    customErrors.add("Phone Đã Tồn Tại");
                }
            }
            List<String> combinedErrors = new ArrayList<>(customErrors);
            combinedErrors.addAll(errors);
            model.addAttribute("errors", combinedErrors);
            model.addAttribute("user", user);
            return "user/profile";
        }
        User user1 = userService.UpdateUser(requestUpdateUser, multipartFile);
        UserDetails userDetails = userService.loadUserByUsername(user1.getUsername());
        userService.updatePrincipal(user1);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        return "redirect:/user/profile";
    }

    @GetMapping("/user/change_password")
    public String ChangePassword(@AuthenticationPrincipal User user, Model model) {
        RequestChangePassUser requestChangePassUser = new RequestChangePassUser();
        requestChangePassUser.setId(user.getId());
        model.addAttribute("user", requestChangePassUser);
        return "/user/change_password";
    }

    @PostMapping("/user/save_change_password")
    public String ChangePassword_Submit(@AuthenticationPrincipal User user, @ModelAttribute("user") RequestChangePassUser requestChangePassUser, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean checkpass = passwordEncoder.matches(requestChangePassUser.getOldPassword(), user.getPasswordHash());
            if (checkpass) {
                userService.ChangePassword(user, requestChangePassUser.getNewPassword());
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
                return "redirect:/user";
            } else {
                redirectAttributes.addFlashAttribute("errormessage", " Mật khẩu cũ không hợp lệ");
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
    public String ForgotPassword_Submit(@RequestParam("email") String email, RedirectAttributes redirectAttributes,Model model) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            user = userService.createTokenResetPassword(user);
            String token = user.getResetPasswordToken();
            String URL = "http://localhost:8080/reset_password?token=" + token;
            userService.SendMail(user.getEmail(), URL, user.getUsername());
            model.addAttribute("message", "Vui lòng kiểm tra email để thay đổi mật khẩu!");
            return "/user/after-sendmail";
        }
        redirectAttributes.addFlashAttribute("message", "Email không tồn tại");
        return "redirect:/forgotpassword";
    }

    @GetMapping("/reset_password")
    public String ResetPassword(@Param("token") String token, Model model, RedirectAttributes redirectAttributes) {
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

    @GetMapping("/user/locker/{id}")
    public String lockAccount(@PathVariable String id) {
        userService.lockAccount(id);
        return "redirect:/user";
    }

    @GetMapping("/user/unlocker/{id}")
    public String unLockAccount(@PathVariable String id) {
        userService.unLockAccount(id);
        return "redirect:/user";
    }

    private boolean verifyReCAPCHA(String RecaptchaResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", RecaptchaResponse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ReCaptchaResponse response = restTemplate.postForObject(recaptchaServerURL, request , ReCaptchaResponse.class);

        System.out.println("Successfully recaptcha response: " + response.isSuccess());
        System.out.println("HostName: " + response.getHostname());
        System.out.println("Challenge: " + response.getChallenge_ts());
        if(response.getErrorCode() != null) {
            for (String error : response.getErrorCode()) {
                System.out.println("\t" + error);
            }
        }
        return response.isSuccess();
    }
}
