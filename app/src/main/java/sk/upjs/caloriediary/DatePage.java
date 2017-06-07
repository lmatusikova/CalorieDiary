package sk.upjs.caloriediary;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePage {
    public static final Calendar FIRST_DAY_OF_TIME;
    public static final Calendar LAST_DAY_OF_TIME;
    public static final int DAYS_OF_TIME;

    static {
        FIRST_DAY_OF_TIME = Calendar.getInstance();
        //prvy datum ked zapnem appku
        FIRST_DAY_OF_TIME.set(2017, Calendar.JUNE, 5);
        LAST_DAY_OF_TIME = Calendar.getInstance();
        LAST_DAY_OF_TIME.set(2020, Calendar.DECEMBER, 31);
        DAYS_OF_TIME = 25;
    }

    public static int getPositionForDay(Calendar day) {
        if (day != null) {
            return (int) ((day.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis())
                    / (24 * 60 * 60 * 1000));
        }
        return 0;
    }

    public static Calendar getDayForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, position);
        return cal;
    }


    public static String getFormattedDate(Context context, long date) {
        final String defaultPattern = "yyyy-MM-dd";

        String pattern = null;
        if (context != null) {
            pattern = context.getString(R.string.date_format);
        }
        if (pattern == null) {
            pattern = defaultPattern;
        }
        SimpleDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = new SimpleDateFormat(pattern);
        } catch (IllegalArgumentException e) {
            simpleDateFormat = new SimpleDateFormat(defaultPattern);
        }

        return simpleDateFormat.format(new Date(date));
    }
}
