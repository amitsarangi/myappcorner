package me.amitsarangi.myappcorner.components;

import java.lang.reflect.InvocationTargetException;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.input.gestureAction.dnd.DragAndDropTarget;

import processing.core.PApplet;

public class SceneWindow extends MTRectangle implements DragAndDropTarget{
	
	AbstractMTApplication app;
	
	MTSceneTexture sceneTex;

	public SceneWindow(float x , float y, float w, float h,
			PApplet pApplet) {
		super(pApplet, x, y-80, w, h+80);
		
		this.app = (AbstractMTApplication) pApplet;
		
		//this.setVertices(vertices);
		this.setFillColor(new MTColor(231,76,60));
		this.setStrokeColor(MTColor.BLACK);
		this.setNoStroke(true);
		
		this.setAnchor(PositionAnchor.UPPER_LEFT);
		
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());

		MTImageButton closeButton = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +"closeButton64.png"));
		closeButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					//close();
					destroy();
				}
				return true;
			}
		});
		this.addChild(closeButton);
		closeButton.setNoStroke(true);
		
		closeButton.setPositionRelativeToParent(this.getVerticesGlobal()[1]);
		
	}

	@Override
	public void componentDropped(MTComponent droppedComponent, DragEvent de) {
		// TODO Auto-generated method stub

		SceneInterface si = (SceneInterface)de.getTarget();
		
		this.setNoStroke(true);
		
		try {
			AbstractScene scene = si.getSceneClass().getConstructor(MTApplication.class, String.class).newInstance(new Object[]{(MTApplication)this.app, si.getSceneName() });
			
			if(sceneTex != null)
			{
				sceneTex.getScene().destroy();
				sceneTex.destroy();
			}
			
			sceneTex = new MTSceneTexture(app, 0F, 0F, (int)this.getWidthXY(TransformSpace.GLOBAL), (int)(this.getHeightXY(TransformSpace.GLOBAL)-80), scene);
			sceneTex.setAnchor(PositionAnchor.UPPER_LEFT);
			sceneTex.setNoStroke(true);
			
			sceneTex.scale(this.getWidthXY(TransformSpace.LOCAL)/sceneTex.getWidthXY(TransformSpace.LOCAL), (this.getHeightXY(TransformSpace.LOCAL)-80)/(sceneTex.getHeightXY(TransformSpace.LOCAL)), 1, sceneTex.getCenterPointGlobal());
			Vector3D pos = this.getPosition(TransformSpace.LOCAL);
			pos.setY(pos.getY()+80);
			sceneTex.setPositionRelativeToParent(pos);
			this.addChild(sceneTex);

			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean dndAccept(MTComponent component) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void componentEntered(MTComponent enteredComponent) {
		// TODO Auto-generated method stub
		this.setNoStroke(false);

	}

	@Override
	public void componentExited(MTComponent exitedComponent) {
		// TODO Auto-generated method stub
		this.setNoStroke(true);
		
	}

}
