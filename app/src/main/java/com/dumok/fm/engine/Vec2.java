package com.dumok.fm.engine;
public final class Vec2 {
 public float x,y;
 public Vec2(){this(0,0);} public Vec2(float x,float y){this.x=x;this.y=y;}
 public Vec2 set(float a,float b){x=a;y=b;return this;}
 public float len(){return (float)Math.sqrt(x*x+y*y);}
 public Vec2 norm(){float l=len();if(l>0.00001f){x/=l;y/=l;}return this;}
 public static float dist(Vec2 a,Vec2 b){float x=a.x-b.x,y=a.y-b.y;return (float)Math.sqrt(x*x+y*y);}
}