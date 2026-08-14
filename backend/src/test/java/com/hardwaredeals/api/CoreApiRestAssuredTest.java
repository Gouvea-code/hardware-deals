package com.hardwaredeals.api;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT) @ActiveProfiles("test")
class CoreApiRestAssuredTest {
 @LocalServerPort int port;@Autowired UserRepository users;@Autowired StoreRepository stores;@Autowired ProductRepository products;
 @Autowired StoreProductRepository storeProducts;@Autowired OfferRepository offers;@Autowired PriceHistoryRepository history;
 @MockBean JavaMailSender mailSender;

 @Test void completesCoreUserJourneyThroughHttp(){RestAssured.port=port;RestAssured.basePath="/api/v1";
  String suffix=UUID.randomUUID().toString();String email="journey-"+suffix+"@example.com";
  given().contentType("application/json").body("{\"name\":\"Journey\",\"email\":\""+email+"\",\"password\":\"safePass123\"}")
   .when().post("/auth/register").then().statusCode(201);
  User user=users.findByEmail(email).orElseThrow();user.setEmailVerified(true);users.save(user);
  String access=given().contentType("application/json").body("{\"email\":\""+email+"\",\"password\":\"safePass123\"}")
   .when().post("/auth/login").then().statusCode(200).body("accessToken",not(emptyString())).extract().path("accessToken");

  Store store=stores.save(Store.builder().name("Journey Store").slug("journey-"+suffix).website("https://journey.example").active(true).build());
  Product product=products.save(Product.builder().name("Journey GPU").brand("Brand").model("X").category("GPU")
   .ean(suffix.substring(0,12)).normalizedName("journey gpu").active(true).build());
  StoreProduct link=storeProducts.save(StoreProduct.builder().store(store).product(product).externalId(suffix).sku("SKU-"+suffix)
   .externalName("Journey GPU").url("https://journey.example/gpu").active(true).build());
  offers.save(Offer.builder().storeProduct(link).price(new BigDecimal("999.90")).originalPrice(new BigDecimal("1299.90")).available(true).build());
  history.save(PriceHistory.builder().product(product).store(store).price(new BigDecimal("999.90")).build());

  given().when().get("/products/{id}",product.getId()).then().statusCode(200).body("name",equalTo("Journey GPU"));
  given().when().get("/deals").then().statusCode(200).body("productName",hasItem("Journey GPU"));
  given().header("Authorization","Bearer "+access).when().put("/favorites/{id}",product.getId()).then().statusCode(200).body("productId",equalTo(product.getId().toString()));
  given().header("Authorization","Bearer "+access).when().get("/favorites").then().statusCode(200).body("productId",hasItem(product.getId().toString()));
  given().header("Authorization","Bearer "+access).contentType("application/json").body("{\"targetPrice\":\"900.00\"}")
   .when().put("/alerts/{id}",product.getId()).then().statusCode(200).body("targetPrice",comparesEqualTo(900.0f));
 }
}
