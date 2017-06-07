package sk.upjs.caloriediary;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import at.grabner.circleprogress.CircleProgressView;
import sk.upjs.caloriediary.provider.DayContentProvider;
import sk.upjs.caloriediary.provider.Provider;

public class DateFragment extends Fragment {
    private static final String DATE = "Date";
    private static final String CALORIE = "Calorie";

    private CircleProgressView circle;
    private TextView calorieTextView;
    private TextView breakfastTextView;
    private TextView lunchTextView;
    private TextView dinnerTextView;
    private TextView snacksTextView;
    private TextView waterTextView;
    private String datum;
    String[] dateArray = new String[1];

    private int breakfast = 0;
    private int lunch = 0;
    private int dinner = 0;
    private int snacks = 0;
    private int water = 0;

    public DateFragment() {

    }

    //to co sa vyplni pre dany fragment...
    public static DateFragment newInstance(String date, int calorie) {
        Bundle args = new Bundle();
        args.putString(DATE, date);
        args.putInt(CALORIE, calorie);
        DateFragment dateFragment = new DateFragment();
        dateFragment.setArguments(args);
        return dateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_fragment, container,false);
        return inflater.inflate(R.layout.date_fragment, container, false);
    }

    //tu je to... co bude vzdy na kazdom fragmente..
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circle = (CircleProgressView)view.findViewById(R.id.circleView);
        circle.setBarColor(getResources().getColor(R.color.colorGreen), getResources()
                .getColor(R.color.colorYellow), getResources().getColor(R.color.colorOrange), getResources().getColor(R.color.colorRed));
        calorieTextView = (TextView)view.findViewById(R.id.calorieTextView);
        breakfastTextView = (TextView)view.findViewById(R.id.breakfastTextView);
        lunchTextView = (TextView)view.findViewById(R.id.lunchTextView);
        dinnerTextView = (TextView)view.findViewById(R.id.dinnerTextView);
        snacksTextView = (TextView)view.findViewById(R.id.snacksTextView);
        waterTextView = (TextView)view.findViewById(R.id.waterTextView);

        Bundle args = this.getArguments();
        if(args != null) {
            this.datum = args.getString(DATE);
            dateArray[0] = datum;
            int calorieZargs = args.getInt(CALORIE);

            this.calorieTextView.setText(String.valueOf(calorieZargs));
            circle.setMaxValueAllowed(calorieZargs);
            circle.setMaxValue(calorieZargs);
        }

        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContext().getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                // ked je nieco v tabulke
                if(cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    breakfast = cursor.getInt(cursor.getColumnIndex(Provider.Day.BREAKFAST));
                    lunch = cursor.getInt(cursor.getColumnIndex(Provider.Day.LUNCH));
                    dinner = cursor.getInt(cursor.getColumnIndex(Provider.Day.DINNER));
                    snacks = cursor.getInt(cursor.getColumnIndex(Provider.Day.SNACKS));
                    water = cursor.getInt(cursor.getColumnIndex(Provider.Day.WATER));

                    breakfastTextView.setText(String.valueOf(breakfast));
                    lunchTextView.setText(String.valueOf(lunch));
                    dinnerTextView.setText(String.valueOf(dinner));
                    snacksTextView.setText(String.valueOf(snacks));
                    waterTextView.setText(String.valueOf(water));

                } else {
                    breakfastTextView.setText(String.valueOf(breakfast));
                    lunchTextView.setText(String.valueOf(lunch));
                    dinnerTextView.setText(String.valueOf(dinner));
                    snacksTextView.setText(String.valueOf(snacks));
                    waterTextView.setText(String.valueOf(water));

                }
                circle.setValueAnimated(breakfast + lunch + dinner + snacks);
                circle.setUnit("%");
                circle.setUnitSize(40);
                circle.setUnitVisible(true);
            }


        };
        queryHandler.startQuery(0, null, DayContentProvider.CONTENT_URI, null, Provider.Day.DATE + " = ?", dateArray, null);


        Button breakfastButton = (Button)view.findViewById(R.id.breakfastButton);
        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breakfast = new Intent(getActivity(), FoodActivity.class);
                breakfast.putExtra("part", "Breakfast");
                startActivity(breakfast);
            }
        });

        Button lunchButton = (Button)view.findViewById(R.id.lunchButton);
        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lunch = new Intent(getActivity(), FoodActivity.class);
                lunch.putExtra("part", "Lunch");
                startActivity(lunch);
            }
        });

        Button dinnerButton = (Button)view.findViewById(R.id.dinnerButton);
        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dinner = new Intent(getActivity(), FoodActivity.class);
                dinner.putExtra("part", "Dinner");
                startActivity(dinner);
            }
        });

        Button snacksButton = (Button)view.findViewById(R.id.snacksButton);
        snacksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent snacks = new Intent(getActivity(), FoodActivity.class);
                snacks.putExtra("part", "Snacks");
                startActivity(snacks);
            }
        });

        Button waterButton = (Button)view.findViewById(R.id.waterButton);
        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent water = new Intent(getActivity(), WaterActivity.class);
                water.putExtra("datum", datum);
                startActivity(water);
            }
        });

        Calendar cal = Calendar.getInstance();
        String currentDate = DatePage.getFormattedDate(getContext(), cal.getTimeInMillis());

        if(!(dateArray[0].equals(currentDate))) {
            breakfastButton.setEnabled(false);
            lunchButton.setEnabled(false);
            dinnerButton.setEnabled(false);
            snacksButton.setEnabled(false);
            waterButton.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Calendar cal = Calendar.getInstance();
        final String currentDate = DatePage.getFormattedDate(getContext(), cal.getTimeInMillis());

        if(dateArray[0].equals(currentDate)){
            AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContext().getContentResolver()) {
                @Override
                protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    // ked je nieco v tabulke
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        breakfast = cursor.getInt(cursor.getColumnIndex(Provider.Day.BREAKFAST));
                        lunch = cursor.getInt(cursor.getColumnIndex(Provider.Day.LUNCH));
                        dinner = cursor.getInt(cursor.getColumnIndex(Provider.Day.DINNER));
                        snacks = cursor.getInt(cursor.getColumnIndex(Provider.Day.SNACKS));
                        water = cursor.getInt(cursor.getColumnIndex(Provider.Day.WATER));

                        breakfastTextView.setText(String.valueOf(breakfast));
                        lunchTextView.setText(String.valueOf(lunch));
                        dinnerTextView.setText(String.valueOf(dinner));
                        snacksTextView.setText(String.valueOf(snacks));
                        waterTextView.setText(String.valueOf(water));

                    } else {
                        breakfastTextView.setText(String.valueOf(breakfast));
                        lunchTextView.setText(String.valueOf(lunch));
                        dinnerTextView.setText(String.valueOf(dinner));
                        snacksTextView.setText(String.valueOf(snacks));
                        waterTextView.setText(String.valueOf(water));
                    }

                    circle.setValueAnimated(breakfast + lunch + dinner + snacks);
                    circle.setUnit("%");
                    circle.setUnitSize(40);
                    circle.setUnitVisible(true);
                }
            };
            queryHandler.startQuery(0, null, DayContentProvider.CONTENT_URI, null, Provider.Day.DATE + " = '" + currentDate + "'", null, null);
        }
    }
}
