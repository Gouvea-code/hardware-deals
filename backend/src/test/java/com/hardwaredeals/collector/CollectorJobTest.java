package com.hardwaredeals.collector;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;

class CollectorJobTest {
 @Test void runsEveryConfiguredCollectorWithoutOwningBusinessLogic(){PriceCollector first=mock(PriceCollector.class);
  PriceCollector second=mock(PriceCollector.class);CollectorPipeline pipeline=mock(CollectorPipeline.class);
  new CollectorJob(List.of(first,second),pipeline).collect();verify(pipeline).run(first);verify(pipeline).run(second);}
}
