package rdoshi.codepath.todoapp;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Date;

@Table(database = MyDatabase.class)
public class TaskItem extends BaseModel {
    @Column
    @PrimaryKey
    long id;

    @Column
    String name;

    @Column
    int priority;

    @Column
    Date due;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDueDate(Date due) {
        this.due = due;
    }

    public Date getDueDate() {
        return due;
    }
}
