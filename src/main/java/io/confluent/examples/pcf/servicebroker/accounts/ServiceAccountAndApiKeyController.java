package io.confluent.examples.pcf.servicebroker.accounts;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController("/accounts")
@Slf4j
public class ServiceAccountAndApiKeyController {

    @Autowired ServiceAccountAndApiKeyService serviceAccountAndApiKeyService;

    @PostMapping
    public void add(@RequestBody Map<String, ApiKeyAndSecret> apiKeyAndSecrets) {
        log.info("Received a list of {} service accounts", apiKeyAndSecrets.size());
        validate(apiKeyAndSecrets);
        serviceAccountAndApiKeyService.addAll(apiKeyAndSecrets);
        log.info("Stored service accounts for future use in memory");
    }

    private void validate(Map<String, ApiKeyAndSecret> apiKeyAndSecrets) {
        apiKeyAndSecrets.forEach(
            (serviceAccountId, apiKeyAndSecret) -> {
                    if (empty(apiKeyAndSecret.getApiKey()))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "apiKey must be set.");
                    if (empty(apiKeyAndSecret.getApiSecret()))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "apiSecret must be set.");
                    if (empty(serviceAccountId))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "serviceAccount must be set.");
                }
        );
        log.info("all service accounts are valid.");
    }

    private boolean empty(String input) {
        return input == null || input.equals("");
    }

    @GetMapping
    public Map<String, ApiKeyAndSecret> getAll() {
        log.info("Listing service accounts with secrets removed");
        return serviceAccountAndApiKeyService.getAll().entrySet().stream()
            .collect(Collectors.toMap(
                Entry::getKey,
                entry -> ApiKeyAndSecret.builder()
                        .apiKey(entry.getValue().getApiKey())
                        .apiSecret("hidden")
                        .build()));
    }

}

