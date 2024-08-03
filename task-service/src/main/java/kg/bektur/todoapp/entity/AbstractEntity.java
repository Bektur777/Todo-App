package kg.bektur.todoapp.entity;

import jakarta.persistence.*;
import kg.bektur.todoapp.enumuration.EntityStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
@Data
public abstract class AbstractEntity implements Serializable {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated", insertable = false)
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    EntityStatus entityStatus;

    @PrePersist
    public void toCreate() {
        setEntityStatus(EntityStatus.ACTIVE);
        setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void toUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }
}