package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
     User findFirstById(String id);
     User findByUserName(String userName);
}
