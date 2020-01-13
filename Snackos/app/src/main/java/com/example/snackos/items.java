package com.example.snackos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class items extends AppCompatActivity {
    private FirebaseDatabase snackosmenudb= FirebaseDatabase.getInstance();
    private DatabaseReference snackosmenuref = snackosmenudb.getReference().child("menu");
TextView t1;
ImageView I2;
    LinearLayout containerCopy;
    LinearLayout.LayoutParams containerLayoutParams;
    LinearLayout.LayoutParams image1Params;
    ImageView I1;
    ImageView I3;


public void checkOut(View v){

    Intent myIntent = new Intent(items.this, Address.class);
    items.this.startActivity(myIntent);
}
    private class orderedImage{
        Integer number;
        Bitmap I;
        private orderedImage(Integer i,Bitmap b){
            this.number=i;
            this.I= b;
        }
        public Integer getImageNumber(){
            return this.number;
        }
        public Bitmap getImage(){
            return this.I;
        }
    }
    private class item{
        int number;
        String name;
        private item(int i,int o, String s){
            this.number=i;
            this.name=s;
        }
        public int getNumber(){
            return this.number;
        }
        public String getName(){
            return this.name;
        }
        private void incrementNumber(){
this.number++;
        }
        private void decrementNumber(){
            if(this.number>0){
            this.number--;}
        }

    }
    private class pack{
        String price;
        String name;
        private pack(String p,String s){
            this.name=s;
            this.price=p;

        }

        public String getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }
    }
HashMap<Integer,item> purchasedList =new HashMap<>();

protected class downloadImage extends AsyncTask<Integer,orderedImage,Void>{

    @Override
    protected Void doInBackground(Integer... integers) {
        try {
            URL url = new URL("https://upload.wikimedia.org/wikipedia/en/a/aa/Bart_Simpson_200px.png");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream in= connection.getInputStream();
            Bitmap image= BitmapFactory.decodeStream(in);
            publishProgress(new orderedImage(integers[0],image));
        }
        catch (Exception e){
            Log.i(" URL Error", "Check the validity of url");
        }

        return null;
    }
    protected void onProgressUpdate(orderedImage... imageObject) {
LinearLayout verticalLayout= findViewById(R.id.linearLayout);
LinearLayout horizontalLayout=(LinearLayout) verticalLayout.getChildAt(imageObject[0].getImageNumber());
ImageView I_1=(ImageView) horizontalLayout.getChildAt(0);
I_1.setImageBitmap(imageObject[0].getImage());
}

}
public int dp2p(int i){
     int pixels= (int) (i*getResources().getDisplayMetrics().density+0.5f);
     return pixels;
}
public void updateUi(){
    TextView t=findViewById(R.id.List);
    String message="Your Cart Has"+"\n";
    for (Map.Entry itemo : purchasedList.entrySet()) {
        // Add some bonus marks
        // to all the students and print it
        item i=(item) itemo.getValue();
        message+=(i.getName()+" : "+i.getNumber()+"\n");
    }
    t.setText(message);
}

    @Override
    protected void onStart() {
        super.onStart();
        snackosmenuref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<pack> packs=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    pack p=new pack(ds.getValue(Long.class).toString(),ds.getKey());
                    packs.add(p);
                }
initUi(packs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initUi(ArrayList<pack> p){
        TextView pp=findViewById(R.id.textView3);
        pp.setBackgroundResource(R.drawable.rect);
        LinearLayout linearLayout= findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        for(int i=0;i<p.size();i++){
            containerCopy =new LinearLayout(this);
            containerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            containerLayoutParams.setMargins(0,0,0,10);
            containerCopy.setLayoutParams(containerLayoutParams);
            containerCopy.setOrientation(LinearLayout.HORIZONTAL);
            image1Params = new LinearLayout.LayoutParams(dp2p(120),dp2p(50),1f);
            I1= new ImageView(this);
//
            I1.setLayoutParams(image1Params);
            new downloadImage().execute(i);
            Log.i("SEE",""+getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams t1Params = new LinearLayout.LayoutParams(dp2p(290),dp2p(50),1f);
            LinearLayout.LayoutParams i2Params = new LinearLayout.LayoutParams(dp2p(80),dp2p(50),1f);
            LinearLayout.LayoutParams i3Params = new LinearLayout.LayoutParams(dp2p(80),dp2p(50),1f);
            t1= new TextView(this);
            t1.setLayoutParams(t1Params);
            t1.setText(p.get(i).getName()+"\n"+"Price:"+p.get(i).getPrice());
            t1.setGravity(Gravity.CENTER);
            I2= new ImageView(this);
            I2.setTag(i);
            I2.setImageResource(R.drawable.plus);
            I2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LinearLayout tempContainer= (LinearLayout) v.getParent();
                    TextView t= (TextView) tempContainer.getChildAt(1);
                    if(purchasedList.containsKey(Integer.parseInt(v.getTag().toString()))){
                        purchasedList.get(v.getTag()).incrementNumber();
                    }
                    else{
                        purchasedList.put(Integer.parseInt(v.getTag().toString()),new item(1,Integer.parseInt(v.getTag().toString()),t.getText().toString()));
                    }
                    updateUi();
//Log.i("HelloSeeMee",purchasedList.get(Integer.parseInt(v.getTag().toString())).getName()+purchasedList.get(Integer.parseInt(v.getTag().toString())).getNumber());
                }
            });
//        I2.setScaleType(ImageView.ScaleType.FIT_XY);
            I2.setLayoutParams(i2Params);
            I3= new ImageView(this);
            I3.setTag(i);
            I3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout tempContainer= (LinearLayout) v.getParent();
                    TextView t= (TextView) tempContainer.getChildAt(1);
                    if(purchasedList.containsKey(Integer.parseInt(v.getTag().toString()))){
                        purchasedList.get(v.getTag()).decrementNumber();
                    }
                    updateUi();
                }
            });
//        I3.setScaleType(ImageView.ScaleType.FIT_XY);
            I3.setLayoutParams(i3Params);
            I3.setImageResource(R.drawable.minus);
            containerCopy.addView(I1);
            containerCopy.addView(t1);
            containerCopy.addView(I2);
            containerCopy.addView(I3);
            linearLayout.addView(containerCopy);}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
//        LinearLayout container= findViewById(R.id.container);
            }
}
