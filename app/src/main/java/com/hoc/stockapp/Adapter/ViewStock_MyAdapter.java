package com.hoc.stockapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoc.stockapp.Model.DesignInformation;
import com.hoc.stockapp.R;

import java.util.ArrayList;

public class ViewStock_MyAdapter extends RecyclerView.Adapter<ViewStock_MyAdapter.MyAdapterViewHolder> {

    public Context c;
    public ArrayList<DesignInformation> arrayList;
    public ViewStock_MyAdapter(Context c, ArrayList<DesignInformation> arrayList){
        this.c = c;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount(){
        return arrayList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public MyAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view_stock_card,parent,false);

        return new MyAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapterViewHolder holder, int position){

        DesignInformation designInformation = arrayList.get(position);

        holder.n.setText(designInformation.getName());
        holder.c.setText(designInformation.getCompany());
        holder.s.setText(designInformation.getSize());
        holder.q.setText(designInformation.getQuantity());
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView n, c, s, q;

        public MyAdapterViewHolder(View itemView){
            super(itemView);

            n = (TextView)itemView.findViewById(R.id.name);
            c = (TextView)itemView.findViewById(R.id.company);
            s = (TextView)itemView.findViewById(R.id.size);
            q = (TextView)itemView.findViewById(R.id.quantity);

        }
    }
}
