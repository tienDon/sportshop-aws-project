package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User sở hữu địa chỉ này
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Địa chỉ đầy đủ: "Số nhà, đường, phường, quận, tỉnh/thành..."
    @Column(name = "address_detail", length = 255, nullable = false)
    private String addressDetail;

    // Địa chỉ mặc định hay không
    @Column(name = "is_default")
    private boolean defaultAddress = false;
}
