package org.covid19.live.module.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.covid19.live.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.dashboard_title);
    }
}
