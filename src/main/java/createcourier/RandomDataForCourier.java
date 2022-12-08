package createcourier;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomDataForCourier {
    private String login;
    private String password;
    private String firstName;
    public String generateLogin(){
        return "log" + new Random().nextInt(100);
    }
    public String generatePassword(){
        int randomPass = 1000 + new Random().nextInt(10000-1000);
        return password ="" + randomPass;
    }
    public String generateFirstName(){
        return RandomStringUtils.randomAlphabetic(6);
    }
}
