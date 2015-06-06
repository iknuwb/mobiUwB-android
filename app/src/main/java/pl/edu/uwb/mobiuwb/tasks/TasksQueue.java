package pl.edu.uwb.mobiuwb.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;
import pl.edu.uwb.mobiuwb.tasks.models.TaskOutput;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class TasksQueue <Out extends TaskOutput>
{
    private List<TaskPair> tasks;

    public TasksQueue()
    {
        tasks = new ArrayList<TaskPair>();
    }

    public void add(Task<Out> task, TaskInput taskInput)
    {
        tasks.add(new TaskPair(task,taskInput));
    }
    public void performAll(Out outputToFill)
    {
        for (TaskPair tuple : tasks)
        {
            Task<Out> task = tuple.task;
            TaskInput input = tuple.taskInput;
            task.execute(input, outputToFill);
        }
    }

    private class TaskPair
    {
        Task<Out> task;
        TaskInput taskInput;

        public TaskPair(Task<Out> task, TaskInput taskInput)
        {
            this.task = task;
            this.taskInput = taskInput;
        }
    }
}
