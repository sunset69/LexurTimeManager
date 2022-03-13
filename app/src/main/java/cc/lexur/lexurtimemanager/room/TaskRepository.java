package cc.lexur.lexurtimemanager.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private LiveData<List<Task>> allTasksLive;
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
    }

    // 为实现AsyncTask静态内部类提供访问的接口
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
}
