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


public class fragment_priority2 extends Fragment {
    public fragment_priority2() {
    }
    ListView listview;
    protected static final int CONTEXT_MENU_OPTION1 = 1;
    protected static final int CONTEXT_MENU_OPTION2 = 2;
    List<P2RowItem> p2RowItems;
    private ListView p2ListView1;
    public View globalview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.priority_list2, container, false);
        globalview = rootView;
        new GetTask().execute();
        return rootView;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(p2RowItems.get(info.position).getTitle() + ":Select Option");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION1, 0, "images");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION2, 1, "work status");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView available = (TextView) listview.getChildAt(info.position).findViewById(R.id.Available);
        switch (item.getItemId()) {
            case CONTEXT_MENU_OPTION1:
                listview.getChildAt(info.position).setBackgroundResource(R.color.Green);
                Intent intent = new Intent(getActivity(), MPhotoActivity2.class);
                startActivity(intent);
                return true;
            case CONTEXT_MENU_OPTION2:
                listview.getChildAt(info.position).setBackgroundResource(R.color.red);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    public class P2RowItem {
        private String title;
        public P2RowItem(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        @Override
        public String toString() {
            return title ;
        }
    }
    public class GetTask extends AsyncTask<Message.AndroidToServer.dataToBeRequested, Void, Message.ServerToAndroid.p2returnTaskData> {
        @Override
        protected Message.ServerToAndroid.p2returnTaskData doInBackground(Message.AndroidToServer.dataToBeRequested... params) {
            Object inMessage;
            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Socket socket = new Socket(serverAddr, Message.SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message.AndroidToServer AS = new Message.AndroidToServer();
                AS.setRequest(Message.AndroidToServer.dataToBeRequested.PRIORITY3);
                System.out.println(AS);
                out.writeObject(AS);
                inMessage = in.readObject();
                return (Message.ServerToAndroid.p2returnTaskData) inMessage;
            } catch (Exception e) {
                e.printStackTrace();
                return new Message.ServerToAndroid.p2returnTaskData();
            }
        }
        protected void onPostExecute(Message.ServerToAndroid.p2returnTaskData params) {
            Message.ServerToAndroid.p2TaskDataClass tdc = params.getNextTask();
            String title;
            String id;
            String description;
            P2RowItem item;
            p2RowItems = new ArrayList<P2RowItem>();
            System.out.println("ENTERED OnPostExecute");
            while (tdc != null) {
                title = new String(tdc.getTitle().toString());
                description = new String(tdc.getDescription().toString());
                id = new String(String.valueOf(tdc.GetTaskID()));
                item = new P2RowItem(title);
                p2RowItems.add(item);
                tdc = params.getNextTask();
            }
            p2ListView1 = (ListView) globalview.findViewById(R.id.listview1);
            MListViewAdapter adapter = new MListViewAdapter(getActivity().getApplicationContext(),
                    R.layout.priority_list1, p2RowItems);
            p2ListView1.setAdapter(adapter);
            registerForContextMenu(p2ListView1);
            adapter.notifyDataSetChanged();
        }
        private class MListViewAdapter extends ArrayAdapter<P2RowItem> {
            Context context;
            public MListViewAdapter(Context context, int priority_list2, List<P2RowItem> p2RowItems) {
                super(getActivity().getApplicationContext(), priority_list2, p2RowItems);
                this.context = context;
                notifyDataSetChanged();
            }
            private class ViewHolder {
                TextView txtTitle;
            }
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                P2RowItem rowItem = getItem(position);
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.priority_list_item2, null);
                    holder.txtTitle = (TextView) convertView.findViewById(R.id.prioritytwo);
                    holder = new ViewHolder();
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder) convertView.getTag();
                holder.txtTitle.setText(rowItem.getTitle());
                return convertView;
            }
        }
    }
}
