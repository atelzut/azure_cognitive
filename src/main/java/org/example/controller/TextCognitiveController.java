package org.example.controller;


import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.core.credential.AzureKeyCredential;
import org.example.common.Constants;
import org.example.operations.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/azure")
public class TextCognitiveController {

    @PostMapping(value = "/recognizeEntities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity recognizeEntities(@RequestBody String jsonObject) {
        TextDocumentInput textDocumentInput = new TextDocumentInput(jsonObject, jsonObject);
        JSONArray responseBody = RecognizeEntities.analizesingleToJson(textDocumentInput, Constants.APIKEY, Constants.ENDPOINT);
        return ResponseEntity.ok(responseBody.toString());
    }

    @PostMapping(value = "/analyzeSentimentWithOpinionMiningASync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity analyzeSentimentWithOpinionMiningASync(@RequestBody String jsonInput) {

        JSONObject responseBody = AnalyzeSentimentWithOpinionMiningASync.analize(getTextDocumentList(jsonInput), Constants.APIKEY, Constants.ENDPOINT);
        return ResponseEntity.ok(responseBody.toString());
    }

    @PostMapping(value = "/analyzeSentimentWithOpinionMining", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity analyzeSentimentWithOpinionMining(@RequestBody String jsonObject) {
       JSONObject responseBody = AnalyzeSentimentWithOpinionMining.analizeDocument(new TextDocumentInput((new JSONObject(jsonObject)).get("id").toString(), (new JSONObject(jsonObject)).get("text").toString()), new TextAnalyticsClientBuilder()
               .credential(new AzureKeyCredential(Constants.APIKEY))
               .endpoint(Constants.ENDPOINT)
               .buildClient());
        return ResponseEntity.ok(responseBody.toString());
    }

    @PostMapping(value = "/analyzeActions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity analyzeActions(@RequestBody String jsonInput) {
        List<TextDocumentInput> batchDocuments = new ArrayList<>();
        
        (new JSONArray(jsonInput).toList()).forEach(x -> batchDocuments.add(new TextDocumentInput((new JSONObject(x)).get("id").toString(), (new JSONObject(x)).get("text").toString())));
        JSONObject responseBody = AnalyzeActions.analize(batchDocuments, Constants.APIKEY, Constants.ENDPOINT);
        return ResponseEntity.ok(responseBody.toString());
    }

    @PostMapping(value = "/extractKeyPhrases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity extractKeyPhrases(@RequestBody String jsonObject) {
        List<TextDocumentInput> batchDocuments = new ArrayList<>();

        JSONArray responseBody = ExtractKeyPhrases.analyze((new JSONObject(jsonObject)).get("text").toString(), Constants.APIKEY, Constants.ENDPOINT);
        return ResponseEntity.ok(responseBody.toString());
    }

    @PostMapping(value = "/extractSummary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity extractSummary(@RequestBody String jsonObject) {
        JSONArray responseBody = ExtractSummary.analyze((new JSONObject(jsonObject)).get("text").toString(), Constants.APIKEY, Constants.ENDPOINT);
        return ResponseEntity.ok(responseBody.toString());
    }
    private static List<TextDocumentInput> getTextDocumentList(String jsonInput) {
        List<TextDocumentInput> batchDocuments = new ArrayList<>();

        (new JSONArray(jsonInput).toList()).forEach(x -> batchDocuments.add(new TextDocumentInput((new JSONObject(x)).get("id").toString(), (new JSONObject(x)).get("text").toString())));
        return batchDocuments;
    }
    @PostMapping(value = "/detectLanguage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity detectLanguage(@RequestBody String jsonObject) {
        JSONObject responseBody = DetectLanguage.analyze((new JSONObject(jsonObject)).get("text").toString(), Constants.APIKEY, Constants.ENDPOINT);
        return ResponseEntity.ok(responseBody.toString());
    }

}
