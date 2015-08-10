package com.example.root.myapplication_eco;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.econolodge.econolodgeapp3.Message.SERVERIP;
import static com.econolodge.econolodgeapp3.Message.SERVERPORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends ActionBarActivity {
    List<TaskRowItem> taskRowItems;
    ListView listview;
    View globalView;
    private String tTitle="";
    @Override
    public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tasks);
        new GetTask().execute();
    }
  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
        menu.clear();
        getMenuInflater().inflate(R.menu.tasks_action_bar, menu);
      return true;
  }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("TaskFragment", "onOptionsItemSelected itemid:" + item.getItemId() + "inflaterId:" + R.id.action_add_task);
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d("TaskFragment", "case matched");
                Intent intent = new Intent(this, CreateTaskActivity.class);
                startActivity(intent);
                return true;
            case R.id.log_out:
                Intent lintent = new Intent(this, LoginActivity.class);
                startActivity(lintent);
                return true;
            default:
                return false;
        }
    }
    public class TaskRowItem {
        private String title;
        public TaskRowItem (String title)
        {   this.title=title;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title){
            this.title=title;
        }
        @Override
        public String toString(){
            return title;
        }
    }
    public class GetTask extends AsyncTask<Message.AndroidToServer.dataToBeRequested,Void,Message.ServerToAndroid.returnTaskData> {
        @Override
        protected Message.ServerToAndroid.returnTaskData doInBackground(Message.AndroidToServer.dataToBeRequested... params) {
            Object inMessage;
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                Socket socket = new Socket(serverAddr, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message.AndroidToServer AS = new Message.AndroidToServer();
                AS.setRequest(Message.AndroidToServer.dataToBeRequested.TASKDATA);
                System.out.println(AS);
                out.writeObject(AS);
                inMessage = in.readObject();
                return (Message.ServerToAndroid.returnTaskData)inMessage;
            } catch (Exception e){
                e.printStackTrace();
                return  new Message.ServerToAndroid.returnTaskData();
            }
        }
        protected void onPostExecute(Message.ServerToAndroid.returnTaskData params) {
            Message.ServerToAndroid.TaskDataClass tdc = params.getNextTask();
            String title;
            TaskRowItem item;
            taskRowItems = new ArrayList<TaskRowItem>();
            System.out.println("ENTERED OnPostExecute");
            while(tdc!=null) {
                title = new String(tdc.getTitle().toString());
                item  = new TaskRowItem(title);
                taskRowItems.add(item);
                tdc=params.getNextTask();
            }
            listview = (ListView) globalView.findViewById(R.id.listview1);
            TaskListViewAdapter adapter = new TaskListViewAdapter(getApplicationContext(),
                    R.layout.fragment_tasks, taskRowItems);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            registerForContextMenu(listview);
        }
    }
    private class TaskListViewAdapter extends ArrayAdapter<TaskRowItem> {
        Context context;
        public TaskListViewAdapter(Context context, int fragment_tasks, List<TaskRowItem> taskRowItems) {
            super(getApplicationContext(), fragment_tasks, taskRowItems);
            this.context= context;
            notifyDataSetChanged();
        }
        private class ViewHolder {
            TextView Title;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            TaskRowItem rowItem = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.tasklist_items, null);
                holder= new ViewHolder();
                holder.Title=(TextView) convertView.findViewById(R.id.Task);
                convertView.setTag(holder);
            }else
                holder =(ViewHolder) convertView.getTag();
            holder.Title.setText(rowItem.getTitle());
            return  convertView;
        }
    }
}



