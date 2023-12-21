package com.concious.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.concious.aidlserver.IMathAidlInterface;


public class MainActivity extends AppCompatActivity {


    IMathAidlInterface iMathAidlInterface;
    private String CLIENT_LOG_TAG = "SERVICE_CONNECTION_CLIENT";
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
           iMathAidlInterface = IMathAidlInterface.Stub.asInterface(iBinder);
            Log.v(CLIENT_LOG_TAG,"Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(CLIENT_LOG_TAG,"Service DisConnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent("AIDLMathService");
        intent.setPackage("com.concious.aidlserver");
        boolean result  = bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        if(result) {
            Log.v("BindResult","Successfull");
        } else {
            Log.v("BindResult","Failed");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button subButton = findViewById(R.id.subbutton);
        Button addButton = findViewById(R.id.addbutton);
        EditText num1Et = findViewById(R.id.num1Text);
        EditText num2Et = findViewById(R.id.num2Text);
        TextView resultView = findViewById(R.id.result);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num1 = Integer.parseInt(num1Et.getText().toString());
                int num2 = Integer.parseInt(num2Et.getText().toString());

                try {
                    int result = iMathAidlInterface.add(num1,num2);
                    resultView.setText("Sum is "+result);
                } catch (RemoteException e) {
                    Toast.makeText(MainActivity.this, "Unable to perform sum", Toast.LENGTH_SHORT).show();
                }catch (NullPointerException e ){
                    Toast.makeText(MainActivity.this, "Null Exception", Toast.LENGTH_SHORT).show();
                }
            }
        });
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num1 = Integer.parseInt(num1Et.getText().toString());
                int num2 = Integer.parseInt(num2Et.getText().toString());

                try {
                    int result = iMathAidlInterface.subtract(num1,num2);
                    resultView.setText("Sub is "+result);
                } catch (RemoteException e) {
                    Toast.makeText(MainActivity.this, "Unable to perform Subtraction", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}