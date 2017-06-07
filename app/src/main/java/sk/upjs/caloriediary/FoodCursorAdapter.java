package sk.upjs.caloriediary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import sk.upjs.caloriediary.provider.Provider;

/**
 * Zobrazuje data v liste podla poziadaviek
 */
public class FoodCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public FoodCursorAdapter(Context context, Cursor c) {
        super(context, c);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.food_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.foodTextView)).setText(cursor.getString(cursor.getColumnIndex(Provider.Food.FOOD_NAME)));
        String calorie = String.valueOf(cursor.getInt(cursor.getColumnIndex(Provider.Food.CALORIE)));
        String unit = cursor.getString(cursor.getColumnIndex(Provider.Food.UNIT));
        ((TextView) view.findViewById(R.id.calorieUnitTextView)).setText(calorie + " cal/" + unit);
    }
}
