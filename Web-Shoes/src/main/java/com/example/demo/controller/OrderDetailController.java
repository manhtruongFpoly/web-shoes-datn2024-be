package com.example.demo.controller;


import com.example.demo.model.dto.ProductDto;
import com.example.demo.payload.response.SampleResponse;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/orderDetail")
@AllArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderService orderService;


    //todo:lấy tất cả danh sách và phân trang theo order
    @GetMapping("order/{id}")
    public ResponseEntity<?> getAllOrderDetailByOrder(
            @PathVariable("id") Long id,
            @RequestParam(value = "page") Integer pageSize,
            @RequestParam(value = "page-number") Integer pageNumber) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Get All OrderDetail By Order id")
                .data(orderDetailService.getByOrder(id, pageSize, pageNumber))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/create-order-detail")
    public ResponseEntity<?> createOrderDetail(
            @RequestBody ProductDto productDto
    ) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Cập nhập thành công")
                .data(orderDetailService.addOrderDetail(productDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    //todo:lấy danh sách order detail theo order id
    @GetMapping("/order-id/{id}")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable("id") Long orderId) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Get OrderDetail by id")
                .data(orderDetailService.getAllOrderId(orderId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/sum-price-order/{id}")
    public ResponseEntity<?> sumOrderDetail(
            @PathVariable("id") Long idOrder
    ) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Cập nhập thành công")
                .data(orderService.sumTotalOrderDetail(idOrder))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/updateQuantitys/{productId}/order/{orderId}")
    public ResponseEntity updateQuantityGet(
            @PathVariable("productId") Long productId,
            @PathVariable("orderId") Long orderId,
            @RequestParam("quantity") Integer quantity
    ) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("update")
                .data(orderDetailService.updateQuantity(productId, orderId, quantity))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("updateQuantity/{productId}/order/{orderId}")
    public ResponseEntity updateQuantity(
            @PathVariable("productId") Long productId,
            @PathVariable("orderId") Long orderId,
            @RequestParam("quantity") Integer quantity
    ) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("update")
                .data(orderDetailService.updateQuantitys(productId, orderId, quantity))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete/{orderDetailId}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("orderDetailId") Long id) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Xóa thành công")
                .data(null)
                .build();
        orderDetailService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
