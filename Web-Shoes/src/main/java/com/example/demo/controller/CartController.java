package com.example.demo.controller;

import com.example.demo.model.dto.CartDto;
import com.example.demo.payload.response.SampleResponse;
import com.example.demo.security.CustomerDetailService;
import com.example.demo.service.CartService;
import com.example.demo.until.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //todo:lấy danh sách giỏ hàng theo người dùng đăng nhập
    @GetMapping("/user")
    public ResponseEntity<?> getCartByUser() {
        CustomerDetailService uDetailService = CurrentUserUtils.getCurrentUserUtils();
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Lấy thông tin Cart theo User")
                .data(cartService.getAllByUser(uDetailService.getId()))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") Long id) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Lấy thông tin Cart theo id")
                .data(cartService.getById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //todo:lấy thông tin tổng tiền giỏ hàng và số lượng sản phẩn trong giỏ hàng
    @GetMapping("/sumTotalAndQuantity")
    public ResponseEntity<?> sumTotalAndQuantity() {
        CustomerDetailService uDetailService = CurrentUserUtils.getCurrentUserUtils();
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Success")
                .data(cartService.sumTotalPriceAndQuantity(uDetailService.getId()))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //todo:lấy toàn bộ danh sách giỏ hàng
    @GetMapping("/list")
    public ResponseEntity<?> getAllCart() {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Lấy thông tin Cart")
                .data(cartService.getAllCart())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //todo:thêm sản phẩm trong giỏ hàng
    @PostMapping("/addToCart")
    public ResponseEntity addToCart(@RequestBody CartDto cartDto) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Add to cart")
                .data(cartService.addToCart(cartDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //todo:cập nhập sản phẩm trong giỏ hàng
    @PutMapping("/updateQuantity/{id}")
    public ResponseEntity updateQuantity(
            @PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity
    ) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("update")
                .data(cartService.updateQuantity(id, quantity))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //todo:xóa sản phẩm trong giỏ hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") Long id) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Xóa thành công")
                .data(null)
                .build();
        cartService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //todo:xóa hêt sản phẩm trong giỏ hàng theo user
    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAll() {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Xóa thành công")
                .data(null)
                .build();
        cartService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
