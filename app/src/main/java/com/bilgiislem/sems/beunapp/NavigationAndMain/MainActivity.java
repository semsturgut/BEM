package com.bilgiislem.sems.beunapp.NavigationAndMain;


import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.bilgiislem.sems.beunapp.AnaSayfa.Menu1_Fragment_AnaSayfa;
import com.bilgiislem.sems.beunapp.Beu3D.Menu5_Fragment_Beu3D;
import com.bilgiislem.sems.beunapp.Duyurular.Menu2_Fragment_Duyurular;
import com.bilgiislem.sems.beunapp.Neredeyim.Menu3_Fragment_Neredeyim;
import com.bilgiislem.sems.beunapp.Ogrenci.Menu4_Fragment_Ogrenci;
import com.bilgiislem.sems.beunapp.R;
import com.bilgiislem.sems.beunapp.YemekListesi.Menu6_Fragment_YemekListesi;

/*
C:\Users\pc>"C:\Program Files\Java\jdk1.8.0_31\bin\keytool.exe" -list -v -alias
Sems -keystore "C:\Users\pc\keystore.jks" -storepass 2470417m -keypass 2470417m


 for keystore
*/
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        android.app.Fragment objFragment = null;
        android.app.FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
                objFragment = new Menu1_Fragment_AnaSayfa();
                break;
            case 1:
                objFragment = new Menu2_Fragment_Duyurular();
                break;
            case 2:
                objFragment = new Menu3_Fragment_Neredeyim();
                break;
            case 3:
                objFragment = new Menu4_Fragment_Ogrenci();
                break;
            case 4:
                objFragment = new Menu5_Fragment_Beu3D();
                break;
            case 5:
                objFragment = new Menu6_Fragment_YemekListesi();
                break;

        }
        fragmentManager.beginTransaction().replace(R.id.container, objFragment).commit();
        /*update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
       fragmentManager.beginTransaction()
               .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
              .commit();*/
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
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
            getMenuInflater().inflate(R.menu.main, menu);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}