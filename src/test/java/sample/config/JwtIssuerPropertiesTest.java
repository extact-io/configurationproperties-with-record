package sample.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.crypto.RsaKeyConversionServicePostProcessor;
import org.springframework.test.context.ActiveProfiles;

import sample.config.JwtIssuerDataProperties.ClockProperties;

class JwtIssuerPropertiesTest {

    @Nested
    @SpringBootTest(webEnvironment = WebEnvironment.NONE)
    @ActiveProfiles("full")
    static class FullPropertiesTest {

        @Configuration(proxyBeanMethods = false)
        @EnableConfigurationProperties({ JwtIssuerDataProperties.class, JwtIssuerRecordProperties.class })
        static class TestConfig {
            @Bean
            static BeanFactoryPostProcessor conversionServicePostProcessor() {
                return new RsaKeyConversionServicePostProcessor();
            }
        }

        @Test
        void tesByData(@Autowired JwtIssuerDataProperties properties) {

            assertThat(properties.isEnable()).isTrue();
            assertThat(properties.getPrivateKey()).isNotNull();

            assertThat(properties.getClock()).isNotNull();
            assertThat(properties.getClock().getType()).isEqualTo(ClockProperties.Type.FIXED);
            assertThat(properties.getClock().getFixedDatetime()).isNotNull();
            assertThat(properties.getClock().getClock()).isNotNull();

            assertThat(properties.getClaim()).isNotNull();
            assertThat(properties.getClaim().getIssuer()).isEqualTo("JwtIssuerProperties");
            assertThat(properties.getClaim().getExp()).isEqualTo(30);
        }

        @Test
        void tesByRecord(@Autowired JwtIssuerRecordProperties properties) {
            System.out.println(properties);

            assertThat(properties.enable()).isTrue();
            assertThat(properties.privateKey()).isNotNull();

            assertThat(properties.clock()).isNotNull();
            assertThat(properties.clock().type()).isEqualTo(sample.config.JwtIssuerRecordProperties.ClockProperties.Type.FIXED);
            assertThat(properties.clock().fixedDatetime()).isNotNull();
            assertThat(properties.clock().clock()).isNotNull();

            assertThat(properties.claim()).isNotNull();
            assertThat(properties.claim().issuer()).isEqualTo("JwtIssuerProperties");
            assertThat(properties.claim().exp()).isEqualTo(30);
        }
    }

    @Nested
    @SpringBootTest(webEnvironment = WebEnvironment.NONE)
    @ActiveProfiles("empty")
    static class EmptyPropertiesTest {

        @Configuration(proxyBeanMethods = false)
        @EnableConfigurationProperties({ JwtIssuerDataProperties.class, JwtIssuerRecordProperties.class })
        static class TestConfig {
            @Bean
            static BeanFactoryPostProcessor conversionServicePostProcessor() {
                return new RsaKeyConversionServicePostProcessor();
            }
        }

        @Test
        void tesByData(@Autowired JwtIssuerDataProperties properties) {

            assertThat(properties.isEnable()).isFalse();
            assertThat(properties.getPrivateKey()).isNull();

            assertThat(properties.getClock()).isNotNull();
            assertThat(properties.getClock().getType()).isEqualTo(ClockProperties.Type.SYSTEM);
            assertThat(properties.getClock().getFixedDatetime()).isNull();
            assertThat(properties.getClock().getClock()).isNotNull();

            assertThat(properties.getClaim()).isNotNull();
            assertThat(properties.getClaim().getIssuer()).isNull();
            assertThat(properties.getClaim().getExp()).isEqualTo(60);
        }

        @Test
        void tesByRecord(@Autowired JwtIssuerRecordProperties properties) {

            assertThat(properties.enable()).isFalse();
            assertThat(properties.privateKey()).isNull();

            assertThat(properties.clock()).isNotNull();
            assertThat(properties.clock().type()).isEqualTo(sample.config.JwtIssuerRecordProperties.ClockProperties.Type.SYSTEM);
            assertThat(properties.clock().fixedDatetime()).isNull();
            assertThat(properties.clock().clock()).isNotNull();

            assertThat(properties.claim()).isNotNull();
            assertThat(properties.claim().issuer()).isNull();
            assertThat(properties.claim().exp()).isEqualTo(60);
        }
    }
}
