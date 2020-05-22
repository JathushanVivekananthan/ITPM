/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package method;

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
public class MethodComplexity {
    private int Wmrtp;
    private int Wmrtc;
    private int Npdtp;
    private int Ncdtp;

    private int[] weights = new int[]{1,1,1,2};
    
     private int Wmrt;

    public void doMethodComplexity(File file) throws FileNotFoundException {
        List<codeLine> codeLineList = getLineList(file);
        List<codeLine> methodList = getMethodList(codeLineList);
        List<codeLine> systemMethodList = getSystemMethodList(codeLineList, methodList);
        setEndLineNumber(codeLineList, methodList);
        List<codeLine> primitive = getPrimitiveReturn(codeLineList,methodList);
        List<codeLine> primitiveData = getPrimitiveData(codeLineList);
        
        Wmrt = primitive.size();
        Npdtp = primitiveData.size();
        Ncdtp = 0;

    }

    public int getCm() {
        return (weights[0] * Wmrt) + (weights[1] * Npdtp) + (weights[2] * Ncdtp) ;
    }

    public int getWmrt() {
        return Wmrt;
    }

    public int getNpdtp() {
        return Npdtp;
    }

    public int getNcdtp() {
        return Ncdtp;
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

    
    public List<codeLine> getPrimitiveReturn(List<codeLine> codeLineList,List<codeLine> methodList) {
        List<codeLine> primitive = new ArrayList<>();
        
        for (codeLine codeLine : codeLineList) { 
            
//            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
                    if (codeLine.getLineContent().contains("return") && codeLine.getLineContent().contains("int") || codeLine.getLineContent().contains("long")
                    || codeLine.getLineContent().contains("double") || codeLine.getLineContent().contains("float")
                    || codeLine.getLineContent().contains("boolean") || codeLine.getLineContent().contains("char")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    primitive.add(new codeLine(codeLine.getLineNumber(), sub[1]));
                   
                }
            }
        
        List<Integer> removableNumbers = new ArrayList<>();
        for (codeLine codeLine : primitive) {
            for (codeLine codeLine_meth : methodList) {
                if (codeLine.getLineNumber() > codeLine_meth.getLineNumber()
                    && codeLine.getLineNumber() < codeLine_meth.getEndLineNumber()) {
                    removableNumbers.add(codeLine.getLineNumber());
                }
            }
        }
        for (int x : removableNumbers) {
            for (int i = 0; i < primitive.size(); i++) {
                if (primitive.get(i).getLineNumber() == x) {
                    primitive.remove(i);
                }
            }
        }
        return primitive;
    }
    
    public List<codeLine> getPrimitiveData(List<codeLine> codeLineList) {
        List<codeLine> primitiveData = new ArrayList<>();
        
        for (codeLine codeLine : codeLineList) { 
            
//            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
                    if (codeLine.getLineContent().contains("int") || codeLine.getLineContent().contains("long")
                    || codeLine.getLineContent().contains("double") || codeLine.getLineContent().contains("float")
                    || codeLine.getLineContent().contains("boolean") || codeLine.getLineContent().contains("char")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    primitiveData.add(new codeLine(codeLine.getLineNumber(), sub[1]));
                   
                }
            }
        return primitiveData;
    }

}


