package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.OrderDetailDto;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.entity.OrderDetailEntity;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.payload.response.orderDetail.TotalPriceResponse;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.security.CustomerDetailService;
import com.example.demo.service.ColorService;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.ProductService;
import com.example.demo.until.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;
//    private final ProductService productService;
//
//    @Override
//    public List<OrderDetailDto> getAll() {
//        return orderDetailRepository.getAll();
//    }
//
//    @Override
//    public Page<OrderDetailDto> getAllAndPage(Integer pageSize, Integer pageNumber) {
//        Page<OrderDetailDto> list = orderDetailRepository.getAllAndPage(PageRequest.of(pageSize, pageNumber));
//        return list;
//    }
//
//    @Override
//    public OrderDetailEntity getById(Long id) {
//        return orderDetailRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Order detail id not found: " + id));
//    }
//
//    @Override
//    public Page<OrderDetailDto> getByOrder(Long id, Integer pageSize, Integer pageNumber) {
//        Page<OrderDetailDto> list = orderDetailRepository.findAllByOrderId(id, PageRequest.of(pageSize, pageNumber));
//        return list;
//    }
//
//    @Override
//    public Page<OrderDetailDto> getByUserLogin(Integer pageSize, Integer pageNumber) {
//        CustomerDetailService user = CurrentUserUtils.getCurrentUserUtils();
//        Page<OrderDetailDto> list = orderDetailRepository.findAllByUserId(user.getId(), PageRequest.of(pageSize, pageNumber));
//        return list;
//    }
//
//    @Override
//    public Page<OrderDetailDto> getByUser(Long userId, Integer pageSize, Integer pageNumber) {
//        Page<OrderDetailDto> list = orderDetailRepository.findAllByUserId(userId, PageRequest.of(pageSize, pageNumber));
//        return list;
//    }
//
    @Override
    public Collection<OrderDetailEntity> getAllOrderId(Long id) {
        List<OrderDetailEntity> list = orderDetailRepository.findAllByOrderId(id);

        Map<Long, OrderDetailEntity> map = list.stream()
                .collect(Collectors.toMap(OrderDetailEntity::getId, Function.identity()));

        for (Map.Entry<Long, OrderDetailEntity> entry : map.entrySet()) {
            OrderDetailEntity orderDetail = entry.getValue();
            Optional<ProductEntity> productEntity = productRepository.findById(orderDetail.getProductId());
            orderDetail.setProductPrice(productEntity.get().getPriceNew());
            orderDetail.setProductName(productEntity.get().getName());
        }
        return map.values();
    }
//
//    @Override
//    public OrderDetailEntity update(Long id, OrderDetailDto orderDetailDto) {
//        OrderDetailEntity find = orderDetailRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Hóa đơn chi tiết không tồn tại"));
//
//        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
//        orderDetailEntity.setId(find.getId());
//        orderDetailEntity.setProductPrice(orderDetailDto.getPrice());
//        orderDetailEntity.setProductName(orderDetailDto.getName());
//        orderDetailEntity.setQuantity(orderDetailDto.getQuantity());
//        orderDetailEntity.setTotal(orderDetailDto.getTotal());
//        orderDetailEntity.setImage(orderDetailDto.getImage());
//        orderDetailEntity.setProductId(orderDetailDto.getProductId());
//        orderDetailEntity.setOrderId(orderDetailDto.getOrderId());
//        orderDetailEntity.setUserId(orderDetailDto.getUserId());
//
//        return orderDetailRepository.save(orderDetailEntity);
//    }
//
    @Override
    public OrderDetailEntity addOrderDetail(ProductDto productDto) {
        CustomerDetailService uDetailService = CurrentUserUtils.getCurrentUserUtils();

        ProductEntity productEntity = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "product id not found: " + productDto.getId()));

        OrderEntity orderEntity = orderRepository.findById(productDto.getIdOrder())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "order id not found: " + productDto.getIdOrder()));

//        List<ImageEntity> imageEntity = imagesRepository.getImageByProduct(idProduct);

        OrderDetailEntity orderDetail = orderDetailRepository.findAllByOrderIdAndProductId(productDto.getIdOrder(), productDto.getId());

        if (productEntity.getQuantity() == 0)
            throw new BadRequestException("Sản phẩm này đã hết hàng");

        if (orderDetail == null) {
            orderDetail = new OrderDetailEntity();
            orderDetail.setProductPrice(productEntity.getPriceNew());
            orderDetail.setProductName(productEntity.getName());
            orderDetail.setQuantity(1);
            orderDetail.setTotal(orderDetail.getProductPrice() * 1);
//            orderDetail.setImage(imageEntity.get(0).getLink());
            orderDetail.setProductId(productDto.getId());

            orderDetail.setSizeName(productDto.getSizeName());
            orderDetail.setColorName(productDto.getColorName());
            orderDetail.setUserId(uDetailService.getId());
            orderDetail.setOrderId(orderEntity.getId());
            System.out.println("vao kkk");
        } else {
            System.out.println("vao kk 56565k");
            orderDetail.setQuantity(orderDetail.getQuantity() + 1);
            if (productEntity.getQuantity() < orderDetail.getQuantity()) {
                throw new BadRequestException("Bạn chỉ có thể mua tối đa :" + productEntity.getQuantity() + " của sản phẩm này");
            } else {
                orderDetail.setTotal(orderDetail.getProductPrice() * orderDetail.getQuantity());
            }
        }

        return orderDetailRepository.save(orderDetail);
    }
