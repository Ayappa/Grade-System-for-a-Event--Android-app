package com.example.grademe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class GraderCheckEmailForgetPassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
     EditText gCheckEmail,gEventName;
    Button gCheckUpdate;
    private OnFragmentInteractionListener mListener;

    public GraderCheckEmailForgetPassword() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GraderCheckEmailForgetPassword newInstance(String param1, String param2) {
        GraderCheckEmailForgetPassword fragment = new GraderCheckEmailForgetPassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        getActivity().setTitle("Grader Update Password");

        final View view= inflater.inflate(R.layout.fragment_grader_check_email_forget_password, container, false);
        gCheckEmail=view.findViewById(R.id.gCheckEmail);
        gEventName=view.findViewById(R.id.gEventName);
        gCheckUpdate=view.findViewById(R.id.gCheckUpdate);

        gCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gCheckEmail.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Enter your Mail Id", Toast.LENGTH_SHORT).show();
                }if(gEventName.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Enter Event Name", Toast.LENGTH_SHORT).show();
                }
                else{

                    StringBuilder sb = new StringBuilder();
                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    final OkHttpClient client = new OkHttpClient();
                    JSONObject postdata=new JSONObject();
                    try {
                        postdata.put ("email",gCheckEmail.getText().toString());
                        postdata.put ("event",gEventName.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body =RequestBody.create(JSON,postdata.toString());
                    Request request = new Request.Builder()
                            .url("https://grader2019.herokuapp.com/api/graders/forgetPassword")
                            .header("Content-Type", "application/json")
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseData=response.body().string();
                            try {
                                JSONObject token=new JSONObject(responseData);
                                final String msg=token.getString("msg");
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(msg.contentEquals("Update Link sent")){
                                            Toast.makeText(getContext(), "Check Your Email for Password Update LinK", Toast.LENGTH_SHORT).show();
                                            ((FragmentActivity)view.getContext()) . getSupportFragmentManager().popBackStack();
                                        }else{
                                            Toast.makeText(getContext(), "Invalid Email ID or Event Name", Toast.LENGTH_SHORT).show();
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


        return  view;
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
