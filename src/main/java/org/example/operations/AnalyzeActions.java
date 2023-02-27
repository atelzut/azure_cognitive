package org.example.operations;

import com.azure.ai.textanalytics.TextAnalyticsAsyncClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.rest.PagedResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnalyzeActions {

    public static JSONObject analize(List<TextDocumentInput> documents, String key, String endpoint) {

        JSONObject results = new JSONObject();
        TextAnalyticsAsyncClient client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildAsyncClient();
        client.beginAnalyzeActions(documents,
                        new TextAnalyticsActions()
                                .setDisplayName("{tasks_display_name}")
                                .setRecognizeEntitiesActions(new RecognizeEntitiesAction())
                                .setExtractKeyPhrasesActions(new ExtractKeyPhrasesAction()),
                        new AnalyzeActionsOptions())
                .flatMap(result -> {
                    AnalyzeActionsOperationDetail operationDetail = result.getValue();
                    System.out.printf("Action display name: %s, Successfully completed actions: %d, in-process actions: %d,"
                                    + " failed actions: %d, total actions: %d%n",
                            operationDetail.getDisplayName(), operationDetail.getSucceededCount(),
                            operationDetail.getInProgressCount(), operationDetail.getFailedCount(),
                            operationDetail.getTotalCount());
                    return result.getFinalResult();
                })
                .flatMap(analyzeActionsResultPagedFlux -> analyzeActionsResultPagedFlux.byPage())
                .subscribe(
                        perPage -> processAnalyzeActionsResult(perPage, results),
                        ex -> System.out.println("Error listing pages: " + ex.getMessage()),
                        () -> System.out.println("Successfully listed all pages"));

        // The .subscribe() creation and assignment is not a blocking call. For the purpose of this example, we sleep
        // the thread so the program does not end before the send operation is complete. Using .block() instead of
        // .subscribe() will turn this into a synchronous call.
        try {
            TimeUnit.MINUTES.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private static void processAnalyzeActionsResult(PagedResponse<AnalyzeActionsResult> perPage, JSONObject results) {
        System.out.printf("Response code: %d, Continuation Token: %s.%n",
                perPage.getStatusCode(), perPage.getContinuationToken());

        JSONArray entities = new JSONArray();
        JSONArray keyPhrasesJson = new JSONArray();
        JSONArray errors = new JSONArray();

        for (AnalyzeActionsResult actionsResult : perPage.getElements()) {
            System.out.println("Entities recognition action results:");
            for (RecognizeEntitiesActionResult actionResult : actionsResult.getRecognizeEntitiesResults()) {
                if (!actionResult.isError()) {
                    for (RecognizeEntitiesResult documentResult : actionResult.getDocumentsResults()) {
                        if (!documentResult.isError()) {
                            for (CategorizedEntity entity : documentResult.getEntities()) {
                                System.out.printf("\tText: %s, category: %s, confidence score: %f.%n",
                                        entity.getText(), entity.getCategory(), entity.getConfidenceScore());
                                entities.put(new JSONObject(entity));

                            }
                        } else {
                            System.out.printf("\tCannot recognize entities. Error: %s%n",
                                    documentResult.getError().getMessage());
                            errors.put(documentResult.getError());

                        }
                    }
                } else {
                    System.out.printf("\tCannot execute Entities Recognition action. Error: %s%n",
                            actionResult.getError().getMessage());
                }
            }

            System.out.println("Key phrases extraction action results:");
            for (ExtractKeyPhrasesActionResult actionResult : actionsResult.getExtractKeyPhrasesResults()) {
                if (!actionResult.isError()) {
                    for (ExtractKeyPhraseResult documentResult : actionResult.getDocumentsResults()) {
                        if (!documentResult.isError()) {
                            System.out.println("\tExtracted phrases:");
                            for (String keyPhrases : documentResult.getKeyPhrases()) {
                                System.out.printf("\t\t%s.%n", keyPhrases);
                                keyPhrasesJson.put(keyPhrases);
                            }
                        } else {
                            System.out.printf("\tCannot extract key phrases. Error: %s%n",
                                    documentResult.getError().getMessage());
                            errors.put(documentResult.getError());
                        }
                    }
                } else {
                    System.out.printf("\tCannot execute Key Phrases Extraction action. Error: %s%n",
                            actionResult.getError().getMessage());
                    errors.put(actionResult.getError());
                }
            }
        }
        results.put("entities", entities).put("keyPhrases", keyPhrasesJson).put("errors", errors);
    }
}
