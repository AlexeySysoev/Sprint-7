package createcourier;

public class CourierWithoutLogin extends Courier {
    private String password;
    private String firstName;
    public CourierWithoutLogin(String password, String firstName){
        this.password = password;
        this.firstName = firstName;
    }
    public CourierWithoutLogin(){}
}
