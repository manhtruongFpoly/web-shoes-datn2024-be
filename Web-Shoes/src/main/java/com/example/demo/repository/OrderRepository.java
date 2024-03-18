package com.example.demo.repository;

import com.example.demo.contants.OrderStatus;
import com.example.demo.contants.OrderStatusEnum;
import com.example.demo.contants.PaymentStatus;
import com.example.demo.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    @Query(value = "SELECT * FROM `orders`ORDER BY orders.id DESC",nativeQuery = true)
    List<OrderEntity> findAllOrderById();


    List<OrderEntity> findAllByStatusEqualsAndOrderStatusOrderByIdDesc(OrderStatusEnum status, OrderStatus oStatus);

    @Query(nativeQuery = true,value = "SELECT count(*) AS 'Số lượng' FROM `orders` WHERE orders.status = ?1 and orders.user_id = ?2 ")
    long countOrderStatus(int status,Long userId);

    List<OrderEntity> findAllByPaymentStatusEquals(PaymentStatus status);

    Optional<OrderEntity> findByMahd(String mahd);

    List<OrderEntity> findAllByOrderStatusEquals(OrderStatus status);

    @Query(value = "SELECT COUNT(*) FROM `orders` WHERE orders.payment_status = 0",nativeQuery = true)
    long countOrderStatus();

    @Query("SELECT ord FROM OrderEntity ord WHERE ord.phone like %?1% and ord.paymentStatus = ?2")
    List<OrderEntity> searchAllByOrder(String name,PaymentStatus status);

    List<OrderEntity> findByPhone(String phone);

}
