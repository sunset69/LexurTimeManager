package cc.lexur.lexurtimemanager.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cc.lexur.lexurtimemanager.MainActivity;

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
    static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {

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

    static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {

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

    static class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {

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

    static class ClearAsyncTask extends AsyncTask<Void, Void, Void> {

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

    static class UpdateAsyncLabel extends AsyncTask<Label, Void, Void> {

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

    static class DeleteAsyncLabel extends AsyncTask<Label, Void, Void> {

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

    static class GetLabelByNameAsyncTask extends AsyncTask<String, Void, List<Label>> {
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
}
