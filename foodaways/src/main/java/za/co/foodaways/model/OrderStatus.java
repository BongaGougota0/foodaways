package za.co.foodaways.model;

public class OrderStatus {

        public enum Status{
            ORDER_PLACED("order_placed"),
            ORDER_ACCEPTED("order_accepted"),
            PREPARING_ORDER("preparing_order"),
            DELIVERED("delivered"),
            IN_TRANSIT("in_transit");

            private String orderStatus;

            Status(String orderStatus){
                this.orderStatus = orderStatus;
            }

            public String getRoleName(){return orderStatus;}
        }
}
