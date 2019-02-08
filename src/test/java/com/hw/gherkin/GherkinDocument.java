//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.Comment;
import gherkin.ast.Location;
import gherkin.ast.Node;

import java.util.Collections;
import java.util.List;

public class GherkinDocument extends Node {
    private Feature feature;
    private  List<Comment> comments;

    public GherkinDocument(Feature feature, List<Comment> comments) {
        super((Location)null);
        this.feature = feature;
        this.comments = Collections.unmodifiableList(comments);
    }

    public GherkinDocument(gherkin.ast.GherkinDocument gherkinDocument) {
        super((Location)null);
        this.feature = new Feature(gherkinDocument.getFeature());
    }

    public Feature getFeature() {
        return this.feature;
    }
    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public String getStringRepresentation() {
        return feature.toString();
    }
}