package pogo;

import java.util.ArrayList;


public class Ingredients {


    private ArrayList<String > ingredients;

    public Ingredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
    public Ingredients(){};

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public Ingredients setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}
