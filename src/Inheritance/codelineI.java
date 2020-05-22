/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inheritance;

import java.util.regex.Matcher;

/**
 *
 * @author Kasthuri
 */
public class codelineI {
    static codelineI compile(String INHERITANCE_CLASS) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private int lineNumber;
    private String lineContent;
    private int endLineNumber;

    public codelineI(int lineNumber, String lineContent) {
        this.lineNumber = lineNumber;
        this.lineContent = lineContent;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineContent() {
        return lineContent;
    }

    public void setLineContent(String lineContent) {
        this.lineContent = lineContent;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public void setEndLineNumber(int endLineNumber) {
        this.endLineNumber = endLineNumber;
    }

    Matcher matcher(String line) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
