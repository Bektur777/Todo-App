package kg.bektur.todoapp.entity;

import jakarta.persistence.*;
import kg.bektur.todoapp.enumuration.TaskStatus;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("status <> 'DELETED'")
@Entity
@Table(name = "task")
public class Task extends AbstractEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus status;
}

