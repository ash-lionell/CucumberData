//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.Location;
import gherkin.ast.Node;

public class Step extends Node {
    private final String keyword;
    private final String text;
    private final Node argument;

    public Step(Location location, String keyword, String text, Node argument) {
        super(location);
        this.keyword = keyword;
        this.text = text;
        this.argument = new DataTable((gherkin.ast.DataTable) argument);
    }

    public Step(gherkin.ast.Step step) {
        super(step.getLocation());
        this.keyword = step.getKeyword();
        this.text = step.getText();
        if (step.getArgument() != null)
            this.argument = new DataTable((gherkin.ast.DataTable) step.getArgument());
        else
            this.argument = null;
    }

    public String getText() {
        return this.text;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public Node getArgument() {
        return this.argument;
    }

    @Override
    public String toString() {
        if (argument!=null) {
            return keyword.trim() + " " + text.trim() + "\n" + argument;
        }
        else {
            return keyword.trim() + " " + text.trim();
        }
    }
}
