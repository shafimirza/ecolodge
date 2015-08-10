package com.example.root.myapplication_eco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
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
 * Created by root on 8/6/15.
 */
public class Maintenance_Work_Screen extends ActionBarActivity {
  TextView Id,Title,Description,Time;
    private Gallery mwlistview1;
    private Gallery mwlistview2;
    private Gallery mwlistview3;
   private List<MWRowItem> mwRowItems;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_work_screen);
   // Intent i = getIntent();
    //String id = i.getStringExtra("ID");
   // String title = i.getStringExtra("Title");
   // String description = i.getStringExtra("Description");
   // String time = i.getStringExtra("Time");
   // Id.findViewById(R.id.TitleNo_);
  // Id.setText("NO."+id);
    //Title.findViewById(R.id.MwTitle);
   // Title.setText("TITLE:"+title);
   // Description.findViewById(R.id.MWDescription);
   // Description.setText("DESCRIPTION"+description);
   // Time.findViewById(R.id.MwTime);
    //Time.setText(time);
     new  GetTask().execute();
}
    public class MWRowItem{
        private Bitmap image1;
        public MWRowItem(Bitmap image1) {
            this.image1 = image1;
        }
        public Bitmap getImage1() {
            return image1;
        }
        public void setImage1() {
            this.image1 = image1;        }
        public Bitmap toBitmap() {return image1;}

    }
        public class GetTask extends AsyncTask<Message.AndroidToServer.dataToBeRequested,Void,Message.ServerToAndroid.returnTaskImageData> {
            @Override
            protected Message.ServerToAndroid.returnTaskImageData doInBackground(Message.AndroidToServer.dataToBeRequested... params) {
                // boolean mRun = true;
                Object inMessage;
                try {
                    InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                    Socket socket = new Socket(serverAddr, SERVERPORT);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Message.AndroidToServer AS = new Message.AndroidToServer();
                    AS.setRequest(Message.AndroidToServer.dataToBeRequested.TASKIMAGEDATA);
                    System.out.println(AS);
                    out.writeObject(AS);
                    inMessage = in.readObject();
                    return (Message.ServerToAndroid.returnTaskImageData) inMessage;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print(e);
                    return new Message.ServerToAndroid.returnTaskImageData();
                }
            }
            protected void onPostExecute(Message.ServerToAndroid.returnTaskImageData params) {
                Message.ServerToAndroid.TaskImageClass tidc = params.getNextImage();
                MWRowItem item;
                mwRowItems = new ArrayList<MWRowItem>();
                while (tidc != null) {
                    byte[] bytes = tidc.getFile();
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    item = new MWRowItem(bm);
                    mwRowItems.add(item);
                    tidc = params.getNextImage();
                }
                mwlistview1 = (Gallery) findViewById(R.id.mwlistview1);
                mwlistview2 = (Gallery) findViewById(R.id.mwlistview2);
                mwlistview3 = (Gallery) findViewById(R.id.mwlistview3);
                MWListViewAdapter adapter = new MWListViewAdapter(getApplicationContext(),
                      R.layout.maintenance_work_screen, mwRowItems);
                mwlistview1.setAdapter(adapter);
                mwlistview2.setAdapter(adapter);
                mwlistview3.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                }
            }
            private class MWListViewAdapter extends ArrayAdapter<MWRowItem> {
                Context context;
                public MWListViewAdapter(Context context, int activity_socket_input_test, List<MWRowItem> mwRowItems) {
                    super(getApplicationContext(), activity_socket_input_test, mwRowItems);
                    this.context = context;
                    notifyDataSetChanged();
                }
                private class ViewHolder {
                    ImageView image;
                }
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewHolder holder = null;
                    MWRowItem rowItem = getItem(position);
                    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.mw_image, parent, false);
                        holder = new ViewHolder();
                        holder.image = (ImageView) convertView.findViewById(R.id.image1);
                        convertView.setTag(holder);
                    } else
                        holder = (ViewHolder) convertView.getTag();
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    holder.image.setMinimumHeight(dm.heightPixels);
                    holder.image.setMinimumWidth(dm.widthPixels);
                    holder.image.setImageBitmap(rowItem.getImage1());
                    holder.image.setOnClickListener(new OnImageClickListener(position));
                    return convertView;
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maintenance_work_screen, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class OnImageClickListener implements View.OnClickListener {
        int _postion;
        // constructor
        public OnImageClickListener(int position) {
            this._postion = position;
        }
        @Override
        public void onClick(View v) {
            // on selecting gallery view image
            // launch full screen activity
            Intent i = new Intent(Maintenance_Work_Screen.this, FullScreenViewActivity.class);
            i.putExtra("position", _postion);
            startActivity(i);
        }
    }}
