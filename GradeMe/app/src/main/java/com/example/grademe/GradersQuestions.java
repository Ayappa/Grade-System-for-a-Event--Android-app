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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GradersQuestions extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView q1,q2,q3,q4,q5,q6,q7;
    Button button3;
   SeekBar s1,s2,s3,s4,s5,s6,s7;
   int a1,a2,a3,a4,a5,a6,a7;
   String teamName;
    String x_auth_token;
    SharedPreferences sharedpreferences;
    public GradersQuestions(String teamName) {
       this. teamName=teamName;
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
        final View view= inflater.inflate(R.layout.fragment_graders_questions, container, false);
        q1=view.findViewById(R.id.q1);
        q2=view.findViewById(R.id.q2);
        q3=view.findViewById(R.id.q3);
        q4=view.findViewById(R.id.q4);
        q5=view.findViewById(R.id.q5);
        q6=view.findViewById(R.id.q6);
        q7=view.findViewById(R.id.q7);
        s1=view.findViewById(R.id.s1);
        s2=view.findViewById(R.id.s2);
        s3=view.findViewById(R.id.s3);
        s4=view.findViewById(R.id.s4);
        s5=view.findViewById(R.id.s5);
        s6=view.findViewById(R.id.s6);
        s7=view.findViewById(R.id.s7);
        button3=view.findViewById(R.id.button3);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        x_auth_token=preferences.getString("x-auth-token","");
        Log.d("x_auth_token",x_auth_token);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q1.setText(String.valueOf(progress) );
                a1=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q2.setText(String.valueOf(progress));
                a2=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        s3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q3.setText(String.valueOf(progress));
                a3=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        s4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q4.setText(String.valueOf(progress));
                a4=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        s5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q5.setText(String.valueOf(progress));
                a5=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        s6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q6.setText(String.valueOf(progress));
                a6=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        s7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q7.setText(String.valueOf(progress));
                a7=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int average=a1+a2+a3+a4+a5+a6+a7;
                average=average/7;

                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                final OkHttpClient client = new OkHttpClient();
                JSONObject postdata=new JSONObject();
                try {
                    postdata.put ("team",teamName);
                    postdata.put ("score",String.valueOf(average));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body =RequestBody.create(JSON,postdata.toString());

                Request request = new Request.Builder()
                        .url("https://grader2019.herokuapp.com/api/contestant/addScore")
                        .header("Content-Type", "application/json")
                        .header("x-auth-token",x_auth_token)
                        .post(body)
                        .build();
                final int finalAverage = average;
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String responseData=response.body().string();
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        Log.d("average", String.valueOf(finalAverage));
                        Log.d("teamName=",teamName);
                        Log.d("teamName=",responseData);

                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((FragmentActivity)(view.getContext())).  getSupportFragmentManager().popBackStack();
                                //notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
