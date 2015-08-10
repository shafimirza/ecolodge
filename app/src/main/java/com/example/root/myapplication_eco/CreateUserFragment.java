package com.example.root.myapplication_eco;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateUserFragment extends ActionBarActivity implements  View.OnClickListener{
    private EditText txtDate, leavingDate, hirDate, fname, lname, citi, zipp, phoneno, e_mail, addres, ss, userid, password, stat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog leaveDialog;
    private DatePickerDialog hireDialog;
    private SimpleDateFormat dateFormatter;
    private Spinner employeetype;
    private Spinner activ;
    ProgressDialog prgDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);        // Inflate the layout for this fragment
       setContentView(R.layout.fragment_create_user);
        dateFormatter = new SimpleDateFormat("yyyy-MM-DD", Locale.US);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Adding Task"); //doesn't work
        prgDialog.setCancelable(false);
        Log.d("CreateTask", "log working");

        fname = (EditText) findViewById(R.id.fName);

        lname = (EditText) findViewById(R.id.lName);

        addres = (EditText) findViewById(R.id.address);
        citi = (EditText) findViewById(R.id.city);

        zipp = (EditText) findViewById(R.id.zip);

        stat = (EditText)findViewById(R.id.state);
        phoneno = (EditText) findViewById(R.id.phone);
        e_mail = (EditText) findViewById(R.id.registerEmail);
        fname = (EditText) findViewById(R.id.fName);
        txtDate = (EditText) findViewById(R.id.txtDate);
        final Calendar newCalendar = Calendar.getInstance();
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             System.out.print("txt date");
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(CreateUserFragment.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        txtDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            }
        });
        leavingDate = (EditText) findViewById(R.id.leavingDate);
        leavingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar leaveCalendar = Calendar.getInstance();
                leaveDialog = new DatePickerDialog(CreateUserFragment.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        leavingDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, leaveCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
               getApplicationContext();
            }
        });
        hirDate = (EditText) findViewById(R.id.hirDate);
        hirDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar hireCalendar = Calendar.getInstance();
                hireDialog = new DatePickerDialog(CreateUserFragment.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        hirDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, hireCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                getApplicationContext();
            }


        });
        ss = (EditText) findViewById(R.id.ssn);
        employeetype = (Spinner) findViewById(R.id.positionspinner);
        activ = (Spinner) findViewById(R.id.activespinner);
        userid = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.registerPassword);
        Button register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                Log.d("RegisterUser", "Test");
                new CreateUserTask().execute();
                Log.d("createtask", "craetetask finished");
                          }
        });

    }
    public void onClick(View view) {
        if (view == txtDate) {
            datePickerDialog.show();
        } else if (view == hirDate) {
            hireDialog.show();
        } else if (view == leavingDate) {
            leaveDialog.show();
        }
    }
    public class CreateUserTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            // Get Last_name Edit View Value
            String last_name = lname.getText().toString();
            // Get First_name Edit View Value
            String first_name = fname.getText().toString();
            String email = e_mail.getText().toString();
            String phone = phoneno.getText().toString();
            String address = addres.getText().toString();
            String city = citi.getText().toString();
            String zip = zipp.getText().toString();
            String state = stat.getText().toString();
            String birthdate = txtDate.getText().toString();
            String leavedate = leavingDate.getText().toString();
            String hiredate = hirDate.getText().toString();
            String ssn = ss.getText().toString();
            String position = employeetype.getSelectedItem().toString();
            String active = activ.getSelectedItem().toString();
            String User = userid.getText().toString();
            String Password = password.getText().toString();


            String tag = "CreateUserTask";
            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.AndroidToServer.RegisterEmployee registerEmployee = new Message.AndroidToServer.RegisterEmployee(last_name, first_name, email, phone, address,
                        city, zip, state, birthdate, hiredate, leavedate, ssn, position, active,User,Password);

                Log.d("Register", "last_name:" + registerEmployee.getLastName() + "first_name" + registerEmployee.getFirstName());
                System.err.println("last_name:" + registerEmployee.getLastName() + "first_name" + registerEmployee.getFirstName());


                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                Log.d(tag, "Created Socket");
                System.err.println("Created Socket");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Log.d(tag, "Created output");
                System.err.println("Created output");

                Log.d("Register", "last_name:" + registerEmployee.getLastName() + "first_name" + registerEmployee.getFirstName());
                out.writeObject(registerEmployee);

                Log.d(tag, "Wrote Output");
                System.err.println("Wrote output");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d("OnpostExecute", "Create user task finished");
        }

    }


}


