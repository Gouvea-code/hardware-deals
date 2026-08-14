package com.hardwaredeals.notification;
import com.google.firebase.messaging.*;import java.util.Map;import org.slf4j.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.MeterRegistry;import io.micrometer.core.instrument.Timer;
@Component @ConditionalOnProperty(name="app.firebase.enabled",havingValue="true")
public class FirebasePushGateway implements PushGateway {
 private static final Logger log=LoggerFactory.getLogger(FirebasePushGateway.class);private final FirebaseMessaging messaging;private final MeterRegistry metrics;
 public FirebasePushGateway(FirebaseMessaging messaging,MeterRegistry metrics){this.messaging=messaging;this.metrics=metrics;}
 public DeliveryResult send(String token,String title,String body,Map<String,String> data){Timer.Sample sample=Timer.start(metrics);String outcome="success";try{
  Message message=Message.builder().setToken(token).setNotification(Notification.builder().setTitle(title).setBody(body).build())
   .putAllData(data).setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build()).build();
  messaging.send(message);return DeliveryResult.success();
 }catch(FirebaseMessagingException ex){MessagingErrorCode code=ex.getMessagingErrorCode();
  if(code==MessagingErrorCode.UNREGISTERED||code==MessagingErrorCode.INVALID_ARGUMENT){outcome="invalid";return DeliveryResult.invalid();}
  outcome="failed";log.warn("FCM delivery failed: code={}",code);return DeliveryResult.failed();
 }finally{sample.stop(metrics.timer("hardware_deals.push.duration","outcome",outcome));}
 }
}
