package com.app.rakez.poscashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by RAKEZ on 2/26/2017.
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.MyViewHolder> {

    private Context mContext;
    private List<TableView> albumList;

    public TableAdapter(List<TableView> albumList, Context mContext) {
        this.albumList = albumList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        TableView currentView = albumList.get(position);
        holder.name.setText(currentView.getTableName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle info = new Bundle();
                String tableNo = albumList.get(holder.getAdapterPosition()).getTableName();
                Intent i  = new Intent(mContext.getApplicationContext(),BillActivity.class);
                info.putString("tableNo",tableNo);
                i.putExtras(info);
                mContext.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.person_name);
        }
    }
}
