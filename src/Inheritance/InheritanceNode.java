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
public class InheritanceNode {
    private String className;
	    private String superClass;
	    private String childClass;

	    public InheritanceNode() {
	    }

	    public InheritanceNode(String className, String superClass, String childClass) {
	        this.className = className;
	        this.superClass = superClass;
	        this.childClass = childClass;
	    }

	    public String getClassName() {
	        return className;
	    }

	    public void setClassName(String className) {
	        this.className = className;
	    }

	    public String getSuperClass() {
	        return superClass;
	    }

	    public void setSuperClass(String superClass) {
	        this.superClass = superClass;
	    }

	    public String getChildClass() {
	        return childClass;
	    }

	    public void setChildClass(String childClass) {
	        this.childClass = childClass;
	    }

}
