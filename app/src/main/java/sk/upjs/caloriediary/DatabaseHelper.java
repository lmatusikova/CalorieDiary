package sk.upjs.caloriediary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sk.upjs.caloriediary.provider.Provider;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {

        super(context, "ActivityDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_temp = "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INT, %s TEXT)";
        String sql = String.format(sql_temp, Provider.Food.TABLE_NAME, Provider.Food._ID, Provider.Food.FOOD_NAME, Provider.Food.CALORIE, Provider.Food.UNIT);
        db.execSQL(sql);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "Pizza");
        contentValues.put(Provider.Food.CALORIE, 100);
        contentValues.put(Provider.Food.UNIT, "slice");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "Pizza");
        contentValues.put(Provider.Food.CALORIE, 100);
        contentValues.put(Provider.Food.UNIT, "slice");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        sql_temp = "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s DATETIME, %s INT, %s INT, %s INT, %s INT, %s INT)";
        sql = String.format(sql_temp, Provider.Day.TABLE_NAME, Provider.Day._ID, Provider.Day.DATE, Provider.Day.BREAKFAST, Provider.Day.LUNCH, Provider.Day.DINNER, Provider.Day.SNACKS, Provider.Day.WATER);
        db.execSQL(sql);

        contentValues = new ContentValues();
        contentValues.put(Provider.Day.DATE, "6-1-2017");
        contentValues.put(Provider.Day.BREAKFAST, 120);
        contentValues.put(Provider.Day.LUNCH, 250);
        contentValues.put(Provider.Day.DINNER, 200);
        contentValues.put(Provider.Day.SNACKS, 15);
        contentValues.put(Provider.Day.WATER, 5);
        db.insert(Provider.Day.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Day.DATE, "6-2-2017");
        contentValues.put(Provider.Day.BREAKFAST, 110);
        contentValues.put(Provider.Day.LUNCH, 240);
        contentValues.put(Provider.Day.DINNER, 50);
        contentValues.put(Provider.Day.SNACKS, 50);
        contentValues.put(Provider.Day.WATER, 7);
        db.insert(Provider.Day.TABLE_NAME, null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nic
    }
}
