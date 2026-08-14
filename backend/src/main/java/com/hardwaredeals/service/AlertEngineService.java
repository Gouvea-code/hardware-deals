package com.hardwaredeals.service;
import com.hardwaredeals.entity.*;import com.hardwaredeals.notification.*;import com.hardwaredeals.repository.*;
import java.math.BigDecimal;import java.time.*;import java.util.*;import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;

@Service @ConditionalOnProperty(name="app.alert-engine.enabled",havingValue="true")
public class AlertEngineService {
 private static final Logger log=LoggerFactory.getLogger(AlertEngineService.class);
 private final PriceAlertRepository alerts;private final OfferRepository offers;private final DeviceTokenRepository devices;
 private final NotificationRepository notifications;private final PushGateway push;private final Duration cooldown;
 public AlertEngineService(PriceAlertRepository alerts,OfferRepository offers,DeviceTokenRepository devices,
  NotificationRepository notifications,PushGateway push,@Value("${app.alert-engine.cooldown:PT24H}") Duration cooldown){
  this.alerts=alerts;this.offers=offers;this.devices=devices;this.notifications=notifications;this.push=push;this.cooldown=cooldown;}
 @Scheduled(cron="${app.alert-engine.cron:0 */15 * * * *}") @Transactional public void evaluate(){
  LocalDateTime now=LocalDateTime.now();for(PriceAlert alert:alerts.findAllByActiveTrue()){try{evaluate(alert,now);}catch(Exception ex){log.error("Alert evaluation failed: alertId={}",alert.getId(),ex);}}}
 void evaluate(PriceAlert alert,LocalDateTime now){if(alert.getLastNotifiedAt()!=null&&alert.getLastNotifiedAt().isAfter(now.minus(cooldown)))return;
  Optional<BigDecimal> current=latestAvailablePrices(alert.getProduct().getId()).stream().min(BigDecimal::compareTo);
  if(current.isEmpty()||current.get().compareTo(alert.getTargetPrice())>0)return;
  String title="Preço desejado encontrado";String body=alert.getProduct().getName()+" por "+current.get().setScale(2);
  notifications.save(Notification.builder().user(alert.getUser()).type("PRICE_ALERT").title(title).message(body).build());
  Map<String,String> data=Map.of("type","PRICE_ALERT","productId",alert.getProduct().getId().toString(),"alertId",alert.getId().toString());
  for(DeviceToken device:devices.findByUserIdAndActiveTrue(alert.getUser().getId())){DeliveryResult result=push.send(device.getToken(),title,body,data);
   if(result.invalidToken()){device.setActive(false);devices.save(device);}}
  alert.setLastNotifiedAt(now);alerts.save(alert);
 }
 private List<BigDecimal> latestAvailablePrices(UUID productId){Set<UUID> seen=new HashSet<>();List<BigDecimal> prices=new ArrayList<>();
  for(Offer offer:offers.findByStoreProductProductIdOrderByCollectedAtDesc(productId)){UUID id=offer.getStoreProduct().getId();
   if(seen.add(id)&&Boolean.TRUE.equals(offer.getAvailable())&&Boolean.TRUE.equals(offer.getStoreProduct().getActive())
    &&Boolean.TRUE.equals(offer.getStoreProduct().getStore().getActive()))prices.add(offer.getPrice());}return prices;}
}
