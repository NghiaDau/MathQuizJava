package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_menu;
    private String name;
    private String url;
    private boolean enabled;
    @ManyToOne
    @JsonBackReference
    private Menu parent;
    @OneToMany(mappedBy = "parent")
    private List<Menu> children;
}
