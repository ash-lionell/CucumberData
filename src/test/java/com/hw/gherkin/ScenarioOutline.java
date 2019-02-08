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

public class ScenarioOutline extends ScenarioDefinition {
    private final List<Tag> tags;
    private final List<Examples> examples;

    public ScenarioOutline(List<Tag> tags, Location location, String keyword, String name, String description, List<Step> steps, List<Examples> examples) {
        super(location, keyword, name, description, steps);
        this.tags = Collections.unmodifiableList(tags);
        this.examples = Collections.unmodifiableList(examples);
    }

    public ScenarioOutline(gherkin.ast.ScenarioOutline scenarioOutline) {
        //super(scenarioOutline.getLocation(), scenarioOutline.getKeyword(), scenarioOutline.getName(), scenarioOutline.getDescription(), scenarioOutline.getSteps());
        super(scenarioOutline);
        this.tags = Collections.unmodifiableList(scenarioOutline.getTags());
        this.examples = new LinkedList<>();
        for (gherkin.ast.Examples example : scenarioOutline.getExamples())
            this.examples.add(new Examples(example));
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public List<Examples> getExamples() {
        return this.examples;
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

        for (Examples examples : examples) {
            builder.append(examples).append("\n");
        }

        return builder.toString().trim();
    }
}
