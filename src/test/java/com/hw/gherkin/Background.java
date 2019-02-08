//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.Location;
import gherkin.ast.Tag;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Background extends ScenarioDefinition {
    public Background(Location location, String keyword, String name, String description, List<Step> steps) {
        super(location, keyword, name, description, steps);
    }

    public Background(gherkin.ast.Background background) {
        //super(background.getLocation(), background.getKeyword(), background.getName(), background.getDescription(), background.getSteps());
        super(background);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getKeyword()).append(": ").append(getName()).append("\n");
        for (Step step : getSteps())
            builder.append(step).append("\n");

        return builder.toString().trim();
    }
}