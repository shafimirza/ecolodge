package com.example.root.myapplication_eco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class fragment_priority1 extends Fragment {
    public fragment_priority1() {
    }

    protected static final int CONTEXT_MENU_OPTION1 = 1;
    protected static final int CONTEXT_MENU_OPTION2 = 2;
    List<MRowItem> mRowItems;
    private ListView mListView1;
    public View globalview;
    String title;
    String id;
    String description;
    String schedule;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.priority_list1, container, false);
        globalview = rootView;
        new GetTask().execute();
        return rootView;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(mRowItems.get(info.position).getTitle() + ":Select Option");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION1, 0, "images");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION2, 1, "work status");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView available = (TextView) mListView1.getChildAt(info.position).findViewById(R.id.Available);
        switch (item.getItemId()) {
            case CONTEXT_MENU_OPTION1:
                Intent intent = new Intent(getActivity(), Maintenance_Work_Screen.class);
                intent.putExtra("ID",mRowItems.get(info.position).getId());
                intent.putExtra("Title",mRowItems.get(info.position).getTitle());
                intent.putExtra("Description",mRowItems.get(info.position).getDescription());
                intent.putExtra("Time",mRowItems.get(info.position).getTime());
                startActivity(intent);
                return true;
            case CONTEXT_MENU_OPTION2:
                mListView1.getChildAt(info.position).setBackgroundResource(R.color.red);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    public class MRowItem {
        private String title;
        private String description;
        private String time;
        private String id;

        public MRowItem(String title, String description, String time, String id) {
            this.title = title;
            this.description = description;
            this.time = time;
            this.id = id;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return title + "/n" + description + "/n" + time;
        }
    }
    public class GetTask extends AsyncTask<Message.AndroidToServer.dataToBeRequested, Void, Message.ServerToAndroid.returnTaskData> {

        @Override
        protected Message.ServerToAndroid.returnTaskData doInBackground(Message.AndroidToServer.dataToBeRequested... params) {
            // boolean mRun = true;
            Object inMessage;
            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Socket socket = new Socket(serverAddr, Message.SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message.AndroidToServer AS = new Message.AndroidToServer();
                AS.setRequest(Message.AndroidToServer.dataToBeRequested.TASKDATA);
                System.out.println(AS);
                out.writeObject(AS);
                inMessage = in.readObject();
                return (Message.ServerToAndroid.returnTaskData) inMessage;
            } catch (Exception e) {
                e.printStackTrace();
                return new Message.ServerToAndroid.returnTaskData();
            }
        }
        protected void onPostExecute(Message.ServerToAndroid.returnTaskData params) {
            Message.ServerToAndroid.TaskDataClass tdc = params.getNextTask();
            MRowItem item;
            mRowItems = new ArrayList<MRowItem>();
            System.out.println("ENTERED OnPostExecute");
            while (tdc != null) {
                title = new String(tdc.getTitle().toString());
                description = new String(tdc.getDescription().toString());
                id = new String(String.valueOf(tdc.GetTaskID()));
                schedule=  new String( String.valueOf(tdc.getSpin()));
                item = new MRowItem(title, description,schedule, id);
                mRowItems.add(item);
              tdc = params.getNextTask();
            }
            mListView1 = (ListView) globalview.findViewById(R.id.listview1);
            MListViewAdapter adapter = new MListViewAdapter(getActivity().getApplicationContext(),
                    R.layout.priority_list1, mRowItems);
            mListView1.setAdapter(adapter);
           registerForContextMenu(mListView1);
            adapter.notifyDataSetChanged();
        }

    }
    private class MListViewAdapter extends ArrayAdapter<MRowItem> {
        Context context;
        public MListViewAdapter(Context context, int fragment_maintenance, List<MRowItem> mRowItems) {
            super(getActivity().getApplicationContext(), fragment_maintenance, mRowItems);
            this.context = context;
            notifyDataSetChanged();
        }
        private class ViewHolder {
            TextView txtTitle;
            TextView txtDescription;
            TextView txtTime;
            TextView taskno;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            MRowItem rowItem = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.priority_list_item1, null);
                holder = new ViewHolder();
                holder.taskno = (TextView) convertView.findViewById(R.id.TaskNo);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.Task);
                holder.txtDescription = (TextView) convertView.findViewById(R.id.Description);
                holder.txtTime = (TextView) convertView.findViewById(R.id.Time);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.taskno.setText(rowItem.getId());
            holder.txtTitle.setText(rowItem.getTitle());
            holder.txtDescription.setText(rowItem.getDescription());
            holder.txtTime.setText(rowItem.getTime());
            return convertView;
        }
    }
}

