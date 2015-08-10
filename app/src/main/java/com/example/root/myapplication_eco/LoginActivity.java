package com.example.root.myapplication_eco;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LoginActivity extends Activity {
    String userPosition;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // User Id Edit View Object
    EditText useridET;
    // Passwprd Edit View Object
    EditText pwdET;
    Button button;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Password = "password";
    public static final String id = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorMsg = (TextView) findViewById(R.id.login_error);

        useridET = (EditText) findViewById(R.id.loginUserid);

        pwdET = (EditText) findViewById(R.id.loginPassword);

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Please wait...");

        prgDialog.setCancelable(false);
    }

    public void login(View view) {
        Log.d("login", "button pressed");
        String uid = useridET.getText().toString();
        String password = pwdET.getText().toString();
        uid=new String("fatima");
        password=new String("familia");
        if (Utility.isNotNull(uid) && Utility.isNotNull(password)) {
            new LoginTask().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public class LoginTask extends AsyncTask<Void, Void, Message.ServerToAndroid.returnLogin> {

        @Override
        protected Message.ServerToAndroid.returnLogin doInBackground(Void... args) {

prgDialog.onStart();            // Get Email Edit View Value
            String uid = useridET.getText().toString();
            // Get Password Edit View Value
            String password = pwdET.getText().toString();

            Object inMessage;
            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Socket socket = new Socket(serverAddr, Message.SERVERPORT);
                Message.AndroidToServer.LogInMessage logInMessage = new Message.AndroidToServer.LogInMessage(uid, password);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                Log.d("LogInTask", "printing uname:" + logInMessage.getUsername() + " password:" + logInMessage.getPassword());
                out.writeObject(logInMessage);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Object data = in.readObject();
                return (Message.ServerToAndroid.returnLogin) data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Message.ServerToAndroid.returnLogin params) {
            System.out.println("ENTERED OnPostExecute");
            String uid = useridET.getText().toString();
            // Get Password Edit View Value
            String password = pwdET.getText().toString();

            String userrole = params.getUSERROLE();
            if (userrole.equals(params.getUSERROLE()))
            {
            String getID = params.getID();
            boolean login = params.getLogin();
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                if (userrole.equals("FRONT_DESK")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                        System.out.print(login);
                    editor.putString(Name, uid);
                    editor.putString(Password, password);
                    editor.putString(id, getID);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), FragmentContainerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    // i.putExtra("ID", getID);
                    //  System.out.println("id =" + getID);
                    startActivity(i);
                }
                // take the app to FRONT DESK SCREEN AND MENU
                else if (userrole.equals("HOUSEKEEPER")) {
                    prgDialog.getProgress();
                    //TAKE THE APP TO HOUSEKEEPER SCREEN AND MENU OPTIONS
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(Name, uid);
                    editor.putString(Password, password);
                    editor.putString(id, getID);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), HousekeepingFragment.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    // i.putExtra("id", getID);
                    //System.out.println("id =" + getID);
                    startActivity(i);
                } else if (userrole.equals("MAINTENANCE")) {
                    //TAKE THE APP TO MAINTENANCE SCREEN AND MENU OPTIONS
                    Intent i = new Intent(getApplicationContext(), MaintenanceFragment.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("id", getID);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot find Employee type in the directory", Toast.LENGTH_LONG).show();

               prgDialog.hide(); }
            }

    }
}}
