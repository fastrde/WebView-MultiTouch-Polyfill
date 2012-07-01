/**
 * fastr.de MTfix for Android 2.x Applications
 *  
 * This JavaScript-Class dispatches events which are 
 * triggerd from the Android-Application TouchListener implementation
 * of this Package.
 * 
 * Author: Fabian Strachanski
 * Version: 0.1
 */


//// for Closure Compiler support uncomment next line
//goog.provide("fastr.android.multitouch"); 

//// for Closure Compiler comment out next 2 lines 


fastr = {};
fastr.android = {};

/**
 * @constructor
 */
fastr.android.multitouch = function(){
  console.log("LOADED");
//	this.touches= new fastr.android.multitouch.TouchList();

}
/**
 *  Datamodel to describe a single touch.
 *  @constructor
 *	@param {number} id Unique identifier for the touch
 *  @param {number} screenX X-coord of touch
 *  @param {number} screenY Y-coord of touch
 *  @param {number} pageX X-coord of touch
 *  @param {number} pageY Y-coord of touch
 *  @param {HTMLElement|null} target
 */
fastr.android.multitouch.Touch = function(id,x,y){ 

	this.identifier     = id;	//long
	this.target 				= null; //EventTarget
    
	this.screenX				= x; //long
	this.screenY				= y; //long
	this.clientX				= x; //long
	this.clientY				= y; //long
	this.pageX					= x; //long
	this.pageY					= y; //long
	
	this.radiusX				= null;
	this.radiusY				= null;
	this.rotaionAngle		= null;
	this.force					= null;

	if (this.target == null) {
		this.target = document.elementFromPoint(this.screenX,this.screenY);
	}
}

/**
 * Sets the resolution for the android display.
 * @param {number} width The width of the device display in pixel.
 * @param {number} height The height of the device display in pixel.
 */ 
fastr.android.multitouch.prototype['setDisplay'] = function(width,height){
	this.androidRes = [width, height];
}

/**
 * Implements the W3C TouchList
 * @constructor
 * @param {Object} data Data to fill in the TouchList
 * @extends Array
 */

fastr.android.multitouch.TouchList = function(data){
	Array.call(this);
	this._data = {};
	data = data || [];
	this._fill(data);
}
fastr.android.multitouch.TouchList.prototype = new Array;
fastr.android.multitouch.TouchList.prototype.constructor = fastr.android.multitouch.TouchList;

/**
 * returns the item at position 'index'
 * @param {number} index
 */
fastr.android.multitouch.TouchList.prototype.item = function(index){
	return this[index];
}

fastr.android.multitouch.TouchList.prototype.add= function(touch){
	this.push(touch);
	this._data[touch.id] = touch;
	this.dbg();
}
fastr.android.multitouch.TouchList.prototype.del= function(touch){
	this.push(touch);
	this._data[touch.id] = touch;
	this.splice(this.indexOf(touch),1);
	delete(this._data[touch.id]);
	this.dbg();
}
fastr.android.multitouch.TouchList.prototype.dbg = function(){
	var str = "ARRAY:  ";
	for (i = 0; i< this.length; i++){
		str = str + "["+i+"](" + this[i].id + this[i].x + this[i].y + ")";
	}
	console.log(str);
	var str = "OBJECT: ";
	for (var i in this._data){
		str = str + "["+i+"](" + this[i].id + this[i].x + this[i].y + ")";
	}
	console.log(str);
}


/**
 * searches for the item with the given identifier 
 * @param identifier
 */
fastr.android.multitouch.TouchList.prototype.identifiedTouch = function(identifier){
	this._data = [];
	for(var i = 0; i < this.length; i++){
		if (this[i].identifier == identifier){
			return this[i];
		}
	}
}
/**
 * fills the array with the given data (internal function)
 * param {Object} data
 */
fastr.android.multitouch.TouchList.prototype._fill = function(data){
	var t;
	for(var i = 0; i < data.length; i++){
		t = data[i];
        //console.log("ttt --- " +t.id + t.x + t.y);
		this.push(new fastr.android.multitouch.Touch(t.id,t.x,t.y,t.x,t.y));
	}
}

/**
 * @constructor
 * @param {string} type the type of the touchevent (eg. touchstart)
 * @param {number} changeid the id of the touch that triggers the event
 * @param {Object} touches the associated touches
 * @return {Object} the generated DOM-Event 
 */
fastr.android.multitouch.TouchEvent = function(type,changedid,touches){
    var str = type + "[";
    for (var i=0; i< touches.length; i++){
        str = str + "("+ touches[i].id + " " + touches[i].x + " " + touches[i].y +")";
    }
    console.log(str+"]");
	var evt = document.createEvent("Event");	
	evt.initEvent(type, true, true);
	evt.touches 					= touches; //new fastr.android.multitouch.TouchList(touches,"Touches");
	evt.targetTouches     = new fastr.android.multitouch.TouchList(touches,"TargetTouches");
	evt.changedTouches    = new fastr.android.multitouch.TouchList(touches,"changedTouches");
	//evt.changedTouches 	= new fastr.android.multitouch.TouchList([],"changedTouches");
	//evt.changedTouches.push(evt.touches.identifiedTouch(changedid));
	evt.altKey					= false;
	evt.metaKey					= false;
	evt.ctrlKey					= false;
	evt.shiftKey				= false;
	evt._target = evt.touches.identifiedTouch(changedid).target;
	evt._target = evt.changedTouches[0].target;
	evt._send = function(){
		if (this._target){
			for (var i = 0; i< this.changedTouches.length; i++){
                this.changedTouches[i].target.dispatchEvent(this);
            }
		}else{
            document.dispatchEvent(this);
		}
	}
	return evt;
}

/**
 * triggers touchstart event
 * @param {number} id the identifier of the touch
 * @param {number} x x-coord of the touch
 * @param {number} y y-coord of the touch
 * @param {Object} data touchevent information send from the phone
 */
fastr.android.multitouch.prototype['touchstart'] = function(id, x, y, data){
	this.touches.add(new fastr.android.multitouch.Touch(id, x, y));
	var evt = new fastr.android.multitouch.TouchEvent("touchstart", id, this.touches);//data);
	evt._send();	
}

/**
 * triggers touchmove event
 * @param {number} id the identifier of the touch
 * @param {number} x x-coord of the touch
 * @param {number} y y-coord of the touch
 * @param {Object} data touchevent information send from the phone
 */
fastr.android.multitouch.prototype['touchmove'] = function(id, x, y, data){
	var evt = new fastr.android.multitouch.TouchEvent("touchmove",id,data);
	evt._send();	
}

/**
 * triggers touchend event
 * @param {number} id the identifier of the touch
 * @param {number} x x-coord of the touch
 * @param {number} y y-coord of the touch
 * @param {Object} data touchevent information send from the phone
 */
fastr.android.multitouch.prototype['touchend'] = function(id, x, y, data){
	this.touches.del(id);
	var evt = new fastr.android.multitouch.TouchEvent("touchend",id,this.touches);
	evt._send();	
}


window['fastrMTfix'] = new fastr.android.multitouch();
window['fastrMTfix'].touches = new fastr.android.multitouch.TouchList();

