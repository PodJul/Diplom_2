package user;

import com.github.javafaker.Faker;
import java.util.Locale;


 public class UserGenerator {
    Faker faker = new Faker(new Locale("en-RU"));
    public String fakeUserEmail(){
        return faker.internet().emailAddress();}
    public String fakeUserPassword(){
        return faker.internet().password();}
    public String fakeUserName(){
        return faker.letterify("?????");}

    }
