package de.fastr.android.multitouch;

import android.app.Activity;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;

/**
 * fastr.de MTfix for Android 2.x Applications
 * Example Activity.
 * 
 * @author Fabian Strachanski
 * @version 0.1
 *
 */
public class TouchActivity extends Activity {
	private WebView wv;		
	/**
	 * is called when the activity starts.
	 * 
	 * @param saveInstanceState see Android Development Guide for details
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 		wv = new WebView(this);
		wv.setOnTouchListener(new MTfixTouchListener(this)); // This is important! 
		//wv.loadUrl(getString(R.string.touchfix_url)); 	// touchfix_url is the URL where your webapp is located
		wv.setHorizontalScrollBarEnabled(false);
     	wv.setVerticalScrollBarEnabled(false);									
		/* 
		 * This WebChromeClient implements the "logging bridge" so you can get 
		 * console.log() Javascript-messages in the Eclipse LogCat window.
		 */
		wv.setWebChromeClient(new WebChromeClient(){	
        	public void onConsoleMessage(String message, int lineNumber, String sourceID) { 
        	    Log.d("fastrConsole", message + " [LINE "
        	                         + lineNumber + "]"
        	                         + sourceID );
        	   
        	  }
        });
		wv.setPictureListener(new PictureListener(){			
		    public void onNewPicture(WebView view, Picture arg1) {
		    	MTfixTouchListener.checkMTfix(wv);		   
		    	//wv.loadUrl("javascript:if(typeof window.fastrMTfix == 'undefined'){alert('No fastr.de MTfix for Android 2.x Javascript-Backend found.');}");
		    	wv.setPictureListener(null);
		    }	
		});
		wv.getSettings().setJavaScriptEnabled(true); // This is important! (enables JS so you can call JS-functions within your Java-Code)
		setContentView(wv);		
	}
}