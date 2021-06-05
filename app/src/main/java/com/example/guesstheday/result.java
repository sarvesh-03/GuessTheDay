package com.example.guesstheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class result extends AppCompatActivity {
    private int Score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        Intent intent=getIntent();

        Score=intent.getIntExtra("score",0);
        String source=intent.getStringExtra("source");
        int highScore=sharedPreferences.getInt("HighestScore",0);

        if((Score>highScore)&&(source.equals("HPP"))){
            editor.putInt("HighestScore",Score);
            editor.commit();
        }

        ((TextView)findViewById(R.id.score)).setText(""+Score);

    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(result.this,MainActivity.class);
        startActivity(intent);
    }
}