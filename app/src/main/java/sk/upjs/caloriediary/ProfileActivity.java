package sk.upjs.caloriediary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Aktivita obsahujuca pouzivatelov profil.
 */
public class ProfileActivity extends NavigationActivity {

    private TextView name;
    private TextView age;
    private TextView start;
    private TextView weight;
    private TextView height;
    private TextView plan;
    private SharedPreferences preferences;
    private static final String MY_PREF = "My_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_profile, null, false);
        drawerLayout.addView(contentView, 0);
        setData();
    }

    public void setData() {
        preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        name = (TextView)findViewById(R.id.nameTextView);
        name.setText(preferences.getString("Name", "-"));

        age = (TextView)findViewById(R.id.ageTextView2);
        age.setText(preferences.getString("Age", "-"));
        start = (TextView)findViewById(R.id.startTextView);
        start.setText(preferences.getString("Date", "-"));

        weight = (TextView)findViewById(R.id.weightTextView2);
        weight.setText(preferences.getString("Current weight", "0") + " kg");

        height = (TextView)findViewById(R.id.heightTextView2);
        height.setText(preferences.getInt("Meter", 0) + " m " + preferences.getInt("Centimeter", 0) + " cm");

        plan = (TextView)findViewById(R.id.planTextView);
        plan.setText(preferences.getString("Target weight", "0") + " kg");
    }

}
