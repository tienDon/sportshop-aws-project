package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    private String couponCodeApplied;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal totalGrossAmount;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal totalDiscountAmount = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal totalFinalAmount;

    @Column(name = "order_code", length = 50)
    private String orderCode;

    // Shipping snapshot
    @Column(nullable = false)
    private String shippingName;

    @Column(nullable = false)
    private String shippingPhone;

    @Column(nullable = false)
    private String shippingStreet;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private OrderStatus status;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}

