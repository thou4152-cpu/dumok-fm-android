package com.dumok.fm.engine;
public final class ContactSystem {
 public void resolve(Player[] p,Ball b){
  if(b.owner>=0){Player o=p[b.owner];b.pos.set(o.pos.x+(float)Math.cos(o.heading)*.012f,o.pos.y+(float)Math.sin(o.heading)*.012f);return;}
  if(b.vel.len()<.0012f){int best=-1;float bd=.026f;for(Player q:p){if(!q.active)continue;float d=Vec2.dist(q.pos,b.pos);if(d<bd){bd=d;best=q.id;}}if(best>=0)b.owner=best;}
 }
}