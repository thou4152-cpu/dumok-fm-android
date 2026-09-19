package com.dumok.fm.engine;
public final class Player {
 public final int id,team; public final Vec2 pos=new Vec2(),vel=new Vec2(),anchor=new Vec2(),intent=new Vec2();
 public float heading,pace=.0022f,accel=.000075f,turn=.055f; public int duty=0; public boolean active=true;
 public Player(int id,int team,float x,float y){this.id=id;this.team=team;pos.set(x,y);anchor.set(x,y);intent.set(x,y);}
 public void motor(float dt){
  float dx=intent.x-pos.x,dy=intent.y-pos.y,d=(float)Math.sqrt(dx*dx+dy*dy);
  if(d<.006f){vel.x*=.82f;vel.y*=.82f;pos.x+=vel.x*dt;pos.y+=vel.y*dt;return;}
  float wanted=(float)Math.atan2(dy,dx),da=wanted-heading;while(da>Math.PI)da-=6.283185f;while(da<-Math.PI)da+=6.283185f;
  float speed=vel.len(),maxTurn=turn/(1f+speed*420f);heading+=Math.max(-maxTurn,Math.min(maxTurn,da));
  float target=pace*(Math.abs(da)>.9f?.38f:1f);speed+=(target-speed)*Math.min(1f,accel*dt*9500f);
  vel.set((float)Math.cos(heading)*speed,(float)Math.sin(heading)*speed);
  pos.x=Math.max(.01f,Math.min(.99f,pos.x+vel.x*dt));pos.y=Math.max(.02f,Math.min(.98f,pos.y+vel.y*dt));
 }
}