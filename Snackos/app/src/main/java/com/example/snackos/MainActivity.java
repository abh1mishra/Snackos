package com.example.snackos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
public void itemsfunc(View view){
    Intent myIntent = new Intent(MainActivity.this, items.class);
    myIntent.putExtra("key", "menu"); //Optional parameters
    MainActivity.this.startActivity(myIntent);
}
    public void preOrder(View view){
        Intent myIntent = new Intent(MainActivity.this, items.class);
        myIntent.putExtra("key", "preOrder"); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);
        ImageView i=(ImageView) findViewById(R.id.Background);
        TextView b=(TextView)findViewById(R.id.Up);
        i.setImageResource(R.drawable.images1);
        ImageView icon=(ImageView) findViewById(R.id.icon);
//        b.setImageResource(R.drawable.images);
    }
}
