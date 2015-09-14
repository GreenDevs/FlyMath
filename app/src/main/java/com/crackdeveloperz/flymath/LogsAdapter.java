package com.crackdeveloperz.flymath;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by script on 9/14/15.
 */
public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder>
{
    private List<String> logs= Collections.emptyList();


    public void triggerUpdates(List<String> logs)
    {
        this.logs=logs;
        notifyItemRangeChanged(0, logs.size());
    }
    @Override
    public LogsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        return new MyViewHolder(inflater.inflate(android.R.layout.simple_list_item_1,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(LogsAdapter.MyViewHolder holder, int i)
    {
        holder.log.setText(logs.get(i));
    }

    @Override
    public int getItemCount()
    {
        return logs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView log;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            log=(TextView)itemView;
        }
    }
}
