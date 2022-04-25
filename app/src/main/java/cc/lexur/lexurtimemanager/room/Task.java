package cc.lexur.lexurtimemanager.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.jar.Attributes;

import cc.lexur.lexurtimemanager.utils.DateFormat;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    int id;

    private String name;

    private String description;

    @ColumnInfo(name = "create_time")
    private Date createTime;

    private int status;

    @ColumnInfo(name = "label_id")
    private int labelId;

    @ColumnInfo(name = "start_time")
    private Date startTime;

    @ColumnInfo(name = "stop_time")
    private Date stopTime;

    @ColumnInfo(name = "priority", defaultValue = "0")
    private int priority;

    @ColumnInfo(name = "is_archive", defaultValue = "false")
    private boolean isArchive;

    @ColumnInfo(name = "archive_time")
    private boolean archiveTime;

    public Task() {
    }

    @Ignore
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 获取格式化时间
    @Ignore
    public String getFormatStartTime(){
        return DateFormat.getAllTime(startTime);
    }

    @Ignore
    public String getFormatStopTime(){
        return DateFormat.getAllTime(stopTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", labelId=" + labelId +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", priority=" + priority +
                ", isArchive=" + isArchive +
                ", archiveTime=" + archiveTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public int getStatus() {
        return status;
    }

    /**
     * status状态：
     *  0：正在进行
     *  1：延期
     *  2：放弃
     *  3：结束
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public boolean isArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(boolean archiveTime) {
        this.archiveTime = archiveTime;
    }
}
