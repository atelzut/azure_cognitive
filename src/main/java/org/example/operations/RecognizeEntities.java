package org.example.operations;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.CategorizedEntity;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.core.credential.AzureKeyCredential;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class RecognizeEntities {
    public static void analize(List<TextDocumentInput> batchDocuments, String apiKey, String endpoint) {
        TextAnalyticsClient client = new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(apiKey)).endpoint(endpoint).buildClient();

        for (TextDocumentInput batchDocument : batchDocuments) {
            extracted(client, batchDocument);
            System.out.println("##############################################");
        }
    }

    private static void extracted(TextAnalyticsClient client, TextDocumentInput batchDocument) {
        client.recognizeEntities(batchDocument.getText()).forEach(entity -> System.out.printf(
                "Message Id: %s, Recognized categorized entity: %s, entity category: %s, entity subcategory: %s, confidence score: %f.%n",
                batchDocument.getId(), entity.getText(), entity.getCategory(), entity.getSubcategory(), entity.getConfidenceScore()));
    }
    public static JSONArray analizesingleToJson(TextDocumentInput textDocumentInput, String apiKey, String endpoint) {
        TextAnalyticsClient client = new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(apiKey)).endpoint(endpoint).buildClient();
        List<CategorizedEntity> response = client.recognizeEntities(textDocumentInput.getText()).stream().collect(Collectors.toList());
        JSONArray jsonArray = new JSONArray();
        response.forEach(x->jsonArray.put(new JSONObject().put("id", textDocumentInput.getId()).put("message", x.getText()).put("subcategory",x.getSubcategory()).put("ConfidenceScore", x.getConfidenceScore())));
       return jsonArray;

    }


}
