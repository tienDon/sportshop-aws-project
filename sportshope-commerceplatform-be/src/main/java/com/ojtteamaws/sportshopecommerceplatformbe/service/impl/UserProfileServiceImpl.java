package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UpdateProfileRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UserAddressRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UserPhoneRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserAddressResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserPhoneResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserProfileResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.UserAddress;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.UserPhone;
import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.Gender;
import com.ojtteamaws.sportshopecommerceplatformbe.mapper.UserProfileMapper;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.IUserRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.UserAddressRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.UserPhoneRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements IUserProfileService {

    private final IUserRepository userRepository;
    private final UserPhoneRepository userPhoneRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserProfileMapper userProfileMapper;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        User user = getUserOrThrow(userId);
        List<UserPhone> phones = userPhoneRepository.findByUser_Id(userId);
        List<UserAddress> addresses = userAddressRepository.findByUser_Id(userId);

        return userProfileMapper.toUserProfile(user, phones, addresses);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserOrThrow(userId);

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                Gender g = Gender.valueOf(request.getGender());
                user.setGender(g);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Invalid gender value");
            }
        }

        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }

        userRepository.save(user);
        return getProfile(userId);
    }

    // ---------- PHONE ----------

    @Override
    @Transactional(readOnly = true)
    public List<UserPhoneResponse> getPhones(Long userId) {
        getUserOrThrow(userId);
        List<UserPhone> phones = userPhoneRepository.findByUser_Id(userId);
        return phones.stream()
                .map(userProfileMapper::toPhoneResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserPhoneResponse addPhone(Long userId, UserPhoneRequest request) {
        User user = getUserOrThrow(userId);

        UserPhone phone = new UserPhone();
        phone.setUser(user);
        phone.setPhoneNumber(request.getPhoneNumber());
        boolean isDefault = request.getDefaultPhone() != null && request.getDefaultPhone();
        phone.setDefaultPhone(isDefault);

        if (isDefault) {
            userPhoneRepository.findByUser_Id(userId).forEach(p -> {
                p.setDefaultPhone(false);
                userPhoneRepository.save(p);
            });
        }

        UserPhone saved = userPhoneRepository.save(phone);
        return userProfileMapper.toPhoneResponse(saved);
    }

    @Override
    @Transactional
    public UserPhoneResponse updatePhone(Long userId, Long phoneId, UserPhoneRequest request) {
        UserPhone phone = userPhoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found"));

        if (!phone.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        if (request.getPhoneNumber() != null) {
            phone.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getDefaultPhone() != null) {
            boolean newDefault = request.getDefaultPhone();
            if (newDefault) {
                userPhoneRepository.findByUser_Id(userId).forEach(p -> {
                    p.setDefaultPhone(false);
                    userPhoneRepository.save(p);
                });
            }
            phone.setDefaultPhone(newDefault);
        }

        UserPhone saved = userPhoneRepository.save(phone);
        return userProfileMapper.toPhoneResponse(saved);
    }

    @Override
    @Transactional
    public void deletePhone(Long userId, Long phoneId) {
        UserPhone phone = userPhoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found"));

        if (!phone.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        userPhoneRepository.delete(phone);
    }

    // ---------- ADDRESS ----------

    @Override
    @Transactional(readOnly = true)
    public List<UserAddressResponse> getAddresses(Long userId) {
        getUserOrThrow(userId);
        List<UserAddress> addresses = userAddressRepository.findByUser_Id(userId);
        return addresses.stream()
                .map(userProfileMapper::toAddressResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserAddressResponse addAddress(Long userId, UserAddressRequest request) {
        User user = getUserOrThrow(userId);

        UserAddress address = new UserAddress();
        address.setUser(user);
        address.setAddressDetail(request.getAddressDetail());
        boolean isDefault = request.getDefaultAddress() != null && request.getDefaultAddress();
        address.setDefaultAddress(isDefault);

        if (isDefault) {
            userAddressRepository.findByUser_Id(userId).forEach(a -> {
                a.setDefaultAddress(false);
                userAddressRepository.save(a);
            });
        }

        UserAddress saved = userAddressRepository.save(address);
        return userProfileMapper.toAddressResponse(saved);
    }

    @Override
    @Transactional
    public UserAddressResponse updateAddress(Long userId, Long addressId, UserAddressRequest request) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        if (request.getAddressDetail() != null) {
            address.setAddressDetail(request.getAddressDetail());
        }

        if (request.getDefaultAddress() != null) {
            boolean newDefault = request.getDefaultAddress();
            if (newDefault) {
                userAddressRepository.findByUser_Id(userId).forEach(a -> {
                    a.setDefaultAddress(false);
                    userAddressRepository.save(a);
                });
            }
            address.setDefaultAddress(newDefault);
        }

        UserAddress saved = userAddressRepository.save(address);
        return userProfileMapper.toAddressResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        userAddressRepository.delete(address);
    }
}
