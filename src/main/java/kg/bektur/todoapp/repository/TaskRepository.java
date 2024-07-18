package kg.bektur.todoapp.repository;

import jakarta.transaction.Transactional;
import kg.bektur.todoapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.entityStatus = 'DELETED' WHERE t.id = :id")
    void softDelete(@Param("id") Long id);
}
