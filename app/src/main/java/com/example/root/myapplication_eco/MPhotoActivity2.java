package com.example.root.myapplication_eco;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MPhotoActivity2 extends ActionBarActivity {
    private ListView mListView1, mListView2;

    private String[] data1 ={"Hiren", "Pratik", "Dhruv", "Narendra", "Piyush", "Priyank"};
    private String[] data2 ={"Kirit", "Miral", "Bhushan", "Jiten", "Ajay", "Kamlesh"};

    /* private Uri pic;
    ArrayList<android.media.Image> imageIDs;
     ScrollView parent;
      private class Image
     {
         public String status;
         public Bitmap bitmap;
         Image(String status, Bitmap bitmap)
         {
             this.status = status;
             this.bitmap = bitmap;
         }
     }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mphoto_activity2);
        mListView1 = (ListView)findViewById(R.id.listView1);
        mListView2 = (ListView)findViewById(R.id.listView2);

        mListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data1));
        mListView2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2));

        ListUtils.setDynamicHeight(mListView1);
        ListUtils.setDynamicHeight(mListView2);
    }


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
        // get the listview
        /* private static final int SUCCESS = 1;
    public void picClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, SUCCESS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUCCESS) {
            if (resultCode == RESULT_OK) {
                pic = data.getData();
                Bitmap bmPic = (Bitmap) data.getExtras().get("data");
                PictureTask background = new PictureTask(this);
                background.execute(bmPic);
            }
        }
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Picture Task~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
 /*   private class PictureTask extends AsyncTask<Bitmap, Void, Void> {
        private Context context;
        private PictureTask(Context c) {
            context = c;
        }
        @Override
        protected Void doInBackground(Bitmap... args) {
            Bitmap download = null;
            String line = null;
            //compress and encode
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            args[0].compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] byte_arr = stream.toByteArray();
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy_hhmmss");
            String timeString = s.format(new Date());
            Log.d("Time", timeString);
            //upload
            try{
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.AndroidToServer.PictureMessage pictureMessage = new Message.AndroidToServer.PictureMessage(byte_arr, timeString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
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
                return (Message.ServerToAndroid.returnTaskImageData)inMessage;
            } catch (Exception e){
                e.printStackTrace();
                return  new Message.ServerToAndroid.returnTaskImageData();
            }
        }
        protected void onPostExecute(Message.ServerToAndroid.returnTaskImageData params) {
            Message.ServerToAndroid.TaskImageClass tidc = params.getNextImage();
            System.out.println("ENTERED OnPostExecute");
            while (tidc != null) {
               // imageIDs = new ArrayList<>(tdc);
                System.out.println(imageIDs);
                tidc = params.getNextImage();
            }
           // final Integer[] finalimageIDs = imageIDs;
            Gallery gallery = (Gallery) findViewById(R.id.gallery1);
           // gallery.setAdapter(new ImageAdapter(MPhotoActivity2.this));
            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Toast.makeText(getBaseContext(), "pic" + (position + 1) + " selected",
                            Toast.LENGTH_SHORT).show();
                    ImageView imageView = (ImageView) findViewById(R.id.image1);
                  //  imageView.setImageResource(finalimageIDs[position]);
                }
            });}}
            public  class ImageAdapter extends BaseAdapter {
                private Context context;
                int numoffiles;
                private int itemBackground;
                File[] paths;
                public ImageAdapter(Context c, File[] paths) {
                    context = c;// sets a grey background; wraps around the images
                    this.paths= paths;
                    numoffiles=paths.length;
                    TypedArray a = obtainStyledAttributes(R.styleable.Theme);
                    itemBackground = a.getResourceId(R.styleable.ActionBar_background, 0);
                    a.recycle();
                }
                // returns the number of images
                public int getCount() {
                    return numoffiles;
                }
                // returns the ID of an item
                public Object getItem(int position) {
                    return paths[position];
                }
                // returns the ID of an item
                public long getItemId(int position) {
                    return 0;
                }
                // returns an ImageView view
                public View getView(int position, View convertView, ViewGroup parent) {
                    FileInputStream is;
                    ImageView imageView = new ImageView( context);
                    File imgfile= paths[position];
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 8;
                    try{
                        is = new FileInputStream(imgfile);
                        Bitmap myBitamp = BitmapFactory.decodeStream(is, null, opt);
                        imageView.setImageBitmap(myBitamp);
                    try {
                        is.close();
                    }catch (IOException e){e.printStackTrace();}}
                        catch (FileNotFoundException e){
                        e.printStackTrace();}
                        //imageView.setImageResource(imageIDs[position]);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new Gallery.LayoutParams(150, 150));
                    imageView.setBackgroundResource(itemBackground);
                    return imageView;
                }
            }*/
