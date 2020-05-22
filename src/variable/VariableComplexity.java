/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import size.codeLine;

/**
 *
 * @author senthu
 */
public class VariableComplexity {
    private int Wvsg;
    private int Wvsl;
    private int Npdtv;
    private int Ncptv;

    private int[] weights = new int[]{2,1,1,2};
    
    private int Wvs;

    public void doVariableComplexity(File file) throws FileNotFoundException {
        List<codeLine> codeLineList = getLineList(file);
        List<codeLine> methodList = getMethodList(codeLineList);
        List<codeLine> systemMethodList = getSystemMethodList(codeLineList, methodList);
        setEndLineNumber(codeLineList, methodList);
        List<codeLine> globalVariableList = getGlobalVariableList(codeLineList, methodList);
        List<codeLine> primitive = getPrimitive(codeLineList);

        Wvs = globalVariableList.size() ;
        Npdtv = primitive.size();
        Ncptv = 0;

    }

    public int getCv() {
        return (weights[0] * Wvs) + (weights[1] * Npdtv) + (weights[2] * Ncptv) ;
    }

    public int getWvs() {
        return Wvs;
    }

    public int getNpdtv() {
        return Npdtv;
    }

    public int getNcptv() {
        return Ncptv;
    }


 

    public List<codeLine> getLineList(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        List<codeLine> codeLineList = new ArrayList<>();
        int count = 1;
        while (scanner.hasNextLine()) {
            codeLineList.add(new codeLine(count++, scanner.nextLine()));
        }
        return codeLineList;
    }
      private List<codeLine> getMethodList(List<codeLine> codeLineList) {
        List<codeLine> methodList = new ArrayList<>();

        for (codeLine codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("public")) {
                String[] sub = codeLine.getLineContent().split("\\(");
                String x = sub[0].replace("public", "").replace("private", "").replace("protected", "")
                    .replace("static", "").replace("final", "").trim();
                if (x.split(" ").length > 1) {
                    methodList.add(new codeLine(codeLine.getLineNumber(), x.split(" ")[1]));
                } else {
                    methodList.add(new codeLine(codeLine.getLineNumber(), x));
                }
            }
        }

        return methodList;
    }

    private List<codeLine> getSystemMethodList(List<codeLine> codeLineList, List<codeLine> methodList) {
        List<codeLine> systemMethodList = new ArrayList<>();

        for (codeLine codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("(") && codeLine.getLineContent().contains(")")
                && codeLine.getLineContent().contains(";")) {
                boolean status = true;
                String[] sub = codeLine.getLineContent().split("\\(");
                String method = sub[0].trim();

                for (codeLine codeLine_method : methodList) {
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

    private void setEndLineNumber(List<codeLine> codeLineList, List<codeLine> methodList) {
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
                        codeLine method = methodList.get(i);
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
                        codeLine method = methodList.get(i);
                        method.setEndLineNumber(++j);
                        methodList.set(i, method);
                        break;
                    }
                }
            }
        }
    }

    private List<codeLine> getGlobalVariableList(List<codeLine> codeLineList, List<codeLine> methodList) {
        List<codeLine> globalVariableList = new ArrayList<>();

        for (codeLine codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("=") && codeLine.getLineContent().contains(";")) {
                if (codeLine.getLineContent().contains("byte") || codeLine.getLineContent().contains("short")
                    || codeLine.getLineContent().contains("int") || codeLine.getLineContent().contains("long")
                    || codeLine.getLineContent().contains("float") || codeLine.getLineContent().contains("double")
                    || codeLine.getLineContent().contains("char") || codeLine.getLineContent().contains("boolean")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    globalVariableList.add(new codeLine(codeLine.getLineNumber(), sub[1]));
                }
            }
        }

        List<Integer> removableNumbers = new ArrayList<>();
        for (codeLine codeLine : globalVariableList) {
            for (codeLine codeLine_meth : methodList) {
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
    public List<codeLine> getPrimitive(List<codeLine> codeLineList) {
        List<codeLine> primitive = new ArrayList<>();
        
        for (codeLine codeLine : codeLineList) { 
            
//            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
                    if (codeLine.getLineContent().contains("int") || codeLine.getLineContent().contains("long")
                    || codeLine.getLineContent().contains("double") || codeLine.getLineContent().contains("float")
                    || codeLine.getLineContent().contains("boolean") || codeLine.getLineContent().contains("char")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    primitive.add(new codeLine(codeLine.getLineNumber(), sub[1]));
                   
                }
            }
        return primitive;
    }



}

