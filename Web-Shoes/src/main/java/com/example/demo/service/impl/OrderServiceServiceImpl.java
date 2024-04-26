package com.example.demo.service.impl;

import com.example.demo.contants.OrderStatus;
import com.example.demo.contants.OrderStatusEnum;
import com.example.demo.contants.PaymentStatus;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.CreateOrderReq;
import com.example.demo.model.entity.CartEntity;
import com.example.demo.model.entity.OrderDetailEntity;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.payload.request.CreateDeliveryOrder;
import com.example.demo.payload.request.CreateOrderAtTheCounter;
import com.example.demo.payload.request.OrderDetailResponse;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.security.CustomerDetailService;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.until.CurrentUserUtils;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CartServiceImpl cartService;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailService orderDetailService;
    private final ProductRepository productRepository;

    @Override
    public List<OrderEntity> getAll() {
        return orderRepository.findAllOrderById();
    }

    @Override
    @Transactional
    public OrderEntity checkoutOrder(CreateOrderReq req) throws MessagingException {
        OrderEntity order = new OrderEntity();
        CustomerDetailService uDetailService = CurrentUserUtils.getCurrentUserUtils();

        Long total_amount = cartRepository.sumPrice(uDetailService.getId());

        System.out.println(total_amount + "total_order");
        if (uDetailService.getId() != null) {

            order.setUserId(uDetailService.getId());
            order.setShipping(req.getShipping());

            if(req.getVnp_ResponseCode() == null){
                order.setStatus(OrderStatusEnum.CHOXACNHAN);
            }else{
                if(req.getVnp_ResponseCode().equals("00")){
                    order.setStatus(OrderStatusEnum.DATHANHTOAN);
                }else{
                    order.setStatus(OrderStatusEnum.CHOXACNHAN);
                }
            }


            order.setMahd("order_class" +
                    Calendar.getInstance().get(Calendar.YEAR) +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1) +
                    Calendar.getInstance().get(Calendar.DATE) +
                    "_" +
                    Calendar.getInstance().get(Calendar.HOUR) +
                    Calendar.getInstance().get(Calendar.MINUTE) +
                    Calendar.getInstance().get(Calendar.MILLISECOND));
            order.setFullname(req.getFullname());
            order.setPhone(req.getPhone());
            order.setDescription(req.getDescription());
            order.setPaymentId(req.getPaymentId());
            order.setAddress(req.getAddress());
//            order.setPaymentStatus(PaymentStatus.CHUATHANHTOAN);
            order.setPaymentId(req.getPaymentId());
            order.setOrderStatus(OrderStatus.DONGIAO);
            order.setProvince(req.getProvince());
            order.setDistrict(req.getDistrict());
            order.setWard(req.getWard());

//            order.setGrandTotal((long) (total_amount + order.getShipping() - order.getDiscount()));
            order.setGrandTotal(req.getGrandTotal());

            System.out.println(uDetailService.getId() + "account id");
            order.setUserId(uDetailService.getId());
            orderRepository.save(order);
        }

        Collection<CartEntity> listCartItem = cartService.getAllByUser(uDetailService.getId());
        for (CartEntity cartItem : listCartItem) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();

            orderDetail.setProductPrice(cartItem.getPrice());
            orderDetail.setQuantity(cartItem.getQuantity());

            long total = orderDetail.getProductPrice() * orderDetail.getQuantity();
            orderDetail.setTotal(total);

            orderDetail.setProductName(cartItem.getName());
            orderDetail.setImage(cartItem.getImage());
            orderDetail.setProductId(cartItem.getProductId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setUserId(uDetailService.getId());
            orderDetail.setColorName(cartItem.getColorName());
            orderDetail.setSizeName(cartItem.getSizeName());

            orderDetailRepository.save(orderDetail);
            ProductEntity productEntity = productService.getOne(cartItem.getProductId());

            if (cartItem.getQuantity() > productEntity.getQuantity())
                throw new BadRequestException("Số lượng đặt hàng vươt quá số lượng trong kho");
            else if (productEntity.getQuantity() == 0)
                throw new BadRequestException("Sản phẩm này đã hết hàng");


            int quantityOrderDetail = orderDetail.getQuantity();
            int quantityProduct = productEntity.getQuantity();

            int updateQuantity = quantityProduct - quantityOrderDetail;
            productService.updateQuantity(productEntity.getId(), updateQuantity);
            // xoa cart
            cartRepository.deleteById(cartItem.getId());
        }
        return order;
    }
    @Override
    public OrderEntity orderConfirmed(Long orderId) {
        CustomerDetailService uDetailService = CurrentUserUtils.getCurrentUserUtils();
        Optional<OrderEntity> findByOrderId = orderRepository.findById(orderId);
        if (findByOrderId.isPresent()) {
            OrderEntity order = findByOrderId.get();
            if (order.getStatus() == OrderStatusEnum.CHOXACNHAN || order.getStatus() == OrderStatusEnum.DAHOANTHANH) {
                order.setStatus(OrderStatusEnum.DANGXULY);
//                order.setStaffId(uDetailService.getId());
                order.setGrandTotal((long) (order.getGrandTotal() + order.getShipping()));
                return orderRepository.save(order);
            } else {
                throw new BadRequestException("Trạng thái phải là đang chờ xác nhận mới xác nhận được");
            }
        }
        return findByOrderId.orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Order id not found: " + orderId));
    }
    @Override
    public OrderEntity beingShipped(Long orderId) {
        Optional<OrderEntity> findByOrderId = orderRepository.findById(orderId);
        if (findByOrderId.isPresent()) {
            OrderEntity order = findByOrderId.get();
            order.setStatus(OrderStatusEnum.DANGVANCHUYEN);
            return orderRepository.save(order);
        }
        return findByOrderId.orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Order id not found: " + orderId));
    }

    //đã giao
    @Override
    public OrderEntity delivered(Long orderId) {
        Optional<OrderEntity> findByOrderId = orderRepository.findById(orderId);
        if (findByOrderId.isPresent()) {
            OrderEntity order = findByOrderId.get();
            if (order.getStatus() == OrderStatusEnum.DANGVANCHUYEN) {
                order.setStatus(OrderStatusEnum.DAGIAO);
                return orderRepository.save(order);
            }
        }
        return findByOrderId.orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Order id not found: " + orderId));
    }

    @Override
    public OrderEntity cancelled(Long orderId, String reason) {
        CustomerDetailService uDetailService = CurrentUserUtils.getCurrentUserUtils();
        Optional<OrderEntity> findByOrderId = orderRepository.findById(orderId);
        if (findByOrderId.isPresent()) {
            OrderEntity order = findByOrderId.get();

//            if (order.getAccountId() != uDetailService.getId()) {
//                throw new BadRequestException("Bạn không phải là chủ đơn hàng");
//            }

            if (order.getStatus() == OrderStatusEnum.DANGVANCHUYEN) {
                throw new BadRequestException("Đơn hàng của bạn đang vận chuyển không thể hủy đơn hàng");
            } else if (order.getStatus() == OrderStatusEnum.DAGIAO) {
                throw new BadRequestException("Đơn hàng đã được giao không thể hủy đơn hàng");
            } else {
                order.setStatus(OrderStatusEnum.DAHUY);
                order.setReason(reason);
//                order.setPaymentStatus(PaymentStatus.HUY);

                Collection<OrderDetailEntity> listOrderDetail = orderDetailRepository.findAllByUserIdAndOrderId(uDetailService.getId(),orderId);
                System.out.println(listOrderDetail + "abc");
                for (OrderDetailEntity listOrder : listOrderDetail) {

                    ProductEntity productEntity = productService.getOne(listOrder.getProductId());

                    int quantityOrder = listOrder.getQuantity();
                    int quantityProduct = productEntity.getQuantity();

                    int updateQuantity = quantityProduct + quantityOrder;
                    productService.updateQuantity(productEntity.getId(), updateQuantity);
                }
                return orderRepository.save(order);
            }
        }
        return findByOrderId.orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Order id not found: " + orderId));
    }

    // danh sách hóa đơn theo status
    @Override
    public List<OrderEntity> listStatus(OrderStatusEnum status) {
        return orderRepository.findAllByStatusEqualsAndOrderStatusOrderByIdDesc(status, OrderStatus.DONGIAO);
    }
    @Override
    public OrderStatusEnum[] status() {
        OrderStatusEnum[] status = OrderStatusEnum.values();
        System.out.println(status);
        return status;
    }

    @Override
    public long countOrderStatus(int status) {
        CustomerDetailService detailService = CurrentUserUtils.getCurrentUserUtils();
        return orderRepository.countOrderStatus(status, detailService.getId());
    }

    @Override
    public OrderEntity findByIdOrder(Long id) {
        Optional<OrderEntity> optional = orderRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BadRequestException("Order id not found");
        }
        return optional.get();
    }

    @Override
    public List<OrderEntity> listStatusPaymentPaid() {
        return orderRepository.findAllByPaymentStatusEquals(PaymentStatus.DATHANHTOAN);
    }

    @Override
    public OrderEntity createAnOrderAtTheCounter(CreateOrderAtTheCounter req) {
        CustomerDetailService detailService = CurrentUserUtils.getCurrentUserUtils();

        OrderEntity order = new OrderEntity();

        order.setFullname(req.getFullname());
        order.setProvince(req.getProvince());
        order.setDistrict(req.getDistrict());
        order.setWard(req.getWard());
        order.setAddress(req.getAddress());
        order.setPhone(req.getPhone());
        order.setDescription(req.getDescription());

//        order.setNameStaff(detailService.getFullname());
//        order.setStaffId(detailService.getId());
        order.setShipping(0);
        order.setPaymentStatus(PaymentStatus.CHUATHANHTOAN);
        order.setStatus(OrderStatusEnum.DANGXULY);
        order.setOrderStatus(OrderStatus.TAIQUAY);

        return orderRepository.save(order);
    }

    @Override
    public OrderEntity checkoutAtTheCounter(Long orderId) {

        long total_amount = orderDetailRepository.sumPrice(orderId);

        Optional<OrderEntity> findByIdOrder = orderRepository.findById(orderId);
        OrderEntity order = findByIdOrder.get();
        if (findByIdOrder.isPresent()) {

            order.setGrandTotal(total_amount + order.getShipping());
            order.setPaymentStatus(PaymentStatus.DATHANHTOAN);

            if (order.getOrderStatus() == OrderStatus.DONGIAO) {
                order.setStatus(OrderStatusEnum.DANGXULY);
                System.out.println("vao order 1");
            } else if (order.getOrderStatus() == OrderStatus.TAIQUAY) {
                order.setStatus(OrderStatusEnum.DAHOANTHANH);
                System.out.println("vao order 2");
            } else {
                System.out.println("vao order 3");
                order.setStatus(OrderStatusEnum.DAHOANTHANH);
            }
            System.out.println("vao order 4");

            Collection<OrderDetailEntity> listOrderDetail = orderDetailService.getAllOrderId(orderId);
            for (OrderDetailEntity orderDetail : listOrderDetail) {

                ProductEntity productEntity = productService.getOne(orderDetail.getProductId());

                if (orderDetail.getQuantity() > productEntity.getQuantity())
                    throw new BadRequestException("Số lượng đặt hàng vươt quá số lượng trong kho");
                else if (productEntity.getQuantity() == 0)
                    throw new BadRequestException("Sản phẩm này đã hết hàng");

                int quantityOrderDetail = orderDetail.getQuantity();
                int quantityProduct = productEntity.getQuantity();

                int updateQuantity = quantityProduct - quantityOrderDetail;
                productService.updateQuantity(productEntity.getId(), updateQuantity);
            }

            return orderRepository.save(order);
        }
        throw new BadRequestException("Lỗi khi thành toán");
    }


    @Override
    public OrderEntity createDeliveryOrder(CreateDeliveryOrder req) {
        CustomerDetailService detailService = CurrentUserUtils.getCurrentUserUtils();

        OrderEntity order = new OrderEntity();

        order.setFullname(req.getFullname());
        order.setProvince(req.getProvince());
        order.setDistrict(req.getDistrict());
        order.setWard(req.getWard());
        order.setAddress(req.getAddress());
        order.setPhone(req.getPhone());
        order.setDescription(req.getDescription());
        order.setShipping(req.getShipping());

//        order.setNameStaff(detailService.getFullname());
//        order.setStaffId(detailService.getId());
        order.setPaymentStatus(PaymentStatus.CHUATHANHTOAN);
        order.setStatus(OrderStatusEnum.DANGXULY);
        order.setOrderStatus(OrderStatus.DONGIAO);

        return orderRepository.save(order);
    }

    @Override
    public OrderEntity updateDeliveryOrder(Long orderId, CreateDeliveryOrder req) {
        Optional<OrderEntity> findById = orderRepository.findById(orderId);

        if (findById.isPresent()) {
            OrderEntity order = findById.get();

            order.setFullname(req.getFullname());
            order.setProvince(req.getProvince());
            order.setDistrict(req.getDistrict());
            order.setWard(req.getWard());
            order.setAddress(req.getAddress());
            order.setPhone(req.getPhone());
            order.setDescription(req.getDescription());
            order.setShipping(req.getShipping());
            order.setOrderStatus(OrderStatus.DONGIAO);

            return orderRepository.save(order);
        } else {
            throw new BadRequestException("Order Id not found");
        }

    }

    @Override
    public OrderEntity retailOrders(Long idOrder) {
        Optional<OrderEntity> findById = orderRepository.findById(idOrder);

        if (findById.isPresent()) {
            OrderEntity order = findById.get();
            order.setFullname("Khách hàng lẻ");
            order.setDescription("Khách hàng mua lẻ không muốn để lại thông tin");

            order.setOrderStatus(OrderStatus.DONLE);
            return orderRepository.save(order);
        }
        throw new BadRequestException("Id order not found");
    }

    @Override
    public OrderEntity updateAtTheCounterOrder(Long orderId, CreateOrderAtTheCounter req) {
        Optional<OrderEntity> findById = orderRepository.findById(orderId);
        if (findById.isPresent()) {
            OrderEntity order = findById.get();

            order.setFullname(req.getFullname());
            order.setProvince(req.getProvince());
            order.setDistrict(req.getDistrict());
            order.setWard(req.getWard());
            order.setPhone(req.getPhone());
            order.setDescription(req.getDescription());
            order.setOrderStatus(OrderStatus.TAIQUAY);
            order.setShipping(0);

            if (req.getAddress() == null) {
                order.setAddress(order.getAddress());
                System.out.println("vao day 1");
            } else {
                order.setAddress(req.getAddress());
                System.out.println("vao day 2");
            }
            System.out.println("vao day 3");

            return orderRepository.save(order);
        } else {
            throw new BadRequestException("Order Id not found");
        }

    }

    private long countOrderStatus() {
        return orderRepository.countOrderStatus();
    }

    @Override
    public OrderEntity createOrder() {
        CustomerDetailService detailService = CurrentUserUtils.getCurrentUserUtils();

        if (this.countOrderStatus() > 9) {
            throw new BadRequestException("Chỉ được tạo tối đa 10 đơn hàng chờ");
        }

        OrderEntity order = new OrderEntity();

        order.setMahd("order_class" +
                Calendar.getInstance().get(Calendar.YEAR) +
                (Calendar.getInstance().get(Calendar.MONTH) + 1) +
                Calendar.getInstance().get(Calendar.DATE) +
                "_" +
                Calendar.getInstance().get(Calendar.HOUR) +
                Calendar.getInstance().get(Calendar.MINUTE) +
                Calendar.getInstance().get(Calendar.MILLISECOND));

//        order.setNameStaff(detailService.getFullname());
//        order.setStaffId(detailService.getId());
        order.setShipping(0);
        order.setPaymentStatus(PaymentStatus.DANGSULY);
        order.setStatus(OrderStatusEnum.DANGXULY);
        order.setOrderStatus(OrderStatus.DONCHO);

        return orderRepository.save(order);
    }

    @Override
    public List<OrderEntity> filterStatusOrder(OrderStatus status) {
        return orderRepository.findAllByOrderStatusEquals(status);
    }

    @Override
    public OrderEntity findByMahd(String mahd) {

        Optional<OrderEntity> findByMahd = orderRepository.findByMahd(mahd);

        if (!findByMahd.isPresent())
            throw new BadRequestException("Mã Hóa Đơn Không tìm thấy");

        return findByMahd.get();
    }


    @Override
    public List<OrderEntity> searchOrder(String name) {
        List<OrderEntity> findByPhone = orderRepository.findByPhone(name);
        List<OrderEntity> list;
        for (OrderEntity order : findByPhone) {
            if (order.getPhone().equals(name)) {
                list = orderRepository.searchAllByOrder(name, PaymentStatus.DATHANHTOAN);
            } else {
                throw new BadRequestException("Không tìm thầy");
            }
            return list;
        }
        throw new BadRequestException("khong tim thay");
    }

    @Override
    public List<OrderEntity> listStatusPayment() {
        return orderRepository.findAllByPaymentStatusEquals(PaymentStatus.DANGSULY);
    }

    @Override
    public OrderDetailResponse sumTotalOrderDetail(Long idOrder) {

        OrderEntity findByOrder = orderRepository.findById(idOrder).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Order id not found: " + idOrder));

        OrderDetailResponse response = new OrderDetailResponse();
        response.setTotalAmount(orderDetailRepository.sumPrice(idOrder));
        response.setShipping(findByOrder.getShipping());
        return response;
    }


    @Override
    public List<OrderEntity> listOrderStatusAndUserId(OrderStatusEnum status) {
        CustomerDetailService detailService = CurrentUserUtils.getCurrentUserUtils();
        return orderRepository.findAllByStatusEqualsAndUserId(status, detailService.getId());
    }

    @Override
    public void reOrder(Long orderId) {
        CustomerDetailService userUtils = CurrentUserUtils.getCurrentUserUtils();

        List<OrderDetailEntity> findByIdOrder = orderDetailRepository.findAllByOrderIdAndUserId(orderId, userUtils.getId());

        for (OrderDetailEntity order : findByIdOrder) {
//          cartRepository.deleteById(order.getProductId());
            CartEntity cart = new CartEntity();
            BeanUtils.copyProperties(order, cart);

            ProductEntity productEntity = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "product id not found: " + cart.getProductId()));
            if (productEntity.getQuantity() <= 0) {
                throw new BadRequestException("Sản phẩm này đã hết hàng, vui lòng chờ cửa hàng nhập thêm");
            }
            cartRepository.save(cart);
        }
    }


}
