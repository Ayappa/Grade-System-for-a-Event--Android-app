package com.example.grademe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GradingTeams extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private OnFragmentInteractionListener mListener;
    ArrayList<EventDetailsPojo> events=new ArrayList<EventDetailsPojo>();
    String x_auth_token;
    SharedPreferences sharedpreferences;
    public GradingTeams() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Teams");

        setHasOptionsMenu(true);
        View view= inflater.inflate(R.layout.fragment_grading_teams, container, false);
        recyclerView = view.findViewById(R.id.teamList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        x_auth_token=preferences.getString("x-auth-token","");

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata=new JSONObject();
        RequestBody body =RequestBody.create(JSON,postdata.toString());

        Request request = new Request.Builder()
                .url("https://grader2019.herokuapp.com/api/contestant/getAverage")
                .header("Content-Type", "application/json")
                .header("x-auth-token",x_auth_token)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData=response.body().string();
                try {
                    events.clear();
                    JSONArray responseList=new JSONArray(responseData);
                    Log.d("eventDetailsList", String.valueOf(responseList.length()));
                    for(int i=0;i<responseList.length();i++){
                        JSONObject item=responseList.getJSONObject(i);
                        Log.d("JSONObject", String.valueOf(item));

                        EventDetailsPojo eventDetail=new EventDetailsPojo();
                        int count=0;
                        eventDetail.setTeamName(item.get("contestantName").toString());
                        if(item.has("grader1")){
                            eventDetail.setGrader1(item.get("grader1").toString());
                            count++;
                        }else {
                            eventDetail.setGrader1("-");
                        }
                        if(item.has("grader2")){
                            eventDetail.setGrader2(item.get("grader2").toString());
                            count++;
                        }else {
                            eventDetail.setGrader2("-");
                        }
                        if(item.has("grader3")){
                            eventDetail.setGrader3(item.get("grader3").toString());
                            count++;
                        }else {
                            eventDetail.setGrader3("-");
                        }
                        if(item.has("grader4")){
                            eventDetail.setGrader4(item.get("grader4").toString());
                            count++;
                        }else {
                            eventDetail.setGrader4("-");
                        }
                        if(item.has("grader5")){
                            eventDetail.setGrader5(item.get("grader5").toString());
                            count++;
                        }else {
                            eventDetail.setGrader5("-");
                        }
                        if(item.has("average")){
                            eventDetail.setAvg(item.get("average").toString());
                        }else {
                            eventDetail.setAvg("0");
                        }
                        Log.d("JSONObject", eventDetail.getTeamName());

                        eventDetail.setTotalGraders(count);
                        events.add(eventDetail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Collections.sort(events, new Comparator<EventDetailsPojo>() {
                    @Override
                    public int compare(EventDetailsPojo o1, EventDetailsPojo o2) {
                        return Integer.valueOf(o2.getAvg()).compareTo(Integer.valueOf(o1.getAvg()));
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return false;
                    }
                });
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter = new GradingTeamsAdapter(events);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            }
        });

        return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.buying);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("x-auth-token","");
                editor.putString("type","");
                editor.apply();
                getActivity(). getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);                return true;
            case R.id.buying:

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
