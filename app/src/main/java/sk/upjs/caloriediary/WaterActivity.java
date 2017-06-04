package sk.upjs.caloriediary;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;

import sk.upjs.caloriediary.provider.DayContentProvider;

public class WaterActivity extends NavigationActivity {
    private static final int COLUMN = 5;
    private static final int ROWS = 3;
    private int numberOfGlasses = 0;
    private ImageView[][] imageViews = new ImageView[ROWS][COLUMN];
    private Drawable emptyGlassDrawable;
    private Drawable fullGlassDrawable;
    private boolean[][] clicked = new boolean[ROWS][COLUMN];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_water, null, false);
        drawerLayout.addView(contentView, 0);
        design();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.water, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //UPDATE databaza... stlpec WATER

        return super.onOptionsItemSelected(item);
    }

    private void design() {

        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMN; j++)
                clicked[i][j] = false;

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
                imageViews[i][j].setImageDrawable(emptyGlassDrawable);

                final int x = i;
                final int y = j;

                imageViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(clicked[x][y] == true) {
                            imageViews[x][y].setImageDrawable(emptyGlassDrawable);
                            clicked[x][y] = false;
                            numberOfGlasses--;
                        }
                        else if(clicked[x][y] == false){
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

}
