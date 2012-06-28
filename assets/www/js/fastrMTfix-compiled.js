var e=null;/*

 This work is licensed under the Creative Commons Attribution 3.0 Unported License. 
 To view a copy of this license, visit http://creativecommons.org/licenses/by/3.0/ 
 or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

 Author: Fabian Strachanski
 Version: 0.1
*/
function f(){this.b=[];this.d=1.03;this.e=1.11}window.fastrMTfix=new f;function g(b,c,d,a,l){var i=window.fastrMTfix.b[0]/window.innerWidth,j=window.fastrMTfix.b[1]/window.innerHeight;this.identifier=b;this.target=e;this.screenX=c/i*window.fastrMTfix.d||e;this.screenY=d/j*window.fastrMTfix.e||e;this.pageX=a/i*window.fastrMTfix.d||e;this.pageY=l/j*window.fastrMTfix.e||e;this.target==e&&(this.target=document.elementFromPoint(this.screenX,this.screenY))}
f.prototype.setDisplay=function(b,c){this.b=[b,c]};function h(b){for(var b=b||[],c,d=0;d<b.length;d++)c=b[d],this.push(new g(c.id,c.x,c.y,c.x,c.y))}h.prototype=[];function k(b,c){for(var d=0;d<b.length;d++)if(b[d].identifier==c)return b[d]}
function m(b,c,d){var a=document.createEvent("Event");a.initEvent(b,!0,!0);a.touches=new h(d);a.targetTouches=new h(d);a.changedTouches=new h([]);a.changedTouches.push(k(a.touches,c));a.altKey=!1;a.metaKey=!1;a.ctrlKey=!1;a.shiftKey=!1;a.c=a.changedTouches[0].target;a.a=function(){this.c?this.c.dispatchEvent(this):document.dispatchEvent(this)};return a}f.prototype.touchstart=function(b,c,d,a){(new m("touchstart",b,a)).a()};f.prototype.touchmove=function(b,c,d,a){(new m("touchmove",b,a)).a()};
f.prototype.touchend=function(b,c,d,a){(new m("touchend",b,a)).a()};
