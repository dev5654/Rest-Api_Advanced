package com.epam.esm.service.order;

import com.epam.esm.dto.reponse.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.repository.gift_certificate.GiftCertificateRepository;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.utils.GiftCertificateServiceTestUtils.getGiftCertificateEntity;
import static com.epam.esm.service.utils.OrderServiceTestUtils.*;
import static com.epam.esm.service.utils.UserServiceTestUtils.getUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;


    private OrderEntity entity;
    private OrderGetResponse response;
    private OrderPostRequest postRequest;

    @BeforeEach
    void setUp() {
        entity = getOrderEntity();
        response = getOrderResponse();
        postRequest = getOrderPostRequest();
    }

    @Test
    void create() {
        when(userRepository.findById(postRequest.getUserId())).thenReturn(Optional.of(getUser()));
        when(giftCertificateRepository.findById(postRequest.getCertificateId()))
                .thenReturn(Optional.of(getGiftCertificateEntity()));
        when(orderRepository.create(Mockito.any(OrderEntity.class))).thenReturn(entity);
        when(modelMapper.map(entity, OrderGetResponse.class)).thenReturn(response);
        OrderGetResponse response = orderService.create(postRequest);
        assertNotNull(response);
    }

    @Test
    void get() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, OrderGetResponse.class)).thenReturn(response);
        OrderGetResponse response = orderService.get(1L);
        assertEquals(new BigDecimal("200.5"), response.getPrice());
    }

    @Test
    void delete() {

    }

    @Test
    void getOrdersByUserId() {
        List<OrderEntity> orders = getOrders();
        when(orderRepository.getOrdersByUserId(1L, 10, 0)).thenReturn(orders);
        when(modelMapper.map(orders, new TypeToken<List<OrderGetResponse>>() {
        }.getType())).thenReturn(getOrderList());
        List<OrderGetResponse> ordersByUserId = orderService.getOrdersByUserId(1L, 10, 0);
        assertEquals(3, ordersByUserId.size());
    }

    @Test
    void getByUserIdAndOrderId() {
        when(orderRepository.getByUserIdAndOrderId(1L, 1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, OrderGetResponse.class)).thenReturn(response);
        OrderGetResponse order = orderService.getByUserIdAndOrderId(1L, 1L);
        assertEquals(new BigDecimal("200.5"), order.getPrice());
    }

    @Test
    void getByCertificateId() {
        List<OrderEntity> orders = getOrders();
        when(orderRepository.getByCertificateId(1L, 10, 0)).thenReturn(orders);
        when(modelMapper.map(orders, new TypeToken<List<OrderGetResponse>>() {
        }.getType())).thenReturn(getOrderList());
        List<OrderGetResponse> byCertificateId = orderService.getByCertificateId(1L, 10, 0);
        assertEquals(3, byCertificateId.size());
    }
}