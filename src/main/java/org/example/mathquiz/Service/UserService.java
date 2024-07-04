package org.example.mathquiz.Service;

import org.example.mathquiz.Entities.User;
import org.example.mathquiz.Repositories.UserRepository;
import org.example.mathquiz.RequesEntities.RequesUpdateUser;
import org.example.mathquiz.RequesEntities.RequesUser;
import org.example.mathquiz.Utilities.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;
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
        return userRepository.findFirstById(id);
    }
    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
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

}
