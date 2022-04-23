package cc.lexur.lexurtimemanager.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private LiveData<List<Task>> allTasksLive;
    private LiveData<List<Label>> allLabelsLive;
    private TaskDao taskDao;
    private LabelDao labelDao;

    public TaskRepository(Context context) {
        //获取数据库实例（唯一）
        TaskDatabase taskDatabase = TaskDatabase.getInstance(context);
        //获取数据库操作dao的实例
        taskDao = taskDatabase.getTaskDao();
        labelDao = taskDatabase.getLabelDao();
        //LiveData格式的数据在获取时,系统自动会调用Async来处理
        allTasksLive = taskDao.getAllTasks();
        allLabelsLive = labelDao.getAllLabels();

    }

    // 为实现AsyncTask静态内部类提供访问的接口

    // Task相关
    public void insertTasks(Task... tasks) {
        new InsertAsyncTask(taskDao).execute(tasks);
    }

    public void updateTasks(Task... tasks) {
        new UpdateAsyncTask(taskDao).execute(tasks);
    }

    public void deleteTasks(Task... tasks) {
        new DeleteAsyncTask(taskDao).execute(tasks);
    }

    public void clearTasks() {
        new ClearAsyncTask(taskDao).execute();
    }

    public List<Task> getTasksByLabel(Label label) {
        List<Task> tasks = new ArrayList<>();
        try {
            tasks = new GetTasksByLabelIdAsyncTask(taskDao).execute(label.getId()).get();
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return tasks;
        }
    }

    public Task getTaskByID(int id) {
        Task task;
        try {
            task = new GetTaskByIdAsyncTask(taskDao).execute(id).get();
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查询方法默认异步线程
    public LiveData<List<Task>> getAllTasksLive() {
        return allTasksLive;
    }

    // Label相关
    public LiveData<List<Label>> getAllLabelsLive() {
        return allLabelsLive;
    }

    public List<Label> getLabelByName(String name) {
        try {
            List<Label> labels = new GetLabelByNameAsyncTask(labelDao).execute(name).get();
            return labels;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LexurLog", "getLabelByName: 获取Label发生错误");
            return new ArrayList<Label>();
        }
    }

    public void insertLabels(Label... labels) {
        new InsertAsyncLabel(labelDao).execute(labels);
    }

    public void updateLabels(Label... labels) {
        new UpdateAsyncLabel(labelDao).execute(labels);
    }

    public void deleteLabels(Label... labels) {
        new DeleteAsyncLabel(labelDao).execute(labels);
    }

    //把对数据库的操作封装到实现AsyncTask的类中，因为Room对数据库的操作是耗时操作，不允许在主线程中执行。
    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao dao;

        public InsertAsyncTask(TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            dao.insertTasks(tasks);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao dao;

        public UpdateAsyncTask(TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            dao.updateTasks(tasks);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao dao;

        public DeleteAsyncTask(TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            dao.deleteTasks(tasks);
            return null;
        }
    }

    private static class ClearAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao dao;

        public ClearAsyncTask(TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllTasks();
            return null;
        }
    }

    private static class InsertAsyncLabel extends AsyncTask<Label, Void, Void> {
        private LabelDao dao;

        public InsertAsyncLabel(LabelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Label... labels) {
            dao.insertLabels(labels);
            return null;
        }
    }

    private static class UpdateAsyncLabel extends AsyncTask<Label, Void, Void> {

        private LabelDao dao;

        public UpdateAsyncLabel(LabelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Label... labels) {
            dao.updateLabels(labels);
            return null;
        }
    }

    private static class DeleteAsyncLabel extends AsyncTask<Label, Void, Void> {

        private LabelDao dao;

        public DeleteAsyncLabel(LabelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Label... labels) {
            dao.deleteLabels(labels);
            return null;
        }
    }

    private static class GetLabelByNameAsyncTask extends AsyncTask<String, Void, List<Label>> {
        private LabelDao dao;

        public GetLabelByNameAsyncTask(LabelDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<Label> doInBackground(String... strings) {
            List<Label> labels = dao.getLabelByName(strings[0]);
            return labels;
        }
    }

    private class GetTasksByLabelIdAsyncTask extends AsyncTask<Integer, Void, List<Task>> {
        TaskDao dao;

        public GetTasksByLabelIdAsyncTask(TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<Task> doInBackground(Integer... integers) {
            List<Task> tasks = dao.getTasksByLabelId(integers[0]);
            return tasks;
        }
    }

    private class GetTaskByIdAsyncTask extends AsyncTask<Integer, Void, Task> {
        TaskDao dao;

        public GetTaskByIdAsyncTask(TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected Task doInBackground(Integer... integers) {
            Task task = dao.getTaskById(integers[0]);
            return task;
        }
    }
}
