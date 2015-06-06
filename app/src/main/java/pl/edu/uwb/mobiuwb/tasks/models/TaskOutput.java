package pl.edu.uwb.mobiuwb.tasks.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public abstract class TaskOutput
{
    private List<String> errors;

    protected TaskOutput()
    {
        errors = new ArrayList<String>();
    }

    public void addError(String errorMessage)
    {
        errors.add(errorMessage);
    }

    public void addErrors(List<String> errorMessages)
    {
        errors.addAll(errorMessages);
    }

    public List<String> getErrors()
    {
        return Collections.unmodifiableList(errors);
    }

    public boolean isValid()
    {
        return errors.isEmpty();
    }
}
