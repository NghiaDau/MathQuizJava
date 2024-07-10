package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
     User findFirstById(String id);

     User findUserByUserName(String username);

     @Query("select u from User u where u.email=?1")
     User findByEmail(String email);

     @Query("select u from User u where u.resetPasswordToken=?1")
     User findByToken(String token);

     User findByUserName(String userName);
     @Query("select u from User u where u.phoneNumber=?1")
     User findByPhone(String phone);

     @Query("select u.userName,count(u) as testTaken " +
             "from User u inner join Result r on u.id = r.user.id " +
             "where YEAR(r.startTime) = YEAR(current_date) and month(r.startTime) = month(current_date) and day(r.startTime) = day(current_date)"+
             "group by u.userName order by testTaken desc ")
     List<Object[]> findUsersByDay();

     @Query("select u.userName,count(u) as testTaken " +
             "from User u inner join Result r on u.id = r.user.id " +
             "where YEAR(r.startTime) = YEAR(current_date) and month(r.startTime) = month(current_date)"+
             "group by u.userName order by testTaken desc ")
     List<Object[]> findUsersByMonth();

     @Query("select u.userName,count(u) as testTaken " +
             "from User u inner join Result r on u.id = r.user.id " +
             "where YEAR(r.startTime) = YEAR(current_date)"+
             "group by u.userName order by testTaken desc")
     List<Object[]> findUsersByYear();
}
