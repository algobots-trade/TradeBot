package com.tradebot.dbcommons;

public class FormulaData {

	int id;  
	String t1,t2,t3,t4;  
	double x,y,z,lcount,stopl;  
	public FormulaData(int id, double x,double y,double z,String t1,String t2,String t3,String t4,double lcount,double stopl) {  
	    this.id = id;  
	    this.x = x;  
	    this.y = y;  
	    this.z = z;  
	    this.t1 = t1;  
	    this.t2 = t2;  
	    this.t3 = t3;  
	    this.t4 = t4;  
	    this.lcount = lcount;  
	    this.stopl = stopl;  
	}
	public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public double getLcount() {
        return lcount;
    }
    public double getStopL() {
        return stopl;
    }
    public String getT1() {
        return t1;
    }
    public String getT2() {
        return t2;
    }
    public String getT3() {
        return t3;
    }
    public String getT4() {
        return t4;
    }
   

}
