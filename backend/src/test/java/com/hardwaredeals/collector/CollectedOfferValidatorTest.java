package com.hardwaredeals.collector;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class CollectedOfferValidatorTest {
 private final CollectedOfferValidator validator=new CollectedOfferValidator();
 @Test void acceptsCompleteHttpsOffer(){assertThatCode(()->validator.validate(offer("https://shop.example/item",new BigDecimal("10")))).doesNotThrowAnyException();}
 @Test void rejectsUnsafeUrlAndNonPositivePrice(){assertThatThrownBy(()->validator.validate(offer("file:///secret",BigDecimal.TEN))).isInstanceOf(IllegalArgumentException.class)
  .hasMessageContaining("HTTP");assertThatThrownBy(()->validator.validate(offer("https://shop.example/item",BigDecimal.ZERO))).hasMessageContaining("positive");}
 private CollectedOffer offer(String url,BigDecimal price){return new CollectedOffer("shop","external","sku","GPU","Brand","Model","GPU","123",url,price,null,null,true,null);}
}
