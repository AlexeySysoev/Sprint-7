package createcourier;
public class CourierWithoutFirstName extends Courier {
    private String login;
    private String password;
    public CourierWithoutFirstName(String login, String password){
        this.login = login;
        this.password = password;
    }
    public CourierWithoutFirstName(){}

    public void setPassword(String password) {
        this.password = password;
    }
    public String  getLogin(){
        return this.login;
    }
}
