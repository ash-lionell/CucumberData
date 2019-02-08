//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.Location;
import gherkin.ast.Node;
//import gherkin.ast.ScenarioDefinition;
//import gherkin.ast.Scenario;
import gherkin.ast.Tag;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Feature extends Node {
    private final List<Tag> tags;
    private final String language;
    private final String keyword;
    private final String name;
    private final String description;
    private final List<ScenarioDefinition> children;

    public Feature(List<Tag> tags, Location location, String language, String keyword, String name, String description, List<ScenarioDefinition> children) {
        super(location);
        this.tags = Collections.unmodifiableList(tags);
        this.language = language;
        this.keyword = keyword;
        this.name = name;
        this.description = description;
        this.children = children;
    }

    public Feature(gherkin.ast.Feature feature) {
        super(feature.getLocation());
        this.tags = Collections.unmodifiableList(feature.getTags());
        this.language = feature.getLanguage();
        this.keyword = feature.getKeyword();
        this.name = feature.getName();
        this.description = feature.getDescription();
        //this.children = feature.getChildren();
        List<ScenarioDefinition> children = new LinkedList<>();
        for (gherkin.ast.ScenarioDefinition scenarioDefinition : feature.getChildren()) {
            if (scenarioDefinition instanceof gherkin.ast.Scenario)
                children.add(new Scenario((gherkin.ast.Scenario) scenarioDefinition));
            else if (scenarioDefinition instanceof gherkin.ast.ScenarioOutline)
                children.add(new ScenarioOutline((gherkin.ast.ScenarioOutline) scenarioDefinition));
        }
        this.children = children;
    }

    public List<ScenarioDefinition> getChildren() {
        return this.children;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ScenarioDefinition scenarioDefinition : children) {
            if (scenarioDefinition.isShouldRun())
                builder.append("\n").append(scenarioDefinition).append("\n");
        }
        if (builder.length()>0) {
            String t;
            t = getKeyword()+": "+getName()+"\n";
            builder.insert(0,t);
            //builder.append(getKeyword()).append(": ").append(getName()).append("\n");
            List<String> list = new LinkedList<>();
            for (Tag tag : getTags())
                list.add(tag.getName());
            t = list.stream().map(Object::toString).collect(Collectors.joining(" ", " ","\n"));
            //builder.append(t);
            builder.insert(0,t);
        }
        return builder.toString().trim();
    }
}