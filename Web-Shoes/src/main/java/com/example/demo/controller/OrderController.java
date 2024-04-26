package com.example.demo.controller;

import com.example.demo.contants.OrderStatus;
import com.example.demo.contants.OrderStatusEnum;
import com.example.demo.model.dto.CreateOrderReq;
import com.example.demo.payload.request.CreateDeliveryOrder;
import com.example.demo.payload.request.CreateOrderAtTheCounter;
import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.service.OrderService;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    //todo:Danh sách tất cả hóa đơn
    @GetMapping("")
    public ResponseEntity<?> getAll()  {
        return ResponseEntity.ok(DefaultResponse.success(orderService.getAll()));
    }



    //todo:Đặt hàng phía user
    @PostMapping("/check-out")
    public ResponseEntity<?> checkoutOrder(@Valid @RequestBody CreateOrderReq order) throws MessagingException {
        return ResponseEntity.ok(DefaultResponse.success(orderService.checkoutOrder(order)));
    }


    //todo:Xác nhận đơn đặt hàng
    @GetMapping("/order-confirm/{id}")
    public ResponseEntity<?> orderConfirmed(
            @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok(
                DefaultResponse.success(orderService.orderConfirmed(orderId)));
    }


    //todo:Đang vận chuyển đơn đặt hàng
    @GetMapping("/being-shipped/{id}")
    public ResponseEntity<?> beingShipped(
            @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.beingShipped(orderId)));
    }

    //todo:Đã giao đơn đặt hàng
    @GetMapping("/delivered/{id}")
    public ResponseEntity<?> delivered(
            @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.delivered(orderId)));
    }

    //todo:Hủy đơn đặt hàng
    @GetMapping("/cancelled/{id}")
    public ResponseEntity<?> cancelled(
            @PathVariable("id") Long orderId,
            @RequestParam(name = "reason", required = false) String reason
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.cancelled(orderId, reason)));
    }

    //todo:Danh sách hóa đơn theo status
    @GetMapping("/list-status/{status}")
    public ResponseEntity<?> listStatus(
            @PathVariable("status") OrderStatusEnum status
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.listStatus(status)));
    }


    @GetMapping("/status")
    public ResponseEntity<?> status(){
        return ResponseEntity.ok(DefaultResponse.success(orderService.status()));
    }

    //todo:Đếm số lượng đơn hàng theo trạng thái và tài khoản người dùng
    @GetMapping("/count-order/{status}")
    public ResponseEntity<?> countOrder(
            @PathVariable("status") int status
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.countOrderStatus(status)));
    }

    //todo:Lấy ra order theo id
    @GetMapping("/get-one/{id}")
    public ResponseEntity<?> findByOrderId(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.findByIdOrder(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(value = "name",required = false) String name
    ) {
        return ResponseEntity.ok(DefaultResponse.success(
                orderService.searchOrder(name)));
    }

    //todo:Danh sách hóa đơn theo trang thái chua thanh toán
    @GetMapping("/list-status-payment")
    public ResponseEntity<?> listStatusPayment(
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.listStatusPayment()));
    }

    //todo:Danh sách hóa đơn theo trang thái đã thanh toán
    @GetMapping("/list-status-payment-paid")
    public ResponseEntity<?> listStatusPaymentPaid(
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.listStatusPaymentPaid()));
    }

    //todo:Tạo hóa đơn tại quầy
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody CreateOrderAtTheCounter order
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.createAnOrderAtTheCounter(order)));
    }

    //todo:Đặt hàng tại quầy
    @GetMapping("/checkout-order/{id}")
    public ResponseEntity<?> checkoutAtTheCounter(
            @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.checkoutAtTheCounter(orderId)));
    }

    //todo:Tạo đơn hang giao
    @PostMapping("/create-delivery-order")
    public ResponseEntity<?> createDeliveryOrder(
            @Valid @RequestBody CreateDeliveryOrder order
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.createDeliveryOrder(order)));
    }


    //todo:Cập nhật đơn hang giao
    @PutMapping("/update-delivery-order/{id}")
    public ResponseEntity<?> updateDeliveryOrder(
            @PathVariable("id") Long orderId,
            @Valid @RequestBody CreateDeliveryOrder order
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.updateDeliveryOrder(orderId,order)));
    }

    //todo:Tạo đơn hàng bán lẻ"
    @GetMapping("/create-retail-order/{id}")
    public ResponseEntity<?> retailOrder(
            @PathVariable("id") Long idOrder
    ) {
        return ResponseEntity.ok(
                DefaultResponse.success(orderService.retailOrders(idOrder))
        );
    }

    //todo:Cập nhật đơn hang tại quầy
    @PutMapping("/update-order/{id}")
    public ResponseEntity<?> updateAtTheCounterOrder(
            @PathVariable("id") Long orderId,
            @Valid @RequestBody CreateOrderAtTheCounter order
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.updateAtTheCounterOrder(orderId,order)));
    }

    //todo:Tạo đơn hàng chờ"
    @GetMapping("/create-or")
    public ResponseEntity<?> createO() {
        return ResponseEntity.ok(DefaultResponse.success(orderService.createOrder()));
    }

    //todo:Lọc theo loại đơn hàng
    @GetMapping("/filter-order/{mahd}")
    public ResponseEntity<?> filterOrder(
            @PathVariable("mahd") OrderStatus status
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.filterStatusOrder(status)));
    }

    //todo:Lọc theo loại đơn hàng
    @GetMapping("/find-mahd/{mahd}")
    public ResponseEntity<?> findByMahd(
            @PathVariable("mahd") String mahd
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.findByMahd(mahd)));
    }


    //todo: Danh sách hóa đơn theo status và account
    @GetMapping("/list-status-account/{status}")
    public ResponseEntity<?> listStatusAndAccount(
            @PathVariable("status") OrderStatusEnum status
    ) {
        return ResponseEntity.ok(DefaultResponse.success(orderService.listOrderStatusAndUserId(status)));
    }


    //todo: Đặt lại đơn hàng đã mua
    @GetMapping("/re-order/{id}")
    public ResponseEntity<?> reOrder(
            @PathVariable("id") Long id
    ) {
        orderService.reOrder(id);
        return ResponseEntity.ok(DefaultResponse.success("Đặt lại đơn hàng thành công"));
    }



}
