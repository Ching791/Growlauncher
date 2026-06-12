package com.rtsoft.growtopia;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {
    static {
        try {
            System.loadLibrary("growtopia");
        } catch (Throwable ignored) {
            // Library is expected after packaging official native files into jniLibs.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView label = new TextView(this);
        label.setText("Growtopia packaged entry (replace with official packaged classes/libs).");
        label.setTextSize(18f);
        label.setPadding(32, 48, 32, 48);
        setContentView(label);
    }
}
