package audit.model;

import java.time.Instant;

public record AuditEvent(
    String requestId,
    String serviceName,
    String source,
    String destination,
    String body,
    Instant timestamp
) {

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private String requestId;
    private String serviceName;
    private String source;
    private String destination;
    private String body;
    private Instant timestamp = Instant.now();

    public Builder requestId(String requestId) {
      this.requestId = requestId;
      return this;
    }

    public Builder serviceName(String serviceName) {
      this.serviceName = serviceName;
      return this;
    }

    public Builder source(String source) {
      this.source = source;
      return this;
    }

    public Builder destination(String destination) {
      this.destination = destination;
      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Builder timestamp(Instant timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public AuditEvent build() {
      return new AuditEvent(requestId, serviceName, source, destination, body, timestamp);
    }
  }
}
