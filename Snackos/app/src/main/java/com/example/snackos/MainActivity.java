package com.example.snackos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
public void itemsfunc(View view){
    Intent myIntent = new Intent(MainActivity.this, items.class);
    myIntent.putExtra("key", "hello"); //Optional parameters
    MainActivity.this.startActivity(myIntent);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
