package yve.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students")
public class Student {
    @Id
    private String ulearnId;
    private String name;
    private String surname;
    private String email;
    private String studentGroup;
    private String sex;
    private String country;
    private String city;
    private String birthdate;
    private String university;
    private String faculty;
    private String vkId;
    private boolean isClosed;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Progress> progresses = new ArrayList<>();
}