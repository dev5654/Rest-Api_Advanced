package com.epam.esm.repository.order;

import com.epam.esm.entity.OrderEntity;
import com.epam.esm.exception.UnknownDataBaseException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Creates a new instance of an entity in the database.
     * @param  order -> Object to create
     * @return optional id of created object
     */
    @Override
    public OrderEntity create(OrderEntity order) {
        entityManager.persist(order);
        if(order.getId() != 0)
            return order;
        throw new UnknownDataBaseException("there was a problem while creating gift certificate. Try again");
    }
    /**
     * Gets all existing entities with provided type and provided limit and offset.
     * @param limit limit of entities
     * @param offset offset for the entities
     * @return list of entities
     */
    @Override
    public List<OrderEntity> getAll(int limit, int offset) {
        return entityManager.createQuery(GET_ALL_ORDERS, OrderEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Gets entity with the provided id.
     * @param orderId id of the needed object
     * @return optional object of provided type
     */
    @Override
    public Optional<OrderEntity> findById(Long orderId) {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, orderId);
        if(orderEntity != null)
            return Optional.of(orderEntity);
        return Optional.empty();
    }

    /**
     * Get Orders By UserId
     * @param userId id of the user
     * @param limit limit to order
     * @param offset offset for order
     * @return list of found orders
     */
    @Override
    public List<OrderEntity> getOrdersByUserId(Long userId, int limit, int offset) {
        return entityManager
                .createQuery(GET_ORDER_BY_USER_ID, OrderEntity.class)
                .setParameter("id", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Get Orders By UserId
     * @param userId id of the user
     * @param orderId order id
     * @return list of found orders
     */
    @Override
    public Optional<OrderEntity> getByUserIdAndOrderId(Long userId, Long orderId){
        List<OrderEntity> orders = entityManager
                .createQuery(
                        GET_ORDER_BY_USER_ID_AND_ORDER_ID, OrderEntity.class)
                .setParameter("userId", userId)
                .setParameter("orderId", orderId)
                .getResultList();
        if(orders.isEmpty())
            return Optional.empty();
        return Optional.of(orders.get(0));
    }

    /**
     * Get By CertificateId
     * @param certificateId id of certificate
     * @param limit limit to order
     * @param offset offset for order
     * @return list of found orders
     */
    @Override
    public List<OrderEntity> getByCertificateId(Long certificateId, int limit, int offset) {
        return entityManager
                .createQuery(GET_ORDERS_BY_CERTIFICATE_ID, OrderEntity.class)
                .setParameter("id", certificateId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Get By UserId And CertificateId
     * @param userId id of the user
     * @param certificateId id of the certificate
     * @return list of found orders
     */
    @Override
    public Optional<OrderEntity> getByUserIdAndCertificateId(Long userId, Long certificateId) {
        List<OrderEntity> resultList
                = entityManager.createQuery(
                        GET_ORDER_BY_USER_ID_AND_CERTIFICATE_ID,
                        OrderEntity.class)
                .setParameter("certificateId", certificateId)
                .setParameter("userId", userId)
                .getResultList();
        if(resultList.isEmpty())
            return Optional.empty();
        return Optional.of(resultList.get(0));
    }
}
