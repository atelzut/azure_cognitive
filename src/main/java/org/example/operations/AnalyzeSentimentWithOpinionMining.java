package org.example.operations;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.*;
import com.azure.core.credential.AzureKeyCredential;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Sample demonstrates how to synchronously analyze the sentiment of document with opinion mining.
 */
public class AnalyzeSentimentWithOpinionMining {
    /*
     * Main method to invoke this demo about how to analyze the sentiment of document.
     *
     * @param args Unused arguments to the program.
     */


    public static JSONArray analizeDocumentList(List<TextDocumentInput> batchDocuments, String apiKey, String endpoint) {
        JSONArray documents = new JSONArray();


        TextAnalyticsClient client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(endpoint)
                .buildClient();

        for (TextDocumentInput batchDoc : batchDocuments) {
            documents.put(analizeDocument(batchDoc, client));
        }
        System.out.println("##########################");
        System.out.println(new JSONObject().put("documents", documents));

        return documents;
    }

    public static JSONObject analizeDocument(TextDocumentInput batchDocument, TextAnalyticsClient client) {

        JSONObject jsonObject = new JSONObject();
        // The text that needs be analyzed.
//        String document = "Bad atmosphere. Not close to plenty of restaurants, hotels, and transit! Staff are not friendly and helpful.";
        String document = batchDocument.getText();

        System.out.printf("Text = %s%n", document);

        AnalyzeSentimentOptions options = new AnalyzeSentimentOptions().setIncludeOpinionMining(true);
        final DocumentSentiment documentSentiment = client.analyzeSentiment(document, "en", options);
        SentimentConfidenceScores scores = documentSentiment.getConfidenceScores();

        System.out.printf(
                "Recognized document sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                documentSentiment.getSentiment(), scores.getPositive(), scores.getNeutral(), scores.getNegative());
        jsonObject.put("confidenceScores", new JSONObject().put("negative", scores.getNegative()).put("neutral", scores.getNeutral()).put("positive", scores.getPositive()));
        JSONArray sentences = new JSONArray();
        documentSentiment.getSentences().forEach(sentenceSentiment -> {

            SentimentConfidenceScores sentenceScores = sentenceSentiment.getConfidenceScores();
            JSONObject jsonSentence = new JSONObject();
            sentences.put(new JSONObject().put("confidenceScores", new JSONObject().put("negative", scores.getNegative()).put("neutral", scores.getNeutral()).put("positive", scores.getPositive())).
                    put("text", sentenceSentiment.getText()).put("sentiment", sentenceSentiment.getSentiment()).put("length", sentenceSentiment.getLength()).put("offset", sentenceSentiment.getOffset()));
            System.out.printf("\tSentence sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                    sentenceSentiment.getSentiment(), sentenceScores.getPositive(), sentenceScores.getNeutral(), sentenceScores.getNegative());
            jsonObject.put("sentences", sentences);
            JSONArray assessments = new JSONArray();
            JSONArray targets = new JSONArray();
            sentenceSentiment.getOpinions().forEach(opinion -> {
                        TargetSentiment targetSentiment = opinion.getTarget();
                        targets.put(targetSentiment);
                        System.out.printf("\t\tTarget sentiment: %s, target text: %s%n", targetSentiment.getSentiment(),
                                targetSentiment.getText());
                        for (AssessmentSentiment assessmentSentiment : opinion.getAssessments()) {
                            assessments.put(assessmentSentiment);
                            System.out.printf("\t\t\t'%s' assessment sentiment because of \"%s\". Is the assessment negated: %s.%n",
                                    assessmentSentiment.getSentiment(), assessmentSentiment.getText(), assessmentSentiment.isNegated());
                        }
                        jsonSentence.put("assessments", assessments);
                        jsonSentence.put("targets", targets);
                        jsonObject.put("targets", targets);
                        jsonObject.put("assessments", assessments);

                    }

            );
        });

        return jsonObject;
    }

}