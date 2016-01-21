package com.baxter.githubexplore.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baxter.githubexplore.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new UserFragment(), this.toString())
                    .commit();
        }
    }
}
