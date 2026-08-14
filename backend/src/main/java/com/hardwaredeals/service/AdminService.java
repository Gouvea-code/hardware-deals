package com.hardwaredeals.service;

import com.hardwaredeals.collector.CollectorProperties;
import com.hardwaredeals.dto.AdminDtos.*;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class AdminService {
 private final UserRepository users;private final ProductRepository products;private final StoreRepository stores;
 private final OfferRepository offers;private final PriceAlertRepository alerts;private final NotificationRepository notifications;
 private final OfferClickRepository clicks;private final AnalyticsEventRepository analytics;private final AdminAuditRepository audit;
 private final CollectorProperties collector;
 public AdminService(UserRepository users,ProductRepository products,StoreRepository stores,OfferRepository offers,
  PriceAlertRepository alerts,NotificationRepository notifications,OfferClickRepository clicks,
  AnalyticsEventRepository analytics,AdminAuditRepository audit,CollectorProperties collector){this.users=users;this.products=products;
  this.stores=stores;this.offers=offers;this.alerts=alerts;this.notifications=notifications;this.clicks=clicks;
  this.analytics=analytics;this.audit=audit;this.collector=collector;}

 @Transactional(readOnly=true) public DashboardResponse dashboard(){return new DashboardResponse(users.count(),users.countByStatus("ACTIVE"),
  products.count(),products.countByActiveTrue(),stores.count(),stores.countByActiveTrue(),offers.count(),offers.countByAvailableTrue(),
  alerts.count(),notifications.count(),clicks.count(),analytics.count());}
 @Transactional(readOnly=true) public List<UserAdminResponse> users(){return users.findAll(Sort.by("createdAt").descending()).stream().map(this::user).toList();}
 public UserAdminResponse updateUser(UUID adminId,UUID targetId,UpdateUserRequest request){
  if(!Set.of("ACTIVE","INACTIVE").contains(request.status()))throw new ApiException(HttpStatus.BAD_REQUEST,"Status inválido");
  if(adminId.equals(targetId)&&(!"ACTIVE".equals(request.status())||request.role()!=UserRole.ADMIN))
   throw new ApiException(HttpStatus.CONFLICT,"O administrador não pode remover o próprio acesso");
  User target=users.findById(targetId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Usuário não encontrado"));
  target.setStatus(request.status());target.setRole(request.role());users.save(target);record(adminId,"UPDATE_USER","USER",targetId);return user(target);}
 @Transactional(readOnly=true) public List<ProductAdminResponse> products(){return products.findAll(Sort.by("name")).stream().map(this::product).toList();}
 public ProductAdminResponse updateProduct(UUID adminId,UUID id,ActiveRequest request){Product value=products.findById(id)
  .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Produto não encontrado"));value.setActive(request.active());products.save(value);
  record(adminId,"UPDATE_PRODUCT_ACTIVE","PRODUCT",id);return product(value);}
 @Transactional(readOnly=true) public List<StoreAdminResponse> stores(){return stores.findAll(Sort.by("name")).stream().map(this::store).toList();}
 public StoreAdminResponse updateStore(UUID adminId,UUID id,ActiveRequest request){Store value=stores.findById(id)
  .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Loja não encontrada"));value.setActive(request.active());stores.save(value);
  record(adminId,"UPDATE_STORE_ACTIVE","STORE",id);return store(value);}
 @Transactional(readOnly=true) public List<OfferAdminResponse> offers(){return offers.findTop50ByOrderByCollectedAtDesc().stream().map(this::offer).toList();}
 @Transactional(readOnly=true) public CollectorStatusResponse collector(){return new CollectorStatusResponse(collector.isEnabled(),
  collector.getFeedUrl()!=null&&!collector.getFeedUrl().isBlank(),collector.getCron());}
 @Transactional(readOnly=true) public ReportResponse reports(){return new ReportResponse(clicks.count(),analytics.count(),notifications.count(),alerts.findAllByActiveTrue().size());}
 @Transactional(readOnly=true) public List<AuditResponse> audit(){return audit.findTop100ByOrderByCreatedAtDesc().stream().map(value->new AuditResponse(value.getId(),
  value.getAdmin()==null?null:value.getAdmin().getId(),value.getAction(),value.getTargetType(),value.getTargetId(),value.getCreatedAt())).toList();}
 private void record(UUID adminId,String action,String type,UUID targetId){User admin=users.findById(adminId)
  .orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Administrador inválido"));audit.save(AdminAudit.builder().admin(admin).action(action).targetType(type).targetId(targetId).build());}
 private UserAdminResponse user(User v){return new UserAdminResponse(v.getId(),v.getName(),v.getEmail(),v.getStatus(),v.getRole(),Boolean.TRUE.equals(v.getEmailVerified()),v.getCreatedAt());}
 private ProductAdminResponse product(Product v){return new ProductAdminResponse(v.getId(),v.getName(),v.getBrand(),v.getModel(),v.getCategory(),Boolean.TRUE.equals(v.getActive()));}
 private StoreAdminResponse store(Store v){return new StoreAdminResponse(v.getId(),v.getName(),v.getSlug(),v.getWebsite(),Boolean.TRUE.equals(v.getActive()));}
 private OfferAdminResponse offer(Offer v){StoreProduct sp=v.getStoreProduct();return new OfferAdminResponse(v.getId(),sp.getProduct().getId(),sp.getProduct().getName(),
  sp.getStore().getId(),sp.getStore().getName(),v.getPrice(),Boolean.TRUE.equals(v.getAvailable()),v.getCollectedAt());}
}
