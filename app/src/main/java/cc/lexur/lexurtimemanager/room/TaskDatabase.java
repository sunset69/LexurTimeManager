package cc.lexur.lexurtimemanager.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class},version = 1,exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    //singleton
    private static TaskDatabase taskDatabase;

    public static synchronized TaskDatabase getInstance(Context context){
        if (taskDatabase == null){
            taskDatabase = Room.databaseBuilder(context,TaskDatabase.class,"task_database.db").build();
        }
        return taskDatabase;
    }

    public abstract TaskDao getTaskDao();

}
