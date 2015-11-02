package ru.javaapp.workmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Complects;

/**
 * Created by User on 01.11.2015.
 */
public class RVComplectAdapter extends RecyclerView.Adapter<RVComplectAdapter.ViewHolder> {
    private List<Complects> complectsList;
    private Context context;
    private LayoutInflater layoutInflater;

    @Override
    public RVComplectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_complects_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public RVComplectAdapter(Context context, List<Complects> complectsList){
        this.context = context;
        this.complectsList = complectsList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvComplectName.setText(complectsList.get(position).getName());
        holder.tvComplectCount.setText(Integer.toString(complectsList.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        return complectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvComplectName, tvComplectCount;
        public ViewHolder(View itemView) {
            super(itemView);

            tvComplectName = (TextView) itemView.findViewById(R.id.tv_complectName);
            tvComplectCount = (TextView) itemView.findViewById(R.id.tv_complectCount);
        }
    }
}
