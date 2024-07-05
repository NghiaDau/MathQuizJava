package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    @JsonBackReference(value = "level-grade")
    private Level level;

    private Boolean isHasMultiMathType;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "grade")
    @JsonManagedReference(value = "grade-chapter")
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "grade")
    @JsonManagedReference(value = "grade-user")
    private List<User> users;
}
