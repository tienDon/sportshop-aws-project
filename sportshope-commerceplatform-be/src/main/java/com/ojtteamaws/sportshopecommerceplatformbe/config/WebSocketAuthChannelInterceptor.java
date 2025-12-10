package com.ojtteamaws.sportshopecommerceplatformbe.config;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.IUserRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JWTProvider jwtProvider;

    private final IUserRepository userRepository;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            String header = accessor.getFirstNativeHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                if (jwtProvider.validateToken(token)) {
                    String userId = jwtProvider.extractSubject(token);

                    // FIX: Lấy user từ DB để lấy role
                    User user = userRepository.findById(Long.parseLong(userId))
                            .orElse(null);

                    if (user != null) {
                        SimpleGrantedAuthority authority =
                                new SimpleGrantedAuthority("ROLE_" + user.getRole());

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        user.getId().toString(),
                                        null,
                                        List.of(authority)
                                );

                        accessor.setUser(auth);
                    }
                }
            }
        }

        return message;
    }
}
