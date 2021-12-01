package one.pmsoft.dayplanner.model.projection;

import one.pmsoft.dayplanner.model.Task;
import one.pmsoft.dayplanner.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_Creates_nNull_Deadline(){
        //given
        var source = new TaskGroup();
        source.setDescription("test");
        source.setTasks(Set.of(new Task("t1",null)));

        //when
        var result = new GroupReadModel(source);
        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline",null);
    }

}