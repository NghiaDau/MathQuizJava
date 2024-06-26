package org.example.mathquiz.Service;

import org.example.mathquiz.Entities.User;
import org.example.mathquiz.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;
    public List<User> getAllUser(){
        List<User> users = userRepository.findAll();
        return users;
    }
}
