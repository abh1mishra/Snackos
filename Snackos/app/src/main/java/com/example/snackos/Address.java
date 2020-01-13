package com.example.snackos;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Address extends AppCompatActivity {
    private class addressInfo {
        String number;
        String name;
        String address;
        private addressInfo(String l, String n, String a){
            this.address=a;
            this.name=n;
            this.number=l;
        }

        public String getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }
    }
    private FirebaseDatabase snackosAddressdb= FirebaseDatabase.getInstance();
    private DatabaseReference snackosAddressref = snackosAddressdb.getReference().child("Address");
public void Post(View v){
    TextView number=(TextView)findViewById(R.id.phone);
    TextView address=(TextView)findViewById(R.id.address);
    TextView name=(TextView)findViewById(R.id.name);
    String num=number.getText().toString();
    String nam=name.getText().toString();
    String hostel=address.getText().toString();
    addressInfo ainfo=new addressInfo(num,nam,hostel);
    snackosAddressref.push().setValue(ainfo);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

    }
}
