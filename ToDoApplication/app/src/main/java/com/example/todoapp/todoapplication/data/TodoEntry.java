package com.example.todoapp.todoapplication.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by qingdi on 8/22/14.
 */
@Table(name = "Todo")
public class TodoEntry extends Model implements Parcelable {
    private static final int DEFAULT_PRIORITY = 3;
    private static final int DEFAULT_DAYS_FOR_DUEDATE = 3; // 3 days after today

    @Column(name = "Description")
    public String       description;
    @Column(name = "Priority")
    public int      priority;
    @Column(name = "DueDate")
    public Date         dueDate;


    public TodoEntry(){
        super();
    }

    public TodoEntry(String description){
        super();
        this.description = description;
        this.priority = DEFAULT_PRIORITY;
        this.dueDate = getDefaultDueDate();
    }

    private Date getDefaultDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, DEFAULT_DAYS_FOR_DUEDATE); //minus number would decrement the days
        return cal.getTime();
    }

    public static List<TodoEntry> getAll() {
        // This is how you execute a query
        return new Select()
                .from(TodoEntry.class)
                .execute();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(description);
        out.writeInt(priority);
        out.writeLong(dueDate.getTime());
    }

    public static final Parcelable.Creator<TodoEntry> CREATOR
            = new Parcelable.Creator<TodoEntry>() {
        public TodoEntry createFromParcel(Parcel in) {
            return new TodoEntry(in);
        }

        public TodoEntry[] newArray(int size) {
            return new TodoEntry[size];
        }
    };

    private TodoEntry(Parcel in) {
        description = in.readString();
        priority = in.readInt();
        dueDate = new Date(in.readLong());
    }

}
