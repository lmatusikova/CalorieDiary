package sk.upjs.caloriediary;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Scanner;

import sk.upjs.caloriediary.provider.DayContentProvider;
import sk.upjs.caloriediary.provider.Provider;

public class WaterActivity extends NavigationActivity {
    private static final int COLUMN = 5;
    private static final int ROWS = 4;
    private int numberOfGlasses = 0;
    private ImageView[][] imageViews = new ImageView[ROWS][COLUMN];
    private Drawable emptyGlassDrawable;
    private Drawable fullGlassDrawable;
    private boolean[][] clicked = new boolean[ROWS][COLUMN];
    private static final String NUMBER_OF_GLASSES = "Number";
    private static final String CLICKED = "Clicked";
    private String datum;
    private String string;
    private Bundle kyblik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_water, null, false);
        drawerLayout.addView(contentView, 0);

        loadWaterFromDatabase();
        datum = getIntent().getStringExtra("datum");

    }

    public void resetClicked() {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMN; j++)
                clicked[i][j] = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.water, menu);
        return true;
    }

    //INSERT A UPDATE tabulky, stlpec WATER iba pre aktualny den!!
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.SaveMenuItem) {
            // dnesny den
            Calendar cal = Calendar.getInstance();
            final String currentDate = DatePage.getFormattedDate(WaterActivity.this, cal.getTimeInMillis());

            final AsyncQueryHandler handler2 = new AsyncQueryHandler(getContentResolver()) {
                @Override
                protected void onInsertComplete(int token, Object cookie, Uri uri) {
                    Toast.makeText(WaterActivity.this, "Water was added.", Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void onUpdateComplete(int token, Object cookie, int result) {
                    Toast.makeText(WaterActivity.this, "Water was added.", Toast.LENGTH_SHORT).show();
                }
            };

            //z pola clicked urobim string, ktory dam do databazy
            StringBuilder sb = new StringBuilder();
            int cislo = 0;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    if (clicked[i][j]) {
                        sb.append(cislo + " ");
                    }
                    cislo++;
                }
            }
            final String clickedWater = sb.toString();

            AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
                @Override
                protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    ContentValues values = new ContentValues();

                    //ked tam nic neni tak insertujem
                    if (cursor.getCount() == 0) {
                        int breakfast = 0;
                        int lunch = 0;
                        int dinner = 0;
                        int snacks = 0;

                        values.put(Provider.Day.DATE, currentDate);
                        values.put(Provider.Day.BREAKFAST, breakfast);
                        values.put(Provider.Day.LUNCH, lunch);
                        values.put(Provider.Day.DINNER, dinner);
                        values.put(Provider.Day.SNACKS, snacks);
                        values.put(Provider.Day.WATER, numberOfGlasses);
                        values.put(Provider.Day.WATER_CLICKED, clickedWater);  // TOTO ESTE NEVIEM AKO
                        handler2.startInsert(0, null, DayContentProvider.CONTENT_URI, values);

                        //ked tam daco je tak updatujem
                    } else {
                        //string mame z metody loadWaterFromDatabase()
                        //treba ho doplnit a poslat do update...

                        cursor.moveToFirst();
                        ContentValues values1 = new ContentValues();
                        values1.put(Provider.Day.WATER, cursor.getInt(cursor.getColumnIndex(Provider.Day.WATER)) + numberOfGlasses);
                        values1.put(Provider.Day.WATER_CLICKED, clickedWater);     //AKO TOTO UROBIT
                        handler2.startUpdate(0, null, DayContentProvider.CONTENT_URI, values1, Provider.Day.DATE + " = '" + currentDate + "'", null);
                    }
                }
            };

            //spusti prve
            handler.startQuery(0, null, DayContentProvider.CONTENT_URI, null, Provider.Day.DATE + " = '" + currentDate + "'", null, null);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //ked otocim zariadenie alebo mi niekto zavola
    private void restoreWaterState(Bundle savedInstanceState) {
        numberOfGlasses = savedInstanceState.getInt(NUMBER_OF_GLASSES);
        clicked = (boolean[][]) savedInstanceState.getSerializable(CLICKED);

        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMN; j++) {
                if (clicked[i][j]) {
                    imageViews[i][j].setImageDrawable(fullGlassDrawable);
                } else {
                    imageViews[i][j].setImageDrawable(emptyGlassDrawable);
                }
            }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUMBER_OF_GLASSES, numberOfGlasses);
        outState.putSerializable(CLICKED, clicked);
        kyblik = outState;
    }

    private void design() {
        emptyGlassDrawable = getResources().getDrawable(R.drawable.w4);
        fullGlassDrawable = getResources().getDrawable(R.drawable.full);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int cellSize = screenWidth / COLUMN;
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(cellSize * COLUMN, cellSize);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(cellSize, cellSize);

        LinearLayout boardLayout = (LinearLayout) findViewById(R.id.waterLayout);

        for (int i = 0; i < ROWS; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            for (int j = 0; j < COLUMN; j++) {
                imageViews[i][j] = new ImageView(this);

                if (clicked[i][j]) {
                    imageViews[i][j].setImageDrawable(fullGlassDrawable);
                } else {
                    imageViews[i][j].setImageDrawable(emptyGlassDrawable);
                }

                final int x = i;
                final int y = j;

                imageViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clicked[x][y]) {
                            imageViews[x][y].setImageDrawable(emptyGlassDrawable);
                            clicked[x][y] = false;
                            numberOfGlasses--;
                        } else if (!clicked[x][y]) {
                            imageViews[x][y].setImageDrawable(fullGlassDrawable);
                            clicked[x][y] = true;
                            numberOfGlasses++;
                        }
                    }
                });
                rowLayout.addView(imageViews[i][j], lpCell);
            }
            boardLayout.addView(rowLayout, lpRow);
        }
    }

    //pri nacitani aktivity skontrolujem ci je nieco v databaze
    //nastavi clicked pole na spravne hodnoty
    public void loadWaterFromDatabase() {
        Calendar cal = Calendar.getInstance();
        final String currentDate = DatePage.getFormattedDate(WaterActivity.this, cal.getTimeInMillis());

        resetClicked();     //zresetuje pole clicked ...

        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    string = cursor.getString(cursor.getColumnIndex(Provider.Day.WATER_CLICKED));
                    if ((string != null) || (!string.equals(""))) {
                        Scanner s = new Scanner(string);
                        while (s.hasNextInt()) {
                            int i = s.nextInt();
                            Log.d("i", String.valueOf(i));
                            int x, y;
                            x = i / COLUMN;
                            y = i % COLUMN;
                            Log.d("x, y", String.valueOf(x) + " " + String.valueOf(y));
                            clicked[x][y] = true;
                        }
                    }
                }
                design();
            }
        };
        queryHandler.startQuery(0, null, DayContentProvider.CONTENT_URI, null, Provider.Day.DATE + " = '" + currentDate + "'", null, null);

        //ked otocim zariadenie
        if (kyblik != null) {
            restoreWaterState(kyblik);
        }


    }


}
