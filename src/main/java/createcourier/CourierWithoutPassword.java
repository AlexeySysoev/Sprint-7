package createcourier;
public class CourierWithoutPassword extends Courier {
    private String login;
    private String firstName;
    public CourierWithoutPassword(String login, String firstName){
        this.login = login;
        this.firstName = firstName;
    }
    public CourierWithoutPassword(){}
}
