package pl.edu.uwb.mobiuwb.tasks;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;
import pl.edu.uwb.mobiuwb.tasks.models.TaskOutput;

/**
 * Reprezentuje pojedyncze zadanie w programie.
 */
public interface Task<Out extends TaskOutput>
{
    /**
     * Enkapsuluje logikę wykonania tego zadania.
     * Wypełnia dane wyjściowe będące parametrem.
     * @param input Dane wejściowe.
     * @param output Dane wyjściowe.
     */
    void execute(TaskInput input, Out output);
}
