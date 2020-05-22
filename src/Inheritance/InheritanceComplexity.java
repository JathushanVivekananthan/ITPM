/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inheritance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 *
 * @author Kasthuri
 */
public class InheritanceComplexity {
    private int Ndi;
    private int Nidi;
    private int Ti;
    private int Ci; 
    private int count = 0;
    private String className = "";
    private int inheritNon = 0;	   
    private int inheritOne = 1;
    private int inheritTwo = 2;	    
    private int inheritThree = 3;		    
    private int inheritMore = 4;		    
    private String code ;	    
    //REGEX PATTERNS	    
    private String INHERITANCE_CLASS = "\\w*\\sextends\\s\\w*";	    
    private String INHERITANCE_INTERFACE = "implements|,";
    private String FIND_INHERITANCE_INTERFACE = "implements[\\s\\w*,]*";
    private String FIND_CLASS = "class\\s\\w*";	     	    	    
                      
		    
		    
 
 public void doInheritanceComplexity(File file) throws FileNotFoundException {
        List<codelineI> codeLineList = getLineList(file);
        List<codelineI> methodList = getMethodList(codeLineList);
        List<codelineI> systemMethodList = getSystemMethodList(codeLineList, methodList);
        setEndLineNumber(codeLineList, methodList);
        List<codelineI> Totalinheritances = getTotalinheritances(codeLineList, methodList);
        
    }
 public int getCi() {
        if(Ti==0){
            return Ci=0;
        }
        else if(Ti==1){
            return Ci=1;
        }
        else if(Ti==2){
            return Ci=2;
        }
        else if(Ti==3){
            return Ci=Ti;
        }
        else if(Ti>3){
            return Ci=4;
        }
        
    return Ti=Ci;     
 }

    public int getNdi() {
        return Ndi;
    }

    public int getNidi() {
        return Nidi;
    }

    public int getTi() {
        return Ti=Ndi+Nidi;
    }




    public List<codelineI> getLineList(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        List<codelineI> codeLineList = new ArrayList<>();
        int count = 1;
        while (scanner.hasNextLine()) {
            codeLineList.add(new codelineI(count++, scanner.nextLine()));
        }
        return codeLineList;
    }

    private List<codelineI> getMethodList(List<codelineI> codeLineList) {
        List<codelineI> methodList = new ArrayList<>();

        for (codelineI codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("public")) {
                String[] sub = codeLine.getLineContent().split("\\(");
                String x = sub[0].replace("public", "").replace("private", "").replace("protected", "")
                    .replace("static", "").replace("final", "").trim();
                if (x.split(" ").length > 1) {
                    methodList.add(new codelineI(codeLine.getLineNumber(), x.split(" ")[1]));
                } else {
                    methodList.add(new codelineI(codeLine.getLineNumber(), x));
                }
            }
        }

        return methodList;
    }

    private List<codelineI> getSystemMethodList(List<codelineI> codeLineList, List<codelineI> methodList) {
        List<codelineI> systemMethodList = new ArrayList<>();

        for (codelineI codeLine : codeLineList) {
            if (codeLine.getLineContent().contains("(") && codeLine.getLineContent().contains(")")
                && codeLine.getLineContent().contains(";")) {
                boolean status = true;
                String[] sub = codeLine.getLineContent().split("\\(");
                String method = sub[0].trim();

                for (codelineI codeLine_method : methodList) {
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

    private void setEndLineNumber(List<codelineI> codeLineList, List<codelineI> methodList) {
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
                        codelineI method = methodList.get(i);
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
                        codelineI method = methodList.get(i);
                        method.setEndLineNumber(++j);
                        methodList.set(i, method);
                        break;
                    }
                }
            }
        }
    }
 
               
List<InheritanceNode> nodeList = new ArrayList<InheritanceNode>();

List<String> classList = new ArrayList<String>();

List<InheritanceModel> inheritanceModelList = new ArrayList<InheritanceModel>();

public InheritanceComplexity() {
}
private static final InheritanceComplexity obj = new InheritanceComplexity();

