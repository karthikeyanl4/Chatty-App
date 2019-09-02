package com.example.chatty_4;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter <ExampleAdapter.ExampleViewHolder>{
    private  ArrayList<LeftRight> mLeftRight;
    public static  class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mtextView;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextView = itemView.findViewById(R.id.textview);
        }
    }
    public ExampleAdapter(ArrayList<LeftRight> LeftRight){
        mLeftRight=LeftRight;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);

    ExampleViewHolder evh=new ExampleViewHolder(v);
    return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        LeftRight currentItem =mLeftRight.get(position);
        if(currentItem.isIsleft()){
            holder.mtextView.setGravity(Gravity.END);
        }else
        {
            holder.mtextView.setGravity(Gravity.START);
        }
        holder.mtextView.setText(currentItem.getText1());

    }

    @Override
    public int getItemCount() {
        return mLeftRight.size();
    }



}
