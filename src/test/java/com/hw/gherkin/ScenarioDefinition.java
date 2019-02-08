//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.Location;
import gherkin.ast.Node;
//import gherkin.ast.Step;

import java.util.LinkedList;
import java.util.List;

public abstract class ScenarioDefinition extends Node {
    private final String keyword;
    private final String name;
    private final String description;
    private final List<Step> steps;
    private boolean shouldRun;

    public ScenarioDefinition(Location location, String keyword, String name, String description, List<Step> steps) {
        super(location);
        this.keyword = keyword;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.shouldRun = true;
    }

    public ScenarioDefinition(gherkin.ast.ScenarioDefinition scenarioDefinition) {
        super(scenarioDefinition.getLocation());
        this.keyword = scenarioDefinition.getKeyword();
        this.name = scenarioDefinition.getName();
        this.description = scenarioDefinition.getDescription();
        List<Step> steps = new LinkedList<>();
        for (gherkin.ast.Step step : scenarioDefinition.getSteps())
            steps.add(new Step(step));
        this.steps = steps;
        this.shouldRun = true;
    }

    public String getName() {
        return this.name;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    public boolean isShouldRun() {
        return shouldRun;
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }
}