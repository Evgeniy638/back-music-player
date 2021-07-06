package music.player.back.dto;

import lombok.Data;

@Data
public class AccessAndRefreshTokenDTO {
    String access_token;
    String refresh_token;
}
