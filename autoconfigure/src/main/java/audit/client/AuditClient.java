package audit.client;

import audit.model.AuditEvent;

public interface AuditClient {
  void send(AuditEvent event);
}
