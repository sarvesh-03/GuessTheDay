package com.example.guesstheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private View layout;
    private TextView scoreText;
    private Button Start;
    private Button Next;
    private boolean isReachedEnd= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker_mode_pp);
        radioGroup = (RadioGroup) findViewById(R.id.HPPradioGroup);
        question=(TextView)findViewById(R.id.HPPQuestion);
        timer=(TextView)findViewById(R.id.HPPtimer);
        Start=(Button)findViewById(R.id.HPPstart);
        Next=(Button)findViewById(R.id.HPPlnext);
        layout=findViewById(R.id.Layout);
        scoreText=(TextView)findViewById(R.id.ScoreText);
        scoreText.setVisibility(View.INVISIBLE);
        if(date==null){
            radioGroup.setVisibility(View.INVISIBLE);//initially to avoid visibility until start button pressed
            Next.setVisibility(View.INVISIBLE);
        }
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssignQuestion();
                PrintQuestion();
                timeSS=10;
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
                if(isCorrectAnswer()) {
                    AssignQuestion();
                    PrintQuestion();
                    radioGroup.clearCheck();
                    Next.setVisibility(View.INVISIBLE);
                }
                else PrintScore();

            }
        });
    }
    public void PrintQuestion() {
        String suffix="th";
        if((date.getDD()%10==1)&&(date.getDD()!=11))
            suffix="st";
        else if((date.getDD()%10==2)&&(date.getDD()!=12))
            suffix="nd";
        else if((date.getDD()%10==3)&&(date.getDD()!=13))
            suffix="rd";
        if(radioGroup.getVisibility()==View.INVISIBLE)
            radioGroup.setVisibility(View.VISIBLE);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setText(OptionList.get(i));
        }
        question.setText("" + date.getDD() + suffix +" "+ date.getMM() + ", " + date.getYY() + " Is ______");
        ((TextView)findViewById(R.id.answer)).setText(date.getDay());
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
        Vibrator vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (isCorrectAnswer()){
            UpdateColorToGreen();
            score += 5;
            timeSS=10;
            countDownTimer.cancel();
            StartTimer();
            vibrator.vibrate(100);}
        else {
            UpdateColorToRed();
            score -= 2;
            vibrator.vibrate(500);
            PrintScore();

        }
    }
    public void StartTimer(){

        countDownTimer = new CountDownTimer(timeSS * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeSS = millisUntilFinished / 1000;

                timer.setText("" + "TIME LEFT  " + String.format("%02d", timeSS / 60) + ":" + String.format("%02d", timeSS % 60) + "\nScore:"+score);
            }

            public void onFinish() {
                    PrintScore();
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
        outState.putBoolean("isReachedEnd",isReachedEnd);

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isReachedEnd=savedInstanceState.getBoolean("isReachedEnd");
        if(!isReachedEnd) {
            int SelectedID = savedInstanceState.getInt("SelectedKey");
            if (SelectedID != -1) {
                ((RadioButton) findViewById(SelectedID)).setChecked(true);
                ((Button) findViewById(R.id.HPPlnext)).setVisibility(View.VISIBLE);
            }

            score = savedInstanceState.getInt("Score");
            date = (Date) savedInstanceState.getSerializable("Date");
            OptionList = savedInstanceState.getStringArrayList("OptionList");
            timeSS = savedInstanceState.getLong("time");
            if (date != null) {//if the user rotates the screen in between the quiz to resume the timer and print the same question
                PrintQuestion();
                StartTimer();
                ((Button) findViewById(R.id.HPPstart)).setVisibility(View.GONE);
            }
        }
        else{
            score = savedInstanceState.getInt("Score");
            PrintScore();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }
    public void UpdateColorToRed(){

        layout.setBackgroundColor(ContextCompat.getColor(HackerModePP.this,R.color.red));
        new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                layout.setBackgroundColor(ContextCompat.getColor(HackerModePP.this,R.color.tan_background));

            }
        }.start();
    }
    @Override
    public void onBackPressed(){
        if(scoreText.getVisibility()==View.VISIBLE)
            startActivity(new Intent(HackerModePP.this,MainActivity.class));//enable back button once score is printed
    }
    public void UpdateColorToGreen() {
        layout.setBackgroundColor(ContextCompat.getColor(HackerModePP.this,R.color.green));
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                layout.setBackgroundColor(ContextCompat.getColor(HackerModePP.this,R.color.tan_background));

            }
        }.start();

    }
    public void PrintScore(){
        radioGroup.setVisibility(View.GONE);
        question.setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.answer)).setVisibility(View.INVISIBLE);
        timer.setVisibility(View.INVISIBLE);
        Start.setVisibility(View.INVISIBLE);
        Next.setVisibility(View.INVISIBLE);
        scoreText.setVisibility(View.VISIBLE);
        scoreText.setText("SCORE"+"\n\n    "+score);
        if(!isReachedEnd)
        isReachedEnd=true;
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int highScore=sharedPreferences.getInt("HighestScore",0);

        if(score>highScore){
            editor.putInt("HighestScore",score);
            editor.commit();
            Toast.makeText(this,"You have a set a new record",Toast.LENGTH_SHORT);
        }
        Toast.makeText(this,"Press Back Button to return to main menu",Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(countDownTimer!=null)
            countDownTimer.cancel();
        if(Start.getVisibility()==View.GONE)
            StartTimer();
    }
}