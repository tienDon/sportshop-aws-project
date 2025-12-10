package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/users/admin
    @GetMapping("/admin")
    public ResponseEntity<?> getUsersForAdmin() {
        List<User> users = userService.getUsersForAdmin();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Fetch data users for admin successfully",
                "data", users
        ));
    }

//    // PATCH /api/users/{id}/status
//    @PatchMapping("/admin/{id}/status")
//    public ResponseEntity<?> updateUserStatus(@PathVariable Long id) {
//        User updatedUser = userService.updateUserStatus(id);
//        return ResponseEntity.ok(Map.of(
//                "success", true,
//                "message", "Update user successfully",
//                "data", updatedUser
//        ));
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok(Map.of(
//                "success", true,
//                "message", "Delete user successfully"
//        ));
//    }
}
