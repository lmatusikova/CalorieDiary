package sk.upjs.caloriediary;

import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.app.AlertDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import sk.upjs.caloriediary.provider.DayContentProvider;
import sk.upjs.caloriediary.provider.FoodContentProvider;
import sk.upjs.caloriediary.provider.Provider;

public class FoodActivity extends NavigationActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText foodEditText;
    private FoodCursorAdapter simpleAdapter;
    private String dayPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_food, null, false);
        drawerLayout.addView(contentView, 0);

        dayPart = getIntent().getStringExtra("part");
        setTitle(dayPart);

        getLoaderManager().initLoader(0, Bundle.EMPTY, this);
        ListView list = (ListView) findViewById(R.id.foodListView);
        this.simpleAdapter = new FoodCursorAdapter(this, null);
        list.setAdapter(simpleAdapter);

        foodEditText = (EditText) findViewById(R.id.foodEditText);

        //dlhe podrzanie vymaze item z databazy - Alert dialog ci chces vymazat
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                new AlertDialog.Builder(FoodActivity.this).setTitle("Delete").setMessage("Do you really want to delete this food?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromDatabase(id);
                    }
                }).setNegativeButton("No", null).show();
                return true;
            }
        });

        // INSERT/UPDATE tabulky Day, ale iba v konkretny den !!
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                final int calorie = cursor.getInt(cursor.getColumnIndex(Provider.Food.CALORIE));

                Calendar cal = Calendar.getInstance();
                final String currentDate = DatePage.getFormattedDate(FoodActivity.this, cal.getTimeInMillis());

                final AsyncQueryHandler handler2 = new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        Toast.makeText(FoodActivity.this, "Calorie was added.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onUpdateComplete(int token, Object cookie, int result) {
                        Toast.makeText(FoodActivity.this, "Calorie was added.", Toast.LENGTH_SHORT).show();
                    }
                };

                AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                        ContentValues values = new ContentValues();

                        //ked tam nic neni tak insertujem
                        if (cursor.getCount() == 0) {
                            values.put(Provider.Day.DATE, currentDate);
                            int breakfast = 0;
                            int lunch = 0;
                            int dinner = 0;
                            int snacks = 0;
                            int water = 0;
                            String clickedWater = "";

                            if (getTitle().equals("Breakfast")) {
                                breakfast = calorie;
                            } else if (getTitle().equals("Lunch")) {
                                lunch = calorie;
                            } else if (getTitle().equals("Dinner")) {
                                dinner = calorie;
                            } else if (getTitle().equals("Snacks")) {
                                snacks = calorie;
                            }

                            values.put(Provider.Day.BREAKFAST, breakfast);
                            values.put(Provider.Day.LUNCH, lunch);
                            values.put(Provider.Day.DINNER, dinner);
                            values.put(Provider.Day.SNACKS, snacks);
                            values.put(Provider.Day.WATER, water);
                            values.put(Provider.Day.WATER_CLICKED, clickedWater);
                            handler2.startInsert(0, null, DayContentProvider.CONTENT_URI, values);

                            //ked tam daco je tak updatujem
                        } else {
                            cursor.moveToFirst();
                            ContentValues values1 = new ContentValues();
                            switch(dayPart) {
                                case "Breakfast":
                                    values1.put(Provider.Day.BREAKFAST, cursor.getInt(cursor.getColumnIndex(Provider.Day.BREAKFAST)) + calorie);
                                    Log.d("RANAJKY PRED", String.valueOf(cursor.getInt(cursor.getColumnIndex(Provider.Day.BREAKFAST))));
                                    Log.d("RANAJKY", String.valueOf(cursor.getInt(cursor.getColumnIndex(Provider.Day.BREAKFAST)) + calorie));
                                    break;
                                case "Lunch":
                                    values1.put(Provider.Day.LUNCH, cursor.getInt(cursor.getColumnIndex(Provider.Day.LUNCH)) + calorie);
                                    break;
                                case "Dinner":
                                    values1.put(Provider.Day.DINNER, cursor.getInt(cursor.getColumnIndex(Provider.Day.DINNER)) + calorie);
                                    break;
                                case "Snacks":
                                    values1.put(Provider.Day.SNACKS, cursor.getInt(cursor.getColumnIndex(Provider.Day.SNACKS)) + calorie);
                                    break;
                            }
                            handler2.startUpdate(0, null, DayContentProvider.CONTENT_URI, values1, Provider.Day.DATE + " = '" + currentDate + "'", null);
                        }
                    }
                };

                //spusti prve
                handler.startQuery(0, null, DayContentProvider.CONTENT_URI, null, Provider.Day.DATE + " = '" + currentDate + "'", null, null);
            }
        });
    }

    //DELETE jedla z databazy
    public void deleteFromDatabase(long id) {
        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                Toast.makeText(FoodActivity.this, "Food deleted!", Toast.LENGTH_SHORT).show();
            }
        };
        Uri uri = ContentUris.withAppendedId(FoodContentProvider.CONTENT_URI, id);
        handler.startDelete(0, null, uri, null, null);
    }

    //vytvori sa loader a caka... tomu loaderu povieme, s ktorym content providerom sa ma spojit
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this);
        cursorLoader.setUri(FoodContentProvider.CONTENT_URI);
        return cursorLoader;

    }

    //vrati mi kurzor - celu tabulku
    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        this.simpleAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        this.simpleAdapter.swapCursor(null);
    }

    //tlacidlo pridavania do databazy
    public void onAddButtonClick(View view) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText calorieEditText = new EditText(this);
        calorieEditText.setHint("Calorie");
        final EditText unitEditText = new EditText(this);
        unitEditText.setHint("Unit");
        layout.addView(calorieEditText);
        layout.addView(unitEditText);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Save", null);
        alert.setNegativeButton("Cancel", null);
        alert.setView(layout);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (foodEditText.getText().toString().equals("") || calorieEditText.getText().toString().equals("") || unitEditText.getText().toString().equals("")) {
                            Toast.makeText(FoodActivity.this, "You did not enter all data!", Toast.LENGTH_SHORT).show();
                        } else {
                            insertIntoContentProvider(foodEditText.getText().toString(), calorieEditText.getText().toString(), unitEditText.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    //tlacidlo na hladanie z databazy
    public void onFindButtonClick(View view) {
        EditText foodEditText = (EditText) findViewById(R.id.foodEditText);
        String food = foodEditText.getText().toString();

        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                simpleAdapter.swapCursor(cursor);       //nastavi kurzor do adapteru (tzn. nahodi tam to co je aktualne v kurzore - co som vyhladala)
            }
        };
        handler.startQuery(0, null, FoodContentProvider.CONTENT_URI, null, Provider.Food.FOOD_NAME + " LIKE '%" + food + "%'", null, null);
    }

    //pridavam nove jedlo do databazy
    public void insertIntoContentProvider(String food, String calorie, String unit) {
        ContentValues values = new ContentValues();
        values.put(Provider.Food.FOOD_NAME, food);
        values.put(Provider.Food.CALORIE, Integer.parseInt(calorie));
        values.put(Provider.Food.UNIT, unit);

        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(FoodActivity.this, "Food added!", Toast.LENGTH_SHORT).show();
            }
        };

        insertHandler.startInsert(0, null, FoodContentProvider.CONTENT_URI, values);
    }

}
