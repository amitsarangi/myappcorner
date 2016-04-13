package me.amitsarangi.myappcorner;

import java.util.HashMap;

import me.amitsarangi.myappcorner.components.MenuComponent;
import me.amitsarangi.myappcorner.components.SceneWindow;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;
import org.mt4jx.input.gestureAction.dnd.example.DnDScene;

public class HomeScene extends AbstractScene {

	private MTApplication app;
	
	private HashMap<Integer, MTRectangle> selectionRectangles;
	
	private boolean hasFBO;
	
	public HomeScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		// TODO Auto-generated constructor stub
		
		this.app = (MTApplication) mtApplication;
		this.hasFBO = GLFBO.isSupported(app);
		
		if(!this.hasFBO)
		{
			System.out.println("Your graphics card does not support FBO");
			System.exit(0);
		}
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		this.selectionRectangles = new HashMap<Integer, MTRectangle>();

		MTComponent backgroundLayer = new MTComponent(mtApplication);
		final MTComponent windowLayer = new MTComponent(mtApplication);
		MTComponent menuLayer = new MTComponent(mtApplication);
		
		
		MTRectangle background = new MTRectangle(app,0.0F, 0.0F, MT4jSettings.getInstance().getWindowWidth(),  MT4jSettings.getInstance().getWindowHeight());
		background.setFillColor(new MTColor(236, 240, 241));
		background.setNoStroke(true);
		background.unregisterAllInputProcessors();		
		background.registerInputProcessor(new MultipleDragProcessor(app));
		background.addGestureListener(MultipleDragProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				// TODO Auto-generated method stub
				DragEvent de = (DragEvent)ge;
				int dragId = (int)de.getDragCursor().getId();
				MTRectangle rec;
				switch(de.getId())
				{
				case MTGestureEvent.GESTURE_STARTED:
					rec = new MTRectangle(app, de.getDragCursor().getCurrentEvtPosX()-40, de.getDragCursor().getCurrentEvtPosY()-22.5F, 80,45 );
					rec.setAnchor(PositionAnchor.UPPER_LEFT);
					rec.unregisterAllInputProcessors();
					rec.setFillColor(new MTColor(100, 150, 250, 55));
					rec.setStrokeColor(new MTColor(0,0,0,255));
					rec.setStrokeWeight(1.5f);
					rec.setDrawSmooth(true);
					rec.setUseDirectGL(true);
					rec.setLineStipple((short)0xBBBB);
					windowLayer.addChild(rec);
					selectionRectangles.put(dragId, rec);
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					rec = selectionRectangles.get(dragId);
					Vertex[] vertices = rec.getVerticesGlobal();

					//Making sure that it is only 1920x1080 ratio
					float calcY = vertices[0].y + (de.getDragCursor().getCurrentEvtPosX()-vertices[0].x)*9/16;
					vertices[1].setXYZ(de.getDragCursor().getCurrentEvtPosX(), vertices[0].y, vertices[1].z);
					vertices[2].setXYZ(de.getDragCursor().getCurrentEvtPosX(), calcY, vertices[2].z);
					vertices[3].setXYZ(vertices[0].x, calcY, vertices[3].z);
					
					if(Math.abs(vertices[0].x - de.getDragCursor().getCurrentEvtPosX()) > 80  && Math.abs(vertices[0].x - de.getDragCursor().getCurrentEvtPosX()) > 45)
					{
						rec.setVertices(vertices);
					}
					

					break;
				case MTGestureEvent.GESTURE_ENDED:
					rec = selectionRectangles.get(dragId);
					windowLayer.removeChild(rec);
					selectionRectangles.remove(dragId);
					windowLayer.addChild(new SceneWindow(rec.getPosition(TransformSpace.GLOBAL).x, rec.getPosition(TransformSpace.GLOBAL).y, rec.getWidthXY(TransformSpace.GLOBAL), rec.getHeightXY(TransformSpace.GLOBAL) , app));
					break;
				}

				return false;
			}
		});
		
		this.getCanvas().addChild(backgroundLayer);
		backgroundLayer.addChild(background);
		this.getCanvas().addChild(windowLayer);
		
		this.getCanvas().addChild(menuLayer);
		
		getCanvas().setFrustumCulling(true); 
		


		
		menuLayer.addChild(new MenuComponent(app, app.loadImage(this.getPathToIcons() + "water_s.png"), DnDScene.class, "Drag and drop Example"));
	}
	
	private String getPathToIcons(){
		return  "advanced" + AbstractMTApplication.separator + "mtShell" + AbstractMTApplication.separator + "data"+ AbstractMTApplication.separator + "images" + AbstractMTApplication.separator;
	}


}
