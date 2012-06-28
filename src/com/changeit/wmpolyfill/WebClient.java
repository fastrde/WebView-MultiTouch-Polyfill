/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.changeit.wmpolyfill;

import de.fastr.android.multitouch.MTfixTouchListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * The idea for this class originates (with many thanks!) from
 * http://stackoverflow.com/questions/2219074/in-android-webview-am-i-able-to-modify-a-webpages-dom
 *
 * @author philzen
 */
@TargetApi(8)
public class WebClient extends WebViewClient {

	public static final String VERSION = "0.2.1";
	//public static final String _WMPJS_ = "(function(){function h(a){var e=a.length,c;for(c=0;c<e;c++)this[c]=a[c];this.length=e}var b=window,g=[],f=null,k=!0,l=function(a){this.clientX=a.clientX;this.clientY=a.clientY;this.pageX=a.pageX;this.pageY=a.pageY;this.screenX=a.screenX;this.screenY=a.screenY;this.identifier=a.identifier?a.identifier:0;this.target=a.target?a.target:b.document.elementFromPoint(this.pageX,this.pageY)};h.prototype.item=function(a){return this[a]};var d={currentTouch:null,knowsTouchAPI:null,mapPolyfillToTouch:{down:\"touchstart\", move:\"touchmove\",up:\"touchend\",cancel:\"touchcancel\"},checkTouchDevice:function(){try{return\"function\"===typeof document.createEvent(\"TouchEvent\").initTouchEvent&&\"function\"===typeof b.document.createTouchList}catch(a){return!1}},checkMouseDevice:function(){try{return document.createEvent(\"MouseEvent\"),!0}catch(a){return!1}},polyfill:function(a){var e=d._getTouchesFromPolyfillData(a);f=e[0];for(action in a)if(\"move\"==action)for(i in e)d._updateTouchMap(e[i]);else\"down\"==action?d._updateTouchMap(f): (\"up\"==action||\"cancel\"==action)&&d._removeFromTouchMap(f);d._raiseTouch(f,d.mapPolyfillToTouch[action]);return!0},nativeTouchListener:function(a){a.isPolyfilled||(f=d._getTouchFromEvent(a.changedTouches[0]),\"touchmove\"==a.type||\"touchstart\"==a.type?d._updateTouchMap(f):(\"touchend\"==a.type||\"touchcancel\"==a.type)&&d._removeFromTouchMap(f))},_raiseTouch:function(a,e){var c=a,j=a.target,d=this.getCleanedTouchMap(e),c=b.document.createEvent(\"Event\");c.initEvent(e,!0,!0,document.body,0);c.changedTouches= new h([f]);c.touches=new h(d);c.targetTouches=new h(this.getTargetTouches(a.target));this._fillUpEventData(c);c.altKey=!1;c.ctrlKey=!1;c.metaKey=!1;c.shiftKey=!1;c.isPolyfilled=!0;j||(j=b.document.elementFromPoint(a.clientX,a.clientY));j?j.dispatchEvent(c):document.dispatchEvent(c)},_getTouchesFromPolyfillData:function(a){var e=[],c,b;for(action in a)if(\"move\"==action)for(c=0;c<a[action].length;c++)for(touchId in a[action][c])b={identifier:parseInt(touchId),clientX:a[action][c][touchId][0],clientY:a[action][c][touchId][1]}, this._fillUpEventData(b),e.push(d._getTouchFromEvent(b));else{b={};if(\"down\"==action)for(touchId in a[action])b.identifier=parseInt(touchId),b.clientX=a[action][touchId][0],b.clientY=a[action][touchId][1];else if(\"up\"==action||\"cancel\"==action)b.identifier=parseInt(a[action]),b.clientX=f.clientX,b.clientY=f.clientY;this._fillUpEventData(b);e.push(d._getTouchFromEvent(b))}return e},_fillUpEventData:function(a){a.target=g[a.identifier]?g[a.identifier].target:b.document.elementFromPoint(a.clientX,a.clientY); a.screenX=a.clientX;a.screenY=a.clientY;a.pageX=a.clientX+b.pageXOffset;a.pageY=a.clientY+b.pageYOffset;return a},_getTouchFromEvent:function(a){return this.knowsTouchAPI?b.document.createTouch(b,a.target,a.identifier?a.identifier:0,a.pageX,a.pageY,a.screenX,a.screenY):new l(a)},getTouchList:function(a){return this.knowsTouchAPI?this._callCreateTouchList(cleanedArray):new h(a)},getCleanedTouchMap:function(){var a,b=[];for(a=0;a<g.length;a++)g[a]&&b.push(g[a]);return b},_updateTouchMap:function(a){g[a.identifier]= a},_removeFromTouchMap:function(a){delete g[a.identifier]},_callCreateTouchList:function(a){switch(a.length){case 1:return b.document.createTouchList(a[0]);case 2:return b.document.createTouchList(a[0],a[1]);case 3:return b.document.createTouchList(a[0],a[1],a[2]);case 4:return b.document.createTouchList(a[0],a[1],a[2],a[3]);case 5:return b.document.createTouchList(a[0],a[1],a[2],a[3],a[4]);default:return b.document.createTouchList()}},getTargetTouches:function(a){var b,c,d=[];for(b=0;b<g.length;b++)(c= g[b])&&c.target==a&&d.push(c);return d},registerNativeTouchListener:function(a){var e=a&&!k?\"removeEventListener\":!a&&k?\"addEventListener\":!1;e&&(b.document[e](\"touchstart\",d.nativeTouchListener,!0),b.document[e](\"touchend\",d.nativeTouchListener,!0),b.document[e](\"touchcancel\",d.nativeTouchListener,!0),b.document[e](\"touchmove\",d.nativeTouchListener,!0));k=a}};d.knowsTouchAPI=d.checkTouchDevice();b.WMP={polyfill:d.polyfill,setPolyfillAllTouches:d.registerNativeTouchListener,Version:\"0.2.1\"}})();";
	//public static final String _WMPJS_ = "var e=null;function f(){this.b=[];this.d=1.03;this.e=1.11}window.fastrMTfix=new f;function g(b,c,d,a,l){var i=window.fastrMTfix.b[0]/window.innerWidth,j=window.fastrMTfix.b[1]/window.innerHeight;this.identifier=b;this.target=e;this.screenX=c/i*window.fastrMTfix.d||e;this.screenY=d/j*window.fastrMTfix.e||e;this.pageX=a/i*window.fastrMTfix.d||e;this.pageY=l/j*window.fastrMTfix.e||e;this.target==e&&(this.target=document.elementFromPoint(this.screenX,this.screenY))};f.prototype.setDisplay=function(b,c){this.b=[b,c]};function h(b){for(var b=b||[],c,d=0;d<b.length;d++)c=b[d],this.push(new g(c.id,c.x,c.y,c.x,c.y))}h.prototype=[];function k(b,c){for(var d=0;d<b.length;d++)if(b[d].identifier==c)return b[d]};function m(b,c,d){var a=document.createEvent(\"Event\");a.initEvent(b,!0,!0);a.touches=new h(d);a.targetTouches=new h(d);a.changedTouches=new h([]);a.changedTouches.push(k(a.touches,c));a.altKey=!1;a.metaKey=!1;a.ctrlKey=!1;a.shiftKey=!1;a.c=a.changedTouches[0].target;a.a=function(){this.c?this.c.dispatchEvent(this):document.dispatchEvent(this)};return a}f.prototype.touchstart=function(b,c,d,a){console.log('START'+b+' '+a);(new m(\"touchstart\",b,a)).a()};f.prototype.touchmove=function(b,c,d,a){(new m(\"touchmove\",b,a)).a()};f.prototype.touchend=function(b,c,d,a){(new m(\"touchend\",b,a)).a()};console.log('TEST');";
	public static String _WMPJS_;
	
