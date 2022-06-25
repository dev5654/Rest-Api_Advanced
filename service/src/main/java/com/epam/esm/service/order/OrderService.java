package com.epam.esm.service.order;

import com.epam.esm.dto.reponse.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;

import java.util.List;

public interface OrderService /*extends BaseService<OrderPostRequest, OrderGetResponse>*/ {

    OrderGetResponse create(OrderPostRequest p);

    OrderGetResponse get(Long id);


    List<OrderGetResponse> getOrdersByUserId(Long userId, int limit, int offset);

    OrderGetResponse getByUserIdAndOrderId(Long userId, Long orderId);

    List<OrderGetResponse> getByCertificateId(Long certificateId, int limit, int offset);

    void validate(OrderPostRequest orderPostRequest);

}
