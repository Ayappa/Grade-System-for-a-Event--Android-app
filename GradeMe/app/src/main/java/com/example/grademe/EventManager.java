package com.example.grademe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class EventManager extends AppCompatActivity implements  EventManagerLogin.OnFragmentInteractionListener,EventManagerRegister.OnFragmentInteractionListener,
EventManagersEventsList.OnFragmentInteractionListener,NewEvent.OnFragmentInteractionListener,EventDetail.OnFragmentInteractionListener{
    SharedPreferences sharedpreferences;
    String x_auth_token,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_manager);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        x_auth_token=preferences.getString("x-auth-token","");
        type=preferences.getString("type","");

        if(type.contentEquals("manager") && x_auth_token.length() !=0){
            getSupportFragmentManager().beginTransaction().addToBackStack("list")
                    .replace(R.id.container, new EventManagersEventsList())
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new EventManagerLogin())
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void changeToHomeFragment(String token) {
        x_auth_token=token;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("x-auth-token",token);
        editor.putString("type","manager");

        editor.apply();
        //  Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().addToBackStack("list")
                .replace(R.id.container, new EventManagersEventsList())
                .commit();
    }
}
