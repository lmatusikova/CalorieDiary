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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import sk.upjs.caloriediary.provider.DayContentProvider;
import sk.upjs.caloriediary.provider.FoodContentProvider;
import sk.upjs.caloriediary.provider.Provider;

public class FoodActivity extends NavigationActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText foodEditText;
    private FoodCursorAdapter simpleAdapter;
    private Cursor dayCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_food, null, false);
        drawerLayout.addView(contentView, 0);
        getLoaderManager().initLoader(0, Bundle.EMPTY, this);   //ten taha jedla
        getLoaderManager().initLoader(1, Bundle.EMPTY, this);   //ten taha z Day
        ListView list = (ListView)findViewById(R.id.foodListView);
        this.simpleAdapter = new FoodCursorAdapter(this, null);
        list.setAdapter(simpleAdapter);
        foodEditText = (EditText)findViewById(R.id.foodEditText);

        //dlhe podrzanie vymaze item z databazy - Alert dialog
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
            //update do databazy Day..
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int calorie;
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                calorie = cursor.getInt(cursor.getColumnIndex(Provider.Food.CALORIE));

//                AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
//                    @Override
//                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//                        super.onQueryComplete(token, cookie, cursor);
//                    }
//                };
//                handler.startQuery();
                int calorieIn;
                if (dayCursor != null) {
                    calorieIn = dayCursor.getInt(dayCursor.getColumnIndex(Provider.Day.BREAKFAST));
                }
            }
        });
    }

    //vymaze jedlo z databazy
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
        if (id == 0) {
            CursorLoader cursorLoader = new CursorLoader(this);
            cursorLoader.setUri(FoodContentProvider.CONTENT_URI);
            return cursorLoader;
        } else { // if (id == 1)
            CursorLoader cursorLoader = new CursorLoader(this);
            cursorLoader.setUri(DayContentProvider.CONTENT_URI);
            return cursorLoader;
        }
    }

    //vrati mi kurzor - celu tabulku
    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 0) {
            this.simpleAdapter.swapCursor(data);
        }

        if (loader.getId() == 1) {
            this.dayCursor = data;
        }
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
                        if(foodEditText.getText().toString().equals("") || calorieEditText.getText().toString().equals("") || unitEditText.getText().toString().equals("")) {
                            Toast.makeText(FoodActivity.this, "You did not enter all data!", Toast.LENGTH_SHORT).show();
                        } else {
                            insertIntoContentProvider(foodEditText.getText().toString(), calorieEditText.getText().toString(), unitEditText.getText().toString());
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    //tlacidlo na hladanie z databazy
    public void onFindButtonClick(View view) {

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
