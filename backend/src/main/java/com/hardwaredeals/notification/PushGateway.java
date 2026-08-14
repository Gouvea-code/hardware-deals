package com.hardwaredeals.notification;
import java.util.Map;
public interface PushGateway { DeliveryResult send(String token, String title, String body, Map<String,String> data); }
