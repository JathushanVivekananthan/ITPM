/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inheritance;

/**
 *
 * @author Kasthuri
 */
public class InheritanceModel {
    private int count;
	    private String className;
	    private int Ndi;
	    private int Nidi;
	    private int Ti;
	    private int Ci;

	    public InheritanceModel() {
	    }

	    public InheritanceModel(int count, String className, int No_of_direct_inheritances, int No_of_indirect_inheritances, int Total_inheritances, int Ci) {
	        this.count = count;
	        this.className = className;
	        this.Ndi= No_of_direct_inheritances;
	        this.Nidi = No_of_indirect_inheritances;
	        this.Ti = Total_inheritances;
	        this.Ci = Ci;
	    }

	    public int getCount() {
	        return count;
	    }

	    public void setCount(int count) {
	        this.count = count;
	    }

	    public String getClassName() {
	        return className;
	    }

	    public void setClassName(String className) {
	        this.className = className;
	    }

	    public int getNo_of_direct_inheritances() {
	        return Ndi;
	    }

	    public void setNo_of_direct_inheritances(int No_of_direct_inheritances) {
	        this.Ndi = No_of_direct_inheritances;
	    }

	    public int getNo_of_indirect_inheritances() {
	        return Nidi;
	    }

	    public void setNo_of_indirect_inheritances(int No_of_indirect_inheritances) {
	        this.Nidi = No_of_indirect_inheritances;
	    }

	    public int getTotal_inheritances() {
	        return Ti;
	    }

	    public void setTotal_inheritances(int Total_inheritances) {
	        this.Ti = Total_inheritances;
	    }

	    public int getCi() {
	        return Ci;
	    }

	    public void setCi(int Ci) {
	        this.Ci = Ci;
	    }
}
