package org.example.operations;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.DetectedLanguage;
import com.azure.core.credential.AzureKeyCredential;
import org.json.JSONObject;

public class DetectLanguage {


    public static JSONObject analyze(String document, String apiKey, String endPoint) {


        // Instantiate a client that will be used to call the service.
        TextAnalyticsClient client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(endPoint)
                .buildClient();

        final DetectedLanguage detectedLanguage = client.detectLanguage(document);
        System.out.printf("Detected primary language: %s, ISO 6391 name: %s, confidence score: %f.%n",
                detectedLanguage.getName(), detectedLanguage.getIso6391Name(), detectedLanguage.getConfidenceScore());

        return (new JSONObject()).put("language",detectedLanguage.getName()).put("iso6391Name",detectedLanguage.getIso6391Name()).put("confidenceScore", detectedLanguage.getConfidenceScore());
    }
}
