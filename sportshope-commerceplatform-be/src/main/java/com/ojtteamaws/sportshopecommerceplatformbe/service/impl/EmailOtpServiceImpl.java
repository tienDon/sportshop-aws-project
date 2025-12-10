package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.IUserRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailOtpServiceImpl implements OtpService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private SesClient sesClient;

    // Email đã verify trong SES
    @Value("${aws.ses.fromEmail}")
    private String fromEmail;

    private final Random random = new Random();

    // lưu OTP tạm thời trong RAM: key = email
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private static final long OTP_TTL_SECONDS = 5 * 60;

    void init() {
        System.out.println("EmailOtpServiceImpl initialized - using in memory OTP store + SES");
    }

    @Override
    public void sendEmailOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));

        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusSeconds(OTP_TTL_SECONDS);
        otpStore.put(email, new OtpEntry(otp, expiresAt));

        String subject = "[Sport Shop] Mã xác thực OTP";

        String textBody = """
                Xin chào %s,

                Mã OTP xác thực email của bạn là: %s

                OTP sẽ hết hạn sau %d phút.

                Nếu bạn không yêu cầu hành động này, vui lòng bỏ qua email này.
                """.formatted(
                user.getFullName() != null ? user.getFullName() : "",
                otp,
                OTP_TTL_SECONDS / 60
        );

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(
                        Destination.builder()
                                .toAddresses(email)
                                .build()
                )
                .message(
                        Message.builder()
                                .subject(Content.builder().data(subject).build())
                                .body(
                                        Body.builder()
                                                .text(Content.builder().data(textBody).build())
                                                .build()
                                )
                                .build()
                )
                .source(fromEmail)
                .build();

        sesClient.sendEmail(request);
        System.out.println("OTP sent to email via SES: " + email + " | otp=" + otp);
    }

    @Override
    @Transactional
    public boolean verifyEmailOtp(String email, String otp) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null) {
            return false;
        }
        if (Instant.now().isAfter(entry.expiresAt())) {
            otpStore.remove(email);
            return false;
        }
        if (!entry.code().equals(otp)) {
            return false;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));
        user.setEmailVerified(true);
        userRepository.save(user);
        otpStore.remove(email);
        return true;
    }

    private String generateOtp() {
        int code = 100000 + random.nextInt(900000); // 6 chữ số
        return String.valueOf(code);
    }

    private record OtpEntry(String code, Instant expiresAt) {}
}
