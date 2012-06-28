package de.fastr.android.multitouch;

import com.changeit.wmpolyfill.WebClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;

/**
 * fastr.de MTfix for Android 2.x Applications
 * This TouchListener grabs the TouchEvents directly from the phone and sends it via Javascript to your webapp.
 * 
 * @author Fabian Strachanski
 * @version 0.1
 * 
 */
@TargetApi(8)
public class MTfixTouchListener implements OnTouchListener{	
	//private Activity activity;
	private WebClient webclient;
	/** A copy of the last Motion Event */
	private MotionEvent lastMotionEvent = null;

	/**
	 * Constructor - sets the current Activity
	 * @param activity
	 */
	public MTfixTouchListener(Activity activity){
		super();
		//this.activity = activity;
	}
	public MTfixTouchListener(WebClient wc){
		super();
		this.webclient = wc;
		//this.activity = activity;
	}
	/**
	 * builds an json compatible Array String
	 * @param event Event to serialize
	 * @return 
	 */
	private String buildTouches(MotionEvent event){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
	 	for (int i = 0; i < event.getPointerCount(); i++) {
	 		sb.append("{'id':"+event.getPointerId(i)+",'x':"+(int)event.getX(i)+",'y':"+(int)event.getY(i)+"}");
	 		if (i + 1 < event.getPointerCount()){
	 			sb.append(",");
	 		}else{
	 			sb.append("]");
	 		}
	 	}
	 	//Log.d("wmp.console","TOUCHES: "+ sb.toString());
		return sb.toString();
	}	
	/**
	 * get Screenresolution Object
	 * @return
	 */
    protected DisplayMetrics getDM(){
    	DisplayMetrics dm = new DisplayMetrics();
        return dm;
    }
    /**
     * Checks if fastrMTfix.js is installed in the WebView.
     * If not an alert is shown 
     * @param wv
     */
    static public void checkMTfix(WebView wv){
		//wv.setPictureListener(new PictureListener(){		
		//    public void onNewPicture(WebView wv, Picture arg1) {
    	//Now called via WebViewClient.onPageFinished
		    	wv.loadUrl("javascript:if(typeof window.fastrMTfix == 'undefined'){console.log('No fastr.de MTfix for Android 2.x Javascript-Backend found.');}");
		    	//wv.setPictureListener(null);
		//    }	
		//});
    }
    /**
     * The Issue MTfix tries to solve is fixed in Android Version 3+
     * So checkVersion returns false when SDK Version is 11 or above to signal that MTfix is not needed.
     * @return
     */
    static public boolean checkVersion(){
    	if (Build.VERSION.SDK_INT >= 11){
    		return false;
    	}   		
    	return true;
    }
    /**
     * called when the device is touched
     * sends the touches via javascript to the webapp
     */
    public boolean onTouch(View v, MotionEvent event) {
    	
        // TODO Auto-generated method stub
        WebView wv = (WebView) v;
        checkMTfix(wv);
        if (webclient.polyfillAllTouches || 
        	event.getPointerCount() > webclient.maxNativeTouches || 
        	event.getPointerId( event.getActionIndex() ) + 1 > webclient.maxNativeTouches 
        	){
        	int action = event.getAction();
        	int actionCode = action & MotionEvent.ACTION_MASK;
        	int id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        	int pid = event.getPointerId(id);
        	//Log.d("wmp.console",webclient.dumpEvent(event));
        	//DisplayMetrics dm = this.getDM();
        	//wv.loadUrl("javascript:window.fastrMTfix.setDisplay("+dm.widthPixels+","+dm.heightPixels+")");
	        switch(actionCode){
	                case MotionEvent.ACTION_DOWN: //touchstart     
	                case MotionEvent.ACTION_POINTER_DOWN:
	                	wv.loadUrl("javascript:window.fastrMTfix.touchstart("+(int)pid+","+(int)event.getX(pid)+","+(int)event.getY(pid)+","+buildTouches(event)+")");
	                break;
	                case MotionEvent.ACTION_UP:
	                case MotionEvent.ACTION_POINTER_UP: //touchend
	                  	wv.loadUrl("javascript:window.fastrMTfix.touchend("+(int)pid+","+(int)event.getX(pid)+","+(int)event.getY(pid)+","+buildTouches(event)+")");
	                break;	             
	                case MotionEvent.ACTION_MOVE: //touchmove
	                 	if (checkMoved(wv, event)){
	                 		Log.d("wmp.console","Move send");
	                 		wv.loadUrl("javascript:window.fastrMTfix.touchmove("+(int)pid+","+(int)event.getX(pid)+","+(int)event.getY(pid)+","+buildTouches(event)+")");
	                 	}
	                break;
	        }
	        return true;               
        }
        return false;
    }
        
	private boolean checkMoved(View view, MotionEvent event) {
		int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
		if (actionCode == MotionEvent.ACTION_MOVE ) {
			if (lastMotionEvent == null) {
				lastMotionEvent = MotionEvent.obtain(event);
				return true;
			}
			for (int i = 0; i < event.getPointerCount(); i++)
			{
				/* Ignore Events that doesn't move at all */
				
				if ( Math.abs((int)lastMotionEvent.getX(i) - (int)event.getX(i)) < webclient.movePixelTolerance
					&& Math.abs((int)lastMotionEvent.getY(i) - (int)event.getY(i)) < webclient.movePixelTolerance
					// Ignore Events outside of viewport
					|| (int)event.getX(i) > view.getWidth()
					|| (int)event.getY(i) > view.getHeight()){
						continue;	
					}

					lastMotionEvent = MotionEvent.obtain(event);
					return true;
			}
			//lastMotionEvent = null;			
		}	
		return false;
	}
}
