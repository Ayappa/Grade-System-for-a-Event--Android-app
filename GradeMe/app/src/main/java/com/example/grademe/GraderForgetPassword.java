package com.example.grademe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

import static android.widget.Toast.LENGTH_SHORT;


public class GraderForgetPassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText  gNewPassword ,gConfirmPassword;
       Button gUpdatePassword;
       String email;
    private OnFragmentInteractionListener mListener;

    public GraderForgetPassword() {
        // Required empty public constructor
    }

    public GraderForgetPassword(String s) {
        email=s;
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

        View view= inflater.inflate(R.layout.fragment_grader_forget_password, container, false);
        gNewPassword=view.findViewById(R.id.gNewPassword);
        gConfirmPassword=view.findViewById(R.id.gConfirmPassword);
        gUpdatePassword =view.findViewById(R.id.gUpdatePassword);

        gUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gNewPassword.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Enter your Mail Id", Toast.LENGTH_SHORT).show();
                }if(gConfirmPassword.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Enter Event Name", Toast.LENGTH_SHORT).show();
                }if(! gConfirmPassword.getText().toString().contentEquals(gNewPassword.getText().toString())){
                    Toast.makeText(getContext(), "Password Do Not Match", Toast.LENGTH_SHORT).show();
                }
                else{

                    StringBuilder sb = new StringBuilder();
                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    final OkHttpClient client = new OkHttpClient();
                    JSONObject postdata=new JSONObject();
                    try {
                        postdata.put ("email",email);
                        postdata.put ("newPassword",gNewPassword.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body =RequestBody.create(JSON,postdata.toString());
                    Request request = new Request.Builder()
                            .url("https://grader2019.herokuapp.com/api/graders/updatePassword")
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
                            Log.d("responseData",responseData);

                            try {
                                JSONObject token=new JSONObject(responseData);
                                String Token=null;
                                String msg=null;
                                if(token.has("token")){
                                    Token=token.getString("token");
                                    Log.d("token",Token);
                                }
                                if(token.has("msg1")){
                                    msg=token.getString("token");
                                    Log.d("msg",msg);

                                }
                                Log.d("email",email);

                                client.dispatcher().executorService().shutdown();
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                final String finalToken = Token;
                                final String finalMsg = msg;
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(finalToken !=null){
                                            mListener.changeToHomeFragment(finalToken);
                                        }
                                        if(finalMsg !=null){
                                            Toast.makeText(getContext(), "Credentials invalid", LENGTH_SHORT).show();
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

        void changeToHomeFragment(String finalToken);
    }
}
