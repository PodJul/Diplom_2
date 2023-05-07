package ingredientList;

import pogo.IngredientData;
import pogo.IngredientsResponse;

import java.util.ArrayList;
import java.util.Random;

public class IngredientsListGenerator {
    ArrayList<IngredientData> data=new ArrayList<>();
    ArrayList<String > ingredientsList=new ArrayList<>();
    Random random = new Random();
    int n= random.nextInt(15);
    int newN = random.nextInt(15);
    public ArrayList <String> createIngredientsList(IngredientsResponse ingredientsResponse){

        ingredientsList.add(ingredientsResponse.getData().get(n).get_id());
        ingredientsList.add(ingredientsResponse.getData().get(newN).get_id());
        return ingredientsList;
    }


}
