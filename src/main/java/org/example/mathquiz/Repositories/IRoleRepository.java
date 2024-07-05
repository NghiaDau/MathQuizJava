package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role,String> {
    Role findFirstById(String id);
}
