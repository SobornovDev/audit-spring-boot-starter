package audit.configuration;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

import audit.client.AuditClient;
import audit.client.impl.AuditClientImpl;
import audit.filter.AuditFilter;
import audit.properties.AuditProperties;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;

@AutoConfiguration
@ConditionalOnProperty(prefix = "audit", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AuditProperties.class)
public class AuditAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public AuditClient auditClient() {
    return new AuditClientImpl(createAuditObjectMapper());
  }

  private ObjectMapper createAuditObjectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .setSerializationInclusion(Include.NON_NULL);
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnWebApplication(type = SERVLET)
  @ConditionalOnClass(OncePerRequestFilter.class)
  static class WebConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditFilter auditFilter(AuditProperties properties, AuditClient auditClient,
        Environment environment) {
      return new AuditFilter(properties, auditClient,
          environment.getProperty("spring.application.name", "unknown"));
    }
  }
}
