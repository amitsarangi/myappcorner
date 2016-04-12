package me.amitsarangi.myappcorner;

import org.mt4j.MTApplication;

public class startMyAppCorner extends MTApplication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){
		initialize();
	}
	
	//@Override
	public void startUp(){
		this.addScene(new HomeScene(this, "Enter Scene"));
	}

}
