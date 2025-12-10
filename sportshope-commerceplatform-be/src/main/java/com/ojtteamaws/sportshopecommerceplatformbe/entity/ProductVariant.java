package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import com.ojtteamaws.sportshopecommerceplatformbe.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "product_variants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "color_id", "size_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    @Column(precision = 10, scale = 2)
    private BigDecimal price; // override basePrice

    @Column(nullable = false)
    private Integer stockQuantity = 0;

    private String sku;

    // Prisma dùng Json? → mình dùng TEXT String
    @Convert(converter = StringListConverter.class)
    private List<String> imageUrls;
// JSON string: ["url1", "url2"]

    @OneToMany(mappedBy = "variant")
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "variant")
    private List<OrderItem> orderItems = new ArrayList<>();
}
