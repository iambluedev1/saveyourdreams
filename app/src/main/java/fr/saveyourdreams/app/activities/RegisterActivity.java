package fr.saveyourdreams.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.saveyourdreams.app.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
    }


}
