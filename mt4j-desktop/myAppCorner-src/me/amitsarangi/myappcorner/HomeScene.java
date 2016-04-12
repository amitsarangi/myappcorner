package me.amitsarangi.myappcorner;

import java.util.HashMap;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;

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

		MTRectangle background = new MTRectangle(app,0.0F, 0.0F, MT4jSettings.getInstance().getWindowWidth(),  MT4jSettings.getInstance().getWindowHeight());
		background.setFillColor(MTColor.WHITE);
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
					rec = new MTRectangle(app, de.getDragCursor().getCurrentEvtPosX(), de.getDragCursor().getCurrentEvtPosY(), 1, 1);
					rec.setAnchor(PositionAnchor.UPPER_LEFT);
					rec.unregisterAllInputProcessors();
					rec.setFillColor(new MTColor(100, 150, 250, 55));
					rec.setStrokeColor(new MTColor(0,0,0,255));
					rec.setStrokeWeight(1.5f);
					rec.setDrawSmooth(true);
					rec.setUseDirectGL(true);
					rec.setLineStipple((short)0xBBBB);
					getCanvas().addChild(rec);
					selectionRectangles.put(dragId, rec);
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					rec = selectionRectangles.get(dragId);
					Vertex[] vertices = rec.getVerticesGlobal();
					vertices[1].setXYZ(de.getDragCursor().getCurrentEvtPosX(), vertices[0].y, vertices[1].z);
					vertices[2].setXYZ(de.getDragCursor().getCurrentEvtPosX(), de.getDragCursor().getCurrentEvtPosY(), vertices[2].z);
					vertices[3].setXYZ(vertices[0].x, de.getDragCursor().getCurrentEvtPosY(), vertices[3].z);
					rec.setVertices(vertices);

					break;
				case MTGestureEvent.GESTURE_ENDED:
					rec = selectionRectangles.get(dragId);
					getCanvas().removeChild(rec);
					selectionRectangles.remove(dragId);
					break;
				}

				return false;
			}
		});
		
		this.getCanvas().addChild(background);

	}

}
