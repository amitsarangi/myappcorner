package me.amitsarangi.myappcorner.components;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4jx.input.gestureAction.dnd.DragAndDropAction;

import processing.core.PApplet;
import processing.core.PImage;

public class MenuComponent extends MTComponent {

	private PApplet app;
	
	
	private MTRectangle temp;
	
	public MenuComponent(PApplet applet, PImage texture, Class<? extends AbstractScene> cls, String name) {
		super(applet);
		
		
		this.app = applet;
		DraggableRectangle img = new DraggableRectangle(applet, texture, cls, name);
		this.addChild(img);
		img.unregisterAllInputProcessors();
		img.setNoStroke(true);
		
		temp = new MTRectangle(applet, getOpaque(app,texture) );
		temp.unregisterAllInputProcessors();
		temp.setNoStroke(true);

		
		img.registerInputProcessor(new MultipleDragProcessor(app));
		img.addGestureListener(MultipleDragProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				// TODO Auto-generated method stub
				DragEvent de = (DragEvent)ge;
				switch(de.getId())
				{
				case MTGestureEvent.GESTURE_STARTED:
					addChild(temp);
					temp.setPositionGlobal(de.getDragCursor().getPosition());
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					temp.setPositionGlobal(de.getDragCursor().getPosition());
					break;
				case MTGestureEvent.GESTURE_ENDED:
					removeChild(temp);
					break;
				}

				return false;
			}
		});
		img.addGestureListener(MultipleDragProcessor.class, new DragAndDropAction());
		
	}
	
	
	private PImage getOpaque(PApplet pa, PImage image) {
		int width =  image.width; 
		int height = image.height;
		
		PImage copyOfImage = pa.createImage(image.width, image.height, PApplet.ARGB);
		image.loadPixels();
		copyOfImage.loadPixels();
		   
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int index = y*image.width+x;
				copyOfImage.pixels[index] = pa.color(image.pixels[index],127); 
			}
		} 
		copyOfImage.updatePixels();
		return copyOfImage;
	}
	
	public class DraggableRectangle extends MTRectangle implements SceneInterface
	{
		private Class<? extends AbstractScene> sceneClass;
		private String name;

		public DraggableRectangle(PApplet pApplet, PImage t, Class<? extends AbstractScene> cls, String name) {
			super(pApplet,t);
			this.sceneClass = cls;
			this.name = name;
			this.unregisterAllInputProcessors();
			this.setNoStroke(true);
		}
		
		public Class<? extends AbstractScene> getSceneClass()
		{
			return sceneClass;
		}
		
		public String getSceneName()
		{
			return name;
		}

	}
}

