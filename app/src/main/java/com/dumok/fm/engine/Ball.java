package com.dumok.fm.engine;
public final class Ball {
 public final Vec2 pos=new Vec2(.5f,.5f), vel=new Vec2();
 public int owner=-1;
 public void kick(float dx,float dy,float power){float l=(float)Math.sqrt(dx*dx+dy*dy);if(l<.0001f)return;owner=-1;vel.set(dx/l*power,dy/l*power);}
 public void step(float dt){if(owner>=0)return;pos.x+=vel.x*dt;pos.y+=vel.y*dt;float drag=(float)Math.pow(.985,dt);vel.x*=drag;vel.y*=drag;if(vel.len()<.00012f)vel.set(0,0);pos.x=Math.max(.01f,Math.min(.99f,pos.x));pos.y=Math.max(.02f,Math.min(.98f,pos.y));}
}