	public Boolean polyfillAllTouches = true;

	protected int moveThreshold = 1;

	/** The number of touches already working out-of-the-box (we'll assume at least one for all devices) */
	public int maxNativeTouches = 1;

	/** A copy of the last Motion Event */
	private MotionEvent lastMotionEvent = null;

	/** True after injectWMPJs() was called */
	private boolean isJsInjected = false;

	/** A String to store only the current changed event info  **/
	private StringBuilder movedBuffer;
	private WebView view;

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
//		android.util.Log.v("console", "OVERRIDEURLLOADING to " + url);
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onLoadResource(WebView view, String url) {
//		android.util.Log.v("console", "loadresource_" + url);

		if (url.indexOf(".html") > 0)				// Stop listening to touches when loading a new page,
			view.setOnTouchListener(null);			// as injected functions are not available during load

		isJsInjected = false;
	}

	@Override
	public void onPageFinished(WebView view, String url)
	{
//		android.util.Log.v("console", "pagefinished_" + url);

		if (Build.VERSION.SDK_INT <= 10) {
			this.view = view;
			WebClient._WMPJS_ = deserializeString();
			injectWMPJs();
			movedBuffer = new StringBuilder();
			view.setOnTouchListener(new MTfixTouchListener(this));
			/*View.OnTouchListener() {
				public boolean onTouch(View arg0, MotionEvent arg1) {
					WebView view = (WebView) arg0;
					if (polyfillAllTouches || arg1.getPointerCount() > maxNativeTouches || arg1.getPointerId( arg1.getActionIndex() ) + 1 > maxNativeTouches ) {
						checkMoved(view, arg1);
						if (movedBuffer.length() > 0 || arg1.getAction() != MotionEvent.ACTION_MOVE) {
							String EventJSON = getEvent(arg1);
							view.loadUrl("javascript: WMP.polyfill(" + EventJSON + ");");

//							android.util.Log.d("debug-console", EventJSON);
						}
						return true;
					}

					/**
					* FALSE : let other handlers do their work (good if we want to test for already working touchevents)
					* TRUE : stop propagating / bubbling event to other handlers (good if we don't want selection or zoom handlers to happen in webview)
					
					return false;
				}
			});*/
		}
	}

