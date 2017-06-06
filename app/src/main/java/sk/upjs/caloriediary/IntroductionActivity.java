package sk.upjs.caloriediary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.RadioButton;
import android.view.MenuItem;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Uvodna aktivita, v ktorej pouzivatel musi vyplnit udaje.
 */
public class IntroductionActivity extends AppCompatActivity {
    private EditText nameEditText;
    private RadioButton female;
    private RadioButton male;
    private EditText ageEditText;
    private EditText requiredKgText;
    private NumberPicker meterNumberPicker;
    private NumberPicker centimeterNumberPicker;
    private EditText targetEditText;
    private SharedPreferences settings;
    private static final String MY_PREF = "My_pref";
    private DatePage datetime;
    private Calendar cal;
    private CalorieCalculation calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        init();
    }

    private void init() {
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        female = (RadioButton) findViewById(R.id.femaleRadioButton);
        male = (RadioButton) findViewById(R.id.maleRadioButton);
        ageEditText = (EditText)findViewById(R.id.ageEditText);
        requiredKgText = (EditText) findViewById(R.id.requiredKgText);
        meterNumberPicker = (NumberPicker) findViewById(R.id.meterNumberPicker);
        centimeterNumberPicker = (NumberPicker) findViewById(R.id.centimeterNumberPicker);
        targetEditText = (EditText) findViewById(R.id.targetEditText);
        datetime = new DatePage();
        cal =  new GregorianCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.introduction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.SaveMenuItem) {
            savePreferenceAndStart();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void savePreferenceAndStart() {
        SharedPreferences.Editor editor = settings.edit();

        //v profile sa nastavi, kedy som si nainstalovala appku...
        String date = datetime.getFormattedDate(this, cal.getTimeInMillis());
        int result;

        String name = nameEditText.getText().toString();
        if(name.equals("")) {
            displayWarning();
            return;
        }

        String gender = "";
        if (female.isChecked()) {
            gender = "Female";
        } else if (male.isChecked()) {
            gender = "Male";
        }

        if (gender.equals("")) {
            displayWarning();
            return;
        }

        String age = ageEditText.getText().toString();
        if (age.equals("")) {
            displayWarning();
            return;
        }

        if(!age.matches("[0-9]+")) {
            displayWarning2();
            return;
        }

        String currentWeight = requiredKgText.getText().toString();
        if (currentWeight.equals("")) {
            displayWarning();
            return;
        }

        if(!currentWeight.matches("[0-9]+")) {
            displayWarning2();
            return;
        }

        int meter = meterNumberPicker.getValue();
        int centimeter = centimeterNumberPicker.getValue();
        String targetWeight = targetEditText.getText().toString();
        if (targetWeight.equals("")) {
            displayWarning();
            return;
        }

        if(!targetWeight.matches("[0-9]+")) {
            displayWarning2();
            return;
        }

        editor.putString("Date", date);
        editor.putString("Name", name);
        editor.putString("Age", age);
        editor.putString("Gender", gender);
        editor.putString("Current weight", currentWeight);
        editor.putInt("Meter", meter);
        editor.putInt("Centimeter", centimeter);
        editor.putString("Target weight", targetWeight);

        if(gender.equals("Female")) {
            calc = new CalorieCalculation(0, Integer.parseInt(currentWeight), (meter * 100) + centimeter, Integer.parseInt(age));
            result = calc.calculation();
        } else {
            calc = new CalorieCalculation(1, Integer.parseInt(currentWeight), (meter * 100) + centimeter, Integer.parseInt(age));
            result = calc.calculation();
        }

        editor.putInt("Calorie", result);
        editor.commit();

        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();

        Intent intro = new Intent(this, MainActivity.class);
        startActivity(intro);
    }

    private void displayWarning() {
        new AlertDialog.Builder(this).setTitle("Warning").setMessage("You did not enter all data!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
    }

    private void displayWarning2() {
        new AlertDialog.Builder(this).setTitle("Warning").setMessage("You can not enter text!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
    }

}
