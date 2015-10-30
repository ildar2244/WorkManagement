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

/**
 * Created by User on 29.10.2015.
 */
public class RVBrakInfoMaster extends RecyclerView.Adapter<RVBrakInfoMaster.EventViewHolder> {

    private List<Defect> brakList;
    private Context context;
    private LayoutInflater layoutInflater;

    @Override
    public RVBrakInfoMaster.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_brak_info_master, parent, false);
        EventViewHolder evh = new EventViewHolder(view);
        return evh;
    }

    public RVBrakInfoMaster(Context context, List<Defect> brakList){
        this.context = context;
        this.brakList = brakList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RVBrakInfoMaster.EventViewHolder holder, int position) {
        holder.tvBrakCause.setText(brakList.get(position).getNameDefect());
        holder.tvBrakCount.setText(Integer.toString(brakList.get(position).getQuantumDefect()));
        holder.tvBrakDate.setText(brakList.get(position).getDate());
        holder.tvBrakTime.setText(brakList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return brakList.size();
    }

    public class EventViewHolder extends  RecyclerView.ViewHolder {

        TextView tvBrakCause, tvBrakCount, tvBrakDate, tvBrakTime;

        public EventViewHolder(View itemView) {
            super(itemView);

            tvBrakCause = (TextView) itemView.findViewById(R.id.tv_brakCause);
            tvBrakCount = (TextView) itemView.findViewById(R.id.tv_brakCount);
            tvBrakDate = (TextView) itemView.findViewById(R.id.tv_brakDate);
            tvBrakTime = (TextView) itemView.findViewById(R.id.tv_brakTime);
        }
    }
}