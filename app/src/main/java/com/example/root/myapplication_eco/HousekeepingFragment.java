package com.example.root.myapplication_eco;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HousekeepingFragment extends ActionBarActivity {
   // public HousekeepingFragment() {
        // Required empty public constructor
    //}
    ListView listView;
    List<HkRowItem> HkrowItems;
    public static final String[] room = new String[]{};
    public static final String[] lines = new String[]{};
    String housekeeper=  new String("housekeeper");
    //  Intent i=  getIntent();
    String name= "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.fragment_housekeeping);
          // ID= i.getExtras().getString("id");
           HkrowItems = new ArrayList<HkRowItem>();
        for (int i = 0; i < room.length; i++) {
            HkRowItem item = new HkRowItem(room[i], lines[i]);
            HkrowItems.add(item);
        }
        // Inflate the layout for this fragment
        listView = (ListView) findViewById(R.id.hklistview);
        hkListViewAdapter adapter = new hkListViewAdapter(getApplicationContext(),
                R.layout.fragment_housekeeping, HkrowItems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
       }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("TaskFragment", "onOptionsItemSelected itemid:" + item.getItemId() + "inflaterId:" + R.id.action_add_task);
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d("TaskFragment", "case matched");
                Intent intent = new Intent(this, CreateTaskActivity.class);
                intent.putExtra("housekeeper", housekeeper);
                startActivity(intent);
                return true;
            case R.id.log_out:

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login Activity
                getApplicationContext().startActivity(i);
                new logOut().execute();
                return true;
            default:
                return false;
        }
    }

    public class HkRowItem {
        private String room;
        private String avail;

        public HkRowItem(String room, String avail) {
            this.room = room;
            this.avail = avail;
        }

        public String getroom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getavail() {
            return avail;
        }

        public void setAvail(String avail) {
            this.avail = avail;
        }

        @Override
        public String toString() {
            return room + "/n" + avail;
        }
    }

    private class hkListViewAdapter extends ArrayAdapter<HkRowItem> {
        Context context;

        public hkListViewAdapter(Context context, int resourceId, List<HkRowItem> items) {
            super(context, resourceId, items);
            this.context = context;
            notifyDataSetChanged();
        }

        private class ViewHolder {
            TextView txtRoomNo;
            TextView txtAvailable;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            HkRowItem rowItem = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.hklist_item, null);
                holder = new ViewHolder();
                holder.txtRoomNo = (TextView) convertView.findViewById(R.id.hkRoomNo);
                holder.txtAvailable = (TextView) convertView.findViewById(R.id.hkAvailable);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.txtRoomNo.setText(rowItem.getroom());
            holder.txtAvailable.setText(rowItem.getavail());
            return convertView;
        }
    }
    public class logOut extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            // Get Last_name Edit View Value
            // EditText eTitle = (EditText) findViewById(R.id.tasktitle);


            String tag = "CreateTaskTask";
            try {
                String ID="";
                SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                ID = sharedpreferences.getString("id", ID);
                System.out.print("ID"+ID);
                editor.clear();
                editor.commit();
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.AndroidToServer.logOut Logout = new Message.AndroidToServer.logOut(ID);

                Log.d("I", "D:" + Logout.getId());
                System.err.println("I" + "D");


                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                Log.d(tag, "Created Socket");
                System.err.println("Created Socket");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Log.d(tag, "Created output");
                System.err.println("Created output");

                //Log.d("task", "title:" + createTask.gettTitle() + "first_name" + createTask.gettDesc());
                out.writeObject(Logout);

                Log.d(tag, "Wrote Output");
                System.err.println("Wrote output");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d("OnpostExecute", "Create task task finished");
        }
    }

}