//
//    @Override
//    public OrderDetailEntity updateQuantity(Long productId, Long orderId, Integer quantity) {
//        OrderDetailEntity orderDetailEntity = orderDetailRepository.findAllByOrderIdAndProductId(orderId, productId);
//        if (orderDetailEntity == null) {
//            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "người dùng chưa có sản phẩm id: " + productId + " trong order");
//        }
//
//        ProductEntity findQuantity = productRepository.findById(productId).get();
//        if (orderDetailEntity.getQuantity() > findQuantity.getQuantity())
//            throw new BadRequestException("Số lượng đặt hàng vươt quá số lượng trong kho");
//        else if (findQuantity.getQuantity() == 0)
//            throw new BadRequestException("Sản phẩm này đã hết hàng");
//
//        orderDetailEntity.setQuantity(quantity);
//        orderDetailEntity.setTotal(orderDetailEntity.getProductPrice() * orderDetailEntity.getQuantity());
//
////        List<OrderDetailEntity> findAllBy = orderDetailRepository.findAllByOrderId(orderId);
////
////        for (OrderDetailEntity orderDetail : findAllBy){
////            orderDetailEntity.setTotal(orderDetail.getPrice() * orderDetail.getQuantity());
////        }
//
//        Optional<OrderEntity> findByOrderId = orderRepository.findById(orderId);
//        if (findByOrderId.isPresent()) {
//            OrderEntity orderEntity = findByOrderId.get();
//            orderEntity.setGrandTotal(orderDetailEntity.getTotal());
//            orderRepository.save(orderEntity);
//        }
//        return orderDetailRepository.save(orderDetailEntity);
//    }
//
//
////    @Override
////    public OrderDetailEntity updateQuantitys(Long productId, Long orderId, Integer quantity){
////        OrderDetailEntity orderDetailEntity = orderDetailRepository.findAllByOrderIdAndProductId(orderId, productId);
////        if (orderDetailEntity == null) {
////            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "người dùng chưa có sản phẩm id: " + productId + " trong order");
////        }
////
////        ProductEntity findQuantity = productRepository.findById(productId).get();
////        if (orderDetailEntity.getQuantity() > findQuantity.getQuantity())
////            throw new BadRequestException("Số lượng đặt hàng vươt quá số lượng trong kho");
////        else if (findQuantity.getQuantity() == 0)
////            throw new BadRequestException("Sản phẩm này đã hết hàng");
////
////        orderDetailEntity.setQuantity(quantity);
////        orderDetailEntity.setTotal(orderDetailEntity.getPrice() * orderDetailEntity.getQuantity());
////
//////        List<OrderDetailEntity> findAllBy = orderDetailRepository.findAllByOrderId(orderId);
//////
//////        for (OrderDetailEntity orderDetail : findAllBy){
//////            orderDetailEntity.setTotal(orderDetail.getPrice() * orderDetail.getQuantity());
//////        }
////
////        Optional<OrderEntity> findByOrderId = orderRepository.findById(orderId);
////        if (findByOrderId.isPresent()) {
////            OrderEntity orderEntity = findByOrderId.get();
////            orderEntity.setGrandTotal(orderDetailEntity.getTotal());
////            orderRepository.save(orderEntity);
////        }
////        return orderDetailRepository.save(orderDetailEntity);
////    }
//
//
//    @Override
//    public OrderDetailEntity updateQuantitys(Long productId, Long orderId, Integer quantity) {
//        OrderDetailEntity orderDetailEntity = orderDetailRepository.findAllByOrderIdAndProductId(orderId, productId);
//        if (orderDetailEntity == null) {
//            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "người dùng chưa có sản phẩm id: " + productId + " trong order");
//        }
//
//        ProductEntity findQuantity = productRepository.findById(productId).get();
//        if (findQuantity.getQuantity() < quantity) {
//            throw new BadRequestException("Bạn chỉ có thể mua tối đa :" + findQuantity.getQuantity() + " của sản phẩm này");
//        }
//        orderDetailEntity.setQuantity(quantity);
//
//        orderDetailEntity.setTotal(orderDetailEntity.getProductPrice() * orderDetailEntity.getQuantity());
//
//        Optional<OrderEntity> findByOrderId = orderRepository.findById(orderId);
//        if (findByOrderId.isPresent()) {
//            OrderEntity orderEntity = findByOrderId.get();
//            long total = orderDetailRepository.totalPrice(orderId);
//
//            orderEntity.setId(orderEntity.getId());
//            orderEntity.setGrandTotal(total);
//            orderRepository.save(orderEntity);
//        }
//
//        return orderDetailRepository.save(orderDetailEntity);
//    }
//
//    @Override
//    public void delete(Long id) {
//        OrderDetailEntity find = orderDetailRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Hóa đơn chi tiết không tồn tại"));
//        orderDetailRepository.deleteById(find.getId());
//    }
//
//    @Transactional
//    @Override
//    public void deleteAllByOrderId(Long orderId) {
//        orderDetailRepository.deleteAllByOrderId(orderId);
//    }
//
//    @Override
//    public TotalPriceResponse total(Long orderId) {
//        TotalPriceResponse totalPriceResponse = new TotalPriceResponse();
//        totalPriceResponse.setTotalPrice(orderDetailRepository.totalPrice(orderId));
//
//        return totalPriceResponse;
//    }
    
}
