package com.example.guesstheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=(TextView)findViewById(R.id.textView2);
        UpdateHighScore();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NormalMode.class);
                startActivity(intent);
            }
        });
        TextView hackerMode=(TextView)findViewById(R.id.textView);
        hackerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HackerMode.class);
                startActivity(intent);
            }
        });
        TextView hackerModePP=(TextView)findViewById(R.id.HPP);
        hackerModePP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HackerModePP.class));
            }
        });
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
    public void UpdateHighScore(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int HighestScore=sharedPreferences.getInt("HighestScore",0);
        TextView highestScore=(TextView)findViewById(R.id.highestScore);
        highestScore.setText("Highest Score(HPP): "+HighestScore);
    }
    @Override
    protected void onResume(){
        super.onResume();
        UpdateHighScore();
    }

}