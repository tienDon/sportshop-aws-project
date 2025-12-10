package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.ICartItemRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.ICartRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private ICartItemRepository cartItemRepository;


    // Lấy tất cả user role = CUSTOMER
    public List<User> getUsersForAdmin() {
        return userRepository.findUserByRole("USER");
    }

//    // Toggle trạng thái active (mình sẽ dùng emailVerified làm ví dụ)
//    public User updateUserStatus(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setEmailVerified(!user.isEmailVerified());
//
//        return userRepository.save(user);
//    }

//    // Xóa user
//    public void deleteUser(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        cartItemRepository.deleteByCartUserId(userId); // xóa items trước
//        cartRepository.deleteByUser_Id((userId));
//
//        userRepository.delete(user);
//
//    }
}
