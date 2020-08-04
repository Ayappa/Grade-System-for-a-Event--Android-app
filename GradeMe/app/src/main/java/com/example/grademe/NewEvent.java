package com.example.grademe;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NewEvent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText eventName, eventNumber ,gMail;
     Button gbutton ,eventCreate;
     ListView gList;
    ArrayList<String>emails;
    String x_auth_token;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;
    private OnFragmentInteractionListener mListener;




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
        View view= inflater.inflate(R.layout.fragment_new_event, container, false);
        eventName=view.findViewById(R.id.eventName);
        eventNumber=view.findViewById(R.id.eventNumber);
        gMail=view.findViewById(R.id.gMail);
        gbutton=view.findViewById(R.id.gbutton);
        eventCreate=view.findViewById(R.id.eventCreate);
        gList=view.findViewById(R.id.gList);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        getActivity().setTitle("Create new Event");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        x_auth_token=preferences.getString("x-auth-token","");
         emails=new ArrayList<>();

        gbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emails.size()==5) {
                    Toast.makeText(getContext(), "Can add only 5 graders", Toast.LENGTH_SHORT).show();
                }else{
                    if(gMail.getText().toString().length()==0) {
                        Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                    }else {
                        emails.add(gMail.getText().toString());
                        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,emails);
                        gList.setAdapter(adapter);
                        gMail.setText("");
                    }
                }
            }
        });

        gList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                emails.remove(position);
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,emails);
                gList.setAdapter(adapter);
                return false;
            }
        });

        eventCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventName.getText().toString().length()==0){Toast.makeText(getContext(), "Enter EventName", Toast.LENGTH_SHORT).show();}
               else if(eventNumber.getText().toString().length()==0){Toast.makeText(getContext(), "Enter number of participants", Toast.LENGTH_SHORT).show();}
               else if(emails.size()==0){Toast.makeText(getContext(), "Invide atleast one grader", Toast.LENGTH_SHORT).show();}
               else{
                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    final OkHttpClient client = new OkHttpClient();
                    JSONObject postdata=new JSONObject();
                    try {
                        postdata.put ("eventName",eventName.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body =RequestBody.create(JSON,postdata.toString());

                    Request request = new Request.Builder()
                            .url("https://grader2019.herokuapp.com/api/eventName")
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
                                JSONObject responseDataMsg=new JSONObject(responseData);
                                final String msg=responseDataMsg.getString("msg");
                                Handler mainHandler = new Handler(Looper.getMainLooper());

                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(msg.contentEquals("EventName registered")){
                                          createTeams();
                                        }else{
                                            Toast.makeText(getContext(), "Event name already taken", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            }
        });

        return view;
    }

    private void createTeams() {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata=new JSONObject();
        try {
            postdata.put ("numberOfContestant",eventNumber.getText().toString());
            postdata.put ("eventName",eventName.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body =RequestBody.create(JSON,postdata.toString());

        Request request = new Request.Builder()
                .url("https://grader2019.herokuapp.com/api/contestant/create")
                .header("Content-Type", "application/json")
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
                    JSONObject responseDataMsg=new JSONObject(responseData);
                    final String msg=responseDataMsg.getString("msg");
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                                InviteGraders();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    int i=0;
    private void InviteGraders() {

        if(i<emails.size()){
            progressBar.setVisibility(View.VISIBLE);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            final OkHttpClient client = new OkHttpClient();
            JSONObject postdata=new JSONObject();

            try {
                postdata.put ("email",emails.get(i));
                postdata.put ("event",eventName .getText().toString());
                postdata.put ("graderNo",String.valueOf(i+1));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body =RequestBody.create(JSON,postdata.toString());

            Request request = new Request.Builder()
                    .url("https://grader2019.herokuapp.com/api/graders/register")
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();
            final int finalI = i;
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseData=response.body().string();
                    Log.d("responseDatamail",responseData.toString());

                    try {
                        JSONObject responseDataMsg=new JSONObject(responseData);
                        final String msg=responseDataMsg.getString("token");
                        Handler mainHandler = new Handler(Looper.getMainLooper());

                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(msg.contentEquals("User already exists")){
                                    Toast.makeText(getContext(), emails.get(finalI)+"  already taken", Toast.LENGTH_SHORT).show();
                                }
                                i++;
                                InviteGraders();
                                Log.d("mailsent","mailsent");
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            getActivity(). getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new EventManagersEventsList())
                    .commit();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
