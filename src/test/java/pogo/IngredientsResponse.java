package pogo;

import java.util.ArrayList;
import java.util.Random;


public class IngredientsResponse {
    private boolean success;
    private ArrayList<IngredientData> data;

    public IngredientsResponse(boolean success, ArrayList<IngredientData> data) {
        this.success = success;
        this.data = data;
    }

    public IngredientsResponse(){};

    public boolean isSuccess() {
        return success;
    }
    public ArrayList<IngredientData> getData() {
        return data;
    }

}