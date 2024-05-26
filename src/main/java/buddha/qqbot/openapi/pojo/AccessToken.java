package buddha.qqbot.openapi.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 */
@Data
public class AccessToken implements Serializable {
    @JSONField(name = "access_token")
    private String token;
    @JSONField(name = "expires_in")
    private Long expiresIn;
    /** token 签署时间*/
    private LocalDateTime signAt = LocalDateTime.now();

    public boolean isExpired() {
        return signAt.plusSeconds(expiresIn).isBefore(LocalDateTime.now());
    }
}
