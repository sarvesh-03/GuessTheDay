package com.example.guesstheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class HackerModePP extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Date date;
    private int score = 0;
    private final Data QuestionSet=new Data();
    private long timeSS;
    private ArrayList<String> OptionList=new ArrayList<>();
    private TextView question;
    private TextView timer;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker_mode_pp);
        radioGroup = (RadioGroup) findViewById(R.id.HPPradioGroup);
        question=(TextView)findViewById(R.id.HPPQuestion);
        timer=(TextView)findViewById(R.id.HPPtimer);
        Button Start=(Button)findViewById(R.id.HPPstart);
        Button Next=(Button)findViewById(R.id.HPPlnext);
        if(date==null){
            radioGroup.setVisibility(View.INVISIBLE);//initially to avoid visibility until start button pressed
            Next.setVisibility(View.INVISIBLE);
        }
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssignQuestion();
                PrintQuestion();
                timeSS=300;
                Start.setVisibility(View.GONE);
                StartTimer();
            }
        });
        if(radioGroup.getCheckedRadioButtonId()==-1)
        Next.setVisibility(View.INVISIBLE);//to visible the next button only after the option gets selected

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Next.getVisibility() == View.INVISIBLE)
                    Next.setVisibility(View.VISIBLE);
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateScore();
                AssignQuestion();
                PrintQuestion();
                radioGroup.clearCheck();
                Next.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void PrintQuestion() {
        if(radioGroup.getVisibility()==View.INVISIBLE)
            radioGroup.setVisibility(View.VISIBLE);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setText(OptionList.get(i));
        }
        question.setText("" + date.getDD() + "." + date.getMM() + "." + date.getYY() + "  Guess The Day");
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
        else {
            score -= 2;
            timeSS=timeSS-30;
            countDownTimer.cancel();
            StartTimer();
        }
    }
    public void StartTimer(){
        countDownTimer=new CountDownTimer(timeSS*1000,1000){
            public void onTick(long millisUntilFinished){
                timeSS=millisUntilFinished/1000;

                timer.setText(""+"TIME LEFT  "+String.format("%02d",timeSS/60)+":"+String.format("%02d",timeSS%60)+"\n+5/-2 and -0.5 min");
            }
            public void onFinish(){
                Intent intent=new Intent(HackerModePP.this,result.class);
                intent.putExtra("score",score);
                intent.putExtra("source","HPP");
                startActivity(intent);
            }
        }.start();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SelectedKey",radioGroup.getCheckedRadioButtonId());
        outState.putInt("Score",score);
        outState.putStringArrayList("OptionList",OptionList);
        outState.putSerializable("Date",date);
        outState.putLong("time",timeSS);

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int SelectedID=savedInstanceState.getInt("SelectedKey");
        if(SelectedID!=-1) {
            ((RadioButton) findViewById(SelectedID)).setChecked(true);
            ((Button) findViewById(R.id.HPPlnext)).setVisibility(View.VISIBLE);
        }

        score=savedInstanceState.getInt("Score");
        date=(Date)savedInstanceState.getSerializable("Date");
        OptionList=savedInstanceState.getStringArrayList("OptionList");
        timeSS=savedInstanceState.getLong("time");
        if(date!=null) {//if the user rotates the screen in between the quiz to resume the timer and print the same question
            PrintQuestion();
            StartTimer();
            ((Button)findViewById(R.id.HPPstart)).setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }
    @Override
    public void onBackPressed(){
                                     //disable back button
    }
}