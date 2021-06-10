package com.example.guesstheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateHighScore();
        Button hackerModePP=(Button)findViewById(R.id.HPP);
        TextView rules=(TextView)findViewById(R.id.rules);
        rules.setText("RULES:\n"+" 1.  10 seconds Will be given for each Question.\n"+"2.  +5 For each correct answer.\n"+"3.  -2 For wrong answer.\n"+"4.  Knock out quiz");
        hackerModePP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HackerModePP.class);
                intent.putExtra("Source","TimerMode");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
    public void UpdateHighScore(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        int HighestScore=sharedPreferences.getInt("HighestScore",0);
        TextView highestScore=(TextView)findViewById(R.id.highestScore);
        highestScore.setText("Highest Score: "+HighestScore);
    }
    @Override
    protected void onResume(){
        super.onResume();
        UpdateHighScore();
    }

}