package audit.filter;

import audit.client.AuditClient;
import audit.model.AuditEvent;
import audit.model.AuditEvent.Builder;
import audit.properties.AuditProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class AuditFilter extends OncePerRequestFilter {

  private final AuditProperties properties;
  private final AuditClient auditClient;
  private final String serviceName;

  public AuditFilter(AuditProperties properties, AuditClient auditClient, String serviceName) {
    this.properties = properties;
    this.auditClient = auditClient;
    this.serviceName = serviceName;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String requestId = request.getHeader("X-Request-Id");
    if (requestId == null) {
      requestId = UUID.randomUUID().toString();
    }
    MDC.put("requestId", requestId);
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    try {
      filterChain.doFilter(requestWrapper, response);
    } finally {
      byte[] requestContent = requestWrapper.getContentAsByteArray();
      int length = Math.min(requestContent.length, properties.maxBodySize());
      String requestBody =
          length == 0 || !properties.logBodyEnabled() ? null
              : new String(requestContent, 0, length, StandardCharsets.UTF_8);

      Builder builder = AuditEvent.builder();
      AuditEvent event = builder
          .requestId(requestId)
          .serviceName(serviceName)
          .body(requestBody)
          .build();

      auditClient.send(event);

      MDC.remove("requestId");
    }
  }


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // TODO: return true if request path matches any of properties.excludedPaths()
    return false;
  }
}
