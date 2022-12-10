package createorderdata;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomDataForOrder {
    public String generateName(){
        return RandomStringUtils.randomAlphabetic(6); //возвращаем 6 случайных букв;
    }
    public String generateAddress(){
        return RandomStringUtils.randomAlphanumeric(20);
    }
    public String generateMetroStation(){
        return "Черкизовская";
    }
    public String generatePhoneNumber(){
        return "+7" + RandomStringUtils.randomNumeric(10);
    }
    public int generateRentTime(){
        return new Random().nextInt(30);
    }
    public String getDeliveryDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
    public String generateComment(){
        return RandomStringUtils.randomAlphanumeric(30);
    }
}
