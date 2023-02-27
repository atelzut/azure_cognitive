package org.example.pojo;

import java.util.ArrayList;

public class DocumentAnalysis{
    public ArrayList<Document> documents;
    public ArrayList<Object> errors;
}
class Assessment{
    public ConfidenceScores confidenceScores;
    public boolean isNegated;
    public int length;
    public int offset;
    public String sentiment;
    public String text;
}
 class ConfidenceScores{
    public double negative;
    public double neutral;
    public double positive;
}
 class Document{
    public ConfidenceScores confidenceScores;
    public String id;
    public ArrayList<Sentence> sentences;
    public String sentiment;
}
 class Relation{
    public String ref;
    public String relationType;
}
 class Sentence{
    public ArrayList<Assessment> assessments;
    public ConfidenceScores confidenceScores;
    public int length;
    public int offset;
    public String sentiment;
    public ArrayList<Target> targets;
    public String text;
}
 class Target{
    public ConfidenceScores confidenceScores;
    public int length;
    public int offset;
    public ArrayList<Relation> relations;
    public String sentiment;
    public String text;
}