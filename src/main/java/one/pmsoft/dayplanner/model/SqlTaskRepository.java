package one.pmsoft.dayplanner.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task,Integer> {

    //Ze wzgledu na zmiane z @RepositoryRestResource na @Repository i wykorzystanie powyzszej sztuczki (stworzenie TaskRepository z metodami
    // ktore maja byc udostepnione i rozszerzenie SqlTaskRepository o interface TaskRepository -> ponizsze metody nie sa potrzebne
  //  //Restricting delete request
  //  @Override
  //  @RestResource(exported = false)
  //  void deleteById(Integer integer);
//
  //  @Override
  //  @RestResource(exported = false)
  //  void delete(Task entity);

}
