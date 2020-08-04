package com.example.grademe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * {@link EventManagersEventsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventManagersEventsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventManagersEventsList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String x_auth_token;
    SharedPreferences sharedpreferences;
    TextView listMsg;
    private OnFragmentInteractionListener mListener;

    public EventManagersEventsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventManagersEventsList.
     */
    // TODO: Rename and change types and number of parameters
    public static EventManagersEventsList newInstance(String param1, String param2) {
        EventManagersEventsList fragment = new EventManagersEventsList();
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
        setHasOptionsMenu(true);

        final View view= inflater.inflate(R.layout.fragment_event_managers_events_list, container, false);
        listMsg=view.findViewById(R.id.listmsg);
        getActivity().setTitle("Event Lists");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        x_auth_token=preferences.getString("x-auth-token","");
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final ListView listView = (ListView)view. findViewById(R.id.managerList);


        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://grader2019.herokuapp.com/api/eventManager/getEvents")
                .header("Content-Type", "application/json")
                .header("x-auth-token",x_auth_token)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData=response.body().string();
                try {
                    final JSONArray events=new JSONArray(responseData);
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    final List<String> list = new ArrayList<String>();
                    for (int i=0; i<events.length(); i++) {
                        list.add( events.getString(i) );
                    }

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            if(list.size()==0){listMsg.setText("No Events Avaliable!!");}
                            else {
                                listMsg.setText("");

                                ArrayAdapter adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, list);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().addToBackStack("list")
                                                .replace(R.id.container, new EventDetail(list.get(position)))
                                                .commit();
                                    }
                                });
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return  view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menucart, menu);
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
                getActivity(). getSupportFragmentManager().popBackStack(0,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);                return true;
            case R.id.home:
                getActivity(). getSupportFragmentManager().beginTransaction().addToBackStack("list")
                                               .replace(R.id.container, new NewEvent())
                                               .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
