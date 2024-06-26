package org.example.mathquiz.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Client extends User {

    private Date activeDate;
    private String avatarUrl;
    private Date createDate;
    private String fullName;
    private String otp;
    private Date otpExpiry;
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @OneToMany(mappedBy = "client")
    private List<Exams> exams;

    @OneToMany(mappedBy = "client")
    private List<Result> results;

}
