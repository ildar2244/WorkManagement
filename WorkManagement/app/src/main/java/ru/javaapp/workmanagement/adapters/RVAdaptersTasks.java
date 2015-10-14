package ru.javaapp.workmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Task;

/**
 * Created by User on 13.10.2015.
 */
public class RVAdaptersTasks extends RecyclerView.Adapter<RVAdaptersTasks.EventViewHolder> {

    private List<Task> taskList;
    private Context context;
    private LayoutInflater layoutInflater;
    private List statusTask;

    @Override
    public RVAdaptersTasks.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_tla_item, parent, false);
        EventViewHolder evh = new EventViewHolder(view);
        return evh;
    }

    public RVAdaptersTasks(Context context, List<Task> taskList, List statusTask) {
        this.context = context;
        this.taskList = taskList;
        this.statusTask = statusTask;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        holder.tvNumber.setText(Integer.toString(taskList.get(position).getIdTask()));
        holder.tvTimeFinish.setText(taskList.get(position).getTimeFinish());
        holder.tvDateFinish.setText(taskList.get(position).getDateFinish());
        if (taskList.get(position).getIdStatus() == 1) {
            holder.tvCount.setText(Integer.toString(taskList.get(position).getCountPlanTask()));
        }
        else {
            holder.tvCount.setText(Integer.toString(taskList.get(position).getCountCurrentTask()));
        }
        holder.tvWhatName.setText(taskList.get(position).getWhatName());
        holder.tvPlaceName.setText(taskList.get(position).getPlaceName());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumber, tvTimeFinish, tvDateFinish, tvCount, tvWhatName, tvPlaceName;

        public EventViewHolder(View itemView) {
            super(itemView);

            tvNumber = (TextView) itemView.findViewById(R.id.task_number);
            tvTimeFinish = (TextView) itemView.findViewById(R.id.task_timefinish);
            tvDateFinish = (TextView) itemView.findViewById(R.id.task_datefinish);
            tvCount = (TextView) itemView.findViewById(R.id.task_count);
            tvWhatName = (TextView) itemView.findViewById(R.id.task_what);
            tvPlaceName = (TextView) itemView.findViewById(R.id.task_place);
        }
    }
}
