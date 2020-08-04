package com.example.grademe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class Grader extends AppCompatActivity implements GraderLogin.OnFragmentInteractionListener,
        GradingTeams.OnFragmentInteractionListener,GraderForgetPassword.OnFragmentInteractionListener ,
           GraderCheckEmailForgetPassword.OnFragmentInteractionListener,GradersQuestions.OnFragmentInteractionListener{
    SharedPreferences sharedpreferences;
    String x_auth_token,type;
    String [] parse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grader);
        Intent intent = getIntent();
        if(!(intent.getAction() ==null)){
            String action = intent.getAction();
            String data = String.valueOf(intent.getData());
            parse=data.split("&");
            data=parse[1];
            Log.d("data",data);
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        x_auth_token=preferences.getString("x-auth-token","");
        type=preferences.getString("type","");

        if(!(parse ==null) && parse[0].contentEquals("http://www.grader.com/")){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("x-auth-token",parse[1].trim());
            editor.putString("type","grader");
            editor.apply();
            getSupportFragmentManager().beginTransaction().addToBackStack("list")
                    .replace(R.id.gContainer, new GradingTeams())
                    .commit();
        }else  if(!(parse ==null) && parse[0].contentEquals("http://www.graderUpdate.com/") ){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.gContainer, new GraderForgetPassword(parse[1]))
                    .commit();
        }else{
            if(x_auth_token.length()!=0){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gContainer, new GradingTeams())
                        .commit();

            }else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gContainer, new GraderLogin())
                        .commit();
            }
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
        editor.putString("type","grader");
        editor.apply();
        //  Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().addToBackStack("list")
                .replace(R.id.gContainer, new GradingTeams())
                .commit();
    }
}
