package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "attribute_values",
        uniqueConstraints = @UniqueConstraint(columnNames = {"attribute_id", "value"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value", length = 100, nullable = false)
    private String value;
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @OneToMany(mappedBy = "attributeValue")
    private List<ProductAttributeValue> productAttributeValues = new ArrayList<>();
}
