package yolo.bachkhoa.com.smilealarm.Activity;

import android.graphics.Bitmap;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yolo.bachkhoa.com.smilealarm.Entity.UserEntity;
import yolo.bachkhoa.com.smilealarm.Fragment.AlarmFragment;
import yolo.bachkhoa.com.smilealarm.Fragment.TimeLineFragment;
import yolo.bachkhoa.com.smilealarm.Fragment.FriendListFragment;
import yolo.bachkhoa.com.smilealarm.Model.AuthenticateModel;
import yolo.bachkhoa.com.smilealarm.Model.EventHandleWithKey;
import yolo.bachkhoa.com.smilealarm.R;
import yolo.bachkhoa.com.smilealarm.Service.UserService;

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
        UserService.getUser("1880362925529801", new EventHandleWithKey<String, UserEntity>() {
            @Override
            public void onSuccess(String key, UserEntity o) {
                Log.d("UserImage", o.getAvatar() + "");
                Log.d("UserName", o.getName() + "");
               // Log.d("UserAlarm", o.getAlarmImage() + "");
            }

            @Override
            public void onError(String o) {

            }
        });

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mViewPager.setCurrentItem(0);
        getSupportActionBar().setTitle("Hello " + UserService.getUserDisplayName().split(" ")[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            AuthenticateModel.getInstance().logoutHandle();
            super.onBackPressed();
        }
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
                    return TimeLineFragment.newInstance();
                case 2:
                    return FriendListFragment.newInstance();
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
