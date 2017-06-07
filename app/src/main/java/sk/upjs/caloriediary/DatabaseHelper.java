package sk.upjs.caloriediary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sk.upjs.caloriediary.provider.Provider;

//Vytvara celu databazu a tabulky + vzorove data
//CREATE TABLE .....
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
        contentValues.put(Provider.Food.FOOD_NAME, "Pizza Margharita");
        contentValues.put(Provider.Food.CALORIE, 76);
        contentValues.put(Provider.Food.UNIT, "one slice");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "Pizza Capri");
        contentValues.put(Provider.Food.CALORIE, 93);
        contentValues.put(Provider.Food.UNIT, "one slice");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "Chicken Soup");
        contentValues.put(Provider.Food.CALORIE, 158);
        contentValues.put(Provider.Food.UNIT, "bowl");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "Strawberry Cupcake");
        contentValues.put(Provider.Food.CALORIE, 112);
        contentValues.put(Provider.Food.UNIT, "a piece");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "3bit");
        contentValues.put(Provider.Food.CALORIE, 58);
        contentValues.put(Provider.Food.UNIT, "a piece");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "Chicken roasting meat");
        contentValues.put(Provider.Food.CALORIE, 335);
        contentValues.put(Provider.Food.UNIT, "150g");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Food.FOOD_NAME, "Fries");
        contentValues.put(Provider.Food.CALORIE, 474);
        contentValues.put(Provider.Food.UNIT, "150g");
        db.insert(Provider.Food.TABLE_NAME, null, contentValues);

        sql_temp = "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INT, %s INT, %s INT, %s INT, %s INT, %s TEXT)";
        sql = String.format(sql_temp, Provider.Day.TABLE_NAME, Provider.Day._ID, Provider.Day.DATE, Provider.Day.BREAKFAST, Provider.Day.LUNCH, Provider.Day.DINNER, Provider.Day.SNACKS, Provider.Day.WATER, Provider.Day.WATER_CLICKED);
        db.execSQL(sql);

        contentValues = new ContentValues();
        contentValues.put(Provider.Day.DATE, "2017-06-05");
        contentValues.put(Provider.Day.BREAKFAST, 100);
        contentValues.put(Provider.Day.LUNCH, 200);
        contentValues.put(Provider.Day.DINNER, 30);
        contentValues.put(Provider.Day.SNACKS, 45);
        contentValues.put(Provider.Day.WATER, 5);
        contentValues.put(Provider.Day.WATER, "0 1 2 3 4");
        db.insert(Provider.Day.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Provider.Day.DATE, "2017-06-06");
        contentValues.put(Provider.Day.BREAKFAST, 96);
        contentValues.put(Provider.Day.LUNCH, 245);
        contentValues.put(Provider.Day.DINNER, 136);
        contentValues.put(Provider.Day.SNACKS, 98);
        contentValues.put(Provider.Day.WATER, 7);
        contentValues.put(Provider.Day.WATER, "0 2 3 5 8 7 4");
        db.insert(Provider.Day.TABLE_NAME, null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nic
    }
}
