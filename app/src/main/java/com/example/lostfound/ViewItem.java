package com.example.randomgenerator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RandomNumberGenerator extends AppCompatActivity {

    TextView randomNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number_generator);

        randomNumberTextView = findViewById(R.id.random_number_text_view);
        Button generateButton = findViewById(R.id.generate_button);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomNumber();
            }
        });
    }

    private void generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100); // Generates a random number between 0 and 99
        randomNumberTextView.setText(String.valueOf(randomNumber));
    }
}
