package za.co.foodaways.model;

public class OrderStatus {

        public enum Status{
            ORDER_PLACED("order_placed"),
            ORDER_ACCEPTED("order_accepted"),
            ORDER_DECLINED("order_declined"),
            PREPARING_ORDER("preparing_order"),
            DELIVERED("delivered"),
            IN_TRANSIT("in_transit"),
            ERROR_PLACING_ORDER("error_placing_order");

            private String orderStatus;

            Status(String orderStatus){
                this.orderStatus = orderStatus;
            }

            public String getRoleName(){return orderStatus;}
        }
}
