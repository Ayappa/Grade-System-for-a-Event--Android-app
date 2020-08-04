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


public class GraderLogin extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText graderEmail, graderPassword;
    Button gLogin ,gUpdate;
    private OnFragmentInteractionListener mListener;

    public GraderLogin() {
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
        final View view= inflater.inflate(R.layout.fragment_grader_login, container, false);
        getActivity().setTitle("Grader Login");

        graderEmail=view.findViewById(R.id.graderEmail);
        graderPassword=view.findViewById(R.id.graderPassword);
        gLogin=view.findViewById(R.id.gLogin);
        gUpdate=view.findViewById(R.id.gUpdate);

        gUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)view.getContext()).getSupportFragmentManager().beginTransaction().addToBackStack("lisk")
                        .replace(R.id.gContainer, new GraderCheckEmailForgetPassword())
                        .commit();
            }
        });

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (graderEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email", LENGTH_SHORT).show();
                    graderEmail.setError("This field can not be blank");
                } else if (graderPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Password", LENGTH_SHORT).show();
                    graderPassword.setError("This field can not be blank");
                } else {

                    final String logInEmail = graderEmail.getText().toString();
                    String logInPassword = graderPassword.getText().toString();
                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    final OkHttpClient client = new OkHttpClient();
                    JSONObject postdata=new JSONObject();
                    try {
                        postdata.put ("firstName",FirstName);
                        postdata.put ("lastName",LastName);
                        postdata.put (	"email",Email);
                        postdata.put (	"gender",Gender);
                        postdata.put (		"city",City);
                        postdata.put (	"phone",PhoneNumber);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body =RequestBody.create(JSON,postdata.toString());
                    Request request = new Request.Builder()
                            .url("https://finalscanme12.herokuapp.com/api/users")
                            .header("Content-Type", "application/json")
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            // Log.d("DEMO",response.body().string());
                            String responseData=response.body().string();
                            try {
                                JSONObject token=new JSONObject(responseData);
                                if(token.has("msg")){
                                    Handler mainHandler = new Handler(Looper.getMainLooper());

                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "Email already taken", Toast.LENGTH_SHORT).show();


                                        }
                                    });

                                }else {
                                    Token = token.getString("token");
                                    Log.d("DEMO1233=", Token);

                                    // onComplete(Token);
                                    client.dispatcher().executorService().shutdown();
                                    Handler mainHandler = new Handler(Looper.getMainLooper());

                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "loged", Toast.LENGTH_SHORT).show();
                                            mListener.changeToHomeFragment(Token, FirstName
                                                    , LastName, Email, PhoneNumber);

                                        }
                                    });


                                }
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

        void changeToHomeFragment(String token);
    }
}
