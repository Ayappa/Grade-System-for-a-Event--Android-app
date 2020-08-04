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
import android.widget.RadioGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventManagerRegister.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventManagerRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventManagerRegister extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    EditText city;
    Button signUp;
    RadioGroup genderRadioGroup;
    String FirstName, LastName, Email, Password, Gender="Male", City,dateOfBirth;

    private OnFragmentInteractionListener mListener;

    public EventManagerRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventManagerRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static EventManagerRegister newInstance(String param1, String param2) {
        EventManagerRegister fragment = new EventManagerRegister();
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
     View view=inflater.inflate(R.layout.fragment_event_manager_register, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preferences.edit();
        getActivity().setTitle("Event Manager Register");

        firstName=view.findViewById(R.id.editFirstName);
        lastName=view.findViewById(R.id.editLastName);
        email=view.findViewById(R.id.editEmailId);
        password=view.findViewById(R.id.editPassword);
        confirmPassword=view.findViewById(R.id.confirmPassword);
        city=view.findViewById(R.id.cty);
        signUp=view.findViewById(R.id.button2);
        genderRadioGroup = view.findViewById(R.id.radioGroup);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                if (firstName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter First Name", Toast.LENGTH_SHORT).show();
                    firstName.setError("This field can not be blank");
                } else if (lastName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Last Name", Toast.LENGTH_SHORT).show();
                    lastName.setError("This field can not be blank");
                } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email ID", Toast.LENGTH_SHORT).show();
                    email.setError("This field can not be blank");
                } else if ((password.getText().toString().trim().equalsIgnoreCase(""))) {
                    Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    password.setError("This field can not be blank");
                } else if ((confirmPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                    Toast.makeText(getContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    confirmPassword.setError("This field can not be blank");
                } else if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                        FirstName = firstName.getText().toString().trim();
                        LastName = lastName.getText().toString().trim();
                        Email = email.getText().toString().trim();
                        Password = password.getText().toString().trim();
                        City = city.getText().toString().trim();
                        // dateOfBirth=dob.getText().toString();
                        StringBuilder sb = new StringBuilder();
                        sb.append("http://localhost:5000/api/eventManager/register");
                        Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();

                        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


                        final OkHttpClient client = new OkHttpClient();

                        JSONObject postdata=new JSONObject();
                        try {
                            postdata.put ("firstName",FirstName);
                            postdata.put ("lastName",LastName);
                            postdata.put (	"email",Email);
                            postdata.put (	"password",Password);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody body =RequestBody.create(JSON,postdata.toString());
                        Request request = new Request.Builder()
                                .url("https://grader2019.herokuapp.com/api/eventManager/register")
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
                                    final String Token=token.getString("token");
                                    Log.d("DEMO123=",Token);
                                    editor.putString("x-auth-token",Token);
                                    editor.putString("type","manager");
                                    // onComplete(Token);
                                    client.dispatcher().executorService().shutdown();
                                    Handler mainHandler = new Handler(Looper.getMainLooper());

                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mListener.changeToHomeFragment(Token);
//                                            ((FragmentActivity) view.getContext()). getSupportFragmentManager().beginTransaction().addToBackStack("list")
//                                                    .replace(R.id.container, new EventManagersEventsList())
//                                                    .commit();

                                        }
                                    });



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                        //  onComplete(Token);

                    } else {
                        password.setError("Passwords do not match");
                        confirmPassword.setError("Passwords do not match");
                    }
                }


            }
        });

     return  view;
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
