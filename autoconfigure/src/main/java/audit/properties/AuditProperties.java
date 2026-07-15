package audit.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "audit")
public record AuditProperties(
    @DefaultValue("true")
    boolean enabled,
    @DefaultValue("true")
    boolean logBodyEnabled,
    @DefaultValue("10000")
    int maxBodySize,
    @DefaultValue({"password", "token"})
    List<String> maskingFields,
    @DefaultValue({"/actuator/**"})
    List<String> excludedPaths
) {

}
