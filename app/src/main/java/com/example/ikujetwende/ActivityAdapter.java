package com.example.ikujetwende;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder>{
    Context context;
    ArrayList<ActivityDetails> list;

    public ActivityAdapter(Context context, ArrayList<ActivityDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_record,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ActivityDetails details= list.get(position);
        holder.fname.setText(details.getFirstname());
        holder.sname.setText(details.getSurname());
        holder.description.setText(details.getDescription());
        holder.location.setText(details.getLocation());
        holder.date.setText(details.getDate());
        holder.time.setText(details.getTime());
        holder.activity.setText(details.getActivity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fname,sname,description,location,date,time,activity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fname=itemView.findViewById(R.id.fname);
            sname=itemView.findViewById(R.id.sname);
            description=itemView.findViewById(R.id.description);
            location=itemView.findViewById(R.id.location);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            activity=itemView.findViewById(R.id.activity);
        }
    }
}

