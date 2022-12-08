package createcourier;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomDataForCourier {
    private String login;
    private String password;
    private String firstName;
    public String generateLogin(){
        //формируем логин log + 2 цифры - пример log77
        return "log" + new Random().nextInt(100);
    }
    public String generatePassword(){
        int randomPass = 1000 + new Random().nextInt(10000-1000);//число от 1000 до 9999 включительно
        return password = "" + randomPass; //приводим к типу Строка и возвращаем пароль
    }
    public String generateFirstName(){
        return RandomStringUtils.randomAlphabetic(6); //возвращаем 6 случайных букв
    }
}
