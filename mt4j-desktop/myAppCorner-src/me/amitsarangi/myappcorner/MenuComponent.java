package me.amitsarangi.myappcorner;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.sceneManagement.AbstractScene;

import processing.core.PApplet;
import processing.core.PImage;

public class MenuComponent extends MTRectangle {

	private Class<? extends AbstractScene> sceneClass;
	private PApplet app;
	private PImage texture;
	private String name;
	
	public MenuComponent(PApplet applet, PImage texture, Class<? extends AbstractScene> cls, String name) {
		super(applet, texture);
		
		this.app = applet;
		this.texture = texture;
		this.sceneClass = cls;
		this.name = name;
		
	}

}
