package com.example.guesstheday;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

public class NormalMode extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Date date;
    private int score = 0;
    private Data QuestionSet=new Data();
    private ArrayList<String> OptionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_mode);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        AssignQuestion();
        PrintQuestion();
        Button Lock = (Button) findViewById(R.id.lock);
        Button Finish = (Button) findViewById(R.id.finish);

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NormalMode.this, result.class);
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
                    AssignQuestion();
                    PrintQuestion();
                    radioGroup.clearCheck();
                    Lock.setVisibility(View.INVISIBLE);
                } else {
                    Intent intent = new Intent(NormalMode.this, result.class);
                    intent.putExtra("score", score);
                    intent.putExtra("source","NormalMode");
                    startActivity(intent);
                }
            }
        });
    }

    public void PrintQuestion() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setText(OptionList.get(i));
        }
        TextView Question = (TextView) findViewById(R.id.Question);
        Question.setText("" + date.getDD() + "." + date.getMM() + "." + date.getYY() + "  Guess The Day");
        OptionList.clear();
    }
    public void AssignQuestion(){
        QuestionSet.AssignDate();
        date= QuestionSet.getDate();
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
    public void onBackPressed(){

    }

}