package polytech.carcassonnevisit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import polytech.carcassonnevisit.fragment.MapFragment;
import polytech.carcassonnevisit.R;
import polytech.carcassonnevisit.fragment.RadarFragment;
import polytech.carcassonnevisit.observer.LocatorObserver;
import polytech.carcassonnevisit.service.LocatorService;

public class MainActivity extends AppCompatActivity {

    private MapFragment mapFragment;
    private RadarFragment radarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mapFragment = new MapFragment();
        this.radarFragment = new RadarFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        Intent intent = new Intent(this, LocatorService.class);
        ArrayList<LocatorObserver> locatorObservers = new ArrayList();
        locatorObservers.add(this.radarFragment);
        intent.putExtra("observers", locatorObservers);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[] { "Tab One", getString(R.string.tabRadar) };
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return mapFragment;
                case 1:
                    return radarFragment;
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public View getTabView(int position) {
            int layoutID;
            switch (position) {
                case 0:
                    layoutID = R.layout.map_tab;
                    break;
                case 1:
                    layoutID = R.layout.radar_tab;
                    break;
                default:
                    layoutID = R.layout.map_tab;
                    break;
            }

            View tab = LayoutInflater.from(MainActivity.this).inflate(layoutID, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }

    }
}
