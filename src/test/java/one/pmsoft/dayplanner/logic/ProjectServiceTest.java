package one.pmsoft.dayplanner.logic;

import one.pmsoft.dayplanner.TaskConfigurationProperties;
import one.pmsoft.dayplanner.model.ProjectRepository;
import one.pmsoft.dayplanner.model.TaskGroup;
import one.pmsoft.dayplanner.model.TaskGroupRepository;
import one.pmsoft.dayplanner.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when configure to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        //given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        //To zawsze zwroci true
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //system under test
        var toTest = new ProjectService(null,mockGroupRepository,mockConfig);

        //Rozbicie na when + then, wieksza czytelnosc testu
        //when
        var exception = catchThrowable(()->toTest.createGroup(LocalDateTime.now(),0));
        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("one undone group");

        //when + then
        //Jedna z mozliwosci
        // assertThatThrownBy(()->
        //     toTest.createGroup(LocalDateTime.now(),0))
        //         .isInstanceOf(IllegalStateException.class);

        //Druga z mozliwosci
        // assertThatIllegalStateException().isThrownBy(()->toTest.createGroup(LocalDateTime.now(),0));
       // assertThatThrownBy(()-> toTest.createGroup(LocalDateTime.now(),0)).hasMessageContaining("undone group");

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProject_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty()); //findById zawsze zwroci pustego Optionala wiec wyjatek -> id not found
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository,null,mockConfig);

        //when
        var exception = catchThrowable(()->toTest.createGroup(LocalDateTime.now(),0));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ID not found");

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when configured to allow just 1 group and no groups and no projects for a given id")
    void createGroup_configurationOk_And_noProject_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty()); //findById zawsze zwroci pustego Optionala wiec wyjatek -> id not found
        //and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository,mockGroupRepository,mockConfig);

        //when
        var exception = catchThrowable(()->toTest.createGroup(LocalDateTime.now(),0));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ID not found");

    }

    @Test
    @DisplayName("Should create new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup(){
        //given
        var today = LocalDate.now().atStartOfDay(); //00:00:00
        //and
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        inMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        int countBeforeCall = inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository,inMemoryGroupRepo,mockConfig);

        //when
        GroupReadModel result = toTest.createGroup(today,1);

        //then
       // assertThat(result).
       assertThat(countBeforeCall+1)
               .isNotEqualTo(inMemoryGroupRepo.count())

    }

    private TaskGroupRepository groupRepositoryReturning(boolean b) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(b);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(boolean result) {
        //given
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private inMemoryGroupRepository inMemoryGroupRepository(){
        return new inMemoryGroupRepository();
    }

    private static class inMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer,TaskGroup> map = new HashMap<>(); //Repozytorium trzymane w pamieci

        public int count(){
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if(entity.getId() == 0){
                try {
                    TaskGroup.class.getDeclaredField("id").set(entity,++index); //Troche brutalna refleksja, nie bylo wyjscia
                }  catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(index, entity);

            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream().filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject()!=null && group.getProject().getId() == projectId);
        }
    };
}