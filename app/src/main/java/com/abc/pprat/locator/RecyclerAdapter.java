package com.abc.pprat.locator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] dataSource;
    private ArrayList<PartnerDetail> partnerDetailArrayList;
    Context context;


    public RecyclerAdapter(ArrayList<PartnerDetail> partnerDetailArrayList,Context context ){
        this.context=context;
        this.partnerDetailArrayList=partnerDetailArrayList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(partnerDetailArrayList.get(position).getNamep());
        if (partnerDetailArrayList.get(position).getStatusp().equals("1")){
         holder.tvStatus.setText("Gps Enabled");
        }
        else {
            holder.tvStatus.setText("Gps Disabled");
        }

        holder.setUserComplainClickListener(new UserClickListener() {
            @Override
            public void onUserItemClick(View view, int position) {
                String userKey=partnerDetailArrayList.get(position).getKeyp();
                Intent intent=new Intent(context,PartnerLocation.class);
                intent.putExtra("User",userKey);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return partnerDetailArrayList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView textView;
        protected TextView tvStatus;
        UserClickListener userClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            textView =  (TextView) itemView.findViewById(R.id.tvPName);
            tvStatus=(TextView)itemView.findViewById(R.id.locationStatus);

            itemView.setOnClickListener(this);

        }

        private void setUserComplainClickListener(UserClickListener userClickListener){
            this.userClickListener=userClickListener;
        }

        @Override
        public void onClick(View v) {
            this.userClickListener.onUserItemClick(v,getLayoutPosition());
        }
    }
}