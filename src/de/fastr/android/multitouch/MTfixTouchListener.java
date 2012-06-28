package de.fastr.android.multitouch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
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
@TargetApi(5)
public class MTfixTouchListener implements OnTouchListener{	
	private Activity activity;
	/**
	 * Constructor - sets the current Activity
	 * @param activity
	 */
	public MTfixTouchListener(Activity activity){
		super();
		this.activity = activity;
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
		return sb.toString();
	}	
	/**
	 * get Screenresolution Object
	 * @return
	 */
    protected DisplayMetrics getDM(){
    	DisplayMetrics dm = new DisplayMetrics();
    	this.activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
		    	wv.loadUrl("javascript:if(typeof window.fastrMTfix == 'undefined'){alert('No fastr.de MTfix for Android 2.x Javascript-Backend found.');}");
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
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        int id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
	 	int pid = event.getPointerId(id);
	 	DisplayMetrics dm = this.getDM();
	 	wv.loadUrl("javascript:window.fastrMTfix.setDisplay("+dm.widthPixels+","+dm.heightPixels+")");
        switch(actionCode){
                case MotionEvent.ACTION_DOWN: //touchstart     
                case MotionEvent.ACTION_POINTER_DOWN:
                   	wv.loadUrl("javascript:window.fastrMTfix.touchstart("+(int)event.getPointerId(pid)+","+(int)event.getX(pid)+","+(int)event.getY(pid)+","+buildTouches(event)+")");
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: //touchend
                  	wv.loadUrl("javascript:window.fastrMTfix.touchend("+(int)event.getPointerId(pid)+","+(int)event.getX(pid)+","+(int)event.getY(pid)+","+buildTouches(event)+")");
                break;
                //TODO: needs revision
                case MotionEvent.ACTION_MOVE: //touchmove
                 	
                   	wv.loadUrl("javascript:window.fastrMTfix.touchmove("+(int)event.getPointerId(pid)+","+(int)event.getX(pid)+","+(int)event.getY(pid)+","+buildTouches(event)+")");
                   	
                break;
        }
        return true;            
    }
}