package com.epam.esm.service.order;

import com.epam.esm.dto.reponse.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.DataAlreadyExistException;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.repository.gift_certificate.GiftCertificateRepository;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Override
    @Transactional
    public OrderGetResponse create(OrderPostRequest orderPostRequest) {
        validator(orderPostRequest);
        GiftCertificateEntity certificateEntity = giftCertificateRepository.findById(orderPostRequest.getCertificateId()).get();
        UserEntity user = userRepository.findById(orderPostRequest.getUserId()).get();
        OrderEntity order =
                new OrderEntity(certificateEntity.getPrice(), certificateEntity, user);
        OrderEntity orderEntity = orderRepository.create(order);
        return modelMapper.map(orderEntity, OrderGetResponse.class);
    }

    @Override
    public OrderGetResponse get(Long id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if(order.isPresent()) {
            return modelMapper.map(order.get(), OrderGetResponse.class);
        }
        throw new NoDataFoundException("order with id: " + id + " not found");
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<OrderGetResponse> getOrdersByUserId(Long userId, int limit, int offset) {
        List<OrderEntity> orders = orderRepository.getOrdersByUserId(userId, limit, offset);
        if(orders.isEmpty()){
            throw new NoDataFoundException("no orders found for this user with id: " + userId);
        }
        return modelMapper.map(orders, new TypeToken<List<OrderGetResponse>>() {
        }.getType());
    }

    @Override
    public OrderGetResponse getByUserIdAndOrderId(Long userId, Long orderId) {
        Optional<OrderEntity> order = orderRepository.getByUserIdAndOrderId(userId, orderId);
        if(order.isPresent()){
            return modelMapper.map(order.get(), OrderGetResponse.class);
        }
        throw new NoDataFoundException("no order found with id: " + orderId + " for this user");
    }

    @Override
    public List<OrderGetResponse> getByCertificateId(Long certificateId, int limit, int offset) {
        List<OrderEntity> ordersForCertificate = orderRepository.getByCertificateId(certificateId, limit, offset);
        if(ordersForCertificate.isEmpty()){
            throw new NoDataFoundException("no orders for certificate with id: " + certificateId + " found");
        }
        return modelMapper.map(ordersForCertificate, new TypeToken<List<OrderGetResponse>>() {
        }.getType());
    }


    @Override
    public void validator(OrderPostRequest orderPostRequest) {
        if(userRepository.findById(orderPostRequest.getUserId()).isEmpty())
            throw new NoDataFoundException(
                    "there is not user with id: " + orderPostRequest.getUserId() + " for this order");
        if(giftCertificateRepository.findById(orderPostRequest.getCertificateId()).isEmpty())
            throw new NoDataFoundException(
                    "ordered certificate with id: " + orderPostRequest.getCertificateId() + " does not exist");
        if(orderRepository.getByUserIdAndCertificateId(
                orderPostRequest.getUserId(), orderPostRequest.getCertificateId()).isPresent())
            throw new DataAlreadyExistException("this user already ordered this type of certificate");
    }
}
