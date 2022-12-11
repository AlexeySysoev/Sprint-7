package createcourier;

public class CourierV2 extends Courier {
    private String login;
    private String firstName;
    public CourierV2(String login, String firstName){
        this.login = login;
        this.firstName = firstName;
    }
    public CourierV2(String firstName){
        this.firstName = firstName;
    }
    public CourierV2(){}
}
