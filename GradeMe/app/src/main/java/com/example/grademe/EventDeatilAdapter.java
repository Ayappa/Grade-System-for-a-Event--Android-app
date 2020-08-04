package com.example.grademe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventDeatilAdapter extends RecyclerView.Adapter<EventDeatilAdapter.ViewHolder> {
    ArrayList<EventDetailsPojo> eventDetailsList;

    public EventDeatilAdapter(ArrayList<EventDetailsPojo> eventDetailsList) {
        this.eventDetailsList = eventDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_details_pojo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.teamName.setText(eventDetailsList.get(position).getTeamName());
        holder.g1.setText(eventDetailsList.get(position).getGrader1());
        holder.g2.setText(eventDetailsList.get(position).getGrader2());
        holder.g3.setText(eventDetailsList.get(position).getGrader3());
        holder.g4.setText(eventDetailsList.get(position).getGrader4());
        holder.g5.setText(eventDetailsList.get(position).getGrader5());
        holder.avg.setText(eventDetailsList.get(position).getAvg());

    }

    @Override
    public int getItemCount() {
        return eventDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView g1,g2,g3,g4,g5,teamName,avg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            g1=itemView.findViewById(R.id.g1);
            g2=itemView.findViewById(R.id.g2);
            g3=itemView.findViewById(R.id.g3);
            g4=itemView.findViewById(R.id.g4);
            g5=itemView.findViewById(R.id.g5);
            teamName=itemView.findViewById(R.id.teamName);
            avg=itemView.findViewById(R.id.avg);


        }
    }
}
