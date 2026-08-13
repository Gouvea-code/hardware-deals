package com.hardwaredeals.integration;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private PriceAlertRepository priceAlertRepository;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void testUserRepository() {
        // Create and save user
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .passwordHash("hashed_password")
                .status("ACTIVE")
                .build();
        userRepository.save(user);

        // Find by email
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    void testStoreRepository() {
        // Create and save store
        Store store = Store.builder()
                .name("Test Store")
                .slug("test-store")
                .website("https://test.com")
                .active(true)
                .build();
        storeRepository.save(store);

        // Find by slug
        Optional<Store> foundStore = storeRepository.findBySlug("test-store");
        assertTrue(foundStore.isPresent());
        assertEquals("Test Store", foundStore.get().getName());
    }

    @Test
    void testProductRepository() {
        // Create and save product
        Product product = Product.builder()
                .name("Test Product")
                .brand("Test Brand")
                .model("Model X")
                .category("Electronics")
                .ean("1234567890123")
                .normalizedName("test product")
                .imageUrl("https://test.com/image.jpg")
                .active(true)
                .build();
        productRepository.save(product);

        // Find by EAN
        Optional<Product> foundProduct = productRepository.findByEan("1234567890123");
        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());

        // Find by category
        List<Product> products = productRepository.findByCategory("Electronics");
        assertTrue(products.size() > 0);
    }

    @Test
    void testStoreProductRepository() {
        // Create store and product
        Store store = Store.builder()
                .name("Store1")
                .slug("store-1")
                .website("https://store1.com")
                .active(true)
                .build();
        storeRepository.save(store);

        Product product = Product.builder()
                .name("Product1")
                .brand("Brand1")
                .model("Model1")
                .category("Category1")
                .ean("EAN1")
                .normalizedName("product1")
                .active(true)
                .build();
        productRepository.save(product);

        // Create store product
        StoreProduct storeProduct = StoreProduct.builder()
                .store(store)
                .product(product)
                .externalId("ext-123")
                .sku("SKU-123")
                .externalName("Product in Store")
                .url("https://test.com/product")
                .active(true)
                .build();
        storeProductRepository.save(storeProduct);

        // Find by store and product
        Optional<StoreProduct> found = storeProductRepository.findByStoreIdAndProductId(
                store.getId(), product.getId()
        );
        assertTrue(found.isPresent());
    }

    @Test
    void testOfferRepository() {
        // Create store and product
        Store store = Store.builder()
                .name("Store2")
                .slug("store-2")
                .website("https://store2.com")
                .active(true)
                .build();
        storeRepository.save(store);

        Product product = Product.builder()
                .name("Product2")
                .brand("Brand2")
                .model("Model2")
                .category("Category2")
                .ean("EAN2")
                .normalizedName("product2")
                .active(true)
                .build();
        productRepository.save(product);

        // Create store product
        StoreProduct storeProduct = StoreProduct.builder()
                .store(store)
                .product(product)
                .externalId("ext-456")
                .sku("SKU-456")
                .externalName("Product Offer")
                .url("https://test.com/product-offer")
                .active(true)
                .build();
        storeProductRepository.save(storeProduct);

        // Create offer
        Offer offer = Offer.builder()
                .storeProduct(storeProduct)
                .price(new BigDecimal("99.99"))
                .originalPrice(new BigDecimal("199.99"))
                .coupon("DISCOUNT10")
                .available(true)
                .build();
        offerRepository.save(offer);

        // Verify
        List<Offer> offers = offerRepository.findByStoreProductId(storeProduct.getId());
        assertTrue(offers.size() > 0);
    }

    @Test
    void testPriceHistoryRepository() {
        // Create store and product
        Store store = Store.builder()
                .name("Store3")
                .slug("store-3")
                .website("https://store3.com")
                .active(true)
                .build();
        storeRepository.save(store);

        Product product = Product.builder()
                .name("Product3")
                .brand("Brand3")
                .model("Model3")
                .category("Category3")
                .ean("EAN3")
                .normalizedName("product3")
                .active(true)
                .build();
        productRepository.save(product);

        // Create price history
        PriceHistory priceHistory = PriceHistory.builder()
                .product(product)
                .store(store)
                .price(new BigDecimal("150.00"))
                .build();
        priceHistoryRepository.save(priceHistory);

        // Verify
        List<PriceHistory> byProduct = priceHistoryRepository.findByProductIdOrderByCollectedAtDesc(
                product.getId()
        );
        assertTrue(byProduct.size() > 0);
    }

    @Test
    void testFavoriteRepository() {
        // Create user and product
        User user = User.builder()
                .name("User1")
                .email("user1@example.com")
                .passwordHash("hash")
                .status("ACTIVE")
                .build();
        userRepository.save(user);

        Product product = Product.builder()
                .name("Product4")
                .brand("Brand4")
                .model("Model4")
                .category("Category4")
                .ean("EAN4")
                .normalizedName("product4")
                .active(true)
                .build();
        productRepository.save(product);

        // Create favorite
        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .build();
        favoriteRepository.save(favorite);

        // Verify
        List<Favorite> byUser = favoriteRepository.findByUserId(user.getId());
        assertTrue(byUser.size() > 0);

        assertTrue(favoriteRepository.existsByUserIdAndProductId(user.getId(), product.getId()));
    }

    @Test
    void testPriceAlertRepository() {
        // Create user and product
        User user = User.builder()
                .name("User2")
                .email("user2@example.com")
                .passwordHash("hash")
                .status("ACTIVE")
                .build();
        userRepository.save(user);

        Product product = Product.builder()
                .name("Product5")
                .brand("Brand5")
                .model("Model5")
                .category("Category5")
                .ean("EAN5")
                .normalizedName("product5")
                .active(true)
                .build();
        productRepository.save(product);

        // Create price alert
        PriceAlert priceAlert = PriceAlert.builder()
                .user(user)
                .product(product)
                .targetPrice(new BigDecimal("100.00"))
                .active(true)
                .build();
        priceAlertRepository.save(priceAlert);

        // Verify
        List<PriceAlert> byUser = priceAlertRepository.findByUserId(user.getId());
        assertTrue(byUser.size() > 0);
    }

    @Test
    void testDeviceTokenRepository() {
        // Create user
        User user = User.builder()
                .name("User3")
                .email("user3@example.com")
                .passwordHash("hash")
                .status("ACTIVE")
                .build();
        userRepository.save(user);

        // Create device token
        DeviceToken deviceToken = DeviceToken.builder()
                .user(user)
                .token("device-token-abc123")
                .platform("Android")
                .active(true)
                .build();
        deviceTokenRepository.save(deviceToken);

        // Verify
        List<DeviceToken> byUser = deviceTokenRepository.findByUserId(user.getId());
        assertTrue(byUser.size() > 0);

        Optional<DeviceToken> found = deviceTokenRepository.findByToken("device-token-abc123");
        assertTrue(found.isPresent());
    }

    @Test
    void testNotificationRepository() {
        // Create user
        User user = User.builder()
                .name("User4")
                .email("user4@example.com")
                .passwordHash("hash")
                .status("ACTIVE")
                .build();
        userRepository.save(user);

        // Create notification
        Notification notification = Notification.builder()
                .user(user)
                .type("PRICE_DROP")
                .title("Price Alert!")
                .message("Product price dropped to $99.99")
                .read(false)
                .build();
        notificationRepository.save(notification);

        // Verify
        List<Notification> byUser = notificationRepository.findByUserIdOrderByCreatedAtDesc(
                user.getId()
        );
        assertTrue(byUser.size() > 0);

        List<Notification> unread = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(
                user.getId()
        );
        assertTrue(unread.size() > 0);
    }
}
