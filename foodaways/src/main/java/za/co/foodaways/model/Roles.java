package za.co.foodaways.model;

public class Roles {

    public enum Role{
        ADMIN("admin"),
        CUSTOMER("customer"),
        STORE_OWNER("store_owner");

        private String roleName;

        Role(String roleName){
            this.roleName = roleName;
        }

        public String getRoleName(){return roleName;}
    }
}
