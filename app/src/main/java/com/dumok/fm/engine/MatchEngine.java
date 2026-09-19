package com.dumok.fm.engine;
public final class MatchEngine {
 public final Player[] players=new Player[22]; public final Ball ball=new Ball(); private final TeamAI ai=new TeamAI(); private final ContactSystem contact=new ContactSystem();
 public MatchEngine(){reset();}
 public void reset(){float[][] a={{.06f,.5f},{.18f,.14f},{.18f,.38f},{.18f,.62f},{.18f,.86f},{.39f,.22f},{.39f,.45f},{.39f,.68f},{.62f,.16f},{.62f,.5f},{.62f,.84f}};
  for(int i=0;i<11;i++){players[i]=new Player(i,0,a[i][0],a[i][1]);players[i+11]=new Player(i+11,1,1-a[i][0],1-a[i][1]);}
  ball.pos.set(.5f,.5f);ball.vel.set(0,0);ball.owner=6;
 }
 public void step(float dt){ai.plan(players,ball,0);ai.plan(players,ball,1);avoid();for(Player q:players)q.motor(dt);contact.resolve(players,ball);ball.step(dt);}
 private void avoid(){for(int i=0;i<22;i++)for(int j=i+1;j<22;j++){Player a=players[i],b=players[j];float dx=a.pos.x-b.pos.x,dy=a.pos.y-b.pos.y,d2=dx*dx+dy*dy;if(d2>.000001f&&d2<.00065f){float d=(float)Math.sqrt(d2),push=(.025f-d)*.18f;if(push>0){dx/=d;dy/=d;a.intent.x+=dx*push;a.intent.y+=dy*push;b.intent.x-=dx*push;b.intent.y-=dy*push;}}}}
 public void passTo(int from,int to){if(ball.owner!=from||to<0||to>=22)return;Player a=players[from],r=players[to];float tx=r.pos.x+r.vel.x*14f,ty=r.pos.y+r.vel.y*14f;ball.pos.set(a.pos.x+(float)Math.cos(a.heading)*.012f,a.pos.y+(float)Math.sin(a.heading)*.012f);ball.kick(tx-ball.pos.x,ty-ball.pos.y,.0105f);}
 public void shoot(int from){if(ball.owner!=from)return;Player a=players[from];float gx=a.team==0?.99f:.01f;ball.pos.set(a.pos.x+(float)Math.cos(a.heading)*.012f,a.pos.y+(float)Math.sin(a.heading)*.012f);ball.kick(gx-ball.pos.x,.5f-ball.pos.y,.018f);}
}