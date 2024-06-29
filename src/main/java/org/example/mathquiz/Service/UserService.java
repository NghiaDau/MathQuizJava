package org.example.mathquiz.Service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.Repositories.IRoleRepository;
import org.example.mathquiz.Repositories.UserRepository;
import org.example.mathquiz.RequesEntities.RequesUpdateUser;
import org.example.mathquiz.RequesEntities.RequesUser;
import org.example.mathquiz.Utilities.FileUtils;
import org.example.mathquiz.constants.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;
    public List<User> getAllUser(){
        List<User> users = userRepository.findAll();
        return users;
    }


    public User addNewUser(RequesUser requesUser,MultipartFile multipartFile){
        try{
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
        }catch (Exception ex){
            throw new RuntimeException("loi add");
        }
    }

    public User findById(String id){
        return  userRepository.findFirstById(id);
    }

    public User UpdateUser( RequesUpdateUser requesUpdateUser,MultipartFile multipartFile) {
        try {
            User user = userRepository.findFirstById(requesUpdateUser.getId());
            user.setFullName(requesUpdateUser.getFullName());
            user.setPhoneNumber(requesUpdateUser.getPhoneNumber());
            user.setEmail(requesUpdateUser.getEmail());
            if(!multipartFile.isEmpty())
                user.setAvatarUrl(FileUtils.saveFile(multipartFile));
            return  userRepository.save(user);
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void save(@NotNull RequesUser requesUser) {
        User user = new User();
        user.setUserName(requesUser.getUserName());
        user.setEmail(requesUser.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder()
                .encode(requesUser.getPasswordHash()));
        userRepository.save(user);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRole(String username){
        userRepository.findUserByUserName(username)
                .getRoles()
                .add(roleRepository
                        .findFirstById(Role.USER.value));
    }
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(userName);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}

