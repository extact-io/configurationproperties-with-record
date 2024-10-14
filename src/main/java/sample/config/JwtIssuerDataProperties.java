package sample.config;

import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "test.jwt-issuer")
@Validated
@Data
public class JwtIssuerDataProperties {

    private boolean enable = false;
    @NotNull
    private RSAPrivateKey privateKey;
    private ClockProperties clock = new ClockProperties();
    private Claim claim = new Claim();

    @Data
    public static class ClockProperties {

        enum Type {
            SYSTEM, FIXED
        }

        private Type type = Type.SYSTEM;
        private LocalDateTime fixedDatetime;

        public Clock getClock() {
            return switch (type) {
                case SYSTEM -> Clock.systemDefaultZone();
                case FIXED -> Clock.fixed(getFixedInstant(), ZoneId.systemDefault());
            };
        }

        public Instant getFixedInstant() {
            return fixedDatetime.atZone(ZoneId.systemDefault()).toInstant();
        }
    }

    @Data
    public static class Claim {

        private String issuer;
        @PositiveOrZero
        private int exp = 60;

        public Instant getExpirationTime(Instant creationTime) {
            return creationTime.plusSeconds(exp * 60);
        }
    }
}
