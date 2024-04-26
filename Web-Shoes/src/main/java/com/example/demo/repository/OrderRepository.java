package com.example.demo.repository;

import com.example.demo.contants.OrderStatus;
import com.example.demo.contants.OrderStatusEnum;
import com.example.demo.contants.PaymentStatus;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.payload.statistical.*;
import org.springframework.data.domain.Pageable;
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

    List<OrderEntity> findAllByStatusEqualsAndUserId(OrderStatusEnum status, Long userId);


    @Query(" SELECT new com.example.demo.payload.statistical.MonthlyAndYearlyStatisticsDto(o.id, YEAR(o.createDate), MONTH(o.createDate), count (o.id), sum(o.grandTotal) )" +
            "FROM OrderEntity o where o.status=3 " +
            "GROUP BY o.id, year (o.createDate), Month (o.createDate)")
    List<MonthlyAndYearlyStatisticsDto> listHoaDonThangVaNam();


//    @Query(" SELECT new com.example.demo.payload.statistical.StatisticalDto(o.id, YEAR(o.createDate), count (o.id), sum(o.grandTotal) )" +
//            "FROM OrderEntity o where o.status=3 " +
//            "GROUP BY year (o.createDate)")
//    List<StatisticalDto> listHoaDonCacNam();
        @Query("SELECT new com.example.demo.payload.statistical.StatisticalDto(YEAR(o.createDate), COUNT(o.id), SUM(o.grandTotal)) " +
                "FROM OrderEntity o WHERE o.status = 3 " +
                "GROUP BY YEAR(o.createDate)")
        List<StatisticalDto> listHoaDonCacNam();

    @Query("select count(o.id) from OrderEntity o where o.status=?1")
    Integer thongKeTrangThaiDonHang(OrderStatusEnum status);

    @Query("SELECT new com.example.demo.payload.statistical.StatisticsOfDaysByMonthAndYearDto(day(o.createDate),month(o.createDate),year(o.createDate), count(o.id), sum(o.grandTotal) )" +
            "FROM OrderEntity o " +
            "WHERE year(o.createDate)=?1 and month(o.createDate)=?2 and o.status=3 " +
            "GROUP BY day(o.createDate),month(o.createDate),year(o.createDate)")
    List<StatisticsOfDaysByMonthAndYearDto> listHoaDonTungNgayTheoThangVaNam(Integer year, Integer month);


    @Query(" SELECT new com.example.demo.payload.statistical.MonthlyAndYearlyStatisticsDto(o.id, YEAR(o.createDate), MONTH(o.createDate), count (o.id), sum(o.grandTotal))" +
            "FROM OrderEntity o " +
            "WHERE year(o.createDate)=?1 and o.status=3 " +
            "GROUP BY o.id,YEAR(o.createDate), MONTH(o.createDate)")
    List<MonthlyAndYearlyStatisticsDto> listHoaDonTungThangTheoNam(Integer year);


    @Query(" SELECT new  com.example.demo.payload.statistical.ThongKeDto(count (o.id), sum(o.grandTotal) )" +
            "FROM OrderEntity o where o.status=3")
    ThongKeDto thongKeTuTruocToiNay();


    @Query(" SELECT new com.example.demo.payload.statistical.ThongKeDto(count (o.id), sum(o.grandTotal) )" +
            "FROM OrderEntity o " +
            "WHERE year(o.createDate)=?1 and month(o.createDate)=?2 and day(o.createDate)=?3 and o.status=3")
    List<ThongKeDto> listHoaDonTheoNgayHienTai(Integer year, Integer mont, Integer day);

    @Query("SELECT new com.example.demo.payload.statistical.ProductBanChayDto(od.productId,od.productName,p.code, sum(od.quantity), sum(od.total)) " +
            "FROM OrderDetailEntity od " +
            "INNER JOIN ProductEntity p ON p.id = od.productId " +
            "INNER join OrderEntity o ON o.id = od.orderId " +
            "WHERE o.status = 3 " +
            "GROUP BY od.productId,od.productName,p.code " +
            "ORDER BY SUM(od.quantity) desc ")
    List<ProductBanChayDto> topSanPhamBanChay(Pageable pageable);

}
