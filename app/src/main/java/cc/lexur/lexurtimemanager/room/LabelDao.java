package cc.lexur.lexurtimemanager.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LabelDao {

    @Insert
    void insertLabels(Label... labels);

    @Update
    void updateLabels(Label... labels);

    @Delete
    void deleteLabels(Label... labels);

    @Query("SELECT * FROM label ORDER BY id DESC")
    LiveData<List<Label>> getAllLabels();

}
