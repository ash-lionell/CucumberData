//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.Location;
import gherkin.ast.Tag;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Scenario extends ScenarioDefinition {
    private final List<Tag> tags;

    public Scenario(List<Tag> tags, Location location, String keyword, String name, String description, List<Step> steps) {
        super(location, keyword, name, description, steps);
        this.tags = Collections.unmodifiableList(tags);
    }

    public Scenario(gherkin.ast.Scenario scenario) {
        //super(scenario.getLocation(), scenario.getKeyword(), scenario.getName(), scenario.getDescription(), scenario.getSteps());
        super(scenario);
        this.tags = Collections.unmodifiableList(scenario.getTags());
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<String> list = new LinkedList<>();
        for (Tag tag : getTags())
            list.add(tag.getName());
        String t;
        t = list.stream().map(Object::toString).collect(Collectors.joining(" ", " ","\n"));
        builder.append(t);
        builder.append(getKeyword()).append(": ").append(getName()).append("\n");
        for (Step step : getSteps())
            builder.append(step).append("\n");

        return builder.toString().trim();
    }
}
