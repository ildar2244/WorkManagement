package ru.javaapp.workmanagement.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Task;

/**
 * Created by User on 14.10.2015.
 */
public class RVAdaptersTasksForMaster extends RecyclerView.Adapter<RVAdaptersTasksForMaster.EventViewHolder> {
    private List<Task> taskList;
    private Context context;
    private LayoutInflater layoutInflater;
    private List statusTask;
    private GradientDrawable gd;

    @Override
    public RVAdaptersTasksForMaster.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_ta_item, parent, false);
        EventViewHolder evh = new EventViewHolder(view);
        return evh;
    }

    public RVAdaptersTasksForMaster(Context context, List<Task> taskList, List statusTask) {
        this.context = context;
        this.taskList = taskList;
        this.statusTask = statusTask;
        layoutInflater = LayoutInflater.from(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(15.0f);

        holder.tvNumber.setText(Integer.toString(taskList.get(position).getIdTask()));
        holder.tvTimeFinish.setText(taskList.get(position).getTimeFinish());
        holder.tvDateFinish.setText(taskList.get(position).getDateFinish());
        if (taskList.get(position).getIdStatus() == 1) {
            holder.tvCount.setText(Integer.toString(taskList.get(position).getCountPlanTask()));
            gd.setColor(Color.parseColor("#727272"));
            holder.tvCount.setBackground(gd);
        }
        if (taskList.get(position).getIdStatus() == 2) {
            holder.tvCount.setText(Integer.toString(taskList.get(position).getCountCurrentTask()));
            gd.setColor(Color.parseColor("#1976D2"));
            holder.tvCount.setBackground(gd);
        }
        if (taskList.get(position).getIdStatus() == 3) {
            holder.tvCount.setText(Integer.toString(taskList.get(position).getCountCurrentTask()));
            if (taskList.get(position).getCountCurrentTask() < taskList.get(position).getCountPlanTask()) {
                gd.setColor(Color.parseColor("#c80b18"));
                holder.tvCount.setBackground(gd);
            }
            else {
                gd.setColor(Color.parseColor("#5FB219"));
                holder.tvCount.setBackground(gd);
            }
        }
        holder.tvHowName.setText(taskList.get(position).getPerformer());
        holder.tvWhatName.setText(taskList.get(position).getWhatName());
        holder.tvPlaceName.setText(taskList.get(position).getPlaceName());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumber, tvTimeFinish, tvDateFinish, tvCount, tvWhatName, tvPlaceName, tvHowName;

        public EventViewHolder(View itemView) {
            super(itemView);

            tvNumber = (TextView) itemView.findViewById(R.id.task_number);
            tvTimeFinish = (TextView) itemView.findViewById(R.id.task_timefinish);
            tvDateFinish = (TextView) itemView.findViewById(R.id.task_datefinish);
            tvCount = (TextView) itemView.findViewById(R.id.task_count);
            tvHowName = (TextView) itemView.findViewById(R.id.task_who);
            tvWhatName = (TextView) itemView.findViewById(R.id.task_what);
            tvPlaceName = (TextView) itemView.findViewById(R.id.task_place);
        }
    }
}
