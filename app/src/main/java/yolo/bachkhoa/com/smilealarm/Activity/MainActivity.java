package yolo.bachkhoa.com.smilealarm.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import yolo.bachkhoa.com.smilealarm.Fragment.AlarmFragment;
import yolo.bachkhoa.com.smilealarm.R;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private String[] tabName = {
            "Alarm",
            "TimeLine",
            "Friend"
    };

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    @Override
    public void onBackPressed(){
        this.finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO init singleton model
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("SmileLogin", "test");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_attach_money_white_24px);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_account_balance_white_24px);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_stars_white_24px);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(tabName[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(0);
        getSupportActionBar().setTitle(tabName[mViewPager.getCurrentItem()]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("YOLO", "getItem: " + position);
            switch (position) {
                case 0:
                    return AlarmFragment.newInstance();
                case 1:
                    return AlarmFragment.newInstance();
                case 2:
                    return AlarmFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabName.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position];
        }
    }
}
