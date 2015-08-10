package com.example.root.myapplication_eco;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class FragmentContainerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    Intent i=  getIntent();
    String ID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        i.getIntExtra("id", Integer.parseInt(ID));

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        //android.app.FragmentManager fMan = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        //android.app.Fragment display;

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section0);
                FrontDeskFragment fdf= new FrontDeskFragment();
                android.app.FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.container,fdf).commit();
                break;
            case 2:
                mTitle = getString(R.string.title_section1);
                Intent maintenance = new Intent(getApplicationContext(),MaintenanceFragment.class);
                maintenance.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maintenance);
                break;
            case 3:
                mTitle = getString(R.string.title_section2);
                Intent user = new Intent(getApplicationContext(),CreateUserFragment.class);
                user.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(user);
                break;
            case 4:
                mTitle = getString(R.string.title_section3);
                Intent hk = new Intent(getApplicationContext(),HousekeepingFragment.class);
                hk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(hk);
                break;
            case 5:
                mTitle = getString(R.string.title_section4);
                Intent task = new Intent(getApplicationContext(),TasksFragment.class);
               task.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(task);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.navigation_drawer, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();
        int id = item.getItemId();
        if (id == R.id.log_out){
        //noinspection SimplifiableIfStatement
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login Activity
                getApplicationContext().startActivity(i);
            new logOut().execute();
                return true;


       // return super.onOptionsItemSelected(item);
    }
        return false;
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((FragmentContainerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    public class logOut extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            // Get Last_name Edit View Value
            // EditText eTitle = (EditText) findViewById(R.id.tasktitle);


            String tag = "CreateTaskTask";
            try {
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


