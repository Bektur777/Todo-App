package kg.bektur.todoapp.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kg.bektur.todoapp.entity.Task;
import kg.bektur.todoapp.enumuration.EntityStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void checkThat_TaskIsEmptyAfter_SoftDelete() {
        // given
        Task task = Task.builder()
                .title("New title")
                .description("New description")
                .build();
        taskRepository.saveAndFlush(task);

        assertThat(task.getEntityStatus()).isEqualTo(EntityStatus.ACTIVE);

        // when
        taskRepository.softDelete(task.getId());
        entityManager.clear();

        Optional<Task> byId = taskRepository.findById(task.getId());

        // then
        assertThat(byId).isEmpty();
    }
}
