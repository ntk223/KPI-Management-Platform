package vdt.kpimanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisRefreshToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private String username;
}
