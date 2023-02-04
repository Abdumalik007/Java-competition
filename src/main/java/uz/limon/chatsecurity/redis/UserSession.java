package uz.limon.chatsecurity.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "userSession", timeToLive = 60 * 24 * 1000)
public class UserSession {
    @Id
    private String id;
    private String userInfo;
}
