package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.IUserRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IPhoneOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
// ⭐ NEW: import thêm để set loại SMS = Transactional
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
// ⭐ NEW: dùng HashMap cho messageAttributes
import java.util.HashMap;

@Service
public class LocalPhoneOtpServiceImpl implements IPhoneOtpService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private SnsClient snsClient;

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final Random random = new Random();

    private static final long OTP_TTL_SECONDS = 5 * 60;

    @Override
    public void sendPhoneOtp(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User with phone " + phone + " not found"));

        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusSeconds(OTP_TTL_SECONDS);
        otpStore.put(phone, new OtpEntry(otp, expiresAt));

        String message = """
                [Sport Shop] OTP Code Request

                Phone: %s
                OTP: %s
                Expires At: 5 phút
                """.formatted(phone, otp);

        String e164Phone = normalizeToE164(phone);

        // ⭐ NEW: set thuộc tính để SNS hiểu đây là tin nhắn Transactional (OTP)
        Map<String, MessageAttributeValue> attrs = new HashMap<>();
        attrs.put("AWS.SNS.SMS.SMSType",
                MessageAttributeValue.builder()
                        .dataType("String")
                        .stringValue("Transactional") // OTP = transactional
                        .build()
        );

        PublishRequest request = PublishRequest.builder()
                .phoneNumber(e164Phone)
                .message(message)
                // ⭐ NEW: truyền kèm messageAttributes
                .messageAttributes(attrs)
                .build();

        snsClient.publish(request);

        System.out.println("OTP sent to phone via SNS: " + phone + " | otp=" + otp);
    }

    @Override
    public boolean verifyPhoneOtp(String phone, String otp) {
        OtpEntry entry = otpStore.get(phone);
        if (entry == null) {
            return false;
        }
        if (Instant.now().isAfter(entry.expiresAt())) {
            otpStore.remove(phone);
            return false;
        }
        if (!entry.code().equals(otp)) {
            return false;
        }

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User with phone " + phone + " not found"));
        user.setPhoneVerified(true);
        userRepository.save(user);
        otpStore.remove(phone);
        return true;
    }

    private String normalizeToE164(String phone) {
        String trimmed = phone.trim();
        if (trimmed.startsWith("+")) {
            return trimmed;
        }
        if (trimmed.startsWith("0")) {
            // Việt Nam: 0xxxxxxxxx -> +84xxxxxxxxx
            return "+84" + trimmed.substring(1);
        }
        return trimmed;
    }

    private String generateOtp() {
        int code = 100000 + random.nextInt(900000); // 6 chữ số
        return String.valueOf(code);
    }

    private record OtpEntry(String code, Instant expiresAt) {}
}
