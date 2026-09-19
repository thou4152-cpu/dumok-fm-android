package com.dumok.fm.engine;
public final class TeamAI {
 public void plan(Player[] p,Ball b,int team){
  int lo=team==0?0:11,hi=lo+11; boolean has=b.owner>=lo&&b.owner<hi; int presser=-1;float best=99;
  if(!has){for(int i=lo;i<hi;i++){if(!p[i].active||i%11==0)continue;float d=Vec2.dist(p[i].pos,b.pos);if(d<best){best=d;presser=i;}}}
  for(int i=lo;i<hi;i++){Player q=p[i];if(!q.active)continue;float dir=team==0?1:-1;
   float sx=(b.pos.x-.5f)*.22f,sy=(b.pos.y-.5f)*.16f;q.intent.set(q.anchor.x+sx,q.anchor.y+sy);
   if(has){q.intent.x+=dir*.035f;if(i==b.owner){q.intent.set(q.pos.x,q.pos.y);q.duty=0;}else q.duty=2;}
   else if(i==presser){q.intent.set(b.pos.x-dir*.012f,b.pos.y);q.duty=1;}
   else {q.duty=(q.anchor.x<(team==0?.34f:.66f))?4:3;}
   if(q.anchor.y<.28f)q.intent.y=Math.min(q.intent.y,.34f); if(q.anchor.y>.72f)q.intent.y=Math.max(q.intent.y,.66f);
   q.intent.x=Math.max(.02f,Math.min(.98f,q.intent.x));q.intent.y=Math.max(.03f,Math.min(.97f,q.intent.y));
  }
 }
}