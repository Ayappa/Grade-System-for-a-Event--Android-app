package com.example.grademe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    String x_auth_token,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button manager=findViewById(R.id.manager);
        Button grader=findViewById(R.id.grader);
      setTitle("Grader Me");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        x_auth_token=preferences.getString("x-auth-token","");
        type=preferences.getString("type","");
        Log.d("x_auth_token",x_auth_token);
        Log.d("type",type);

        if(type.contentEquals("manager") && x_auth_token.length() !=0){
            Intent intent=new Intent(getApplicationContext(),EventManager.class);
            startActivity(intent);
        }
       else if(type.contentEquals("grader") && x_auth_token.length() !=0){
            Intent intent=new Intent(getApplicationContext(),Grader.class);
            startActivity(intent);
        }
       else {

            manager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EventManager.class);
                    startActivity(intent);
                }
            });

            grader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Grader.class);
                    startActivity(intent);
                }
            });
        }

    }
}
