package one.pmsoft.dayplanner.logic;

import one.pmsoft.dayplanner.model.TaskGroup;
import one.pmsoft.dayplanner.model.TaskGroupRepository;
import one.pmsoft.dayplanner.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("Should throw when undone tasks")
    //Metoda -> Stan -> Wynik dzialania
    void toggleGroup_undoneTasks_throws_IllegalStateException() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        //system under test
        var toTest = new TaskGroupService(null,mockTaskRepository);
        //when
        var exception = catchThrowable(()->toTest.toggleGroup(1));
        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("undone tasks");
    }


    @Test
    @DisplayName("Should throw when no undone tasks when no group for a given id")
    void toggleGroup_wrongID_noUndoneTasks_throws_IllegalArgumentException() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //and
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ID not found");

    }

    @Test
    @DisplayName("Should toggle group")
    void toggleGroup_worksAsExpected() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        //and
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(0);
        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);

    }


    private TaskRepository taskRepositoryReturning(boolean result) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}