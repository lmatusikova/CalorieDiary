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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.util.Calendar;

public class MainActivity extends NavigationActivity {

    private ViewPager viewPager;
    private SharedPreferences settings;
    private static final String MY_PREF = "My_pref";
    private DateFragmentStatePagerAdapter adapter;
    private static Context context;
    private String date;
    private int calorie;
    private String[] dateArray = new String[1];

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        context = this;
        calorie = settings.getInt("Calorie", 0);

        if (!(settings.getString("Gender", "").equals("Female") || settings.getString("Gender", "").equals("Male"))) {
            Intent intro = new Intent(this, IntroductionActivity.class);
            intro.setFlags(intro.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intro);
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_main, null, false);
            drawerLayout.addView(contentView, 0);
            this.viewPager = (ViewPager) findViewById(R.id.dateViewPager);
            adapter = new MyDateAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(DatePage.getPositionForDay(Calendar.getInstance()));

            Calendar calendar = Calendar.getInstance();
           // calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 19);
            calendar.set(Calendar.MINUTE, 34);
            calendar.set(Calendar.SECOND, 0);
         //   calendar.set(Calendar.AM_PM,Calendar.PM);

            Intent myIntent = new Intent(this, NotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
          //  alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        //    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
           // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class MyDateAdapter extends DateFragmentStatePagerAdapter {

        public MyDateAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        //vráti konkrétnu inštanciu fragmentu na danej pozícii
        @Override
        public Fragment getItem(int position) {
            date = getPageTitle(position).toString();
            dateArray[0] = date;
            return DateFragment.newInstance(date, calorie);

        }

        @Override
        public Fragment getRegisteredFragment(int position) {
            return super.getRegisteredFragment(position);
        }

        //vrati pocet fragmentov
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


