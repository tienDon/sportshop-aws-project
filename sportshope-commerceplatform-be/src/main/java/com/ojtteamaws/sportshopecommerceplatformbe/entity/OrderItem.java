package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Đơn hàng chứa item này
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Sản phẩm gốc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Biến thể cụ thể
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    // Snapshot thông tin tại thời điểm đặt hàng
    @Column(name = "product_name_snapshot", length = 255)
    private String productNameSnapshot;

    @Column(name = "variant_sku_snapshot", length = 100)
    private String variantSkuSnapshot;

    @Column(name = "variant_color_snapshot", length = 100)
    private String variantColorSnapshot;

    @Column(name = "variant_size_snapshot", length = 100)
    private String variantSizeSnapshot;



}
