package sk.upjs.caloriediary;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;
    protected DrawerLayout drawerLayout;
    ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

    class NavigationItem {
        String title;
        int icon;

        public NavigationItem(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView iv = (ImageView) findViewById(R.id.fruitImageView);
        Drawable fruit = getResources().getDrawable(R.drawable.fruit);

        navigationItems.add(new NavigationItem("Diet Dashboard", R.drawable.diet2));
        navigationItems.add(new NavigationItem("Breakfast", R.drawable.breakfast));
        navigationItems.add(new NavigationItem("Lunch", R.drawable.lunch2));
        navigationItems.add(new NavigationItem("Dinner", R.drawable.dinner2));
        navigationItems.add(new NavigationItem("Snacks", R.drawable.snacks2));
        navigationItems.add(new NavigationItem("Water", R.drawable.full));
        navigationItems.add(new NavigationItem("My Profile", R.drawable.profile));

        iv.setImageResource(R.drawable.fruit);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.openDrawer,
                R.string.closeDrawer
        );
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter(this, navigationItems);
        drawerLayout.setDrawerListener(drawerToggle);
        ListView navigationListView = (ListView) findViewById(R.id.navigationListView);
        navigationListView.setAdapter(drawerListAdapter);
        navigationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemTitle = navigationItems.get(position).title.toString();

                //akcie ked zatlacim daktory item...
                if (selectedItemTitle.equals("Diet Dashboard")) {
                    Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (selectedItemTitle.equals("Breakfast")) {
                    Intent intent = new Intent(NavigationActivity.this, FoodActivity.class);
                    startActivity(intent);
                } else if (selectedItemTitle.equals("Lunch")) {
                    Intent intent = new Intent(NavigationActivity.this, FoodActivity.class);
                    startActivity(intent);
                } else if (selectedItemTitle.equals("Dinner")) {
                    Intent intent = new Intent(NavigationActivity.this, FoodActivity.class);
                    startActivity(intent);
                } else if (selectedItemTitle.equals("Water")) {
                    Intent intent = new Intent(NavigationActivity.this, WaterActivity.class);
                    startActivity(intent);
                } else if (selectedItemTitle.equals("Snacks")) {
                    Intent intent = new Intent(NavigationActivity.this, FoodActivity.class);
                    startActivity(intent);
                } else if (selectedItemTitle.equals("My Profile")) {
                    Intent intent = new Intent(NavigationActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawers();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
