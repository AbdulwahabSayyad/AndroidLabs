package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    EditText savedEmail;
    SharedPreferences sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);
        savedEmail = findViewById(R.id.editEmail);
        sharedPrefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = sharedPrefs.getString("Key", "");
        savedEmail.setText(savedString);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(btn -> {

            Intent nextPage = new Intent(MainActivity.this, ProfileActivity.class);
            nextPage.putExtra("email", savedEmail.getText().toString());
            startActivityForResult(nextPage, 2);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
        String typedEmail = savedEmail.getText().toString();
        sharedPrefsEditor.putString("Key", typedEmail);
        sharedPrefsEditor.apply();
    }
}
