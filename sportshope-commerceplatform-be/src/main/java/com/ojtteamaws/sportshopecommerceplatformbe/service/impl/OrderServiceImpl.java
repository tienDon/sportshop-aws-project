package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CreateOrderRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.*;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.*;

import com.ojtteamaws.sportshopecommerceplatformbe.repository.*;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.OrderStatus.CONFIRMED;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserPhoneRepository userPhoneRepository;
    private final IUserRepository userRepository;

    @Override
    public OrderDetailResponse createOrder(Long userId, CreateOrderRequest request) {

        // 1. Lấy cart
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getUser() == null || !cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("Cart does not belong to user");
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Lấy địa chỉ giao hàng (UserAddress)
        UserAddress address = userAddressRepository.findById(request.getUserAddressId())
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        if (address.getUser() == null || !address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Invalid shipping address");
        }

        // 3. Lấy số điện thoại (UserPhone)
        UserPhone userPhone = userPhoneRepository.findById(request.getUserPhoneId())
                .orElseThrow(() -> new RuntimeException("Phone not found"));

        if (userPhone.getUser() == null || !userPhone.getUser().getId().equals(userId)) {
            throw new RuntimeException("Invalid phone");
        }

        // 4. Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Tạo Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(CONFIRMED); // hoặc PENDING tuỳ flow
        order.setOrderCode("#ORD-" + System.currentTimeMillis());

        // Snapshot thông tin giao hàng
        order.setShippingName(user.getFullName());
        order.setShippingPhone(userPhone.getPhoneNumber());
        order.setShippingStreet(address.getAddressDetail());


        order.setNote(request.getNote());

        order.setTotalDiscountAmount(BigDecimal.ZERO);
        order.setTotalGrossAmount(BigDecimal.ZERO);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 6. Tạo OrderItems
        for (CartItem ci : cart.getItems()) {
            Product product = ci.getProduct();
            ProductVariant variant = ci.getVariant();

            BigDecimal price = variant.getPrice() != null
                    ? variant.getPrice()
                    : product.getBasePrice();

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setVariant(variant);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(price);

            // snapshots
            oi.setProductNameSnapshot(product.getName());
            oi.setVariantSkuSnapshot(variant.getSku() != null ? variant.getSku() : "");
            oi.setVariantColorSnapshot(
                    variant.getColor() != null ? variant.getColor().getName() : ""
            );
            oi.setVariantSizeSnapshot(
                    variant.getSize() != null ? variant.getSize().getName() : ""
            );

            order.getItems().add(oi);
        }

        order.setTotalFinalAmount(totalAmount);
        order.setTotalGrossAmount(totalAmount);

        order = orderRepository.save(order);

        // 7. Xoá items trong cart sau khi đặt hàng
        cartItemRepository.deleteByCart_Id(cart.getId());

        return mapOrderToDetail(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListResultResponse getOrders(Long userId, Integer page, Integer limit) {
        int p = (page != null && page > 0) ? page : 1;
        int l = (limit != null && limit > 0) ? limit : 10;

        PageRequest pr = PageRequest.of(p - 1, l);

        Page<Order> orderPage = orderRepository.findByUser_IdOrderByCreatedAtDesc(userId, pr);

        List<OrderListItemResponse> orderDtos = orderPage.getContent().stream()
                .map(this::mapOrderToListItem)
                .collect(Collectors.toList());

        OrderListResultResponse result = new OrderListResultResponse();
        result.setOrders(orderDtos);

        PaginationResponse pagination = new PaginationResponse();
        pagination.setTotal(orderPage.getTotalElements());
        pagination.setPage(p);
        pagination.setLimit(l);
        pagination.setTotalPages( orderPage.getTotalPages());

        result.setPagination(pagination);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Order not found");
        }

        return mapOrderToDetail(order);
    }

    // ---------- mapping helpers ----------

    private OrderListItemResponse mapOrderToListItem(Order order) {
        OrderListItemResponse dto = new OrderListItemResponse();
        dto.setOrderId(order.getId());
        dto.setOrderCode(order.getOrderCode());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalFinalAmount(order.getTotalFinalAmount());
        dto.setReceiverName(order.getShippingName());
        dto.setShippingAddress(buildShippingAddress(order));

        List<OrderItemSummaryResponse> items = order.getItems().stream()
                .map(this::mapOrderItemToSummary)
                .collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }

    private OrderDetailResponse mapOrderToDetail(Order order) {
        OrderDetailResponse dto = new OrderDetailResponse();
        dto.setOrderId(order.getId());
        dto.setOrderCode(order.getOrderCode());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalFinalAmount(order.getTotalFinalAmount());
        dto.setReceiverName(order.getShippingName());
        dto.setShippingAddress(buildShippingAddress(order));
        dto.setNote(order.getNote());

        List<OrderItemSummaryResponse> items = order.getItems().stream()
                .map(this::mapOrderItemToSummary)
                .collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }

    private OrderItemSummaryResponse mapOrderItemToSummary(OrderItem item) {
        OrderItemSummaryResponse dto = new OrderItemSummaryResponse();
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setProductName(item.getProductNameSnapshot());
        dto.setVariantDetails(
                (item.getVariantColorSnapshot() != null ? item.getVariantColorSnapshot() : "") +
                        " / " +
                        (item.getVariantSizeSnapshot() != null ? item.getVariantSizeSnapshot() : "")
        );

        Product product = item.getProduct();
        dto.setMainImageUrl(product != null ? product.getMainImageUrl() : null);
        return dto;
    }

    private String buildShippingAddress(Order order) {
        // vì bạn dùng 1 field addressDetail -> mình map đơn giản:
        // nếu sau này Order có field riêng như shippingStreet/City thì sửa theo đó
        if (order.getShippingStreet() != null) {
            return order.getShippingStreet();
        }
        return "";
    }
}
