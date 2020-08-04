package com.example.grademe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GradingTeamsAdapter extends RecyclerView.Adapter<GradingTeamsAdapter.ViewHolder> {
    ArrayList<EventDetailsPojo> teams;

    public GradingTeamsAdapter(ArrayList<EventDetailsPojo> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public GradingTeamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grading_teams_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GradingTeamsAdapter.ViewHolder holder, final int position) {
         holder.tgraders.setText("Graded by "+teams.get(position).getTotalGraders().toString());
        holder.tName.setText(teams.get(position).getTeamName());
        holder.tavg.setText(teams.get(position).getAvg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)(v.getContext())).  getSupportFragmentManager().beginTransaction().addToBackStack("li")
                        .replace(R.id.gContainer, new GradersQuestions(teams.get(position).getTeamName()))
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tgraders ,tName ,tavg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tgraders=itemView.findViewById(R.id.tgraders);
            tName=itemView.findViewById(R.id.tName);
            tavg=itemView.findViewById(R.id.tavg);


        }
    }
}
