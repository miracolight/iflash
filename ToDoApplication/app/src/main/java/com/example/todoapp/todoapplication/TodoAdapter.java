package com.example.todoapp.todoapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.todoapp.todoapplication.data.TodoEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by qingdi on 8/22/14.
 */
public class TodoAdapter extends ArrayAdapter<TodoEntry> {
    private static final DateFormat dueDateFormate = new SimpleDateFormat("MMM. dd, yyyy");

    private Context             context;
    private List<TodoEntry>     values;


    public TodoAdapter(Context context, int resource, List<TodoEntry> values) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_todo_item, parent, false);

        ViewHolder viewHolder = (ViewHolder)rowView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        viewHolder.descriptionView.setText(values.get(position).description);
        viewHolder.dueDateView.setText(dueDateFormate.format(values.get(position).dueDate));
        viewHolder.priorityView.setText(Integer.toString(values.get(position).priority));

        return rowView;
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView descriptionView;
        public final TextView dueDateView;
        public final TextView priorityView;

        public ViewHolder(View view) {
            descriptionView = (TextView) view.findViewById(R.id.todoItem);
            dueDateView = (TextView) view.findViewById(R.id.dueDate);
            priorityView = (TextView) view.findViewById(R.id.priority);

        }
    }
}
