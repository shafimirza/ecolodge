package com.example.root.myapplication_eco;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrontDeskFragment extends Fragment {

    public View globalView;
    public ProgressDialog prDialog;
    protected static final int CONTEXT_MENU_OPTION1 = 1;
    protected static final int CONTEXT_MENU_OPTION2 = 2;
    protected static final int CONTEXT_MENU_OPTION3 = 3;
    public static final String[] room = new String[]
            {"101", "102", "103", "104", "105", "106", "107", "108",
                    "111", "112", "113", "114", "115", "116", "117", "118",
                    "200", "201", "202", "203", "204", "205", "206", "207", "208",
                    "210", "211", "212", "213", "214", "215", "216", "217", "218"};
    ListView listView;
    List<RowItem> rowItems;

    public String[] availability = new String[room.length];

    Button button;


    public FrontDeskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_front_desk, container, false);
        globalView = rootView;
        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < room.length; i++) {

            RowItem item = new RowItem(room[i], "Available", "Nk", "Clean");
            rowItems.add(item);
        }

        listView = (ListView) globalView.findViewById(R.id.listview1);
        ListViewAdapter adapter = new ListViewAdapter(getActivity(),
                R.layout.fragment_front_desk, rowItems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //prDialog.hide();
        registerForContextMenu(listView);
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(room[info.position] + ":Select Option");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION1, 0, "Checked Out");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION2, 1, "Clean");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION3, 2, "In House");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView available = (TextView) listView.getChildAt(info.position).findViewById(R.id.Available);
        switch (item.getItemId()) {
            case CONTEXT_MENU_OPTION1:
                listView.getChildAt(info.position).setBackgroundResource(R.color.red);
                //Intent i = new Intent(getActivity().getApplicationContext(), HousekeepingFragment.class);
                switch (available.getText().toString()) {
                    case ("Available"):
                        Toast.makeText(getActivity(), "Error: Can't check out if room is AVAILABLE", Toast.LENGTH_LONG).show();
                        return true;
                    case ("Occupied"):
                        //TODO do server work
                        break;
                    case ("Dirty"):
                        Toast.makeText(getActivity(), "Error: Room already checked out", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }

                return true;
            case CONTEXT_MENU_OPTION2:
                listView.getChildAt(info.position).setBackgroundResource(R.color.Green);
                switch (available.getText().toString()) {
                    case ("Available"):
                        Toast.makeText(getActivity(), "Error: Room already clean", Toast.LENGTH_LONG).show();
                        return true;
                    case ("Occupied"):
                        Toast.makeText(getActivity(), "Error: Can't clean an OCCUPIED room", Toast.LENGTH_LONG).show();
                        return true;
                    case ("Dirty"):
                        Intent i = new Intent(getActivity(), CleanApprovalActivity.class);
                        i.putExtra("room", room[info.position]);
                        //TODO get pictures
                        break;
                    default:
                        return false;
                }

                return true;
            case CONTEXT_MENU_OPTION3:
                listView.getChildAt(info.position).setBackgroundResource(R.color.Return);
                switch (available.getText().toString()) {
                    case ("Available"):
                        //TODO do server work
                        break;
                    case ("Occupied"):
                        Toast.makeText(getActivity(), "Error: Room is OCCUPIED", Toast.LENGTH_LONG).show();
                        return true;
                    case ("Dirty"):
                        Toast.makeText(getActivity(), "Error: Room is DIRTY", Toast.LENGTH_LONG).show();
                        return true;
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public class RowItem {
        private String room;
        private String avail;
        private String roomtype;
        private String clean;

        public RowItem(String room, String avail, String roomtype, String clean) {
            this.room = room;
            this.avail = avail;
            this.roomtype = roomtype;
            this.clean = clean;
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
        public void setavail(String avail) {
            this.avail = avail;
        }
        public void setroomtype(String roomtype) {
            this.roomtype = roomtype;
        }
        public String getroomtype() {
            return roomtype;
        }
        public void setclean(String clean) {
            this.clean = clean;
        }
        public String getclean() {
            return clean;
        }

        @Override
        public String toString() {
            return room + "/n" + avail + "/n" + roomtype + "/n" + clean;
        }

    }

    private class ListViewAdapter extends ArrayAdapter<RowItem> {
        Context context;

        public ListViewAdapter(Context context, int resourceId, List<RowItem> items) {
            super(context, resourceId, items);
            this.context = context;
            notifyDataSetChanged();
        }

        private class ViewHolder {
            TextView txtRoomNo;
            TextView txtAvailable;
            TextView txtRoomtype;
            TextView txtClean;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            RowItem rowItem = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listitem, null);
                holder = new ViewHolder();
                holder.txtRoomNo = (TextView) convertView.findViewById(R.id.RoomNo);
                holder.txtAvailable = (TextView) convertView.findViewById(R.id.Available);
                holder.txtRoomtype = (TextView) convertView.findViewById(R.id.Roomtype);
                holder.txtClean = (TextView) convertView.findViewById(R.id.Clean);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.txtRoomNo.setText(rowItem.getroom());
            holder.txtAvailable.setText(rowItem.getavail());
            holder.txtRoomtype.setText(rowItem.getroomtype());
            holder.txtClean.setText(rowItem.getclean());
            return convertView;
        }
    }
}



