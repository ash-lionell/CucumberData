//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Examples extends Node {
    private final List<Tag> tags;
    private final String keyword;
    private final String name;
    private final String description;
    private final TableRow tableHeader;
    private List<TableRow> tableBody;

    public Examples(Location location, List<Tag> tags, String keyword, String name, String description, TableRow tableHeader, List<TableRow> tableBody) {
        super(location);
        this.tags = Collections.unmodifiableList(tags);
        this.keyword = keyword;
        this.name = name;
        this.description = description;
        this.tableHeader = tableHeader;
        this.tableBody = tableBody != null ? Collections.unmodifiableList(tableBody) : null;
    }

    public Examples(gherkin.ast.Examples examples) {
        super(examples.getLocation());
        this.tags = Collections.unmodifiableList(examples.getTags());
        this.keyword = examples.getKeyword();
        this.name = examples.getName();
        this.description = examples.getDescription();
        this.tableHeader = examples.getTableHeader();
        this.tableBody = examples.getTableBody() != null ? examples.getTableBody() : null;
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

    public List<TableRow> getTableBody() {
        return this.tableBody;
    }

    public void createTableBody() {
        this.tableBody = new LinkedList<>();
    }

    public TableRow getTableHeader() {
        return this.tableHeader;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(keyword.trim()).append(":\n");
        List<String> list = new LinkedList<>();
        for (TableCell cell : tableHeader.getCells())
            list.add(cell.getValue());
        String t = list.stream().map(Object::toString).map(String::trim).collect(Collectors.joining(" | ",
            " | ",
            " |"));
        builder.append(t).append("\n");
        for (TableRow row : tableBody) {
            list = new LinkedList<>();
            for (TableCell tableCell : row.getCells())
                list.add(tableCell.getValue().trim());
            t = list.stream().map(Object::toString).map(String::trim).collect(Collectors.joining(" | ",
                    " | ",
                    " |"));
            builder.append(t).append("\n");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}