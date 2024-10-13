package sample.config;

import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

// @Beanで実行時にバインドプロパティを指定する作戦はできない
@ConfigurationProperties(prefix = "test.jwt-issuer")
public record JwtIssuerRecordProperties(

        @DefaultValue("false") //
        boolean enable,
        RSAPrivateKey privateKey,
        @DefaultValue ClockProperties clock,
        @DefaultValue Claim claim) {

    public static record ClockProperties(
            @DefaultValue("SYSTEM") //
            Type type,
            LocalDateTime fixedDatetime) {

        enum Type {
            SYSTEM, FIXED
        }

        public Clock clock() {
            return switch (type) {
                case SYSTEM -> Clock.systemDefaultZone();
                case FIXED -> Clock.fixed(getFixedInstant(), ZoneId.systemDefault());
            };
        }

        public Instant getFixedInstant() {
            return fixedDatetime.atZone(ZoneId.systemDefault()).toInstant();
        }
    }

    public static record Claim(
            String issuer,
            @DefaultValue("60") //
            int exp) {

        public Instant expirationTime(Instant creationTime) {
            return creationTime.plusSeconds(exp * 60);
        }
    }
}
