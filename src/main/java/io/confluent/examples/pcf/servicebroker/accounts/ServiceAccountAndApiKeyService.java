package io.confluent.examples.pcf.servicebroker.accounts;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This class maintains a list of service accounts with associated Api Keys.
 * Each service account is used only once, never reused.
 * Api keys and secrets are only held in memory, never written to persistent storage.
 * Initially, the list is empty. New Api-Keys can be added via REST.
 */

@Service
@Slf4j
public class ServiceAccountAndApiKeyService {

    Map<String, ApiKeyAndSecret> store = new HashMap<>();

    public ApiKeyAndSecret get(String serviceAccountId) {
        if (store.isEmpty()) throw new RuntimeException("No api keys configured");
        ApiKeyAndSecret first = store.get(serviceAccountId);
        log.info("Retrieving ServiceAccount with Key {} and account number {}", first.getApiKey(), serviceAccountId);
        return first;
    }

    public void addAll(Map<String, ApiKeyAndSecret> entries) {
        store.putAll(entries);
    }

    public Map<String, ApiKeyAndSecret> getAll() {
        return store;
    }
}

