package sk.upjs.caloriediary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import java.util.Calendar;

public class MainActivity extends NavigationActivity {

    private ViewPager viewPager;
    private SharedPreferences settings;
    private static final String MY_PREF = "My_pref";
    private DateFragmentStatePagerAdapter adapter;
    private static Context context;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        context = this;

        if (!(settings.getString("Gender", "").equals("Female") || settings.getString("Gender", "").equals("Male"))) {
            Intent intro = new Intent(this, IntroductionActivity.class);
            intro.setFlags(intro.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intro);
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_main, null, false);
            drawerLayout.addView(contentView, 0);
            notification();
            final String[] dates = getResources().getStringArray(R.array.dates);
            this.viewPager = (ViewPager) findViewById(R.id.dateViewPager);
            adapter = new MyDateAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);

            viewPager.setCurrentItem(DatePage.getPositionForDay(Calendar.getInstance()));
    }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * co to robi
     */
    public void notification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    public static class MyDateAdapter extends DateFragmentStatePagerAdapter {

        public MyDateAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            long timeForPosition = DatePage.getDayForPosition(position).getTimeInMillis();
            return DateFragment.newInstance(timeForPosition);
        }

        @Override
        public int getCount() {
            return DatePage.DAYS_OF_TIME;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Calendar cal = DatePage.getDayForPosition(position);
            return DatePage.getFormattedDate(context, cal.getTimeInMillis());
        }
    }



}


