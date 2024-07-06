package org.example.mathquiz.Service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.Repositories.IRoleRepository;
import org.example.mathquiz.Repositories.UserRepository;
import org.example.mathquiz.RequesEntities.RequestUpdateUser;
import org.example.mathquiz.RequesEntities.RequestUser;
import org.example.mathquiz.Utilities.FileUtils;
import org.example.mathquiz.Utilities.RandomUtils;
import org.example.mathquiz.constants.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender emailSender;

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }


    public User addNewUser(RequestUser requesUser, MultipartFile multipartFile) {
        try {
            User user = new User();
            user.setUserName(requesUser.getUserName());
            user.setEmail(requesUser.getEmail());
            user.setFullName(requesUser.getFullName());
            user.setAvatarUrl(requesUser.getAvatarUrl());
            user.setPasswordHash(requesUser.getPasswordHash());
            user.setPhoneNumber(requesUser.getPhoneNumber());
            Date date = new Date(System.currentTimeMillis());
            user.setCreateDate(date);
            user.setAvatarUrl(FileUtils.saveFile(multipartFile));
            userRepository.save(user);
            return user;
        } catch (Exception ex) {
            throw new RuntimeException("loi add");
        }
    }

    public User findById(String id) {
        return userRepository.findFirstById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User UpdateUser(RequestUpdateUser requestUpdateUser, MultipartFile multipartFile) {
        try {
            User user = userRepository.findFirstById(requestUpdateUser.getId());
            user.setFullName(requestUpdateUser.getFullName());
            user.setPhoneNumber(requestUpdateUser.getPhoneNumber());
            user.setEmail(requestUpdateUser.getEmail());
            Date date = new Date(System.currentTimeMillis());
            user.setCreateDate(date);
            if (!multipartFile.isEmpty())
                user.setAvatarUrl(FileUtils.saveFile(multipartFile));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void save(@NotNull RequestUser requesUser) {
        try {
            User user = new User();
            user.setUserName(requesUser.getUserName());
            user.setEmail(requesUser.getEmail());
            user.setPasswordHash(new BCryptPasswordEncoder()
                    .encode(requesUser.getPasswordHash()));
        userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRole(String username) {
        userRepository.findUserByUserName(username)
                .getRoles()
                .add(roleRepository
                        .findFirstById(Role.USER.value));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(userName);
        return user;
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .authorities(user.getAuthorities())
//                .accountExpired(false)
//                .accountLocked(false)
//                .credentialsExpired(false)
//                .disabled(false)
//                .build();
    }

    public void updatePrincipal(User user) {
        UserDetails userDetails = user;
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public User ChangePassword(User user, String password) {
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        return userRepository.save(user);
    }

    public boolean checkPass(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPasswordHash());
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createTokenResetPassword(User user) {
        String token = RandomUtils.generateRandomString(45);
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpired(new Date(System.currentTimeMillis() + 10 * 60 * 1000));
        return userRepository.save(user);
    }

    public User findUserByResetPasswordToken(String token) {
        return userRepository.findByToken(token);
    }

    public User updateResetPasswordToken(User user, String password) {
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        user.setResetPasswordToken("");
        user.setResetPasswordTokenExpired(null);
        return userRepository.save(user);
    }


    public void SendMail(String DesMail, String URL, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("todream@gmail.com");
        message.setTo(DesMail);
        message.setSubject("Reset Your Password");
        String emailContent =
                "Hello, " + username + "\n" +
                        "You have requested to reset your password.\n" +
                        "Click the link below to change your password:" + URL + "\n" +
                        "Ignore this email if you remember your password or you have not made this request.\n" +
                        "Thanh you!";
        message.setText(emailContent);
        emailSender.send(message);
    }
}

