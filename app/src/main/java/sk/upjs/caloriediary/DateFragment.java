package sk.upjs.caloriediary;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.grabner.circleprogress.CircleProgressView;

public class DateFragment extends Fragment {
    public static final String DATE = "Date";
    private TextView dateTextView;
    private String tvContentValue;
    CircleProgressView circle;

    //tu su texty co su hore vypisane
    public static DateFragment newInstance(long date) {
        DateFragment dateFragment = new DateFragment();
        Bundle args = new Bundle();
        args.putLong(DATE, date);
        dateFragment.setArguments(args);
        return dateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.date_fragment, container, false);
    }

    //tu je to ... co bude vzdy na kazdom fragmente.. cize tu musia byt vsade 0....
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circle = (CircleProgressView)view.findViewById(R.id.circleView);
        circle.setBarColor(getResources().getColor(R.color.colorGreen), getResources()
                .getColor(R.color.colorYellow), getResources().getColor(R.color.colorOrange), getResources().getColor(R.color.colorRed));
       //toto neni animovane circle.setValue(450);
       // circle.setMaxValue(HODNOTA MAX KALORII NA DEN);
     //   circle.setValueAnimated(450);   // tu dame value a sa bude vyplnat animovane

        // circle.setMaxValue(HODNOTA MAX KALORII NA DEN);
       // circle.setValueAnimated(0);



    }
}
