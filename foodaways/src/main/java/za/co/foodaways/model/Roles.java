package za.co.foodaways.model;

public class Roles {

    public enum Role{
        ADMIN("ADMIN"),
        CUSTOMER("CUSTOMER"),
        STORE_OWNER("STORE_OWNER");

        private String roleName;

        Role(String roleName){
            this.roleName = roleName;
        }

        public String getRoleName(){return roleName;}
    }
}
