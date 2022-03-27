package cc.lexur.lexurtimemanager.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTasks(Task... tasks);  //...表示可以传递多个同类型参数

    @Update
    void updateTasks(Task... tasks);

    @Delete
    void deleteTasks(Task... tasks);

    @Query("DELETE FROM Task")
        //查询语句写的清空所有数据
    void deleteAllTasks();

    //LiveData格式的数据在获取时,系统自动会调用Async来处理
    @Query("SELECT * FROM task ORDER BY id DESC")
    LiveData<List<Task>> getAllTasks();  //设置数据为可观察类型LiveData

    @Query("SELECT * FROM task where label_id = :id ORDER BY id DESC")
    List<Task> getTasksByLabelId(int id);

    @Query("SELECT * FROM task where id = :id")
    Task getTaskById(int id);
}
