/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Structure;

/**
 *
 * @author kajan
 */
public class StructureModel {
    
     private int CCs;
	    private int Wtcs;
	    private int NC;
	    private int Ccspps;

     public StructureModel(){
         
     }
    public StructureModel(int CCs, int Wtcs, int NC, int Ccspps) {
        this.CCs = CCs;
        this.Wtcs = Wtcs;
        this.NC = NC;
        this.Ccspps = Ccspps;
    }

    public int getCCs() {
        return CCs;
    }

    public void setCCs(int CCs) {
        this.CCs = CCs;
    }

    public int getWtcs() {
        return Wtcs;
    }

    public void setWtcs(int Wtcs) {
        this.Wtcs = Wtcs;
    }

    public int getNC() {
        return NC;
    }

    public void setNC(int NC) {
        this.NC = NC;
    }

    public int getCcspps() {
        return Ccspps;
    }

    public void setCcspps(int Ccspps) {
        this.Ccspps = Ccspps;
    }
    
            
            
    
}
