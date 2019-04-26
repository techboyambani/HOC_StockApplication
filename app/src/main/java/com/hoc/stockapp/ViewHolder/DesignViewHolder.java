package com.hoc.stockapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hoc.stockapp.R;

public class DesignViewHolder extends RecyclerView.ViewHolder{

    public TextView name, company, size, quantity;

    public DesignViewHolder(View itemView){
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.name);
        company = (TextView)itemView.findViewById(R.id.company);
        size = (TextView)itemView.findViewById(R.id.size);
        quantity = (TextView)itemView.findViewById(R.id.quantity);
    }
}
