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
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String nameTask;
    private String type;
    private String nameTheme;
    private Integer maxScore;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Progress> progresses = new ArrayList<>();

    @Override
    public String toString() {
        return nameTheme + "\t" + type + "\t" + nameTask + "\t" + maxScore;
    }
}
