package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String userName;

    private String passwordHash;
    private String email;
    private Boolean emailConfirmed;
    private String phoneNumber;
    private Date activeDate;
    private String avatarUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    private String fullName;

    @ManyToOne
    @JsonBackReference(value = "grade-user")
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @JsonManagedReference(value = "user-exam")
    @OneToMany(mappedBy = "user")
    private List<Exam> exams;

    @JsonManagedReference(value = "user-result")
    @OneToMany(mappedBy = "user")
    private List<Result> results;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