	/**
	 * Add a JSON representation of the pointer information to the JSON Move Buffer
	 * @param event A motion Event
	 * @return
	 */
	private void addMoveToBuffer(MotionEvent event, int pointerIndex) {

		if (movedBuffer.length() > 0) {
			movedBuffer.append(",");
		}

		StringBuilder sb = new StringBuilder();
			sb.append("{").append(event.getPointerId(pointerIndex))
					.append(":[")
					.append((int)event.getX(pointerIndex)).append(",")
					.append((int) event.getY(pointerIndex))
					.append("]")
					.append("}");
		movedBuffer.append(sb.toString());
	}

	private void addAllMovesToBuffer(MotionEvent event) {

		for (int i=0; i < event.getPointerCount(); i++) {
			addMoveToBuffer(event, i);
		}
	}

	private String getEvent(MotionEvent event) {

		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		//sb.append("code").append( actionCode );
		if (actionCode == MotionEvent.ACTION_MOVE) {
			sb.append("{move:[").append(movedBuffer).append("]}");
		} else if (actionCode == MotionEvent.ACTION_POINTER_DOWN
			|| actionCode == MotionEvent.ACTION_DOWN) {
			sb.append("{down:").append(movedBuffer).append("}");
		} else if (actionCode == MotionEvent.ACTION_POINTER_UP
			|| actionCode == MotionEvent.ACTION_UP) {
			sb.append("{up:").append(event.getPointerId(event.getActionIndex())).append("}");
		} else if (actionCode == MotionEvent.ACTION_CANCEL) {
			sb.append("{cancel:").append(event.getPointerId(event.getActionIndex()));
		}

		return sb.toString();
	}

	public boolean getPolyfillAllTouches() {
		return polyfillAllTouches;
	}

	/**
	 * @param polyfillAllTouches If set to TRUE, all touch events will be stopped and replaced by polyfills
	 * @return WebClient (Fluent Interface)
	 */
	public WebClient setPolyfillAllTouches(boolean polyfillAllTouches)  {
		this.polyfillAllTouches = polyfillAllTouches;
		if (isJsInjected)
			this.view.loadUrl("javascript:" + getCurrentSettingsInjectionJs());
		return this;
	}

	private void injectWMPJs()
	{
		StringBuilder wmpJs = new StringBuilder();
		wmpJs.append("javascript: if (!window.WMP || WMP.Version != '" )
				.append( WebClient.VERSION)
				.append("')")
				.append(_WMPJS_)
				.append( getCurrentSettingsInjectionJs() );

		view.loadUrl(wmpJs.toString());
//		android.util.Log.v("console", "injecting: WMP-Script (plus: '" + getCurrentSettingsInjectionJs() + "')");
		isJsInjected = true;
	}

	private String getCurrentSettingsInjectionJs() {

		if (polyfillAllTouches != true || isJsInjected) // only needed if not true (default) or if setting was changed after initialisation
		{
			StringBuilder wmpJs = new StringBuilder();
			wmpJs.append("window.WMP.setPolyfillAllTouches( ")
					.append( polyfillAllTouches.toString() )
					.append(" );");
			return wmpJs.toString();
		}

		return "";
	}


	/**
	 * Taken with a lot of appreciation from
	 * http://www.zdnet.com/blog/burnette/how-to-use-multi-touch-in-android-2-part-3-understanding-touch-events/1775
	 *
	 * @param event
	 * @return String Some information on the MotionEvent
	 */
	public String dumpEvent(MotionEvent event) {

		String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
			"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;

		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP
				|| actionCode == MotionEvent.ACTION_DOWN
				|| actionCode == MotionEvent.ACTION_UP) {
			sb.append("FINGER ").append(
					(action >> MotionEvent.ACTION_POINTER_ID_SHIFT) + 1);
			sb.append(": ");
		}

		sb.append("ACTION_").append(names[actionCode]);
		sb.append(" [");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid_").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount()) {
				sb.append("; ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String deserializeString(){
		try{
		  int len;
	      char[] chr = new char[4096];
	      final StringBuffer buffer = new StringBuffer();
	      
	      final BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.assetmgr.open("www/fastrMTfix.js") ));
	      try {
	          while ((len = reader.read(chr)) > 0) {
	              buffer.append(chr, 0, len);
	              Log.d("wmp.console",String.valueOf(chr));
	          }
	      } finally {
	          reader.close();
	          Log.d("wmp.console","CLOSED");
	      }
	      //Log.d("wmp.console",buffer.toString());
	      return buffer.toString();
		}catch(IOException e){
			Log.d("wmp.console",":..",e);
			return "";	
		}
		
	}
  

}
