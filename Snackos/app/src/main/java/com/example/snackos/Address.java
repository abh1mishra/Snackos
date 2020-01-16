package com.example.snackos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Address extends AppCompatActivity {
    Integer pr;
    final int UPI_PAYMENT=123;
HashMap<String,String> map =new HashMap<>();
String[] nameItem;
String[] numberItem;
String parentMessage;
    private FirebaseDatabase snackosAddressdb= FirebaseDatabase.getInstance();
public void Post(View v){
    TextView note=(TextView) findViewById(R.id.note);
    String notes=note.getText().toString();
    payUsingUpi("ArcherAk",pr.toString(),"abirkanjilal1998@oksbi",notes);
}
public void postFree(View v){
    TextView number=(TextView)findViewById(R.id.phone);
    TextView address=(TextView)findViewById(R.id.address);
    TextView name=(TextView)findViewById(R.id.name);
    String num=number.getText().toString();
    String nam=name.getText().toString();
    String hostel=address.getText().toString();
    map.put("name",nam);
    map.put("Hostel",hostel);
    map.put("phone",num);
    map.put("price",pr+"");
    for(int i=0;i<numberItem.length;i++){
        map.put(nameItem[i],numberItem[i]);
    }
    DatabaseReference snackosAddressref = snackosAddressdb.getReference().child(parentMessage);
    snackosAddressref.push().setValue(map);
    Button button =(Button) findViewById(R.id.button);
    Button button2 =(Button) findViewById(R.id.button2);
    button.setAlpha(0.0f);
    button2.setAlpha(0.0f);
    Toast.makeText(this,"ThankYou!! Order on its way",Toast.LENGTH_LONG).show();

}

    private void payUsingUpi(String nam, String s, String upiId, String notes) {
        Uri uri=Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa",upiId).appendQueryParameter("pn",nam).appendQueryParameter("tn",notes).appendQueryParameter("am",s).appendQueryParameter("cu","INR").build();
//        Intent upi_pay=new Intent(Intent.ACTION_VIEW);
//        upi_pay.setData(uri);
//        Intent chooser=Intent.createChooser(upi_pay,"pay with");
        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        if(null != intent.resolveActivity(getPackageManager())) {
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        Log.i("ENT",requestCode+"");
            /*
           E/main: response -1
           E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
           E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
           E/UPI: payment successfull: 922118921612
             */

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.i("Switch1",requestCode+"");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
    Log.i("Enter",data.toString());
        if (isConnectionAvailable(this)) {
            String str = data.get(0);
            Log.i("SEE THIS",data.toString());
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.

//                snackosAddressref.push().setValue(map);
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
                TextView number=(TextView)findViewById(R.id.phone);
                TextView address=(TextView)findViewById(R.id.address);
                TextView name=(TextView)findViewById(R.id.name);
                String num=number.getText().toString();
                String nam=name.getText().toString();
                String hostel=address.getText().toString();
                map.put("name",nam);
                map.put("Hostel",hostel);
                map.put("phone",num);
                map.put("price",pr+"");
                for(int i=0;i<numberItem.length;i++){
                    map.put(nameItem[i],numberItem[i]);
                }
                map.put("TXN",data.get(0).toString());

                try{
                    DatabaseReference snackosAddressref = snackosAddressdb.getReference().child(parentMessage);
    snackosAddressref.push().setValue(map);
                    Toast.makeText(this,"ThankYou!! Order on its way",Toast.LENGTH_LONG).show();

                }
                catch (Exception e){
                    Log.i("Yup",e.getMessage());
                }
            }
            else if("Payment cancelled by user.".toLowerCase().equals(paymentCancel.toLowerCase())) {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_address);
        try {
            pr = getIntent().getExtras().getInt("price");
            if(pr<130){
                pr+=10;
            }
            TextView am=(TextView) findViewById(R.id.amount);
            am.setText("Amount: "+pr);
            nameItem=getIntent().getExtras().getStringArray("item");
            numberItem=getIntent().getStringArrayExtra("number");
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        parentMessage=getIntent().getExtras().getString("key");
    }
}

