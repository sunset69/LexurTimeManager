package cc.lexur.lexurtimemanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import cc.lexur.lexurtimemanager.room.Label;
import cc.lexur.lexurtimemanager.room.Task;
import cc.lexur.lexurtimemanager.room.TaskRepository;


public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getAllTasksLive(){
        return taskRepository.getAllTasksLive();
    }

    public void insertTasks(Task... tasks){
        taskRepository.insertTasks(tasks);
    }

    public void updateTasks(Task... tasks){
        taskRepository.updateTasks(tasks);
    }

    public void deleteTasks(Task... tasks){
        taskRepository.deleteTasks(tasks);
    }

    public void clearTasks(){
        taskRepository.clearTasks();
    }

    public LiveData<List<Label>> getAllLabelsLive(){
        return taskRepository.getAllLabelsLive();
    }

    public void insertLabels(Label... labels){
        taskRepository.insertLabels(labels);
    }

    public void updateLabels(Label... labels){
        taskRepository.updateLabels(labels);
    }

    public void deleteLabels(Label... labels){
        taskRepository.deleteLabels(labels);
    }



}
