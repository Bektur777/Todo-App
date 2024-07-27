package kg.bektur.todoapp.entity;

import jakarta.persistence.*;
import kg.bektur.todoapp.enumuration.Role;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("status <> 'DELETED'")
@Entity
@Table(name = "todo_user")
public class User extends AbstractEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;
}
