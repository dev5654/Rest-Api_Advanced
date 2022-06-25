package com.epam.esm.repository.order;

import com.epam.esm.repository.CrudRepository;
import com.epam.esm.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long>/*, OrderQueries*/{
    String GET_ALL_ORDERS = "select o from OrderEntity o";
    String GET_ORDER_BY_USER_ID = "select o from OrderEntity o where o.user.id = :id";
    String GET_ORDER_BY_USER_ID_AND_ORDER_ID
            = "select o from OrderEntity o where o.user.id = :userId and o.id = :orderId";
    String GET_ORDERS_BY_CERTIFICATE_ID = "select o from OrderEntity o where o.certificate.id = :id";
    String GET_ORDER_BY_USER_ID_AND_CERTIFICATE_ID
            = "select o from OrderEntity o where o.certificate.id = :certificateId and o.user.id = :userId";

    List<OrderEntity> getOrdersByUserId(Long userId, int limit, int offset);

    Optional<OrderEntity> getByUserIdAndOrderId(Long userId, Long orderId);

    Optional<OrderEntity> getByUserIdAndCertificateId(Long userId, Long certificateId);

    List<OrderEntity> getByCertificateId(Long certificateId, int limit, int offset);
}
