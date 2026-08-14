package com.hardwaredeals.service;
import com.hardwaredeals.entity.*;import com.hardwaredeals.notification.*;import com.hardwaredeals.repository.*;
import java.math.BigDecimal;import java.time.*;import java.util.*;import org.junit.jupiter.api.*;import org.mockito.*;
import static org.mockito.Mockito.*;
class AlertEngineServiceTest {
 @Mock PriceAlertRepository alerts;@Mock OfferRepository offers;@Mock DeviceTokenRepository devices;@Mock NotificationRepository notifications;@Mock PushGateway push;
 private AutoCloseable mocks;private AlertEngineService engine;private PriceAlert alert;private DeviceToken device;
 @BeforeEach void setup(){mocks=MockitoAnnotations.openMocks(this);engine=new AlertEngineService(alerts,offers,devices,notifications,push,Duration.ofHours(24));
  User user=User.builder().id(UUID.randomUUID()).name("U").build();Product product=Product.builder().id(UUID.randomUUID()).name("GPU").build();
  alert=PriceAlert.builder().id(UUID.randomUUID()).user(user).product(product).targetPrice(new BigDecimal("3700")).active(true).build();
  device=DeviceToken.builder().id(UUID.randomUUID()).user(user).token("bad").platform("android").active(true).build();}
 @AfterEach void close()throws Exception{mocks.close();}
 @Test void notifiesAtTargetAndDisablesInvalidToken(){Store store=Store.builder().id(UUID.randomUUID()).active(true).build();
  StoreProduct link=StoreProduct.builder().id(UUID.randomUUID()).store(store).product(alert.getProduct()).active(true).build();
  Offer offer=Offer.builder().storeProduct(link).price(new BigDecimal("3600")).available(true).build();
  when(offers.findByStoreProductProductIdOrderByCollectedAtDesc(alert.getProduct().getId())).thenReturn(List.of(offer));
  when(devices.findByUserIdAndActiveTrue(alert.getUser().getId())).thenReturn(List.of(device));when(push.send(any(),any(),any(),any())).thenReturn(DeliveryResult.invalid());
  engine.evaluate(alert,LocalDateTime.now());verify(notifications).save(any(Notification.class));verify(alerts).save(alert);verify(devices).save(device);
  Assertions.assertFalse(device.getActive());Assertions.assertNotNull(alert.getLastNotifiedAt());}
 @Test void respectsPriceAndCooldown(){alert.setLastNotifiedAt(LocalDateTime.now().minusHours(1));engine.evaluate(alert,LocalDateTime.now());verifyNoInteractions(offers,push,notifications);
  alert.setLastNotifiedAt(null);when(offers.findByStoreProductProductIdOrderByCollectedAtDesc(any())).thenReturn(List.of());engine.evaluate(alert,LocalDateTime.now());verifyNoInteractions(push,notifications);}
}
