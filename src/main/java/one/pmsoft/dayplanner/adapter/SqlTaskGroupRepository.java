package one.pmsoft.dayplanner.adapter;

import one.pmsoft.dayplanner.model.TaskGroup;
import one.pmsoft.dayplanner.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup,Integer> {

    @Override
    @Query("from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();
}
