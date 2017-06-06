package sk.upjs.caloriediary.provider;

import android.provider.BaseColumns;

//Zadefinovanie tabuliek - stlpcov v tabulkach
public interface Provider  {
    public interface Food extends BaseColumns {
        public static final String TABLE_NAME = "Food";
        public static final String FOOD_NAME = "Food_name";
        public static final String CALORIE = "Calorie";
        public static final String UNIT = "Unit";
    }

    public interface Day extends BaseColumns {
        public static final String TABLE_NAME = "Day";
        public static final String DATE = "Date";
        public static final String BREAKFAST = "Breakfast";
        public static final String LUNCH = "Lunch";
        public static final String DINNER = "Dinner";
        public static final String SNACKS = "Snacks";
        public static final String WATER = "Water";
        public static final String WATER_CLICKED = "Water_clicked";
    }
}
