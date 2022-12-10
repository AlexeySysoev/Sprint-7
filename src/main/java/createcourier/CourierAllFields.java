package createcourier;

public class CourierAllFields extends Courier {
    private String login;
    private String password;
    private String firstName;
    public CourierAllFields(String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
    public CourierAllFields(){}
}
