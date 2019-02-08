//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hw.gherkin;

import gherkin.ast.Node;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DataTable extends Node {
    private List<TableRow> rows;

    public DataTable(List<TableRow> rows) {
        super(((TableRow)rows.get(0)).getLocation());
        this.rows = new LinkedList<>(rows);
    }

    public DataTable(gherkin.ast.DataTable dataTable) {
        super(dataTable.getRows().get(0).getLocation());
        this.rows = new LinkedList<>(dataTable.getRows());
    }

    public List<TableRow> getRows() {
        return this.rows;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (TableRow row : rows) {
            List<String> list = new LinkedList<>();
            for (TableCell tableCell : row.getCells())
                list.add(tableCell.getValue().trim());
            String t = list.stream().map(Object::toString).map(String::trim).collect(Collectors.joining(" | ",
                    " | ",
                    " |"));
            builder.append(t).append("\n");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}