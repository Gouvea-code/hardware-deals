package com.hardwaredeals.service;

import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.Test;
import java.time.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {
 @Test void removesEventsOutsideRetention(){AnalyticsEventRepository events=mock(AnalyticsEventRepository.class);when(events.deleteByOccurredAtBefore(any())).thenReturn(7L);
  AnalyticsService service=new AnalyticsService(events,mock(UserRepository.class),mock(ProductRepository.class),mock(NotificationRepository.class),Duration.ofDays(90));
  LocalDateTime before=LocalDateTime.now().minusDays(90);assertThat(service.deleteExpired()).isEqualTo(7);
  verify(events).deleteByOccurredAtBefore(argThat(value->!value.isBefore(before.minusSeconds(2))&&!value.isAfter(before.plusSeconds(2))));}
}
