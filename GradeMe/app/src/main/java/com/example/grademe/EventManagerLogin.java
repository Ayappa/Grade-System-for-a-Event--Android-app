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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventManagerLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventManagerLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventManagerLogin extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button login;
    Button signUp;
    EditText email;
    EditText password;
    private OnFragmentInteractionListener mListener;

    public EventManagerLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventManagerLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static EventManagerLogin newInstance(String param1, String param2) {
        EventManagerLogin fragment = new EventManagerLogin();
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

      final View view= inflater.inflate(R.layout.fragment_event_manager_login, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preferences.edit();
        getActivity().setTitle("Event Manager Login");

        login = view.findViewById(R.id.login);

        signUp = view.findViewById(R.id.signup);
        email = view.findViewById(R.id.editLastName);
        password = view.findViewById(R.id.editPassword);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) view.getContext()). getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new EventManagerRegister())
                        .commit();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email", LENGTH_SHORT).show();
                    email.setError("This field can not be blank");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Password", LENGTH_SHORT).show();
                    password.setError("This field can not be blank");
                } else {
                    Log.d("email", email.getText().toString());
                    Log.d("password", password.getText().toString());
                    final OkHttpClient client = new OkHttpClient();
                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    JSONObject postdata = new JSONObject();
                    try {

                        postdata.put("email", email.getText().toString());
                        postdata.put("password", password.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(JSON, postdata.toString());
                    Request request = new Request.Builder()
                            .url("https://grader2019.herokuapp.com/api/eventManager/login")
                            .header("Content-Type", "application/json")
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseData = response.body().string();
                             String Token = null;
                            try {
                                JSONObject token = new JSONObject(responseData);
                                if(token.has("token")){
                                     Token = token.getString("token");
                                    Log.d("responseData", Token);
                                    editor.putString("x-auth-token",Token);
                                    editor.putString("type","manager");
                                }

                                client.dispatcher().executorService().shutdown();
                                Handler mainHandler = new Handler(Looper.getMainLooper());

                                final String finalToken = Token;
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(finalToken==null){
                                            Toast.makeText(getContext(), "Invalid Credentials", LENGTH_SHORT).show();
                                        }else {
                                            mListener.changeToHomeFragment(finalToken);
                                        }
//                                        ((FragmentActivity) view.getContext()). getSupportFragmentManager().beginTransaction().addToBackStack("list")
//                                                .replace(R.id.container, new EventManagersEventsList())
//                                                .commit();

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

        void changeToHomeFragment(String token);
    }
}
