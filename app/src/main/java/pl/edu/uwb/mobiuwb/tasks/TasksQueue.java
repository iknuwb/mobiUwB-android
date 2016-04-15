package pl.edu.uwb.mobiuwb.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;
import pl.edu.uwb.mobiuwb.tasks.models.TaskOutput;

/**
 * Jest to reprezentacja kolejki Zadań typu Task.
 */
public class TasksQueue <Out extends TaskOutput>
{
    /**
     * Lista zadań do wykonania.
     */
    private List<TaskPair> tasks;

    /**
     * Tworzy obiekt i wypełnia pola.
     */
    public TasksQueue()
    {
        tasks = new ArrayList<TaskPair>();
    }

    /**
     * Dodaje zadanie do kolejki, razem z towarzyszącymi zadaniu
     * danymi wejściowymi.
     * @param task Zadanie do wykonania przez tą kolejkę.
     * @param taskInput Dane wejściowe do tegoż zadania.
     */
    public void add(Task<Out> task, TaskInput taskInput)
    {
        tasks.add(new TaskPair(task,taskInput));
    }

    /**
     * Wywołuje sekwencyjnie wszystkie zadania z kolejki.
     * @param outputToFill Zadania wypełniają niniejszy parametr
     *                     danych wyjściowych.
     */
    public void performAll(Out outputToFill)
    {
        for (TaskPair tuple : tasks)
        {
            Task<Out> task = tuple.task;
            TaskInput input = tuple.taskInput;
            task.execute(input, outputToFill);
        }
    }

    /**
     * Pomocniczy typ enkapsulujący w sobie parę zadanie - dane wejściowe.
     */
    private class TaskPair
    {
        /**
         * Zadanie.
         */
        Task<Out> task;

        /**
         * Dane wejściowe.
         */
        TaskInput taskInput;

        /**
         * Wypełnia obiekty oraz tworzy instancję.
         * @param task Zadanie.
         * @param taskInput Dane wejściowe.
         */
        public TaskPair(Task<Out> task, TaskInput taskInput)
        {
            this.task = task;
            this.taskInput = taskInput;
        }
    }
}
