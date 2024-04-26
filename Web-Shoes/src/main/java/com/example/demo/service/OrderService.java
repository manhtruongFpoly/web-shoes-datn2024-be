package com.example.demo.service;

import com.example.demo.contants.OrderStatus;
import com.example.demo.contants.OrderStatusEnum;
import com.example.demo.model.dto.CreateOrderReq;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.payload.request.CreateDeliveryOrder;
import com.example.demo.payload.request.CreateOrderAtTheCounter;
import com.example.demo.payload.request.OrderDetailResponse;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

import java.util.List;

public interface OrderService {
    List<OrderEntity> getAll();

    OrderEntity checkoutOrder(CreateOrderReq req) throws MessagingException;

    OrderEntity orderConfirmed(Long orderId);

    OrderEntity beingShipped(Long orderId);

    //đã giao
    OrderEntity delivered(Long orderId);

    OrderEntity cancelled(Long orderId, String reason);

    // danh sách hóa đơn theo status
    List<OrderEntity> listStatus(OrderStatusEnum status);

    OrderStatusEnum[] status();

    long countOrderStatus(int status);

    OrderEntity findByIdOrder(Long id);

    List<OrderEntity> listStatusPaymentPaid();

    OrderEntity createAnOrderAtTheCounter(CreateOrderAtTheCounter req);

    OrderEntity checkoutAtTheCounter(Long orderId);

    OrderEntity createDeliveryOrder(CreateDeliveryOrder req);

    OrderEntity updateDeliveryOrder(Long orderId, CreateDeliveryOrder req);

    OrderEntity retailOrders(Long idOrder);

    OrderEntity updateAtTheCounterOrder(Long orderId, CreateOrderAtTheCounter req);

    OrderEntity createOrder();

    List<OrderEntity> filterStatusOrder(OrderStatus status);

    OrderEntity findByMahd(String mahd);

    List<OrderEntity> searchOrder(String name);

    List<OrderEntity> listStatusPayment();

    OrderDetailResponse sumTotalOrderDetail(Long idOrder);

    List<OrderEntity> listOrderStatusAndUserId(OrderStatusEnum status);

    void reOrder(Long orderId);
}
