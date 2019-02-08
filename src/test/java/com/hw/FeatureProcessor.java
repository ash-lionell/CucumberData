package com.hw;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.*;
import com.hw.gherkin.DataTable;
import com.hw.gherkin.Examples;
import com.hw.gherkin.Feature;
import com.hw.gherkin.GherkinDocument;
import com.hw.gherkin.Scenario;
import com.hw.gherkin.ScenarioDefinition;
import com.hw.gherkin.ScenarioOutline;
import com.hw.gherkin.Step;
import io.cucumber.tagexpressions.Expression;
import io.cucumber.tagexpressions.TagExpressionParser;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FeatureProcessor {
    public FeatureProcessor() {}

    static class ProcessContext {
        String featureFilename;
        Feature feature;
        ScenarioDefinition scenarioDefinition;
        public ProcessContext(String featureFilename, Feature feature, ScenarioDefinition scenarioDefinition) {
            this.featureFilename = featureFilename;
            this.feature = feature;
            this.scenarioDefinition = scenarioDefinition;
        }
    }

    public static boolean process(File srcFeature, File destFeature, ExtDataProvider dataProvider, List<String> tagsToRun) throws FileNotFoundException {
        //System.out.println("proc file : "+srcFeature.getName());
        AstBuilder builder = new AstBuilder();
        Parser parser = new Parser(builder);
        gherkin.ast.GherkinDocument srcDoc = (gherkin.ast.GherkinDocument) parser.parse(new FileReader(srcFeature));
        GherkinDocument compiledDoc = new GherkinDocument(srcDoc);
        Feature feature = compiledDoc.getFeature();
        List<ScenarioDefinition> scenarios = feature.getChildren();
        //System.out.println("no of children : "+srcDoc.getFeature().getChildren().size());
        System.out.println("no of children : "+scenarios.size());

        List<Expression> expressions = new LinkedList<>();
        if (tagsToRun!=null)
            for (String tagToRun : tagsToRun)
                expressions.add(new TagExpressionParser().parse(tagToRun));

        List<Tag> featureTags = feature.getTags();

        ProcessContext context;
        for (ScenarioDefinition scenarioDef : scenarios) {
            context = new ProcessContext(srcFeature.getName(), feature, scenarioDef);
            processScenarioDefinition(context, scenarioDef, dataProvider, featureTags, expressions);
        }

        context = new ProcessContext(srcFeature.getName(),null,null);
        generateFeatureFileFromDocument(context, compiledDoc, destFeature);

        return false;
    }

    private static void processScenarioDefinition(ProcessContext context, ScenarioDefinition scenarioDef, ExtDataProvider dataProvider, List<Tag> featureTags, List<Expression> expressions) {
        if (scenarioDef instanceof Scenario) {
            Scenario scenario = (Scenario) scenarioDef;

            if (shouldScenarioDefinitionRun(expressions, scenario.getTags(), featureTags)) {
                String dataSourceNames[] = getDataSourceNames(scenario);
                if (dataSourceNames.length>0) {
                    HashMap<String, String[]> sceData = new LinkedHashMap<>();
                    for (String dataSource : dataSourceNames) {
                        HashMap<String, String[]> datum = dataProvider.provideFor(dataSource);
                        if (datum!=null)
                            sceData.putAll(datum);
                        else
                            System.out.println("ERROR : No data found for data source : [ "+dataSource+" ] at scenario : [ "+context.scenarioDefinition.getName()+" ] in feature : [ "+context.featureFilename+" ]");
                    }
                    processScenario(context, scenario, sceData);
                }
            }
            else
                scenarioDef.setShouldRun(false);
        }
        else if (scenarioDef instanceof ScenarioOutline) {
            ScenarioOutline scenario = (ScenarioOutline) scenarioDef;
            if (shouldScenarioDefinitionRun(expressions, scenario.getTags(), featureTags)) {
                String dataSourceNames[] = getDataSourceNames(scenario);
                if (dataSourceNames.length>0) {
                    HashMap<String, String[]> sceData = new LinkedHashMap<>();
                    for (String dataSource : dataSourceNames) {
                        HashMap<String, String[]> datum = dataProvider.provideFor(dataSource);
                        if (datum!=null)
                            sceData.putAll(datum);
                        else
                            System.out.println("ERROR : No data found for data source : [ "+dataSource+" ] at scenario : [ "+context.scenarioDefinition.getName()+" ] in feature : [ "+context.featureFilename+" ]");
                    }
                    processScenario(context, scenario, sceData);
                }
            }
            else
                scenarioDef.setShouldRun(false);
        }
    }

    private static boolean shouldScenarioDefinitionRun(List<Expression> expressions, List<Tag> scenarioTags, List<Tag> featureTags) {
        boolean shouldRun = true;
        List<Tag> expandedTags = new LinkedList<>(scenarioTags);
        expandedTags.addAll(featureTags);
        List<String> tags = new LinkedList<>();
        for (Tag tag : expandedTags)
            tags.add(tag.getName());
        for (Expression expression : expressions)
            if (!expression.evaluate(tags)) {
                shouldRun = false;
                break;
            }
        return shouldRun;
    }

    private static void processScenario(ProcessContext context, Scenario scenario, HashMap<String, String[]> data) {
        processScenarioDefinition(context, scenario, data);
    }

    private static void processScenario(ProcessContext context, ScenarioOutline scenario, HashMap<String, String[]> data) {
        processScenarioDefinition(context, scenario, data);

    }

    private static void processScenarioDefinition(ProcessContext context, ScenarioDefinition scenario, HashMap<String, String[]> data) {
        if (!data.isEmpty()) {
            List<Step> steps = scenario.getSteps();
            for (Step step : steps) {
                Node node = step.getArgument();
                if (node != null) {
                    DataTable dataTable = (DataTable) node;
                    if (doesTableNeedData(dataTable)) {
                        reconstructTableWithData(context,dataTable,data);
                    }
                }
            }

            if (scenario instanceof ScenarioOutline) {
                Examples examples = ((ScenarioOutline) scenario).getExamples().get(0);
                if (doesExampleNeedData(examples)) {
                    reconstructExamplesWithData(context,examples,data);
                }
            }
        }
        else {
            System.out.println("ERROR : Data sources do not have any data.");
        }
    }

    private static String[] getDataSourceNames(Scenario scenario) {
        return getDataSourceNames(scenario.getTags());
    }

    private static String[] getDataSourceNames(ScenarioOutline scenario) {
        return getDataSourceNames(scenario.getTags());
    }

    private static String[] getDataSourceNames(List<Tag> tags) {
        List<String> dataSourceTags = new ArrayList<>();
        Iterator<Tag> it = tags.iterator();
        while (it.hasNext()) {
            String tagName = it.next().getName();
            if (tagName.startsWith("@data")) {
                String dataProviderText = tagName.split("=")[1];
                dataProviderText = dataProviderText.replaceAll("[\\{\\}]","");
                String dataProviders[] = dataProviderText.split(",");
                for (String dataProvider : dataProviders)
                    dataSourceTags.add(dataProvider);
            }
        }
        String retTags[] = new String[dataSourceTags.size()];
        return dataSourceTags.toArray(retTags);
    }

    private static boolean doesTableNeedData(DataTable table) {
        List<TableRow> rows = table.getRows();
        if (rows.size() == 1) {
            return true;
        }
        else {
            return false;
        }
    }
    private static boolean doesExampleNeedData(Examples table) {
        List<TableRow> rows = table.getTableBody();
        if (rows == null || rows.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    private static DataTable reconstructTableWithData(ProcessContext context, DataTable table, HashMap<String, String[]> data) {
        TableRow headerRow = table.getRows().get(0);
        List<TableCell> headerCells = headerRow.getCells();
        String data2[][] = new String[headerCells.size()][];
        if (isDataAndTableValid(context,headerRow,data,data2)) {
            for (int i=0;i<data2[0].length;++i) {
                List<TableCell> tCells = new ArrayList<>();
                for (int j=0;j<data2.length;++j) {
                    tCells.add(new TableCell(headerCells.get(j).getLocation(),data2[j][i]));
                }
                TableRow tRow = new TableRow(headerRow.getLocation(),tCells);
                table.getRows().add(tRow);
            }
        }
        return table;
    }

    private static Examples reconstructExamplesWithData(ProcessContext context, Examples table, HashMap<String, String[]> data) {
        TableRow headerRow = table.getTableHeader();
        List<TableCell> headerCells = headerRow.getCells();
        String data2[][] = new String[headerCells.size()][];
        if (isDataAndTableValid(context,headerRow,data,data2)) {
            table.createTableBody();
            for (int i=0;i<data2[0].length;++i) {
                List<TableCell> tCells = new ArrayList<>();
                for (int j=0;j<data2.length;++j) {
                    tCells.add(new TableCell(headerCells.get(j).getLocation(),data2[j][i]));
                }
                TableRow tRow = new TableRow(headerRow.getLocation(),tCells);
                table.getTableBody().add(tRow);
            }
        }
        return table;
    }

    static boolean isDataAndTableValid(ProcessContext context, TableRow headerRow, HashMap<String, String[]> data, String data2[][]) {
        List<TableCell> cells = headerRow.getCells();
        String headers[] = new String[cells.size()];
        List<String> cellValues = new LinkedList<>();
        for (TableCell cell : cells)
            cellValues.add(cell.getValue());
        cellValues.toArray(headers);

        List<String> missingData = new ArrayList<>();
        HashMap<String, Integer> dataCount = new LinkedHashMap<>();
        //String data2[][] = new String[headers.length][];
        int lastDataCount = -1;
        boolean doDataCountsMatch = true;
        int counter = 0;
        for (String header : headers) {
            if (data.containsKey(header)) {
                String tData[] = data.get(header);
                int tDataCount = tData.length;
                dataCount.put(header, tDataCount);
                data2[counter++] = tData;
                if (lastDataCount == -1)
                    lastDataCount = tDataCount;
                else if (lastDataCount != tDataCount)
                    doDataCountsMatch = false;
            }
            else
                missingData.add(header);
        }
        if (!missingData.isEmpty()) {
            System.out.println(missingData.stream().map(Object::toString).collect(Collectors.joining(" ], [ ",
                    "ERROR : Following parameters do not have data present : [ ",
                    " ] in scenario [ "+context.scenarioDefinition.getName()+" ] in [ "+context.featureFilename+" ].")));
        }
        if (!doDataCountsMatch) {
            System.out.println(dataCount.entrySet().stream().map(Object::toString).collect(Collectors.joining(" ], [ ",
                    "ERROR : Following parameters data counts mismatch : [ ",
                    " ] in scenario [ "+context.scenarioDefinition.getName()+" ] in [ "+context.featureFilename+" ].")));
        }

        if (!missingData.isEmpty() || !doDataCountsMatch)
            return false;
        else
            return true;
    }

    static void generateFeatureFileFromDocument(ProcessContext context, GherkinDocument document, File destFile) {
        String fileContents = document.getStringRepresentation();
        //System.out.println("DOC :\n"+fileContents);
        try {
            FileWriter writer = new FileWriter(destFile);
            writer.write(fileContents);
            writer.close();
        } catch (IOException ioe) {
            System.out.println("ERROR : Error occurred while trying to write the processed feature [ "+context.featureFilename+" ] to the outpput directory: "+ioe.getMessage());
        }
    }
}
