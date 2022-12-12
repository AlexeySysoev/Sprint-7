package createcourier;

public class CourierV3 implements Courier {
        private String password;
        private String firstName;
    public CourierV3(String password, String firstName){
        this.password = password;
        this.firstName = firstName;
    }
        public CourierV3(String password){
            this.password = password;
        }
        public CourierV3(){}
}
