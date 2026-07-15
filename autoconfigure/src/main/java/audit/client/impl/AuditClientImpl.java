package audit.client.impl;

import audit.client.AuditClient;
import audit.model.AuditEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditClientImpl implements AuditClient {

  private final ObjectMapper auditObjectMapper;
  private static final Logger AUDIT = LoggerFactory.getLogger("AUDIT");
  private static final Logger log = LoggerFactory.getLogger(AuditClientImpl.class);

  public AuditClientImpl(ObjectMapper auditObjectMapper) {
    this.auditObjectMapper = auditObjectMapper;
  }

  @Override
  public void send(AuditEvent event) {
    try {
      String strEvent = auditObjectMapper.writeValueAsString(event);
      AUDIT.info(strEvent);
    } catch (Exception e) {
      log.warn("Failed to serialize audit event, requestId={}", event.requestId(), e);
    }
  }
}
