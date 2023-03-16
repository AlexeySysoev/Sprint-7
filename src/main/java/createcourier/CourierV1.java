package createcourier;

public class CourierV1 implements Courier {
    private String login;
    private String password;
    private String firstName;
    public CourierV1(String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
    public CourierV1(String login, String password){
        this.login = login;
        this.password = password;
    }
    public CourierV1(String login){
        this.login = login;
    }
    public CourierV1(){}
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
