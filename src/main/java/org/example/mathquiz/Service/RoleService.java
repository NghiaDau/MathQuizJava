package org.example.mathquiz.Service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.Role;
import org.example.mathquiz.Repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private IRoleRepository roleRepository;

    public Role findById(String id){
        return roleRepository.findFirstById(id);
    }
}
