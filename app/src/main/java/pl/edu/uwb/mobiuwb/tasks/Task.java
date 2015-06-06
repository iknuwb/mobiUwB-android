package pl.edu.uwb.mobiuwb.tasks;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;
import pl.edu.uwb.mobiuwb.tasks.models.TaskOutput;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public interface Task<Out extends TaskOutput>
{
    void execute(TaskInput input, Out output);
}
