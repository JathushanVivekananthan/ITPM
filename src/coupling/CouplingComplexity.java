package coupling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CouplingComplexity {
    private int Nr;
    private int Nmcms;
    private int Nmcmd;
    private int Nmcrms;
    private int Nmcrmd;
    private int Nrmcrms;
    private int Nrmcrmd;
    private int Nrmcms;
    private int Nrmcmd;
    private int Nmrgvs;
    private int Nmrgvd;
    private int Nrmrgvs;
    private int Nrmrgvd;

    private int[] weights = new int[]{2, 2, 3, 3, 4, 4, 5, 3, 4, 1, 2, 1, 2};

    public void doCouplingComplexity(File file) throws FileNotFoundException {
        List<CodeLine> codeLineList = getLineList(file);
        List<CodeLine> methodList = getMethodList(codeLineList);
        List<CodeLine> systemMethodList = getSystemMethodList(codeLineList, methodList);
        setEndLineNumber(codeLineList, methodList);
        List<CodeLine> globalVariableList = getGlobalVariableList(codeLineList, methodList);
        List<CodeLine> recursiveMethodList = getRecursiveMethodList(codeLineList, methodList);
        List<CodeLine> recursiveMethodCallList = getRecursiveMethodCallList(codeLineList, methodList);
        List<CodeLine> regularMethodList = getRegularMethodList(codeLineList, methodList);
        List<CodeLine> regularInRegularMethodList = getRegularInRegularMethodList(codeLineList, methodList, regularMethodList, systemMethodList);
        List<CodeLine> recursiveInRegularMethodList = getRecursiveInRegularMethodList(codeLineList, methodList, regularMethodList, recursiveMethodList);
        List<CodeLine> recursiveInRecursiveMethodList = getRecursiveInRecursiveMethodList(codeLineList, methodList, recursiveMethodList);
        List<CodeLine> regularInRecursiveMethodList = getRegularInRecursiveMethodList(codeLineList, methodList, recursiveMethodList, regularMethodList, systemMethodList);
        List<CodeLine> globalVariableListInRec = getGlobalVariableListInRec(codeLineList, methodList, recursiveMethodList, globalVariableList);

        Nr = recursiveMethodCallList.size();
        Nmcms = regularInRegularMethodList.size();
        Nmcrms = recursiveInRegularMethodList.size();
        Nrmcrms = recursiveInRecursiveMethodList.size();
        Nrmcms = regularInRecursiveMethodList.size();
        Nmrgvs = globalVariableList.size();
        Nrmrgvs = globalVariableListInRec.size();
        Nmcmd = 0;
        Nmcrmd = 0;
        Nrmcrmd = 0;
        Nrmcmd = 0;
        Nmrgvd = 0;
        Nrmrgvd = 0;
    }

    public int getCcp() {
        return (weights[0] * Nr) + (weights[1] * Nmcms) + (weights[2] * Nmcmd) + (weights[3] * Nmcrms) + (weights[4] * Nmcrmd) + (weights[5] * Nrmcrms) + (weights[6] * Nrmcrmd)
            + (weights[7] * Nrmcms) + (weights[8] * Nrmcmd) + (weights[9] * Nmrgvs) + (weights[10] * Nmrgvd) + (weights[11] * Nrmrgvs) + (weights[12] * Nrmrgvd);
    }

    public int getNr() {
        return Nr;
    }

    public int getNmcms() {
        return Nmcms;
    }

    public int getNmcmd() {
        return Nmcmd;
    }

    public int getNmcrms() {
        return Nmcrms;
    }

    public int getNmcrmd() {
        return Nmcrmd;
    }

    public int getNrmcrms() {
        return Nrmcrms;
    }

    public int getNrmcrmd() {
        return Nrmcrmd;
    }

    public int getNrmcms() {
        return Nrmcms;
    }

    public int getNrmcmd() {
        return Nrmcmd;
    }

    public int getNmrgvs() {
        return Nmrgvs;
    }

    public int getNmrgvd() {
        return Nmrgvd;
    }

    public int getNrmrgvs() {
        return Nrmrgvs;
    }

    public int getNrmrgvd() {
        return Nrmrgvd;
    }

    public List<CodeLine> getLineList(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        List<CodeLine> codeLineList = new ArrayList<>();
        int count = 1;
        while (scanner.hasNextLine()) {
            codeLineList.add(new CodeLine(count++, scanner.nextLine()));
        }
        return codeLineList;
    }

    private List<CodeLine> getMethodList(List<CodeLine> codeLineList) {
        List<CodeLine> methodList = new ArrayList<>();

        for (CodeLine codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("public")) {
                String[] sub = codeLine.getLineContent().split("\\(");
                String x = sub[0].replace("public", "").replace("private", "").replace("protected", "")
                    .replace("static", "").replace("final", "").trim();
                if (x.split(" ").length > 1) {
                    methodList.add(new CodeLine(codeLine.getLineNumber(), x.split(" ")[1]));
                } else {
                    methodList.add(new CodeLine(codeLine.getLineNumber(), x));
                }
            }
        }

        return methodList;
    }

    private List<CodeLine> getSystemMethodList(List<CodeLine> codeLineList, List<CodeLine> methodList) {
        List<CodeLine> systemMethodList = new ArrayList<>();

        for (CodeLine codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("(") && codeLine.getLineContent().contains(")")
                && codeLine.getLineContent().contains(";")) {
                boolean status = true;
                String[] sub = codeLine.getLineContent().split("\\(");
                String method = sub[0].trim();

                for (CodeLine codeLine_method : methodList) {
                    if (method.contains(codeLine_method.getLineContent())) {
                        status = false;
                        break;
                    }
                }
                if (status) {
                    systemMethodList.add(codeLine);
                }
            }
        }

        return systemMethodList;
    }

    private void setEndLineNumber(List<CodeLine> codeLineList, List<CodeLine> methodList) {
        for (int i = 0; i < methodList.size(); i++) {
            int opnBrkt = 0;
            int clsBrkt = 0;

            if (i == methodList.size() - 1) {
                for (int j = methodList.get(i).getLineNumber() - 1; j < codeLineList.size(); ++j) {
                    if (codeLineList.get(j).getLineContent().contains("{")) {
                        ++opnBrkt;
                    } else if (codeLineList.get(j).getLineContent().contains("}")) {
                        --opnBrkt;
                    }
                    if (opnBrkt == 0) {
                        CodeLine method = methodList.get(i);
                        method.setEndLineNumber(++j);
                        methodList.set(i, method);
                        break;
                    }
                }
            } else {
                for (int j = methodList.get(i).getLineNumber() - 1; j < methodList.get(i + 1).getLineNumber() - 1; ++j) {
                    if (codeLineList.get(j).getLineContent().contains("{")) {
                        ++opnBrkt;
                    } else if (codeLineList.get(j).getLineContent().contains("}")) {
                        --opnBrkt;
                    }
                    if (opnBrkt == clsBrkt) {
                        CodeLine method = methodList.get(i);
                        method.setEndLineNumber(++j);
                        methodList.set(i, method);
                        break;
                    }
                }
            }
        }
    }

    private List<CodeLine> getGlobalVariableList(List<CodeLine> codeLineList, List<CodeLine> methodList) {
        List<CodeLine> globalVariableList = new ArrayList<>();

        for (CodeLine codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("=") && codeLine.getLineContent().contains(";")) {
                if (codeLine.getLineContent().contains("byte") || codeLine.getLineContent().contains("short")
                    || codeLine.getLineContent().contains("int") || codeLine.getLineContent().contains("long")
                    || codeLine.getLineContent().contains("float") || codeLine.getLineContent().contains("double")
                    || codeLine.getLineContent().contains("char") || codeLine.getLineContent().contains("boolean")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    globalVariableList.add(new CodeLine(codeLine.getLineNumber(), sub[1]));
                }
            }
        }

        List<Integer> removableNumbers = new ArrayList<>();
        for (CodeLine codeLine : globalVariableList) {
            for (CodeLine codeLine_meth : methodList) {
                if (codeLine.getLineNumber() > codeLine_meth.getLineNumber()
                    && codeLine.getLineNumber() < codeLine_meth.getEndLineNumber()) {
                    removableNumbers.add(codeLine.getLineNumber());
                }
            }
        }
        for (int x : removableNumbers) {
            for (int i = 0; i < globalVariableList.size(); i++) {
                if (globalVariableList.get(i).getLineNumber() == x) {
                    globalVariableList.remove(i);
                }
            }
        }

        return globalVariableList;
    }

    private List<CodeLine> getRecursiveMethodList(List<CodeLine> codeLineList, List<CodeLine> methodList) {
        List<CodeLine> recursiveMethodList = new ArrayList<>();

        for (CodeLine codeLine : methodList) {
            boolean no_recursive = true;

            for (int i = codeLine.getLineNumber(); i < codeLine.getEndLineNumber(); i++) {
                if (codeLineList.get(i).getLineContent().contains(codeLine.getLineContent())) {
                    no_recursive = false;
                    break;
                }
            }
            if (!no_recursive) {
                recursiveMethodList.add(codeLine);
            }
        }

        return recursiveMethodList;
    }

    private List<CodeLine> getRecursiveMethodCallList(List<CodeLine> codeLineList, List<CodeLine> methodList) {
        List<CodeLine> recursiveMethodCalls = new ArrayList<>();

        for (CodeLine codeLine : methodList) {
            for (int i = codeLine.getLineNumber(); i < codeLine.getEndLineNumber(); i++) {
                if (codeLineList.get(i).getLineContent().contains(codeLine.getLineContent())) {
                    recursiveMethodCalls.add(codeLineList.get(i));
                }
            }
        }

        return recursiveMethodCalls;
    }

    private List<CodeLine> getRegularMethodList(List<CodeLine> codeLineList, List<CodeLine> methodList) {
        List<CodeLine> regularMethodList = new ArrayList<>();

        for (CodeLine codeLine : methodList) {
            boolean no_recursive = true;

            for (int i = codeLine.getLineNumber(); i < codeLine.getEndLineNumber(); i++) {
                if (codeLineList.get(i).getLineContent().contains(codeLine.getLineContent())) {
                    no_recursive = false;
                    break;
                }
            }
            if (no_recursive) {
                regularMethodList.add(codeLine);
            }
        }

        return regularMethodList;
    }

    private List<CodeLine> getRegularInRegularMethodList(List<CodeLine> codeLineList, List<CodeLine> methodList, List<CodeLine> regularMethodList, List<CodeLine> systemMethodList) {
        List<CodeLine> regularInRegularMethodList = new ArrayList<>();

        for (CodeLine codeLine : regularMethodList) {
            for (CodeLine regCodeLine : regularMethodList) {
                if (codeLine.getLineNumber() != regCodeLine.getLineNumber()) {
                    for (int i = codeLine.getLineNumber(); i < getEndLineNumber(methodList, codeLine); i++) {
                        if (codeLineList.get(i).getLineContent().contains(regCodeLine.getLineContent())) {
                            regularInRegularMethodList.add(codeLineList.get(i));
                        }
                    }
                }
            }
            for (CodeLine codeLine_sys : systemMethodList) {
                if (codeLine_sys.getLineNumber() > codeLine.getLineNumber()
                    && codeLine_sys.getLineNumber() < getEndLineNumber(methodList, codeLine)) {
                    regularInRegularMethodList.add(codeLine_sys);
                }
            }
        }

        return regularInRegularMethodList;
    }

    private int getEndLineNumber(List<CodeLine> methodList, CodeLine codeLine) {
        int endNumber = 0;
        for (CodeLine codeLine1 : methodList) {
            if (codeLine1.getLineNumber() == codeLine.getLineNumber()) {
                endNumber = codeLine1.getEndLineNumber();
                break;
            }
        }
        return endNumber;
    }

    private List<CodeLine> getRecursiveInRegularMethodList(List<CodeLine> codeLineList, List<CodeLine> methodList, List<CodeLine> regularMethodList, List<CodeLine> recursiveMethodList) {
        List<CodeLine> recursiveInRegularMethodList = new ArrayList<>();

        for (CodeLine regCodeLine : regularMethodList) {
            for (int i = regCodeLine.getLineNumber(); i < getEndLineNumber(methodList, regCodeLine); i++) {
                for (CodeLine recCodeLine : recursiveMethodList) {
                    if (codeLineList.get(i).getLineContent().contains(recCodeLine.getLineContent())) {
                        recursiveInRegularMethodList.add(codeLineList.get(i));
                    }
                }
            }
        }

        return recursiveInRegularMethodList;
    }

    private List<CodeLine> getRecursiveInRecursiveMethodList(List<CodeLine> codeLineList, List<CodeLine> methodList, List<CodeLine> recursiveMethodList) {
        List<CodeLine> recursiveInRecursiveMethodList = new ArrayList<>();

        for (CodeLine codeLine : recursiveMethodList) {
            for (CodeLine recCodeLine : recursiveMethodList) {
                if (codeLine.getLineNumber() != recCodeLine.getLineNumber()) {
                    for (int i = codeLine.getLineNumber(); i < getEndLineNumber(methodList, codeLine); i++) {
                        if (codeLineList.get(i).getLineContent().contains(recCodeLine.getLineContent())) {
                            recursiveInRecursiveMethodList.add(codeLineList.get(i));
                        }
                    }
                }
            }
        }

        return recursiveInRecursiveMethodList;
    }

    private List<CodeLine> getRegularInRecursiveMethodList(List<CodeLine> codeLineList, List<CodeLine> methodList, List<CodeLine> recursiveMethodList, List<CodeLine> regularMethodList, List<CodeLine> systemMethodList) {
        List<CodeLine> regularInRecursiveMethodList = new ArrayList<>();

        for (CodeLine recCodeLine : recursiveMethodList) {
            for (int i = recCodeLine.getLineNumber(); i < getEndLineNumber(methodList, recCodeLine); i++) {
                for (CodeLine regCodeLine : regularMethodList) {
                    if (codeLineList.get(i).getLineContent().contains(regCodeLine.getLineContent())) {
                        regularInRecursiveMethodList.add(codeLineList.get(i));
                    }
                }
            }
            for (CodeLine codeLine_sys : systemMethodList) {
                if (codeLine_sys.getLineNumber() > recCodeLine.getLineNumber()
                    && codeLine_sys.getLineNumber() < getEndLineNumber(methodList, recCodeLine)) {
                    regularInRecursiveMethodList.add(codeLine_sys);
                }
            }
        }

        return regularInRecursiveMethodList;
    }

    private List<CodeLine> getGlobalVariableListInRec(List<CodeLine> codeLineList, List<CodeLine> methodList, List<CodeLine> recursiveMethodList, List<CodeLine> globalVariableList) {
        List<CodeLine> globalVariableListInRec = new ArrayList<>();

        for (CodeLine codeLine : globalVariableList) {
            for (CodeLine methodCodeLine : recursiveMethodList) {
                for (int i = methodCodeLine.getLineNumber(); i < getEndLineNumber(methodList, methodCodeLine); i++) {
                    if (codeLineList.get(i).getLineContent().contains(codeLine.getLineContent())
                        && !codeLineList.get(i).getLineContent().contains("." + codeLine.getLineContent())) {
                        String text = codeLineList.get(i).getLineContent();
                        String regex = "\\b" + codeLine.getLineContent() + "\\b";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(text);
                        while (matcher.find()) {
                            globalVariableListInRec.add(codeLineList.get(i));
                        }
                    }
                }
            }
        }

        return globalVariableListInRec;
    }

}
