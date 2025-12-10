package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_audiences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "audience_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAudience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "audience_id", nullable = false)
    private Audience audience;
}
