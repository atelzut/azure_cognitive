package org.example.operations;


import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.json.JSONArray;

/**
 * Sample demonstrates how to extract the key phrases of document.
 */
public class ExtractKeyPhrases {
    /**
     * Main method to invoke this demo about how to extract the key phrases of document.
     *
     * @param args     Unused arguments to the program.
     * @param document
     * @param key
     * @param endpoint
     * @return
     */
    public static JSONArray analyze(String document, String key, String endpoint) {
        // Instantiate a client that will be used to call the service.
        TextAnalyticsClient client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();

        // The document that needs be analyzed.

        JSONArray results=new JSONArray();
        System.out.println("Extracted phrases:");
        client.extractKeyPhrases(document).forEach(keyPhrase -> {
            System.out.printf("%s.%n", keyPhrase);
        results.put(keyPhrase);
        });
        return results;
    }
}