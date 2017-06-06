package sk.upjs.caloriediary;

/**
 * Trieda na vypocet maximalneho prijmu kalorii na den zo zadanych informacii
 */
public class CalorieCalculation {

    private static final int WOMAN_CONSTANT = 655;
    private static final int MAN_CONSTANT = 66;

    private static final double WOMAN_WEIGHT = 9.6;
    private static final double MAN_WEIGHT = 13.7;

    private static final double WOMAN_HEIGHT = 1.8;
    private static final double MAN_HEIGHT = 5;

    private static final double WOMAN_AGE = 4.7;
    private static final double MAN_AGE = 6.8;

    private int gender;
    private int weight;
    private int height;
    private int age;
    private int result;

    public CalorieCalculation(int gender, int weight, int height, int age) {
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.age = age;
    }

    public int calculation() {
        if(gender == 0 ) {  //zena
            result = WOMAN_CONSTANT + (int)((WOMAN_WEIGHT * weight) + (WOMAN_HEIGHT * height) - (WOMAN_AGE * age));

        } else {        //muz
            result = MAN_CONSTANT + (int)((MAN_WEIGHT * weight) + (MAN_HEIGHT * height) - (MAN_AGE * age));
        }


        return result;
    }





}