public static InheritanceComplexity getInstance() {
    return obj;
}
public void setCode(String code) {
    this.code = code;
}
private void reset() {
    className = "";
    Ndi = 0;
    Nidi = 0;
    Ti = 0;
    Ci = 0;
}

public int[] getWeights() {
int weights[] = {inheritNon, inheritOne, inheritTwo, inheritThree, inheritMore};
return weights;
}

public void setWeights(int inheritNon, int inheritOne, int inheritTwo, int inheritThree, int inheritMore) {
    this.inheritNon = inheritNon;
    this.inheritOne = inheritOne;
    this.inheritTwo = inheritTwo;
    this.inheritThree = inheritThree;
    this.inheritMore = inheritMore;
}
//get directInheritance
public List<InheritanceModel> getComplexity() {
    loadDatatoList(code);
    inheritanceModelList.removeAll(inheritanceModelList);
    count = 0;
    String Lines[] = code.split("\\r?\\n");
		       
    for (String line : Lines) {
	//direct inheritance
        codelineI directInheritancePattern = codelineI.compile(INHERITANCE_CLASS);
	Matcher directInheritanceMatcher = directInheritancePattern.matcher(line);
	while (directInheritanceMatcher.find()) {

            if (isUserDefindClass(directInheritanceMatcher.group().split(" ")[2])) {
		Ndi++;
            }
            //indirect inheritance
    Nidi += getIndirectInheritance(directInheritanceMatcher.group().split(" ")[2]);
}

//find class name
Pattern classNamePattern = Pattern.compile(FIND_CLASS);
Matcher classNameMatcher = classNamePattern.matcher(line);
if (classNameMatcher.find()) {
    className = classNameMatcher.group().replaceFirst("class ", "");
}

    Ti = Ndi + Nidi;

    if (Ti == 1) {
        Ci = inheritOne;
    } else if (Ti == 2) {
        Ci = inheritTwo;
    } else if (Ti == 3) {
        Ci = inheritThree;
    } else if (Ti > 3) {
        Ci = inheritMore;
    } else {
        Ci = inheritNon;
    }

    count++;
    InheritanceModel obj = new InheritanceModel(count, className, Ndi, Nidi, Ti, Ci);
    inheritanceModelList.add(obj);
    reset();
    }

    return inheritanceModelList;
}

private void loadDatatoList(String fileListPath) {

		        
String Lines[] = code.split("\\r?\\n");
    for (String line : Lines) {

	//create node
	Pattern nodePattern = Pattern.compile(INHERITANCE_CLASS);
	Matcher nodeMatcher = nodePattern.matcher(line);
        if (nodeMatcher.find()) {
		InheritanceNode obj = new InheritanceNode();
		obj.setClassName(nodeMatcher.group().split(" ")[0]);
		obj.setSuperClass(nodeMatcher.group().split(" ")[2]);
		obj.setChildClass("");

		nodeList.add(obj);
	}

	//find class name
    Pattern classNamePattern = Pattern.compile(FIND_CLASS);
    Matcher classNameMatcher = classNamePattern.matcher(line);
    if (classNameMatcher.find()) {
		                   
        classList.add(classNameMatcher.group().replaceFirst("class ", ""));
            }
        }
    }
		    

private boolean isUserDefindClass(String className) {

    for (int i = 0; i < classList.size(); i++) {
	if (classList.get(i).equals(className)) {
            return true;
	}
		        
    }
    return false;
}
                    
//getIndirectInheritance                    
private int getIndirectInheritance(String className) {
    int Nidi = 0;
    for (int i = 0; i < nodeList.size(); i++) {
        if (nodeList.get(i).getClassName().equals(className)) {
            if (isUserDefindClass(nodeList.get(i).getSuperClass())) {
		Nidi++;
		getIndirectInheritance(nodeList.get(i).getSuperClass());
            }
        }
    }
     return Nidi;
}

private List<codelineI> getTotalinheritances(List<codelineI> codeLineList, List<codelineI> methodList){
     List<codelineI> Totalinheritances = new ArrayList<>();
     Ti=Ndi+ Nidi;
    return Totalinheritances;
}
}
    	   
 



