package com.example.guesstheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class HackerMode extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Date date;
    private int score = 0;
    private final Data QuestionSet=new Data();
    private ArrayList<String> OptionList=new ArrayList<>();
    private View Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker_mode);
        Vibrator vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        radioGroup = (RadioGroup) findViewById(R.id.HradioGroup);
        Layout=findViewById(R.id.Background);
        if(date==null||QuestionSet==null){
             AssignQuestion();
             PrintQuestion();
        }
        Button Lock = (Button) findViewById(R.id.Hlock);
        Button Finish = (Button) findViewById(R.id.Hfinish);

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HackerMode.this, result.class);
                intent.putExtra("score", score);
                startActivity(intent);
            }
        });
        Lock.setVisibility(View.INVISIBLE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Lock.getVisibility() == View.INVISIBLE)
                    Lock.setVisibility(View.VISIBLE);
            }
        });
        Lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateScore();
                if (isCorrectAnswer()) {
                    UpdateColorToGreen();
                    vibrator.vibrate(100);
                    Lock.setVisibility(View.INVISIBLE);
                } else {
                    UpdateColorToRed();
                    vibrator.vibrate(500);
                }
            }
        });
    }

    public void PrintQuestion() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setText(OptionList.get(i));
        }
        TextView Question = (TextView) findViewById(R.id.HQuestion);
        Question.setText("" + date.getDD() + "." + date.getMM() + "." + date.getYY() + "  Guess The Day");
    }
    public void AssignQuestion(){
        QuestionSet.AssignDate();
        date = QuestionSet.getDate();
        if(OptionList.size()!=0)
        OptionList.clear();
        OptionList = QuestionSet.getoptionList();
        Collections.shuffle(OptionList);
    }

    public boolean isCorrectAnswer() {
        boolean isCorrect = false;
        if (((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString() == date.getDay())
            isCorrect = true;
        return isCorrect;
    }

    public void UpdateScore() {
        if (isCorrectAnswer())
            score += 5;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SelectedKey",radioGroup.getCheckedRadioButtonId());
        outState.putInt("Score",score);
        outState.putStringArrayList("OptionList",OptionList);
        outState.putSerializable("Date",date);

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int SelectedID=savedInstanceState.getInt("SelectedKey");
        if(SelectedID!=-1)
            ((RadioButton)findViewById(SelectedID)).setChecked(true);
        score=savedInstanceState.getInt("Score");
        date=(Date)savedInstanceState.getSerializable("Date");
        OptionList=savedInstanceState.getStringArrayList("OptionList");
        PrintQuestion();
    }
    public void UpdateColorToGreen() {
        Layout.setBackgroundColor(ContextCompat.getColor(HackerMode.this,R.color.green));
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Layout.setBackgroundColor(ContextCompat.getColor(HackerMode.this,R.color.tan_background));
                AssignQuestion();
                PrintQuestion();
                radioGroup.clearCheck();
            }
        }.start();

    }
    public void UpdateColorToRed(){
        Layout.setBackgroundColor(ContextCompat.getColor(HackerMode.this,R.color.red));
        new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Layout.setBackgroundColor(ContextCompat.getColor(HackerMode.this,R.color.tan_background));
                Intent intent = new Intent(HackerMode.this, result.class);
                intent.putExtra("score", score);
                intent.putExtra("source","HackerMode");
                startActivity(intent);
            }
        }.start();
    }
    @Override
    public void onBackPressed(){

    }

}
