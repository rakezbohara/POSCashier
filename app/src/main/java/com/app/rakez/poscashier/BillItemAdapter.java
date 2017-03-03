package com.app.rakez.poscashier;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by RAKEZ on 2/26/2017.
 */
public class BillItemAdapter extends RecyclerView.Adapter<BillItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<BillItem> albumList;

    public BillItemAdapter(List<BillItem> albumList, Context mContext) {
        this.albumList = albumList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BillItem currentView = albumList.get(position);
        holder.itemName.setText(currentView.getItemName());
        holder.itemQty.setText(currentView.getItemQty());
        holder.itemPrice.setText(currentView.getItemPrice());
        holder.itemTotal.setText(currentView.getItemTotal());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName,itemQty,itemPrice,itemTotal;

        public MyViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemQty = (TextView) view.findViewById(R.id.itemQty);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
            itemTotal = (TextView) view.findViewById(R.id.itemTotal);
        }
    }
}
