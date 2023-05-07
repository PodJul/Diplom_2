package data;

import pogo.User;
import user.UserGenerator;

public class Credentials {

    static UserGenerator userGenerator = new UserGenerator();

    public static String fakeEmail=userGenerator.fakeUserEmail();
    public static String fakePassword=userGenerator.fakeUserPassword();
    public static String fakeName=userGenerator.fakeUserName();
    public static User userWithoutEmail = new User().setPassword(fakePassword).setName(fakeName);
    public static User userWithoutPassword = new User().setEmail(fakeEmail).setName(fakeName);
    public static User userWithoutName = new User().setEmail(fakeEmail).setPassword(fakePassword);
    public static User user = new User().setEmail(fakeEmail).setPassword(fakePassword).setName(fakeName);
    public static String newFakePassword=userGenerator.fakeUserPassword();
    public static String newFakeEmail=userGenerator.fakeUserEmail();
    public static String newFakeName=userGenerator.fakeUserName();
    public static User newUser = new User().setEmail(newFakeEmail).setPassword(newFakePassword);
    public static User userWithNewName = new User().setEmail(fakeEmail).setPassword(fakePassword).setName(newFakeName);
    public static User userWithNewEmail = new User().setEmail(newFakeEmail).setPassword(fakePassword).setName(fakeName);
    public static User userWithNewPassword = new User().setEmail(fakeEmail).setPassword(newFakePassword).setName(fakeName);



}

