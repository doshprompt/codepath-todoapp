package rdoshi.codepath.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskItemAdapter extends ArrayAdapter<TaskItem> {

    private static class ViewHolder {
        TextView name;
        TextView priority;
        TextView dueDate;
    }

    public TaskItemAdapter(Context context, ArrayList<TaskItem> items) {
        super(context, R.layout.list_item_task, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskItem taskItem = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_task, parent, false);
            
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.tvPrio);
            viewHolder.dueDate = (TextView) convertView.findViewById(R.id.tvDueDate);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(taskItem.getName());

        if (taskItem.getDueDate() != null) {
            SimpleDateFormat sf = new SimpleDateFormat("MMM d yyyy");
            sf.setLenient(true);
            viewHolder.dueDate.setText(sf.format(taskItem.getDueDate()));
        } else {
            viewHolder.dueDate.setText(getContext().getString(R.string.no_due_date));
        }

        viewHolder.priority.setText(getContext().getResources().getStringArray(R.array.task_priorities)[taskItem.getPriority()]);
        switch (taskItem.getPriority()) {
            case 0:
                viewHolder.priority.setBackgroundColor(getContext().getResources().getColor(R.color.priorityHigh));
                break;
            case 1:
                viewHolder.priority.setBackgroundColor(getContext().getResources().getColor(R.color.priorityMedium));
                break;
            case 2:
                viewHolder.priority.setBackgroundColor(getContext().getResources().getColor(R.color.priorityLow));
                break;
        }

        return convertView;
    }

}
