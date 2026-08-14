package com.hardwaredeals.notification;
public record DeliveryResult(boolean delivered, boolean invalidToken) {
    public static DeliveryResult success(){return new DeliveryResult(true,false);}
    public static DeliveryResult failed(){return new DeliveryResult(false,false);}
    public static DeliveryResult invalid(){return new DeliveryResult(false,true);}
}
