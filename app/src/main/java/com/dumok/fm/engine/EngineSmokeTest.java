package com.dumok.fm.engine;
public final class EngineSmokeTest {
 public static void main(String[] x){MatchEngine e=new MatchEngine();for(int i=0;i<5000;i++){e.step(1f);if(Float.isNaN(e.ball.pos.x))throw new RuntimeException("NaN");}
 System.out.println("OK "+e.ball.pos.x+" "+e.ball.pos.y);}
}