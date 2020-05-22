package size;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import size.SizeComplexity;
/**
 *
 * @author senthu
 */

public class SizeComplexity {
    private String inputString = "";
    private double lineCount = 0;
       private double sizeComplexity;
    public static double totalSizeComplexity = 0;
    
    private int Nkw;
    private int Nid;
    private int Nop;
    private int Nnv;
    private int Nsl;

    private int[] weights = new int[]{1,1,1,1,1};

    public void doSizeComplexity(File file) throws FileNotFoundException {
        List<codeLine> codeLineList = getLineList(file);
        List<codeLine> keyword = getKeyword(codeLineList);
        List<codeLine> identifier = getidentifier(codeLineList);
//        List<codeLine> operator = getoperator(codeLineList);
        List<codeLine> numerical = getNumericals(codeLineList);


        Nkw =  keyword.size();
        Nid = identifier.size();
        Nop = 0;//operator.size();
        Nnv = numerical.size();
        Nsl = 0;
    }

    public int getCs() {
        return (weights[0] * Nkw) + (weights[1] * Nid) + (weights[2] * Nop) + (weights[3] * Nnv) + (weights[4] * Nsl) ;
    }

    public int getNkw() {
        return Nkw;
    }

    public int getNid() {
        return Nid;
    }

    public int getNop() {
        return Nop;
    }

    public int getNnv() {
        return Nnv;
    }

    public int getNsl() {
        return Nsl;
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
       private int getEndLineNumber(  List<codeLine> codeLineList, codeLine codeLine) {
        int endNumber = 0;
        for (codeLine codeLine1 : codeLineList) {
            if (codeLine1.getLineNumber() == codeLine.getLineNumber()) {
                endNumber = codeLine1.getEndLineNumber();
                break;
            }
        }
        return endNumber;
    }
   
   
   
      public List<codeLine> getKeyword(List<codeLine> codeLineList) {
        List<codeLine> keyword = new ArrayList<>();
        
        for (codeLine codeLine : codeLineList) { 
            
//            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
                    if (codeLine.getLineContent().contains("new") || codeLine.getLineContent().contains("delete")
                    || codeLine.getLineContent().contains("throw") || codeLine.getLineContent().contains("throws")
                    || codeLine.getLineContent().contains("void") || codeLine.getLineContent().contains("println")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    keyword.add(new codeLine(codeLine.getLineNumber(), sub[1]));
                   
                }
            }
        return keyword;
    }
      
      public List<codeLine> getidentifier (List<codeLine> codeLineList) {
        List<codeLine> identifier = new ArrayList<>();
        
        for (codeLine codeLine : codeLineList) { 
            
            
//            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
                    if (codeLine.getLineContent().contains("(") || codeLine.getLineContent().contains(";")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    identifier.add(new codeLine(codeLine.getLineNumber(), sub[1]));
                   
                }
            }
        return identifier;
    }
      
      public List<codeLine> getNumericals (List<codeLine> codeLineList) {
        List<codeLine> numerical = new ArrayList<>();
        
        for (codeLine codeLine : codeLineList) { 
            
            
//            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
                    if (codeLine.getLineContent().contains("1") || codeLine.getLineContent().contains("2")
                        || codeLine.getLineContent().contains("3") || codeLine.getLineContent().contains("4")
                        || codeLine.getLineContent().contains("5") || codeLine.getLineContent().contains("6")
                        || codeLine.getLineContent().contains("7") || codeLine.getLineContent().contains("8")
                        || codeLine.getLineContent().contains("9") || codeLine.getLineContent().contains("0")) {
                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
                    String[] sub = value.split(" ");
                    numerical.add(new codeLine(codeLine.getLineNumber(), sub[1]));
                   
                }
            }
        return numerical;
    }
      
//    public List<codeLine> getStringLiterals (List<codeLine> codeLineList) {
//        List<codeLine> stringliteral = new ArrayList<>();
//        
//        for (codeLine codeLine : codeLineList) { 
//            
//            
////            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
//                    if (codeLine.getLineContent().contains("")) {
//                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
//                    String[] sub = value.split(" ");
//                    stringliteral.add(new codeLine(codeLine.getLineNumber(), sub[1]));
//                   
//                }
//            }
//        return stringliteral;
//    }
            
//     public List<codeLine> getoperator (List<codeLine> codeLineList) {
//        List<codeLine> operator = new ArrayList<>();
//          
//        
//        for (codeLine codeLine : codeLineList) {  
//            
////            for (int i = codeLine.getLineNumber(); i < getEndLineNumber(codeLineList,codeLine); i++)
//                    if (codeLine.getLineContent().contains("+") || codeLine.getLineContent().contains("-")
//                    || codeLine.getLineContent().contains("*") || codeLine.getLineContent().contains("/")
//                    || codeLine.getLineContent().contains("=") || codeLine.getLineContent().contains("++")
//                    || codeLine.getLineContent().contains("--") || codeLine.getLineContent().contains("==")
//                    || codeLine.getLineContent().contains("!=") || codeLine.getLineContent().contains("<") 
//                    || codeLine.getLineContent().contains(">") || codeLine.getLineContent().contains("<=")
//                    || codeLine.getLineContent().contains(">=") || codeLine.getLineContent().contains("&&")
//                    || codeLine.getLineContent().contains("||") || codeLine.getLineContent().contains(",")
//                    || codeLine.getLineContent().contains(".") || codeLine.getLineContent().contains("%")){
//                    String value = codeLine.getLineContent().replace("static", "").replace("public", "").trim();
//                    String[] sub = value.split(" ");
//                    operator.add(new codeLine(codeLine.getLineNumber(), sub[1]));
//                   
//                }
//            }
//        return operator;
//    }
      
}


    

