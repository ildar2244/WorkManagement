package ru.javaapp.workmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.javaapp.workmanagement.R;

/**
 * Created by User on 01.11.2015.
 */
public class RVProductsAdapter extends RecyclerView.Adapter<RVProductsAdapter.ViewHolder> {
    private List productList;
    private Context context;
    private LayoutInflater layoutInflater;

    @Override
    public RVProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_products_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public RVProductsAdapter(Context context, List productList){
        this.context = context;
        this.productList = productList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RVProductsAdapter.ViewHolder holder, int position) {
        holder.tvProductName.setText(productList.get(position).toString());
        holder.tvProductCount.setText("");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        TextView tvProductName, tvProductCount;

        public ViewHolder(View itemView) {
            super(itemView);

            tvProductName = (TextView) itemView.findViewById(R.id.tv_productName);
            tvProductCount = (TextView) itemView.findViewById(R.id.tv_productCount);
        }
    }
}
