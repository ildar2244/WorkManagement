package ru.javaapp.workmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Defect;
import ru.javaapp.workmanagement.dao.Downtime;

/**
 * Created by User on 30.10.2015.
 */
public class RVStopInfoMaster extends RecyclerView.Adapter<RVStopInfoMaster.EventViewHolder> {

    private List<Downtime> stopList;
    private Context context;
    private LayoutInflater layoutInflater;

    @Override
    public RVStopInfoMaster.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_stop_info_master, parent, false);
        EventViewHolder evh = new EventViewHolder(view);
        return evh;
    }

    public RVStopInfoMaster(Context context, List<Downtime> stopList){
        this.context = context;
        this.stopList = stopList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RVStopInfoMaster.EventViewHolder holder, int position) {
        holder.tvStopCause.setText(stopList.get(position).getNameDowntime());
        holder.tvStopDate.setText(stopList.get(position).getDate());
        holder.tvStopTime.setText(stopList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return stopList.size();
    }

    public class EventViewHolder extends  RecyclerView.ViewHolder {

        TextView tvStopCause, tvStopDate, tvStopTime;

        public EventViewHolder(View itemView) {
            super(itemView);

            tvStopCause = (TextView) itemView.findViewById(R.id.tv_stopCause);
            tvStopDate = (TextView) itemView.findViewById(R.id.tv_stopDate);
            tvStopTime = (TextView) itemView.findViewById(R.id.tv_stopTime);
        }
    }
}
