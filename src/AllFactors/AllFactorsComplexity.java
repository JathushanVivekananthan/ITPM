/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AllFactors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import size.SizeComplexity;
import size.codeLine;
import size.SizeComplexity; 
/**
 *
 * @author senthu
 */
public class AllFactorsComplexity {
    
    private int Cs;
    private int Cv;
    private int Cm;
    private int Ci;
    private int Ccp;
    private int Ccs;
    private int Tcps;
    
   

    private int[] weights = new int[]{1,1,1,1,1};
    
    
    public void doAllFactorsComplexity(File file) throws FileNotFoundException {
        List<codeLine> codeLineList = getLineList(file);
        
     Cs = 0;   
     Cv = 0;
     Cm = 0;
     Ci = 0;
     Ccp = 0;
     Ccs = 0;
    }
    
    
    public int getTcps() {
        return (Cs) + (Cv) + (Cm) + (Ci) + (Ccp)+ (Ccs)+ (Tcps);
    }

    
    public int getCs() {
        return Cs ;
    }

    public int getCv() {
        return Cv;
    }

    public int getCm() {
        return Cm;
    }

    public int getCi() {
        return Ci;
    }

    public int getCcp() {
        return Ccp;
    }
     public int getCcs() {
        return Ccs;
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
   
}
