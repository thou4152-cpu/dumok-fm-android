package com.dumok.fm;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.time.*;
import java.util.*;

public class MainActivity extends Activity {
 static final String[] TECH={"골결정력","드리블","퍼스트터치","패스","크로스","헤딩","태클","중거리슛"};
 static final String[] MENT={"침착성","판단력","시야","오프더볼","위치선정","집중력","활동량","적극성"};
 static final String[] PHYS={"주력","가속도","민첩성","균형감각","몸싸움","점프력","지구력","타고난체력"};
 static final String[] POS={"GK","RB","CB","LB","DM","CM","AM","RW","LW","ST"};
 static class Player implements java.io.Serializable{
  String name,nation,pos,club,body,nick="",preferredFoot="오른발"; int weakFoot=8,age,height,weight,ca,pa,fitness=100,apps,goals,value,wage;
  int[] te=new int[8],me=new int[8],ph=new int[8]; double[] xp=new double[24];
  double growth,professional,physicalRetention; int declineAge; boolean fa,starter;
 }
 static class Team implements java.io.Serializable{String name;int p,w,d,l,gf,ga,pts;Team(String n){name=n;}}
 static class Fixture implements java.io.Serializable{LocalDate date;Team h,a;boolean played;int hg,ag;Fixture(LocalDate d,Team x,Team y){date=d;h=x;a=y;}}
 ArrayList<Player>squad=new ArrayList<>(),pool=new ArrayList<>();ArrayList<Team>teams=new ArrayList<>();ArrayList<Fixture>fixtures=new ArrayList<>();
 Random R=new Random(4152); LocalDate now=LocalDate.of(2026,7,1); String club="DUMOK FC",formation="4-2-3-1",mentality="균형";int budget=650;
 int BG=Color.rgb(8,15,27),CARD=Color.rgb(18,29,45),GREEN=Color.rgb(48,211,145),MUTED=Color.rgb(150,166,186);
 String[] SLOT={"GK","LB","LCB","CB","RCB","RB","LDM","DM","RDM","LCM","CM","RCM","LAM","AM","RAM","LW","LR","ST","RS","RW"};
 Player[] XI=new Player[SLOT.length];
 String[] XIROLE=new String[SLOT.length], XIINST=new String[SLOT.length];
 LinearLayout main,side,body;

 String[][] NM={
  {"대한민국","김","이","박","최","정","강","조","윤","민준","서준","도윤","지훈","현우","재민"},
  {"브라질","Lucas","Gabriel","Matheus","Rafael","Joao","Silva","Costa","Santos","Oliveira","Pereira"},
  {"아르헨티나","Mateo","Thiago","Julian","Nicolas","Lautaro","Gomez","Romero","Alvarez","Fernandez","Diaz"},
  {"잉글랜드","James","Oliver","George","Harry","Jack","Smith","Brown","Wilson","Taylor","Walker"},
  {"스페인","Alejandro","Pablo","Diego","Javier","Carlos","Garcia","Ruiz","Torres","Navarro","Santos"},
  {"프랑스","Hugo","Theo","Lucas","Enzo","Mathis","Martin","Dubois","Bernard","Laurent","Petit"},
  {"독일","Leon","Jonas","Felix","Lukas","Max","Muller","Schmidt","Fischer","Weber","Wagner"},
  {"이탈리아","Luca","Marco","Matteo","Andrea","Davide","Rossi","Romano","Ricci","Conti","Moretti"},
  {"일본","Haruto","Ren","Takumi","Kaito","Riku","Sato","Suzuki","Takahashi","Tanaka","Watanabe"},
  {"포르투갈","Joao","Tiago","Diogo","Rui","Andre","Silva","Santos","Costa","Pereira","Fernandes"},
  {"네덜란드","Daan","Sem","Luuk","Finn","Jesse","De Jong","Van Dijk","Visser","Smit","Bakker"},
  {"나이지리아","Chinedu","Victor","Samuel","Ibrahim","Emeka","Okafor","Balogun","Musa","Adebayo","Eze"}
 };

 public void onCreate(Bundle b){super.onCreate(b);getWindow().setNavigationBarColor(BG);getWindow().setStatusBarColor(BG);startMenu();}
 int dp(int x){return(int)(x*getResources().getDisplayMetrics().density+.5f);}
 TextView tx(String s,int z,int c){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(c);v.setPadding(dp(10),dp(7),dp(10),dp(7));return v;}
 Button bt(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(12);return b;}
 LinearLayout card(){LinearLayout x=new LinearLayout(this);x.setOrientation(LinearLayout.VERTICAL);x.setPadding(dp(8),dp(6),dp(8),dp(6));x.setBackgroundColor(CARD);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(dp(6),dp(5),dp(6),dp(5));x.setLayoutParams(p);return x;}

 void startMenu(){
  LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setGravity(Gravity.CENTER);root.setPadding(dp(45),dp(25),dp(45),dp(25));root.setBackgroundColor(BG);
  TextView logo=tx("DUMOK FM",32,GREEN);logo.setGravity(Gravity.CENTER);root.addView(logo,new LinearLayout.LayoutParams(-1,dp(70)));
  TextView sub=tx("감독 커리어",15,MUTED);sub.setGravity(Gravity.CENTER);root.addView(sub,new LinearLayout.LayoutParams(-1,dp(45)));
  boolean has=new java.io.File(getFilesDir(),"dumok_career.sav").exists();
  Button cont=bt("이어하기"+(has?"":"  · 저장 데이터 없음"));cont.setEnabled(has);cont.setOnClickListener(v->{java.io.File sf=new java.io.File(getFilesDir(),"dumok_career.sav");if(sf.exists()){loadGame();if(!squad.isEmpty())home();}});
  root.addView(cont,new LinearLayout.LayoutParams(dp(380),dp(65)));
  Button fresh=bt("새 게임 시작");fresh.setOnClickListener(v->confirmNewGame());
  LinearLayout.LayoutParams fp=new LinearLayout.LayoutParams(dp(380),dp(65));fp.setMargins(0,dp(14),0,0);root.addView(fresh,fp);
  root.addView(tx("이어하기: 마지막 진행상황 불러오기\n새 게임: 기존 저장을 초기화하고 처음부터 시작",12,MUTED));
  setContentView(root);
 }
 void confirmNewGame(){
  new AlertDialog.Builder(this).setTitle("새 게임 시작").setMessage("기존 진행 데이터가 있다면 새 게임으로 덮어씁니다. 시작할까요?")
   .setNegativeButton("취소",null).setPositiveButton("시작",(d,w)->newGame()).show();
 }
 void newGame(){
  // A new career is a true reset: delete the complete previous save first.
  java.io.File f=new java.io.File(getFilesDir(),"dumok_career.sav");if(f.exists())f.delete();
  getSharedPreferences("dumok_save",0).edit().clear().apply();
  squad.clear();pool.clear();teams.clear();fixtures.clear();for(int i=0;i<XI.length;i++)XI[i]=null;
  now=LocalDate.of(2026,7,1);budget=650;formation="4-2-3-1";mentality="균형";
  seed();saveGame();home();
 }
 static class CareerSave implements java.io.Serializable{
  ArrayList<Player>squad,pool;ArrayList<Team>teams;ArrayList<Fixture>fixtures;
  String date,club,formation,mentality;int budget;int[] xiIndex;
 }
 void saveGame(){
  if(squad.isEmpty())return;
  CareerSave c=new CareerSave();c.squad=new ArrayList<>(squad);c.pool=new ArrayList<>(pool);c.teams=new ArrayList<>(teams);c.fixtures=new ArrayList<>(fixtures);
  c.date=now.toString();c.club=club;c.formation=formation;c.mentality=mentality;c.budget=budget;c.xiIndex=new int[XI.length];
  java.util.Arrays.fill(c.xiIndex,-1);
  for(int i=0;i<XI.length;i++)if(XI[i]!=null)c.xiIndex[i]=squad.indexOf(XI[i]);
  java.io.File tmp=new java.io.File(getFilesDir(),"dumok_career.tmp"),dst=new java.io.File(getFilesDir(),"dumok_career.sav");
  try(java.io.ObjectOutputStream o=new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream(tmp)))){o.writeObject(c);}
  catch(Exception ex){return;}
  if(dst.exists())dst.delete();tmp.renameTo(dst);
  getSharedPreferences("dumok_save",0).edit().putBoolean("hasSave",true).apply();
 }
 void loadGame(){
  java.io.File f=new java.io.File(getFilesDir(),"dumok_career.sav");
  try(java.io.ObjectInputStream in=new java.io.ObjectInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(f)))){
   CareerSave c=(CareerSave)in.readObject();
   squad.clear();squad.addAll(c.squad);pool.clear();pool.addAll(c.pool);teams.clear();teams.addAll(c.teams);fixtures.clear();fixtures.addAll(c.fixtures);
   now=LocalDate.parse(c.date);club=c.club;formation=c.formation;mentality=c.mentality;budget=c.budget;
   for(int i=0;i<XI.length;i++)XI[i]=null;
   if(c.xiIndex!=null)for(int i=0;i<Math.min(XI.length,c.xiIndex.length);i++){int k=c.xiIndex[i];if(k>=0&&k<squad.size())XI[i]=squad.get(k);}
   for(Player q:squad)q.starter=false;for(Player q:XI)if(q!=null)q.starter=true;
  }catch(Exception ex){
   // Corrupt/incompatible save: keep it untouched and start no fake continuation.
   Toast.makeText(this,"세이브 파일을 불러오지 못했습니다.",Toast.LENGTH_LONG).show();startMenu();
  }
 }
 @Override
 protected void onStop(){
  super.onStop();
  if(!squad.isEmpty())saveGame();
 }

 void seed(){
  String[] ns={"DUMOK FC","JEONJU UNITED","SEOUL CITY","BUSAN ATHLETIC","INCHEON BLUE","DAEGU REDS","SUWON KNIGHTS","DAEJEON PHOENIX"};
  for(String n:ns)teams.add(new Team(n));
  String[] guaranteed={"GK","LB","CB","CB","RB","DM","CM","AM","LW","ST","RW"};
  for(String gp:guaranteed){Player p=gen(false,club,true);p.pos=gp;p.starter=true;squad.add(p);}
  for(int i=11;i<25;i++){Player p=gen(false,club,true);p.starter=false;squad.add(p);}
  // Default shape only for first launch; user can freely rebuild it on the tactics board.
  int[] ds={0,1,2,4,5,7,10,13,15,17,19};
  for(int i=0;i<11&&i<squad.size();i++)XI[ds[i]]=squad.get(i);
  for(int i=0;i<3500;i++){boolean fa=R.nextInt(100)<18;pool.add(gen(fa,fa?"FA":ns[1+R.nextInt(ns.length-1)],false));}
  schedule();
 }
 Player gen(boolean fa,String owner,boolean first){
  Player p=new Player();String[] n=NM[R.nextInt(NM.length)];p.nation=n[0];
  if(p.nation.equals("대한민국"))p.name=n[1+R.nextInt(8)]+n[9+R.nextInt(n.length-9)];
  else p.name=n[1+R.nextInt(5)]+" "+n[6+R.nextInt(n.length-6)];
  p.pos=POS[R.nextInt(POS.length)];p.age=16+R.nextInt(30);p.fa=fa;p.club=owner;
  int baseH=p.pos.equals("GK")||p.pos.equals("CB")||p.pos.equals("ST")?180:173;
  p.height=Math.max(158,Math.min(205,baseH+(int)Math.round(R.nextGaussian()*8)));
  double bmi=20.5+R.nextDouble()*4.8;p.weight=(int)Math.round(bmi*p.height*p.height/10000.0);
  double bmi2=p.weight*10000.0/(p.height*p.height);p.body=bmi2<21.5?"마름":bmi2>24.0?"건장":"보통";
  p.growth=.65+R.nextDouble()*.8;p.professional=.65+R.nextDouble()*.7;p.physicalRetention=.65+R.nextDouble()*.7;p.declineAge=29+R.nextInt(7);
  int quality=first?11+R.nextInt(5):6+(int)(12*Math.pow(R.nextDouble(),1.7));
  // Position template first, small individual variance second: identity stays football-realistic.
  for(int i=0;i<8;i++){p.te[i]=clamp(quality-2+R.nextInt(5));p.me[i]=clamp(quality-2+R.nextInt(5));p.ph[i]=clamp(quality-2+R.nextInt(5));}
  applyPositionTemplate(p,quality);shapePlayer(p);
  p.preferredFoot=R.nextInt(100)<24?"왼발":"오른발";
  // Most players are clearly one-footed; true two-footed players are uncommon.
  int fr=R.nextInt(100);p.weakFoot=fr<4?18+R.nextInt(3):fr<14?15+R.nextInt(3):fr<38?11+R.nextInt(4):5+R.nextInt(6);
  p.ca=calcCA(p);int room=p.age<=20?25+R.nextInt(65):8+R.nextInt(45);p.pa=Math.min(200,Math.max(p.ca,p.ca+room));
  p.value=Math.max(1,(p.ca*p.ca)/80+Math.max(0,p.pa-p.ca)*2);p.wage=Math.max(1,p.ca/4);p.nick=nickname(p);return p;
 }
 int pv(int q,int bonus){return clamp(q+bonus-1+R.nextInt(3));}
 void setT(Player p,int q,int...b){for(int i=0;i<8;i++)p.te[i]=pv(q,b[i]);}
 void setM(Player p,int q,int...b){for(int i=0;i<8;i++)p.me[i]=pv(q,b[i]);}
 void setP(Player p,int q,int...b){for(int i=0;i<8;i++)p.ph[i]=pv(q,b[i]);}
 void applyPositionTemplate(Player p,int q){
  // te: finishing, dribbling, first touch, passing, crossing, heading, tackling, technique
  // me: composure, decisions, vision, off-ball, positioning, concentration, work rate, teamwork
  // ph: pace, acceleration, agility, balance, strength, jumping, stamina, natural fitness
  if(p.pos.equals("GK")){
   setT(p,q,-4,-4,-1,0,-3,-1,-2,-1);setM(p,q,2,2,0,-4,3,3,0,2);setP(p,q,-1,-1,0,1,2,2,0,1);
  }else if(p.pos.equals("CB")){
   setT(p,q,-4,-3,-1,0,-2,3,4,-1);setM(p,q,1,2,-1,-2,4,3,1,2);setP(p,q,0,0,-1,1,4,3,1,1);
  }else if(p.pos.equals("LB")||p.pos.equals("RB")){
   setT(p,q,-2,1,1,2,3,-2,2,1);setM(p,q,0,2,1,1,2,1,3,2);setP(p,q,3,3,2,1,0,-1,4,2);
  }else if(p.pos.equals("DM")){
   setT(p,q,-3,-1,1,3,-1,0,3,1);setM(p,q,1,3,2,-1,4,3,3,3);setP(p,q,0,0,0,2,2,1,3,2);
  }else if(p.pos.equals("CM")){
   setT(p,q,-1,1,2,4,0,-2,1,2);setM(p,q,1,4,4,1,1,2,4,3);setP(p,q,0,0,1,1,0,-2,4,2);
  }else if(p.pos.equals("AM")){
   setT(p,q,2,3,4,4,1,-3,-3,3);setM(p,q,2,3,4,3,-2,0,1,1);setP(p,q,1,2,3,2,-2,-3,1,0);
  }else if(p.pos.equals("LW")||p.pos.equals("RW")){
   setT(p,q,1,4,3,1,3,-3,-4,3);setM(p,q,1,2,2,3,-3,0,1,0);setP(p,q,4,4,4,2,-2,-3,2,1);
  }else{ // ST
   setT(p,q,4,2,3,0,-3,1,-5,2);setM(p,q,3,2,0,4,-4,0,0,0);setP(p,q,3,4,2,2,1,0,1,1);
  }
 }
 int clamp(int x){return Math.max(1,Math.min(20,x));}
 void add(int[]a,int i,int n){a[i]=clamp(a[i]+n);}
 void shapePlayer(Player p){
  if(p.pos.equals("ST")){
   if(p.height>=188){add(p.te,5,3);add(p.ph,4,3);add(p.ph,5,3);if(R.nextInt(100)<20){add(p.ph,0,4);add(p.ph,1,3);add(p.me,3,3);}} // rare Haaland-like
   else if(p.height<=175){add(p.te,1,3);add(p.te,2,3);add(p.ph,1,3);add(p.ph,2,4);add(p.ph,3,3);add(p.me,3,3);}
   add(p.te,0,3);add(p.me,0,2);add(p.me,3,3);
  }else if(p.pos.equals("CB")){add(p.te,6,4);add(p.me,4,4);add(p.me,5,3);add(p.ph,4,3);add(p.ph,5,3);}
  else if(p.pos.equals("CM")||p.pos.equals("AM")){add(p.te,3,4);add(p.te,2,3);add(p.me,1,3);add(p.me,2,4);}
  else if(p.pos.equals("RW")||p.pos.equals("LW")){add(p.te,1,4);add(p.ph,0,3);add(p.ph,1,4);add(p.ph,2,3);}
 }
 double cost(int a){if(a<=5)return a*.8;if(a<=10)return 4+(a-5)*1.25;if(a<=14)return 10.25+(a-10)*1.8;if(a<=17)return 17.45+(a-14)*2.7;if(a<=19)return 25.55+(a-17)*4.2;return 35.0;}
 double w(Player p,int cat,int i){
  double v=cat==2?1.28:cat==0?1.0:.86; // physical importance intentionally higher
  if(p.pos.equals("ST")){
   if(cat==0){double[]q={1.45,1.0,1.05,.65,.55,1.0,.08,.9};v*=q[i];}
   if(cat==1){double[]q={1.15,.85,.55,1.35,.08,.55,.55,.65};v*=q[i];}
   if(cat==2){double[]q={1.45,1.5,1.15,1.0,1.15,1.0,.8,.75};v*=q[i];}
  }else if(p.pos.equals("CB")){
   if(cat==0){double[]q={.05,.3,.7,.65,.45,1.15,1.55,.1};v*=q[i];}
   if(cat==1){double[]q={.65,1.0,.65,.25,1.55,1.4,.65,1.0};v*=q[i];}
   if(cat==2){double[]q={1.0,1.0,.8,1.0,1.4,1.35,.9,.8};v*=q[i];}
  }else if(p.pos.equals("CM")||p.pos.equals("AM")){
   if(cat==0){double[]q={.55,1.0,1.25,1.5,.75,.3,.65,.85};v*=q[i];}
   if(cat==1){double[]q={.9,1.3,1.45,.95,.7,.9,1.0,.65};v*=q[i];}
  }else if(p.pos.equals("LW")||p.pos.equals("RW")){
   if(cat==0){double[]q={.9,1.45,1.15,.75,1.2,.15,.05,1.0};v*=q[i];}
   if(cat==1){double[]q={.8,1.0,.85,1.2,.2,.65,.8,.65};v*=q[i];}
   if(cat==2){double[]q={1.5,1.55,1.35,1.0,.55,.35,1.0,.9};v*=q[i];}
  }else if(p.pos.equals("LB")||p.pos.equals("RB")){
   if(cat==0){double[]q={.15,.8,.85,1.0,1.3,.25,1.1,.75};v*=q[i];}
   if(cat==1){double[]q={.65,1.05,.7,.7,1.15,.9,1.2,1.05};v*=q[i];}
   if(cat==2){double[]q={1.3,1.35,1.0,.9,.8,.65,1.45,1.0};v*=q[i];}
  }else if(p.pos.equals("DM")){
   if(cat==0){double[]q={.1,.55,.85,1.25,.4,.55,1.3,.8};v*=q[i];}
   if(cat==1){double[]q={.8,1.3,1.05,.4,1.45,1.2,1.2,1.25};v*=q[i];}
   if(cat==2){double[]q={.8,.8,.75,1.0,1.05,.8,1.2,1.0};v*=q[i];}
  }
  return v;
 }
 int calcCA(Player p){
  double pts=0,max=0;
  for(int c=0;c<3;c++)for(int i=0;i<8;i++){double ww=w(p,c,i);pts+=cost(c==0?p.te[i]:c==1?p.me[i]:p.ph[i])*ww;max+=cost(20)*ww;}
  double attrCA=200*pts/max;
  // Weak-foot proficiency consumes CA budget non-linearly:
  // ordinary weak foot is cheap, 15+ is costly, 18-20 (near two-footed) is a premium trait.
  double wf=Math.max(1,Math.min(20,p.weakFoot));
  double footCost=wf<=8?0:Math.pow((wf-8)/12.0,1.65)*18.0;
  return Math.max(1,Math.min(200,(int)Math.round(attrCA+footCost)));
 }
 String footLabel(Player p){if(p.weakFoot>=18)return "거의 양발";if(p.weakFoot>=15)return "양발 활용 우수";if(p.weakFoot>=11)return "반대발 준수";if(p.weakFoot>=7)return "주발 선호";return "강한 주발 의존";}
 String nickname(Player p){
  double target=p.height*.18+p.te[5]*2+p.ph[4]*1.8+p.ph[5]*2;
  double run=p.ph[0]*2+p.ph[1]*2.2+p.me[3]*2+p.te[0];
  double link=p.te[2]*1.7+p.te[3]*2+p.me[2]*1.8+p.me[1]*1.6;
  double magic=p.te[1]*2.2+p.te[2]*1.8+p.ph[2]*2+p.ph[3]*1.6+p.me[2]*1.5;
  if(p.pos.equals("ST")&&target>105&&run>90)return "괴물형 침투 공격수";
  if(p.pos.equals("ST")&&target>108&&link>82)return "연계형 타깃맨";
  if(p.pos.equals("ST")&&target>108)return "타깃형 공격수";
  if(p.height<176&&magic>105)return "작은 테크니션";
  if(run>100)return "폭발적인 침투형";
  if(link>95)return "플레이메이커형";
  return p.pos+" 선수";
 }
 void schedule(){
  ArrayList<Team>rot=new ArrayList<>(teams);LocalDate start=LocalDate.of(2026,8,15);int rr=0,n=rot.size();
  for(int leg=0;leg<2;leg++)for(int r=0;r<n-1;r++){LocalDate d=start.plusWeeks(rr++);for(int i=0;i<n/2;i++){Team a=rot.get(i),b=rot.get(n-1-i);boolean f=(r+i+leg)%2==1;Team h=f?b:a,aw=f?a:b;if(leg==1){Team t=h;h=aw;aw=t;}fixtures.add(new Fixture(d,h,aw));}Team last=rot.remove(n-1);rot.add(1,last);}
 }
 boolean open(){int y=now.getYear();return between(now,LocalDate.of(y,6,30),LocalDate.of(y,9,1))||between(now,LocalDate.of(y,1,1),LocalDate.of(y,2,1));}
 boolean between(LocalDate d,LocalDate a,LocalDate b){return !d.isBefore(a)&&!d.isAfter(b);}
 Fixture next(){for(Fixture f:fixtures)if(!f.played&&!f.date.isBefore(now)&&(f.h.name.equals(club)||f.a.name.equals(club)))return f;return null;}

 void frame(String title){
  main=new LinearLayout(this);main.setOrientation(LinearLayout.HORIZONTAL);main.setBackgroundColor(BG);
  side=new LinearLayout(this);side.setOrientation(LinearLayout.VERTICAL);side.setBackgroundColor(Color.rgb(11,24,38));side.setPadding(dp(6),dp(10),dp(6),dp(10));
  side.addView(tx("DUMOK\nFM",20,GREEN));String[][] n={{"홈","H"},{"선수단","S"},{"전술","T"},{"일정","C"},{"이적시장","M"},{"리그","L"}};
  for(String[]x:n){Button b=bt(x[0]);b.setOnClickListener(v->go(x[1]));side.addView(b,new LinearLayout.LayoutParams(-1,0,1));}
  main.addView(side,new LinearLayout.LayoutParams(dp(115),-1));
  LinearLayout right=new LinearLayout(this);right.setOrientation(LinearLayout.VERTICAL);
  right.addView(tx(title+"   |   "+now+"   |   예산 "+budget+"억",16,Color.WHITE));
  ScrollView sv=new ScrollView(this);body=new LinearLayout(this);body.setOrientation(LinearLayout.VERTICAL);sv.addView(body);right.addView(sv,new LinearLayout.LayoutParams(-1,0,1));
  main.addView(right,new LinearLayout.LayoutParams(0,-1,1));setContentView(main);
 }
 void go(String x){if(x.equals("H"))home();else if(x.equals("S"))squadPage();else if(x.equals("T"))tactic();else if(x.equals("C"))calendar();else if(x.equals("M"))market();else league();}
 void home(){
  frame("감독 대시보드");Fixture f=next();LinearLayout x=card();x.addView(tx("다음 경기",14,MUTED));
  if(f!=null){x.addView(tx(f.date+"   "+f.h.name+" vs "+f.a.name,21,Color.WHITE));if(f.date.equals(now)){Button b=bt("2D 경기 시작");b.setOnClickListener(v->match(f));x.addView(b);}}body.addView(x);
  LinearLayout a=card();a.addView(tx("시간 진행",15,Color.WHITE));Button d=bt("하루 진행");d.setOnClickListener(v->advance(false));a.addView(d);Button e=bt("다음 중요 이벤트까지");e.setOnClickListener(v->advance(true));a.addView(e);body.addView(a);
  LinearLayout m=card();m.addView(tx(open()?"🟢 이적시장 OPEN":"🔴 이적시장 CLOSED · FA만 계약 가능",15,open()?GREEN:MUTED));m.addView(tx("월드 선수 풀 "+pool.size()+"명 · CA/PA 1~200",13,MUTED));body.addView(m);
 }
 void squadPage(){
  frame("선수단");LinearLayout head=card();head.setOrientation(LinearLayout.HORIZONTAL);String[] hs={"선수 / 유형","포지션","나이","국적","신체","CA","PA","체력"};
  for(String h:hs)head.addView(tx(h,12,MUTED),new LinearLayout.LayoutParams(0,-2,h.equals("선수 / 유형")?2:1));body.addView(head);
  for(Player p:squad){LinearLayout row=card();row.setOrientation(LinearLayout.HORIZONTAL);TextView n=tx((p.starter?"★ ":"")+p.name+"\n"+p.nick,12,Color.WHITE);n.setOnClickListener(v->detail(p));row.addView(n,new LinearLayout.LayoutParams(0,-2,2));row.addView(tx(p.pos,12,GREEN),new LinearLayout.LayoutParams(0,-2,1));row.addView(tx(""+p.age,12,Color.WHITE),new LinearLayout.LayoutParams(0,-2,1));row.addView(tx(p.nation,12,Color.WHITE),new LinearLayout.LayoutParams(0,-2,1));row.addView(tx(p.height+"cm\n"+p.weight+"kg "+p.body,11,MUTED),new LinearLayout.LayoutParams(0,-2,1));row.addView(tx(""+p.ca,13,Color.WHITE),new LinearLayout.LayoutParams(0,-2,1));row.addView(tx(""+p.pa,13,GREEN),new LinearLayout.LayoutParams(0,-2,1));row.addView(tx(p.fitness+"%",12,Color.WHITE),new LinearLayout.LayoutParams(0,-2,1));body.addView(row);}
 }
 void detail(Player p){
  frame(p.name+" · "+p.nick);LinearLayout info=card();info.addView(tx(p.nation+" | "+p.pos+" | "+p.age+"세 | "+p.height+"cm / "+p.weight+"kg | "+p.body+" 체형",16,Color.WHITE));info.addView(tx("CA "+p.ca+" / PA "+p.pa+"     경기 "+p.apps+" / 골 "+p.goals+"     가치 "+p.value+"억",15,GREEN));body.addView(info);
  body.addView(tx("주발 "+p.preferredFoot+"   |   반대발 "+p.weakFoot+"/20   |   "+footLabel(p),14,Color.WHITE));
  LinearLayout cols=new LinearLayout(this);cols.setOrientation(LinearLayout.HORIZONTAL);cols.addView(attrCol("기술",TECH,p.te),new LinearLayout.LayoutParams(0,-2,1));cols.addView(attrCol("정신",MENT,p.me),new LinearLayout.LayoutParams(0,-2,1));cols.addView(attrCol("신체",PHYS,p.ph),new LinearLayout.LayoutParams(0,-2,1));body.addView(cols);
  body.addView(tx("※ 플레이 성향 내부점수와 성장속도/프로의식/노쇠 데이터는 공개되지 않습니다.",12,MUTED));
 }
 LinearLayout attrCol(String title,String[] names,int[] a){LinearLayout c=card();c.addView(tx(title,16,GREEN));for(int i=0;i<a.length;i++)c.addView(tx(names[i]+"   "+a[i],13,col(a[i])));return c;}
 int col(int x){if(x>=18)return GREEN;if(x>=15)return Color.rgb(170,235,120);if(x>=11)return Color.rgb(245,210,90);if(x>=6)return Color.WHITE;return MUTED;}
 String defaultRole(String sl){String p=baseSlot(sl);if(p.equals("ST"))return "침투형 공격수";if(p.equals("LW")||p.equals("RW"))return "인사이드 포워드";if(p.equals("AM"))return "플레이메이커";if(p.equals("CM"))return "박스투박스";if(p.equals("DM"))return "수비형 미드필더";if(p.equals("LB")||p.equals("RB"))return "풀백";if(p.equals("CB"))return "센터백";return "골키퍼";}
 String roleAt(int i){if(XIROLE[i]==null)XIROLE[i]=defaultRole(SLOT[i]);return XIROLE[i];}
 String instAt(int i){if(XIINST[i]==null)XIINST[i]="균형";return XIINST[i];}
 void roleDialog(int idx){
  if(XI[idx]==null){pickSlot(idx);return;}final Dialog d=new Dialog(this);LinearLayout b=new LinearLayout(this);b.setOrientation(LinearLayout.VERTICAL);b.setPadding(dp(12),dp(12),dp(12),dp(12));b.setBackgroundColor(BG);
  b.addView(tx(XI[idx].name+" · "+SLOT[idx],18,Color.WHITE));b.addView(tx("역할은 움직임 성향, 능력치는 실행 품질을 결정",11,MUTED));
  String p=baseSlot(SLOT[idx]);String[] rr;
  if(p.equals("ST"))rr=new String[]{"침투형 공격수","타겟맨","연계형 공격수","내려오는 공격수","만능형"};
  else if(p.equals("LW")||p.equals("RW"))rr=new String[]{"인사이드 포워드","윙어","와이드 플레이메이커","침투형 윙어"};
  else if(p.equals("AM"))rr=new String[]{"플레이메이커","세컨드 스트라이커","공격형 미드필더"};
  else if(p.equals("CM"))rr=new String[]{"박스투박스","플레이메이커","전진형 미드필더","중앙 미드필더"};
  else if(p.equals("DM"))rr=new String[]{"수비형 미드필더","딥라잉 플레이메이커","볼위닝 미드필더"};
  else if(p.equals("LB")||p.equals("RB"))rr=new String[]{"풀백","공격형 풀백","수비형 풀백"};
  else if(p.equals("CB"))rr=new String[]{"센터백","스토퍼","커버","빌드업 센터백"};else rr=new String[]{"골키퍼","스위퍼 키퍼"};
  for(String r0:rr){Button x=bt((r0.equals(roleAt(idx))?"✓ ":"")+r0);x.setOnClickListener(v->{XIROLE[idx]=r0;d.dismiss();tactic();});b.addView(x);}
  b.addView(tx("개인지시",14,Color.WHITE));for(String in:new String[]{"균형","더 자주 침투","공 받으러 내려오기","측면으로 벌리기","중앙으로 침투","드리블 적극적","슈팅 적극적","압박 적극적"}){Button x=bt((in.equals(instAt(idx))?"✓ ":"")+in);x.setOnClickListener(v->{XIINST[idx]=in;d.dismiss();tactic();});b.addView(x);}
  Button ch=bt("선수 변경");ch.setOnClickListener(v->{d.dismiss();pickSlot(idx);});b.addView(ch);d.setContentView(b);d.show();
 }
 String[] matchRoles(){ArrayList<String>a=new ArrayList<>();for(int i=0;i<XI.length;i++)if(XI[i]!=null)a.add(roleAt(i));return a.toArray(new String[0]);}
 String[] matchInst(){ArrayList<String>a=new ArrayList<>();for(int i=0;i<XI.length;i++)if(XI[i]!=null)a.add(instAt(i));return a.toArray(new String[0]);}
 void tactic(){
  frame("전술 · 드래그 배치");
  LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.HORIZONTAL);root.setPadding(dp(8),dp(6),dp(8),dp(6));

  LinearLayout rail=new LinearLayout(this);rail.setOrientation(LinearLayout.VERTICAL);rail.setPadding(dp(6),dp(6),dp(6),dp(6));rail.setBackgroundColor(Color.rgb(13,31,43));
  rail.addView(tx("우리팀 선수단",15,Color.WHITE));
  rail.addView(tx("길게 눌러 끌어서 배치",11,MUTED));
  ScrollView rsv=new ScrollView(this);LinearLayout players=new LinearLayout(this);players.setOrientation(LinearLayout.VERTICAL);
  for(Player q:squad){
   TextView pv=playerChip(q);pv.setOnLongClickListener(v->{android.content.ClipData data=android.content.ClipData.newPlainText("player",q.name);v.startDragAndDrop(data,new View.DragShadowBuilder(v),q,0);return true;});
   pv.setOnClickListener(v->detail(q));players.addView(pv);
  }
  rsv.addView(players);rail.addView(rsv,new LinearLayout.LayoutParams(dp(185),0,1));
  root.addView(rail,new LinearLayout.LayoutParams(dp(195),-1));

  LinearLayout board=new LinearLayout(this);board.setOrientation(LinearLayout.VERTICAL);board.setPadding(dp(10),dp(8),dp(10),dp(8));board.setBackgroundColor(Color.rgb(25,83,58));
  board.addView(tx("직접 만든 포메이션",15,Color.WHITE));
  String[][] rows={{"LW","LR","ST","RS","RW"},{"LAM","AM","RAM"},{"LCM","CM","RCM"},{"LDM","DM","RDM"},{"LB","LCB","CB","RCB","RB"},{"GK"}};
  for(String[] row:rows){
   LinearLayout rr=new LinearLayout(this);rr.setGravity(Gravity.CENTER);
   for(String sl:row){int idx=slotIndex(sl);TextView slot=tacticSlot(idx);rr.addView(slot,new LinearLayout.LayoutParams(0,dp(62),1));}
   board.addView(rr,new LinearLayout.LayoutParams(-1,0,1));
  }
  root.addView(board,new LinearLayout.LayoutParams(0,dp(455),1));body.addView(root);
  int count=0;for(Player q:XI)if(q!=null)count++;
  body.addView(tx("선발 "+count+"/11 · 선수단 또는 전술판의 선수를 길게 눌러 다른 포지션으로 드래그",12,count==11?GREEN:Color.YELLOW));
 }
 TextView playerChip(Player q){
  TextView v=tx(q.name+"\n"+q.pos+"  CA "+q.ca,11,Color.WHITE);v.setBackgroundColor(Color.rgb(24,48,62));
  LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(52));lp.setMargins(dp(3),dp(3),dp(3),dp(3));v.setLayoutParams(lp);return v;
 }
 int slotColor(String sl){
  if(sl.equals("GK"))return Color.rgb(181,145,35);
  if(sl.contains("B"))return Color.rgb(38,91,145);
  if(sl.contains("DM")||sl.contains("CM")||sl.contains("AM"))return Color.rgb(36,119,76);
  return Color.rgb(150,52,52);
 }
 TextView tacticSlot(final int idx){
  final TextView v=tx(slotLabel(idx),10,Color.WHITE);v.setGravity(Gravity.CENTER);v.setBackgroundColor(slotColor(SLOT[idx]));
  v.setPadding(dp(3),dp(3),dp(3),dp(3));
  v.setOnDragListener((view,event)->{
   switch(event.getAction()){
    case DragEvent.ACTION_DRAG_ENTERED: view.setAlpha(.68f);return true;
    case DragEvent.ACTION_DRAG_EXITED: view.setAlpha(1f);return true;
    case DragEvent.ACTION_DROP:
      view.setAlpha(1f);Object o=event.getLocalState();
      if(o instanceof Player){
       Player q=(Player)o;int from=-1;for(int i=0;i<XI.length;i++)if(XI[i]==q){from=i;break;}
       if(from>=0 && from!=idx){Player target=XI[idx];XI[idx]=q;XI[from]=target;} // tactical slot -> slot: swap
       else if(from<0){if(XI[idx]==null&&xiCount()>=11){Toast.makeText(this,"선발은 최대 11명입니다",Toast.LENGTH_SHORT).show();return true;}if(XI[idx]!=null)XI[idx].starter=false;XI[idx]=q;}       // squad rail -> slot
       for(Player z:squad)z.starter=false;for(Player z:XI)if(z!=null)z.starter=true;
       saveGame();tactic();
      }return true;
    case DragEvent.ACTION_DRAG_ENDED:view.setAlpha(1f);return true;
   }return true;
  });
  v.setOnLongClickListener(x->{
   if(XI[idx]==null)return false;
   Player moving=XI[idx];
   android.content.ClipData data=android.content.ClipData.newPlainText("tactic-player",moving.name);
   x.startDragAndDrop(data,new View.DragShadowBuilder(x),moving,0);
   return true;
  });
  v.setOnClickListener(x->{if(XI[idx]==null)pickSlot(idx);else roleDialog(idx);});return v;
 }
 int slotIndex(String n){for(int i=0;i<SLOT.length;i++)if(SLOT[i].equals(n))return i;return 0;}
 String slotLabel(int i){Player q=XI[i];return SLOT[i]+"\n"+(q==null?"＋":q.name+" ["+roleCode(roleAt(i))+"]\n"+q.pos+(q.pos.equals(baseSlot(SLOT[i]))||baseSlot(SLOT[i]).equals(q.pos)?"":" ⚠"));}
 int xiCount(){int n=0;for(Player q:XI)if(q!=null)n++;return n;}
 void syncStarters(){for(Player z:squad)z.starter=false;for(Player z:XI)if(z!=null)z.starter=true;}
 String baseSlot(String sl){if(sl.equals("LR")||sl.equals("RS"))return "ST";if(sl.equals("LCB")||sl.equals("RCB"))return "CB";if(sl.equals("LDM")||sl.equals("RDM"))return "DM";if(sl.equals("LCM")||sl.equals("RCM"))return "CM";if(sl.equals("LAM")||sl.equals("RAM"))return "AM";return sl;}
 double fit(Player p,String raw){String pos=baseSlot(raw);double v=0;
  if(pos.equals("ST"))v=p.te[0]*2.2+p.me[3]*1.8+p.me[0]*1.2+p.ph[1]*1.3+p.ph[0]*1.1+p.ph[4]*.7+p.te[5]*.6;
  else if(pos.equals("LW")||pos.equals("RW"))v=p.te[1]*1.7+p.ph[1]*1.5+p.ph[0]*1.3+p.te[4]*1.1+p.te[2]*.9+p.me[3]*1.1+p.te[0]*.7;
  else if(pos.equals("AM"))v=p.te[3]*1.7+p.me[2]*1.8+p.te[2]*1.2+p.te[1]+p.me[1]*1.1+p.me[3]*.8;
  else if(pos.equals("CM"))v=p.te[3]*1.8+p.me[2]*1.5+p.me[1]*1.3+p.me[6]*1.2+p.te[2]+p.ph[6]*.8;
  else if(pos.equals("DM"))v=p.me[4]*1.7+p.te[6]*1.5+p.me[1]*1.3+p.te[3]*1.1+p.me[5]+p.ph[4]*.9+p.ph[6]*.8;
  else if(pos.equals("LB")||pos.equals("RB"))v=p.te[6]*1.5+p.ph[0]*1.4+p.ph[6]*1.2+p.me[4]*1.2+p.te[4]+p.me[5]*.8+p.ph[1]*.8;
  else if(pos.equals("CB"))v=p.te[6]*1.8+p.me[4]*1.7+p.te[5]*1.3+p.ph[5]*1.2+p.ph[4]*1.3+p.me[5]*1.1;
  else v=p.me[5]*1.7+p.me[1]*1.5+p.me[0]*1.3+p.ph[2]+p.ph[5]+p.ph[3]*.8;
  if(p.pos.equals(pos))v+=18;return v;}
 String fitStars(Player p,String sl){double f=fit(p,sl);int n=f>=145?5:f>=125?4:f>=105?3:f>=85?2:1;StringBuilder x=new StringBuilder();for(int i=0;i<5;i++)x.append(i<n?"★":"☆");return x.toString();}
 void pickSlot(int idx){
  final Dialog d=new Dialog(this);LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(dp(10),dp(10),dp(10),dp(10));box.setBackgroundColor(BG);
  box.addView(tx(SLOT[idx]+" 선수 선택 · 추천순",17,Color.WHITE));box.addView(tx("해당 포지션에 필요한 능력치를 기준으로 정렬",11,MUTED));
  if(XI[idx]!=null){Button rm=bt("현재 선수 제외 · "+XI[idx].name);rm.setOnClickListener(v->{XI[idx].starter=false;XI[idx]=null;syncStarters();saveGame();d.dismiss();tactic();});box.addView(rm);}
  ArrayList<Player> cand=new ArrayList<>();for(Player q:squad){boolean used=false;for(Player z:XI)if(z==q)used=true;if(!used||XI[idx]==q)cand.add(q);}
  Collections.sort(cand,(x,y)->Double.compare(fit(y,SLOT[idx]),fit(x,SLOT[idx])));
  ScrollView sv=new ScrollView(this);LinearLayout list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);int rank=1;
  for(Player q:cand){Button bb=bt((rank++)+". "+q.name+"   "+q.pos+"   적합 "+(int)Math.round(fit(q,SLOT[idx]))+"   "+fitStars(q,SLOT[idx])+"   CA "+q.ca);bb.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
   bb.setOnClickListener(v->{int from=-1;for(int i=0;i<XI.length;i++)if(XI[i]==q){from=i;break;}if(from<0&&XI[idx]==null&&xiCount()>=11){Toast.makeText(this,"선발은 최대 11명입니다",Toast.LENGTH_SHORT).show();return;}if(from>=0&&from!=idx){Player t=XI[idx];XI[idx]=q;XI[from]=t;}else XI[idx]=q;syncStarters();saveGame();d.dismiss();tactic();});list.addView(bb);}
  sv.addView(list);box.addView(sv,new LinearLayout.LayoutParams(dp(540),dp(440)));d.setContentView(box);d.show();
 }
 void calendar(){frame("일정");for(Fixture f:fixtures)if(!f.date.isBefore(now.minusDays(14))&&(f.h.name.equals(club)||f.a.name.equals(club))){LinearLayout c=card();c.addView(tx((f.played?"✓ ":"⚽ ")+f.date+"   "+f.h.name+" "+(f.played?f.hg+" - "+f.ag:"vs")+" "+f.a.name,14,f.played?MUTED:GREEN));body.addView(c);}}
 void market(){frame("이적시장");body.addView(tx(open()?"🟢 OPEN — 소속 선수와 FA 계약 가능":"🔴 CLOSED — FA만 계약 가능",15,open()?GREEN:MUTED));int shown=0;for(Player p:pool){if(!open()&&!p.fa)continue;LinearLayout r=card();r.setOrientation(LinearLayout.HORIZONTAL);TextView n=tx((p.fa?"[FA] ":"")+p.name+"\n"+p.nick,12,Color.WHITE);n.setOnClickListener(v->detail(p));r.addView(n,new LinearLayout.LayoutParams(0,-2,2));r.addView(tx(p.nation+" "+p.pos,12,MUTED),new LinearLayout.LayoutParams(0,-2,1));r.addView(tx(p.age+"세 "+p.height+"cm",12,MUTED),new LinearLayout.LayoutParams(0,-2,1));r.addView(tx("CA "+p.ca+" / PA "+p.pa,12,GREEN),new LinearLayout.LayoutParams(0,-2,1));Button b=bt(p.fa?"자유계약":"영입 "+p.value+"억");b.setOnClickListener(v->sign(p));r.addView(b,new LinearLayout.LayoutParams(0,-2,1));body.addView(r);if(++shown>=70)break;}}
 void sign(Player p){if(!p.fa&&!open()){Toast.makeText(this,"이적시장 기간이 아닙니다",0).show();return;}int fee=p.fa?0:p.value;if(budget<fee){Toast.makeText(this,"예산 부족",0).show();return;}budget-=fee;p.fa=false;p.club=club;squad.add(p);pool.remove(p);saveGame();market();}
 void league(){frame("리그");ArrayList<Team>a=new ArrayList<>(teams);Collections.sort(a,(x,y)->y.pts!=x.pts?y.pts-x.pts:(y.gf-y.ga)-(x.gf-x.ga));int i=1;for(Team t:a){body.addView(tx(i+++"  "+t.name+"   "+t.p+"경기   "+t.w+"승 "+t.d+"무 "+t.l+"패   "+t.gf+":"+t.ga+"   "+t.pts+"점",14,t.name.equals(club)?GREEN:Color.WHITE));}}
 void advance(boolean event){
  Fixture f=next();if(f!=null&&f.date.equals(now)){match(f);return;}LocalDate target=now.plusDays(1);if(event&&f!=null)target=f.date;
  while(now.isBefore(target)){now=now.plusDays(1);dailyGrowth();}
  f=next();if(f!=null&&f.date.equals(now))match(f);else{saveGame();home();}
 }
 void dailyGrowth(){
  for(Player p:squad){
   // Training: technical/mental can grow; physical only through age 20.
   int idx=R.nextInt(p.age<=20?24:16);double gain=.20*p.growth*p.professional;
   p.xp[idx]+=gain;
   if(p.xp[idx]>=100){p.xp[idx]-=100;raise(p,idx);}
   // Physical decline is deliberately strong after hidden decline age.
   if(p.age>=p.declineAge&&R.nextDouble()<0.0035*(p.age-p.declineAge+1)/p.physicalRetention){int k=R.nextInt(8);p.ph[k]=Math.max(1,p.ph[k]-1);}
   p.ca=calcCA(p);p.nick=nickname(p);
  }
 }
 void raise(Player p,int idx){if(idx<8)p.te[idx]=clamp(p.te[idx]+1);else if(idx<16)p.me[idx-8]=clamp(p.me[idx-8]+1);else if(p.age<=20)p.ph[idx-16]=clamp(p.ph[idx-16]+1);p.ca=Math.min(p.pa,calcCA(p));}
 float[][] tacticalCoords(){
  ArrayList<float[]> a=new ArrayList<>();String[][] map={{"GK","0.06","0.50"},{"LB","0.20","0.86"},{"LCB","0.20","0.65"},{"CB","0.19","0.50"},{"RCB","0.20","0.35"},{"RB","0.20","0.14"},{"LDM","0.34","0.66"},{"DM","0.33","0.50"},{"RDM","0.34","0.34"},{"LCM","0.45","0.66"},{"CM","0.45","0.50"},{"RCM","0.45","0.34"},{"LAM","0.57","0.66"},{"AM","0.58","0.50"},{"RAM","0.57","0.34"},{"LW","0.68","0.88"},{"LR","0.70","0.64"},{"ST","0.72","0.50"},{"RS","0.70","0.36"},{"RW","0.68","0.12"}};
  for(String[] m:map){int k=slotIndex(m[0]);if(XI[k]!=null)a.add(new float[]{Float.parseFloat(m[1]),Float.parseFloat(m[2])});}
  while(a.size()<11)a.add(new float[]{.42f+(a.size()%3)*.08f,.14f+(a.size()%6)*.14f});
  float[][] out=new float[11][2];for(int i=0;i<11;i++)out[i]=a.get(i);return out;
 }
 Player[] matchProfiles(){
  ArrayList<Player>a=new ArrayList<>();for(Player q:XI)if(q!=null)a.add(q);
  Player[] out=new Player[11];for(int i=0;i<11&&i<a.size();i++)out[i]=a.get(i);return out;
 }
 void match(Fixture f){
  if(xiCount()!=11){Toast.makeText(this,"경기 시작 전 선발 11명을 배치해주세요 ("+xiCount()+"/11)",Toast.LENGTH_LONG).show();tactic();return;}

  final Dialog d=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
  LinearLayout all=new LinearLayout(this);all.setOrientation(LinearLayout.HORIZONTAL);all.setBackgroundColor(BG);
  SmoothPitch pitch=new SmoothPitch(this, tacticalCoords(), matchProfiles(), matchRoles(), matchInst());
  all.addView(pitch,new LinearLayout.LayoutParams(0,-1,4));
  LinearLayout panel=new LinearLayout(this);panel.setOrientation(LinearLayout.VERTICAL);panel.setPadding(dp(8),dp(8),dp(8),dp(8));panel.setBackgroundColor(Color.rgb(10,20,32));
  TextView score=tx(f.h.name+"\n0 - 0\n"+f.a.name,20,Color.WHITE);score.setGravity(Gravity.CENTER);panel.addView(score);
  TextView clock=tx("00:00",18,GREEN);clock.setGravity(Gravity.CENTER);panel.addView(clock);
  TextView action=tx("KICK OFF",15,Color.WHITE);action.setGravity(Gravity.CENTER);panel.addView(action);
  TextView stats=tx("슈팅 0 - 0\n유효슈팅 0 - 0\n점유율 50% - 50%",13,MUTED);panel.addView(stats);
  TextView log=tx("경기가 시작됩니다.",12,MUTED);panel.addView(log,new LinearLayout.LayoutParams(-1,0,1));
  Button speed=bt("속도 x1");panel.addView(speed);all.addView(panel,new LinearLayout.LayoutParams(0,-1,1));
  d.setContentView(all);d.show();

  final int[] sec={0},hg={0},ag={0},shotsH={0},shotsA={0},sotH={0},sotA={0},possH={0},possA={0},delay={95};
  final boolean homeIsBlue=f.h.name.equals(club);
  Handler H=new Handler(Looper.getMainLooper());

  Runnable game=new Runnable(){public void run(){
    // Engine runs in small football-time steps. Score can ONLY change after pitch reports a real goal event.
    sec[0]+=4;
    pitch.step();

    if(pitch.lastPossessionBlue){if(homeIsBlue)possH[0]++;else possA[0]++;}else{if(homeIsBlue)possA[0]++;else possH[0]++;}

    if(pitch.consumeShot()){
      boolean blue=pitch.lastEventBlue;
      if((blue&&homeIsBlue)||(!blue&&!homeIsBlue))shotsH[0]++;else shotsA[0]++;
    }
    if(pitch.consumeOnTarget()){
      boolean blue=pitch.lastEventBlue;
      if((blue&&homeIsBlue)||(!blue&&!homeIsBlue))sotH[0]++;else sotA[0]++;
    }
    if(pitch.consumeGoal()){
      boolean blue=pitch.lastEventBlue;
      if((blue&&homeIsBlue)||(!blue&&!homeIsBlue))hg[0]++;else ag[0]++;
      awardMatchXP(true);
    }

    int mm=Math.min(90,sec[0]/60),ss=sec[0]%60;
    score.setText(f.h.name+"\n"+hg[0]+" - "+ag[0]+"\n"+f.a.name);
    clock.setText(String.format(java.util.Locale.US,"%02d:%02d",mm,ss));
    action.setText(pitch.eventText);
    int tot=possH[0]+possA[0],ph=tot==0?50:(100*possH[0]/tot);
    stats.setText("슈팅 "+shotsH[0]+" - "+shotsA[0]+"\n유효슈팅 "+sotH[0]+" - "+sotA[0]+"\n점유율 "+ph+"% - "+(100-ph)+"%");
    if(pitch.newLog){log.setText(pitch.eventText+"\n\n"+log.getText());pitch.newLog=false;}

    if(sec[0]>=5400){
      finish(f,hg[0],ag[0]);action.setText("FULL TIME");
      speed.setText("경기 종료");speed.setOnClickListener(v->{d.dismiss();home();});
    }else H.postDelayed(this,delay[0]);
  }};
  speed.setOnClickListener(v->{delay[0]=delay[0]==95?55:delay[0]==55?28:95;speed.setText(delay[0]==95?"속도 x1":delay[0]==55?"속도 x2":"속도 x3");});
  H.postDelayed(game,500);
 }
 double teamAttack(){double s=0;int n=0;for(Player p:squad)if(p.starter){double run=p.ph[0]*1.4+p.ph[1]*1.5+p.me[3]*1.3;double tech=p.te[0]*1.5+p.te[1]+p.te[2]+p.me[0];s+=run+tech;n++;}return n==0?0:s/n;}
 void awardMatchXP(boolean attack){for(Player p:squad)if(p.starter){int[] cand=p.pos.equals("ST")||p.pos.equals("RW")||p.pos.equals("LW")?new int[]{0,1,2,8,11,16,17,18}:new int[]{3,10,9,14,22};int idx=cand[R.nextInt(cand.length)];if(idx>=16&&p.age>20)idx=R.nextInt(16);p.xp[idx]+=2.0*p.growth*p.professional;p.apps++;}}
 void finish(Fixture f,int x,int y){f.played=true;f.hg=x;f.ag=y;result(f.h,f.a,x,y);for(Fixture q:fixtures)if(!q.played&&q.date.equals(f.date)){int a=R.nextInt(4),b=R.nextInt(4);q.played=true;q.hg=a;q.ag=b;result(q.h,q.a,a,b);}for(Player p:squad)if(p.starter)p.fitness=Math.max(55,p.fitness-6-R.nextInt(10));now=now.plusDays(1);saveGame();}
 void result(Team h,Team a,int x,int y){h.p++;a.p++;h.gf+=x;h.ga+=y;a.gf+=y;a.ga+=x;if(x>y){h.w++;a.l++;h.pts+=3;}else if(x<y){a.w++;h.l++;a.pts+=3;}else{h.d++;a.d++;h.pts++;a.pts++;}}

 static class SmoothPitch extends View{
  Paint p=new Paint(1); Random r=new Random();
  Player[] simPlayer=new Player[22]; String[] simRole=new String[22],simInst=new String[22];
  float[][] xy=new float[22][2], base=new float[22][2], dest=new float[22][2], vel=new float[22][2];
   float[] thinkOffset=new float[22], runBias=new float[22], laneBias=new float[22], aggression=new float[22];
   float[] stridePhase=new float[22],headingX=new float[22],headingY=new float[22];
   float[][] stepTarget=new float[22][2];int[] stepHold=new int[22],turnHold=new int[22];
   float[][] prevXY=new float[22][2];float[] visualStep=new float[22];
   int[] roleState=new int[22]; int animFrame=0;
  float bx=.5f,by=.5f,btx=.5f,bty=.5f; int owner=0,receiver=-1,state=0,ticks=0,dribbleDef=-1,dribblePhase=0;
  // states: 0 possession, 1 pass, 2 through ball, 3 cross, 4 shot, 5 save/reset, 6 goal celebration, 7 one-v-one dribble
  boolean ballFlying=false,lastPossessionBlue=true,lastEventBlue=true,shotFlag,onTargetFlag,goalFlag,newLog=true;
  String eventText="KICK OFF";
  Handler h=new Handler(Looper.getMainLooper());
  SmoothPitch(Context c,float[][] userShape,Player[] userPlayers,String[] userRoles,String[] userInst){
   super(c);
   if(userPlayers!=null)for(int i=0;i<11&&i<userPlayers.length;i++)simPlayer[i]=userPlayers[i];if(userRoles!=null)for(int i=0;i<11&&i<userRoles.length;i++)simRole[i]=userRoles[i];if(userInst!=null)for(int i=0;i<11&&i<userInst.length;i++)simInst[i]=userInst[i];
   float[][] f={{.055f,.50f},{.20f,.14f},{.19f,.38f},{.19f,.62f},{.20f,.86f},{.35f,.38f},{.35f,.62f},{.52f,.18f},{.55f,.50f},{.52f,.82f},{.70f,.50f}};
   for(int i=0;i<11;i++){float ux=(userShape!=null&&i<userShape.length)?userShape[i][0]:f[i][0],uy=(userShape!=null&&i<userShape.length)?userShape[i][1]:f[i][1];base[i][0]=ux;base[i][1]=uy;base[i+11][0]=1-f[i][0];base[i+11][1]=1-f[i][1];}
   for(int i=0;i<22;i++){xy[i][0]=dest[i][0]=base[i][0];xy[i][1]=dest[i][1]=base[i][1];}
   for(int i=0;i<22;i++){thinkOffset[i]=r.nextFloat()*60f;runBias[i]=.78f+r.nextFloat()*.48f;laneBias[i]=(r.nextFloat()-.5f)*.12f;aggression[i]=.72f+r.nextFloat()*.55f;}
   for(int i=0;i<22;i++){stridePhase[i]=r.nextFloat()*6.28f;headingX[i]=1f;}
   for(int i=0;i<22;i++){stepTarget[i][0]=xy[i][0];stepTarget[i][1]=xy[i][1];stepHold[i]=r.nextInt(3);turnHold[i]=0;}
   for(int i=0;i<22;i++){prevXY[i][0]=xy[i][0];prevXY[i][1]=xy[i][1];visualStep[i]=r.nextFloat()*6.28f;}
   owner=0;bx=xy[0][0];by=xy[0][1];h.post(anim);
  }
  Runnable anim=new Runnable(){public void run(){
   animFrame++;
   for(int i=0;i<22;i++){prevXY[i][0]=xy[i][0];prevXY[i][1]=xy[i][1];}
   for(int i=0;i<22;i++){float dx=dest[i][0]-xy[i][0],dy=dest[i][1]-xy[i][1];float accN=norm(A(i,"acc")),paceN=norm(A(i,"pace")),agiN=norm(A(i,"agi")),dl=(float)Math.sqrt(dx*dx+dy*dy);
    if(stepHold[i]>0){stepHold[i]--;}else if(turnHold[i]>0){turnHold[i]--;}
    else if(dl>.003f){
     float nx=dx/dl,ny=dy/dl,dot=headingX[i]*nx+headingY[i]*ny;
     if(dot<.20f){turnHold[i]=Math.max(1,3-(int)(agiN*2));headingX[i]=headingX[i]*.45f+nx*.55f;headingY[i]=headingY[i]*.45f+ny*.55f;}
     else{
      boolean sprint=roleState[i]==1||roleState[i]==2||roleState[i]==4;
      float stride=(sprint?(.0048f+.0036f*paceN):(.0027f+.0019f*paceN))*(.82f+.24f*accN);
      // Natural lateral variation prevents every marker tracing ruler-straight rails.
      float lateral=((i%2==0)?1f:-1f)*(sprint?.00055f:.00032f)*(1f-.45f*agiN);
      float sx=nx*stride-ny*lateral,sy=ny*stride+nx*lateral;
      stepTarget[i][0]=clip(xy[i][0]+sx);stepTarget[i][1]=clip(xy[i][1]+sy);
      // Hard footfall: move to the next planted position in one short step, then visibly hold.
      xy[i][0]=stepTarget[i][0];xy[i][1]=stepTarget[i][1];
      headingX[i]=nx;headingY[i]=ny;visualStep[i]+=(sprint?1.15f:.82f);
      stepHold[i]=sprint?3:5;
     }
    }
    vel[i][0]=0;vel[i][1]=0;separateKeeper(i);}
   if(ballFlying){
    // Normal passes track the receiver continuously. Through balls/crosses deliberately target space.
    if((state==1||state==3)&&receiver>=0){btx=xy[receiver][0];bty=xy[receiver][1];}
    float dx=btx-bx,dy=bty-by,dist=(float)Math.sqrt(dx*dx+dy*dy);float sp=state==4?.036f:state==3?.024f:state==2?.021f:.017f;
    if(dist>sp){bx+=dx/dist*sp;by+=dy/dist*sp;}else{bx=btx;by=bty;}
   }
   else {float lead=(state==7?.018f:.011f),hx=headingX[owner],hy=headingY[owner];float footX=xy[owner][0]+hx*lead,footY=xy[owner][1]+hy*lead;float follow=state==7?.62f:.48f;bx+=(footX-bx)*follow;by+=(footY-by)*follow;}
   if(state==0 && h.getLooper()!=null && animFrame%3==0) updateIndependentMovement();
   invalidate();h.postDelayed(this,16);
  }};
  void playerSpacing(){
   for(int i=0;i<22;i++)for(int j=i+1;j<22;j++){float dx=xy[i][0]-xy[j][0],dy=xy[i][1]-xy[j][1],d=(float)Math.sqrt(dx*dx+dy*dy);if(d>.0005f&&d<.022f){float push=(.022f-d)*.18f,nx=dx/d,ny=dy/d;xy[i][0]=clip(xy[i][0]+nx*push);xy[i][1]=clip(xy[i][1]+ny*push);xy[j][0]=clip(xy[j][0]-nx*push);xy[j][1]=clip(xy[j][1]-ny*push);}}
  }
  boolean inGoalMouth(int i){boolean b=i<11;return (b?xy[i][0]>.925f:xy[i][0]<.075f)&&xy[i][1]>.39f&&xy[i][1]<.61f;}
  boolean inBox(int i){boolean b=i<11;return (b?xy[i][0]>.80f:xy[i][0]<.20f)&&xy[i][1]>.25f&&xy[i][1]<.75f;}
  void separateKeeper(int i){if(i%11==0)return;boolean b=i<11;int g=b?11:0;float dx=xy[i][0]-xy[g][0],dy=xy[i][1]-xy[g][1],d=(float)Math.sqrt(dx*dx+dy*dy);if(d<.035f){float nx=d>.001f?dx/d:(b?-1f:1f),ny=d>.001f?dy/d:0f;xy[i][0]=clip(xy[g][0]+nx*.037f);xy[i][1]=clip(xy[g][1]+ny*.037f);}}
  int nearestToBall(){int best=0;float bd=99;for(int i=0;i<22;i++){float dx=xy[i][0]-bx,dy=xy[i][1]-by,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  void rescueDeadBall(){
   if(owner>=0)return;float speed=(float)Math.sqrt(bvx*bvx+bvy*bvy);
   if(speed<.00035f){int n=nearestToBall();float dx=bx-xy[n][0],dy=by-xy[n][1],d=(float)Math.sqrt(dx*dx+dy*dy);
    if(d<.035f){owner=n;state=1;bx=xy[n][0]+headingX[n]*.011f;by=xy[n][1]+headingY[n]*.011f;}
    else{dest[n][0]=bx;dest[n][1]=by;bvx*=.96f;bvy*=.96f;}
   }
  }
  void step(){
   ticks++; lastPossessionBlue=owner<11;
   if(state==1){if(closeBall()){owner=receiver;receiver=-1;ballFlying=false;state=0;eventText=stateName(owner)+" 패스 성공";newLog=true;shape();}return;}
   if(state==2){
    if(receiver>=0){dest[receiver][0]=btx;dest[receiver][1]=bty;float dx=bx-xy[receiver][0],dy=by-xy[receiver][1];
     if(dx*dx+dy*dy<.00075f){owner=receiver;receiver=-1;ballFlying=false;state=0;eventText="스루패스 연결";newLog=true;shape();}}
    return;
   }
   if(state==3){if(closeBall()){owner=receiver;receiver=-1;ballFlying=false;state=0;eventText="크로스 연결";newLog=true;shape();}return;}
   if(state==4){ // shot must physically reach goal before outcome exists
    if(closeBall()){
     boolean blue=lastEventBlue;shotFlag=true;
     float fq=finishQ(owner);boolean on=r.nextFloat()<(.48f+.40f*fq);
     if(on){onTargetFlag=true;boolean goal=r.nextFloat()<(.10f+.32f*fq);
       if(goal){goalFlag=true;state=6;eventText="⚽ GOAL!";newLog=true;ballFlying=false;bx=blue?.985f:.015f;by=.5f;ticks=0;}
       else{state=5;eventText="🧤 골키퍼 선방";newLog=true;owner=blue?11:0;receiver=-1;ballFlying=false;ticks=0;}
     }else{state=5;eventText="슛이 골문을 벗어납니다";newLog=true;owner=blue?11:0;receiver=-1;ballFlying=false;ticks=0;}
    }return;
   }
   if(state==7){ // visible 1v1: feint -> burst -> defender recovery
    boolean blue=owner<11;float dir=blue?1f:-1f;
    if(dribblePhase==0){ // square the defender and make a small lateral feint
     dest[owner][0]=clip(xy[owner][0]+dir*.018f);dest[owner][1]=clip(xy[owner][1]+(by<.5f?.028f:-.028f));
     if(dribbleDef>=0){dest[dribbleDef][0]=xy[owner][0]+dir*.025f;dest[dribbleDef][1]=xy[owner][1];}
     dribblePhase=1;eventText="1대1 · 페인트";newLog=true;return;
    }
    if(dribblePhase==1){
     float side=(xy[owner][1] <= xy[dribbleDef][1])?-.055f:.055f;
     float aq=attack1v1(owner),dq=defend1v1(dribbleDef),win=Math.max(.18f,Math.min(.82f,.50f+(aq-dq)*.65f));
     if(r.nextFloat()<win){
      float burst=.075f+.065f*norm(A(owner,"acc"));dest[owner][0]=clip(xy[owner][0]+dir*burst);dest[owner][1]=clip(xy[owner][1]+side*(.65f+.55f*norm(A(owner,"agi"))));
      if(dribbleDef>=0){dest[dribbleDef][0]=clip(xy[dribbleDef][0]-dir*.018f);dest[dribbleDef][1]=clip(xy[dribbleDef][1]-side*.35f);}
      dribblePhase=2;eventText="⚡ "+styleOf(owner)+" · 수비수를 벗겨냅니다";newLog=true;
     }else{owner=dribbleDef;state=0;dribbleDef=-1;dribblePhase=0;eventText="수비가 1대1을 막아냅니다";newLog=true;shape();}
     return;
    }
    if(dribblePhase==2){float dx=dest[owner][0]-xy[owner][0],dy=dest[owner][1]-xy[owner][1];if(dx*dx+dy*dy<.0012f){state=0;dribbleDef=-1;dribblePhase=0;eventText="돌파 성공 · 전진";newLog=true;shape();}return;}
   }
   if(state==5){if(ticks%12==0){state=0;eventText="골키퍼가 다시 전개합니다";shape();}return;}
   if(state==6){if(ticks>22){resetKickoff(lastEventBlue?11:0);}return;}

   if(ticks%4!=0)return;
   int pressing=nearestOpponent(owner);if(dist(owner,pressing)<.052f && r.nextFloat()<.34f){tackle(pressing);return;}
   shape();
   boolean blue=owner<11;int b=blue?0:11,local=owner-b;float x=xy[owner][0],progress=blue?x:1-x;

   // Defenders can actually win the ball when close to carrier.
   int opp=nearestOpponent(owner);float dx=xy[opp][0]-x,dy=xy[opp][1]-xy[owner][1];
   if(dx*dx+dy*dy<.0105f && local>=5 && r.nextFloat()<.42f){
    // Attackers/midfielders sometimes engage the nearest defender instead of instantly passing.
    if(r.nextFloat()<.62f){state=7;dribbleDef=opp;dribblePhase=0;ballFlying=false;eventText="1대1 돌파 시도";newLog=true;return;}
    else if(r.nextFloat()<.45f){owner=opp;receiver=-1;ballFlying=false;eventText="드리블 저지 · 소유권 전환";newLog=true;shape();return;}
   }
   if(dx*dx+dy*dy<.0055f&&r.nextFloat()<.16f){owner=opp;receiver=-1;ballFlying=false;eventText="태클 성공 · 소유권 전환";newLog=true;shape();return;}

   // Basic Laws of the Game restarts: the ball cannot continue from nowhere.
   if(by<=.025f||by>=.975f){owner=nearestPlayerTo(bx,by,!blue);receiver=-1;ballFlying=false;eventText="스로인";newLog=true;shape();return;}
   if(bx<=.025f||bx>=.975f){
    boolean attackingEnd=(blue&&bx>=.975f)||(!blue&&bx<=.025f);
    if(attackingEnd&&r.nextBoolean()){owner=blue?(11+1):1;eventText="골킥";}else{owner=blue?8:19;eventText="코너킥";}
    receiver=-1;ballFlying=false;newLog=true;shape();return;
   }
   // Offside check at the moment a forward pass is selected is handled before through balls.
   // Final third: attackers become goal-seeking instead of endlessly recycling possession.
   int ownLocal=owner%11;
   if(finalThird(blue,x) && ownLocal>=8 && !ballFlying){
    int duelOpp=nearestOpponent(owner);float dd=dist(owner,duelOpp);if(inBox(owner)){int enemyGK=blue?11:0;if(dist(owner,enemyGK)<.095f||inGoalMouth(owner)){shoot(blue);return;}}
    if(duelOpp%11!=0&&!inGoalMouth(owner)&&dd<.12f&&r.nextFloat()<Math.max(.18f,Math.min(.78f,.40f+.25f*attack1v1(owner)+dribbleRole(owner)))){startDribbleDuel(duelOpp);return;}
    float urge=Math.max(.18f,Math.min(.88f,.24f+.52f*finishQ(owner)+shootRole(owner)));if((blue?x>.76f:x<.24f)&&r.nextFloat()<urge){shoot(blue);return;}
   }
   // Final third: visible shot only from plausible positions.
   if(progress>.73f && Math.abs(xy[owner][1]-.5f)<.30f && r.nextFloat()<.34f){shoot(blue);return;}
   // Wide attackers/fullbacks cross from wide final-third positions.
   if(progress>.62f && (xy[owner][1]<.28f||xy[owner][1]>.72f) && r.nextFloat()<.42f){cross(blue);return;}
   // Midfielders sometimes send a through ball into space for an attacker to chase.
   if((local==5||local==6||local==7)&&progress>.42f&&r.nextFloat()<.30f){through(blue);return;}
   // Carrier may dribble a short distance; ball remains visibly at feet.
   float action=r.nextFloat();
   if(local>=8 && action<.46f){ // attackers default to advancing, not recycling
    float dir=blue?.070f:-.070f;dest[owner][0]=clip(x+dir);dest[owner][1]=clip(xy[owner][1]+(r.nextFloat()-.5f)*.040f);eventText="공격수가 골문을 향해 전진";return;
   }
   if(local>=5 && action<.27f){float dir=blue?.052f:-.052f;dest[owner][0]=clip(x+dir);eventText="미드필더 전진 운반";return;}
   pass(blue);
  }
  float dist(int a,int b){float dx=xy[a][0]-xy[b][0],dy=xy[a][1]-xy[b][1];return (float)Math.sqrt(dx*dx+dy*dy);}
  void startDribbleDuel(int defender){state=7;dribbleDef=defender;dribblePhase=0;ballFlying=false;eventText="1대1 돌파 시도";newLog=true;}
  void tackle(int defender){
   if(defender<0)return;
   // close pressure can win it, but attackers can escape and keep attacking
   float aq=attack1v1(owner),dq=defend1v1(defender),tw=Math.max(.18f,Math.min(.72f,.40f+(dq-aq)*.58f));
   if(r.nextFloat()<tw){owner=defender;receiver=-1;ballFlying=false;state=0;eventText="태클 성공 · 소유권 전환";newLog=true;shape();}
   else if((owner%11)>=8){startDribbleDuel(defender);}
   else{eventText="압박을 버티고 전진";newLog=true;}
  }
  void pass(boolean blue){
   int b=blue?0:11,local=owner-b;int[] opts;
   if(local==0)opts=new int[]{1,2,3,4}; else if(local<=4)opts=new int[]{5,6,7}; else if(local<=7)opts=new int[]{8,9,10}; else opts=new int[]{5,6,7,8,9,10};
   int n=opts[r.nextInt(opts.length)];
   float progressive=.25f+.50f*passQ(owner);
   if(local<=7 && r.nextFloat()<progressive){ // vision + passing + decisions drive forward play
    int[] forward=local<=4?new int[]{5,6,7}:new int[]{8,9,10};n=forward[r.nextInt(forward.length)];
   }
   if(n==local)n=6;receiver=b+n;state=1;lastEventBlue=blue;
   btx=xy[receiver][0];bty=xy[receiver][1];ballFlying=true;eventText="→ 패스";newLog=true;
  }
  void through(boolean blue){
   int b=blue?0:11;receiver=b+8+r.nextInt(3);float dir=blue?.12f:-.12f;
   dest[receiver][0]=clip(xy[receiver][0]+dir);btx=dest[receiver][0];bty=dest[receiver][1];state=2;lastEventBlue=blue;ballFlying=true;eventText="⇢ 스루패스!";newLog=true;
  }
  void cross(boolean blue){
   int b=blue?0:11;receiver=b+9;dest[receiver][0]=blue?.83f:.17f;dest[receiver][1]=.48f;
   btx=dest[receiver][0];bty=dest[receiver][1];state=3;lastEventBlue=blue;ballFlying=true;eventText="⤴ 크로스";newLog=true;
  }
  void shoot(boolean blue){
   receiver=-1;state=4;lastEventBlue=blue;ballFlying=true;btx=blue?.985f:.015f;bty=.38f+r.nextFloat()*.24f;eventText="💥 슈팅!";newLog=true;
  }
  boolean closeBall(){
   if(receiver>=0&&(state==1||state==3)){float dx=bx-xy[receiver][0],dy=by-xy[receiver][1];return dx*dx+dy*dy<.00055f;}
   return Math.abs(bx-btx)<.014f&&Math.abs(by-bty)<.014f;
  }
  boolean consumeShot(){boolean q=shotFlag;shotFlag=false;return q;} boolean consumeOnTarget(){boolean q=onTargetFlag;onTargetFlag=false;return q;} boolean consumeGoal(){boolean q=goalFlag;goalFlag=false;return q;}
  String stateName(int i){return i<11?"BLUE":"RED";}
  void resetKickoff(int team){owner=team+6;receiver=-1;dribbleDef=-1;dribblePhase=0;state=0;ballFlying=false;bx=.5f;by=.5f;xy[owner][0]=dest[owner][0]=.5f;xy[owner][1]=dest[owner][1]=.5f;eventText="센터서클에서 킥오프";newLog=true;ticks=0;shape();}
  int nearestPlayerTo(float x,float y,boolean blueTeam){int lo=blueTeam?0:11,hi=lo+11,best=lo;float bd=99;for(int i=lo;i<hi;i++){float dx=xy[i][0]-x,dy=xy[i][1]-y,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  int nearestOpponent(int me){int lo=me<11?11:0,hi=lo+11,best=lo;float bd=99;for(int i=lo;i<hi;i++){float dx=xy[i][0]-xy[me][0],dy=xy[i][1]-xy[me][1],d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  float A(int i,String a){Player q=simPlayer[i];if(q==null)return 10f;
   if(a.equals("pace"))return q.ph[0];if(a.equals("acc"))return q.ph[1];if(a.equals("agi"))return q.ph[2];if(a.equals("bal"))return q.ph[3];if(a.equals("str"))return q.ph[4];if(a.equals("jump"))return q.ph[5];if(a.equals("sta"))return q.ph[6];
   if(a.equals("fin"))return q.te[0];if(a.equals("dri"))return q.te[1];if(a.equals("touch"))return q.te[2];if(a.equals("pass"))return q.te[3];if(a.equals("cross"))return q.te[4];if(a.equals("head"))return q.te[5];if(a.equals("tackle"))return q.te[6];
   if(a.equals("comp"))return q.me[0];if(a.equals("dec"))return q.me[1];if(a.equals("vis"))return q.me[2];if(a.equals("off"))return q.me[3];if(a.equals("pos"))return q.me[4];if(a.equals("con"))return q.me[5];if(a.equals("work"))return q.me[6];return 10f;
  }
  float norm(float v){return Math.max(0f,Math.min(1f,(v-1f)/19f));}
  float runQ(int i){return .38f*norm(A(i,"off"))+.27f*norm(A(i,"dec"))+.20f*norm(A(i,"acc"))+.15f*norm(A(i,"pace"));}
  float attack1v1(int i){return .31f*norm(A(i,"dri"))+.22f*norm(A(i,"agi"))+.20f*norm(A(i,"acc"))+.12f*norm(A(i,"touch"))+.09f*norm(A(i,"dec"))+.06f*norm(A(i,"str"));}
  float defend1v1(int i){return .38f*norm(A(i,"tackle"))+.27f*norm(A(i,"pos"))+.15f*norm(A(i,"agi"))+.10f*norm(A(i,"acc"))+.10f*norm(A(i,"str"));}
  float finishQ(int i){return .48f*norm(A(i,"fin"))+.30f*norm(A(i,"comp"))+.14f*norm(A(i,"dec"))+.08f*norm(A(i,"touch"));}
  float passQ(int i){return .42f*norm(A(i,"pass"))+.32f*norm(A(i,"vis"))+.26f*norm(A(i,"dec"));}
  String styleOf(int i){float speed=A(i,"pace")+A(i,"acc"),tech=A(i,"dri")+A(i,"touch")+A(i,"pass"),target=A(i,"str")+A(i,"jump")+A(i,"head");
   if(target>=46)return tech>=42?"연계형 타겟":"타겟맨";if(speed>=32&&A(i,"off")>=13)return "침투형";if(tech>=45&&A(i,"agi")>=14)return "테크니션";return "균형형";
  }
  String R(int i){if(i<11&&simRole[i]!=null)return simRole[i];int l=i%11;if(l==9)return "침투형 공격수";if(l==8||l==10)return "인사이드 포워드";return "균형형";}
  String I(int i){return i<11&&simInst[i]!=null?simInst[i]:"균형";}
  float runRole(int i){String r=R(i),n=I(i);float x=0;if(r.contains("침투")||r.equals("세컨드 스트라이커")||r.equals("공격형 풀백"))x+=.18f;if(r.contains("타겟")||r.contains("내려오는"))x-=.12f;if(n.equals("더 자주 침투")||n.equals("중앙으로 침투"))x+=.15f;if(n.equals("공 받으러 내려오기"))x-=.18f;return x;}
  float dribbleRole(int i){String r=R(i),n=I(i);return (r.contains("인사이드")||r.contains("윙어")||r.contains("만능")?.10f:0f)+(n.equals("드리블 적극적")?.18f:0f);}
  float shootRole(int i){String r=R(i),n=I(i);return (r.contains("스트라이커")||r.equals("세컨드 스트라이커")?.10f:0f)+(n.equals("슈팅 적극적")?.18f:0f);}
  void updateIndependentMovement(){
   // Players commit to a short movement idea instead of changing destination every render frame.
   if(animFrame%9!=0)return;
   boolean blue=owner<11;float dir=blue?1f:-1f;float ballX=ballFlying?bx:xy[owner][0],ballY=ballFlying?by:xy[owner][1];
   int attackBase=blue?0:11, defendBase=blue?11:0;
   for(int j=0;j<22;j++){
    if(j==owner)continue;
    int local=j%11;boolean attacking=(j<11)==blue;
    // Each player re-evaluates on a different frame, preventing synchronized "table football" movement.
    int cadence=5+(j*3)%9;
    if((animFrame+(int)thinkOffset[j])%cadence!=0)continue;
    float tx=dest[j][0],ty=dest[j][1];
    if(attacking){
      float progress=blue?ballX:1-ballX;
      if(local==0){ // keeper: sweeper position, but never joins normal attack
        tx=blue?.065f:.935f;ty=clip(.5f+(ballY-.5f)*.12f);roleState[j]=0;
      }else if(local>=1&&local<=4){ // back four: one fullback may overlap, CBs hold rest defence
        boolean fullback=(local==1||local==4);
        if(fullback && progress>.45f && ((j+animFrame/90)%2==0)){
          tx=clip(base[j][0]+dir*(.10f+.08f*aggression[j]));ty=clip(base[j][1]+laneBias[j]);roleState[j]=6;
        }else{
          tx=clip(base[j][0]+(ballX-.5f)*.14f);ty=clip(base[j][1]+(ballY-base[j][1])*.10f);roleState[j]=5;
        }
      }else if(local>=5&&local<=7){ // midfield: show, support, or make a third-man run
        int mode=(j+(animFrame/75))%3;
        if(mode==0){tx=clip(ballX-dir*.10f);ty=clip(ballY+(local-6)*.11f+laneBias[j]);roleState[j]=3;} // show for ball
        else if(mode==1){tx=clip(ballX+dir*.06f);ty=clip(base[j][1]+(ballY-.5f)*.20f);roleState[j]=1;} // support ahead
        else{tx=clip(base[j][0]+dir*(progress>.58f?.18f:.08f));ty=clip(base[j][1]+laneBias[j]);roleState[j]=2;} // break line
      }else{ // FRONT THREE: think like attackers
        boolean final3=finalThird(blue,ballX);
        boolean carrierIsWide=(owner%11==8||owner%11==10);
        if(local==9){ // striker: threaten goal first
          if(final3){
            float rq=runQ(j),goalDepth=Math.min(.905f,.82f+.075f*rq+.025f*runRole(j));tx=blue?goalDepth:1-goalDepth;
            ty=clip((ballY<.5f?.43f:.57f)+(r.nextFloat()-.5f)*(.12f*(1-rq)));
            if(carrierIsWide){float d=.86f+.07f*rq;tx=blue?d:1-d;}
            roleState[j]=2;
          }else{
            // alternate checking short with spinning in behind
            float rq=runQ(j);boolean run=((animFrame/70+j)%100)<(35+(int)(rq*55)+(int)(runRole(j)*35));
            tx=clip(ballX+dir*(run?(.10f+.15f*rq):-.045f));ty=clip(.5f+laneBias[j]*(1f-rq*.55f));roleState[j]=run?1:3;
          }
        }else{ // wide forwards: diagonal runs, back-post attacks, occasional width
          boolean left=(local==8);
          if(final3){
            if((ballY<.5f)==left){ // ball-side winger attacks inside channel
              tx=blue?.82f:.18f;ty=left?.34f:.66f;roleState[j]=1;
            }else{ // far winger attacks back post aggressively
              tx=blue?.90f:.10f;ty=left?.38f:.62f;roleState[j]=2;
            }
          }else{
            float rq=runQ(j);boolean diagonal=((animFrame/85+j)%100)<(30+(int)(rq*60)+(int)(runRole(j)*30));
            tx=clip(ballX+dir*(diagonal?(.10f+.11f*rq):.065f));
            ty=diagonal?(left?.34f:.66f):(left?.16f:.84f);
            roleState[j]=diagonal?1:6;
          }
        }
      }
    }else{
      // DEFENDING: one presses, one covers, others mark lanes instead of standing still.
      int press=nearestPlayerTo(ballX,ballY,!blue);
      int second=secondNearestDefender(ballX,ballY,!blue,press);
      if(local==0){
        tx=blue?.935f:.065f;ty=clip(.5f+(ballY-.5f)*.20f);roleState[j]=0;
      }else if(j==press){
        tx=clip(ballX-dir*.018f);ty=clip(ballY);roleState[j]=4;
      }else if(j==second){
        tx=clip(ballX-dir*.075f);ty=clip(ballY+(base[j][1]-ballY)*.45f);roleState[j]=5;
      }else if(local>=1&&local<=4){
        float line=blue?.79f:.21f;tx=clip(line+(ballX-.5f)*.10f);
        // defenders track attacking lanes and retreat when a runner goes beyond
        int mark=bestMarkFor(j,blue);
        ty=clip(xy[mark][1]+(base[j][1]-xy[mark][1])*.38f);roleState[j]=5;
      }else{
        tx=clip(base[j][0]+(ballX-.5f)*.16f);ty=clip(base[j][1]+(ballY-base[j][1])*.34f);roleState[j]=5;
      }
    }
    dest[j][0]=tx;dest[j][1]=ty;
   }
  }
  int secondNearestDefender(float x,float y,boolean blueTeam,int first){int lo=blueTeam?0:11,hi=lo+11,best=lo;float bd=99;for(int i=lo;i<hi;i++){if(i==first||i%11==0)continue;float dx=xy[i][0]-x,dy=xy[i][1]-y,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  int bestMarkFor(int defender,boolean possessionBlue){int atk=possessionBlue?0:11,best=atk+8;float bd=99;for(int i=atk+8;i<=atk+10;i++){float dx=xy[i][0]-xy[defender][0],dy=xy[i][1]-xy[defender][1],d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  boolean finalThird(boolean blue,float x){return blue?x>.66f:x<.34f;}
  void shape(){updateIndependentMovement();}
  float clip(float v){return Math.max(.035f,Math.min(.965f,v));}
  protected void onDraw(Canvas c){
   float w=getWidth(),h=getHeight();c.drawColor(Color.rgb(12,20,27));
   float l=dpv(18),t=dpv(14),rr=w-dpv(18),bb=h-dpv(14),fw=rr-l,fh=bb-t;
   p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(34,126,72));c.drawRect(l,t,rr,bb,p);
   p.setColor(Color.argb(25,255,255,255));for(int i=0;i<10;i+=2)c.drawRect(l+i*fw/10,t,l+(i+1)*fw/10,bb,p);
   p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dpv(2));p.setColor(Color.WHITE);c.drawRect(l,t,rr,bb,p);c.drawLine(l+fw/2,t,l+fw/2,bb,p);c.drawCircle(l+fw/2,t+fh/2,fh*.12f,p);
   c.drawRect(l,t+fh*.27f,l+fw*.16f,t+fh*.73f,p);c.drawRect(l+fw*.84f,t+fh*.27f,rr,t+fh*.73f,p);
   c.drawRect(l,t+fh*.39f,l+fw*.065f,t+fh*.61f,p);c.drawRect(l+fw*.935f,t+fh*.39f,rr,t+fh*.61f,p);
   // visible goals/net
   p.setColor(Color.LTGRAY);c.drawRect(l-dpv(10),t+fh*.43f,l,t+fh*.57f,p);c.drawRect(rr,t+fh*.43f,rr+dpv(10),t+fh*.57f,p);
   p.setStyle(Paint.Style.FILL);
   for(int i=0;i<22;i++){
    float px=l+xy[i][0]*fw,py=t+xy[i][1]*fh,hx=headingX[i],hy=headingY[i],hl=(float)Math.sqrt(hx*hx+hy*hy);if(hl<.01f){hx=1;hy=0;hl=1;}hx/=hl;hy/=hl;
    float sx=-hy,sy=hx,phase=(float)Math.sin(visualStep[i]),bob=Math.abs(phase)*dpv(.8f);
    p.setStyle(Paint.Style.FILL);p.setColor(Color.argb(70,0,0,0));c.drawOval(px-dpv(6),py+dpv(9),px+dpv(6),py+dpv(12),p);
    // short SD torso
    p.setStrokeWidth(dpv(4));p.setColor(i<11?Color.rgb(40,130,255):Color.rgb(235,64,64));
    c.drawLine(px,py-dpv(1)-bob,px,py+dpv(5)-bob,p);
    // oversized head
    p.setColor(Color.rgb(244,205,170));c.drawCircle(px,py-dpv(7)-bob,dpv(5.4f),p);
    // very short swinging arms and legs
    p.setStrokeWidth(dpv(2.2f));p.setColor(i<11?Color.rgb(40,130,255):Color.rgb(235,64,64));
    c.drawLine(px,py+dpv(1)-bob,px+sx*dpv(4)+hx*phase*dpv(2),py+sy*dpv(4)-bob,p);
    c.drawLine(px,py+dpv(1)-bob,px-sx*dpv(4)-hx*phase*dpv(2),py-sy*dpv(4)-bob,p);
    p.setColor(Color.WHITE);c.drawLine(px,py+dpv(5)-bob,px+sx*dpv(2)+hx*phase*dpv(3),py+dpv(10)-bob,p);c.drawLine(px,py+dpv(5)-bob,px-sx*dpv(2)-hx*phase*dpv(3),py+dpv(10)-bob,p);
    p.setTextSize(dpv(6.5f));p.setColor(Color.rgb(30,30,30));c.drawText(""+(i%11+1),px-dpv(2),py-dpv(5)-bob,p);
   }
   // football: white body + black panels, not a plain dot
   float px=l+bx*fw,py=t+by*fh;p.setColor(Color.WHITE);c.drawCircle(px,py,dpv(6),p);p.setColor(Color.BLACK);c.drawCircle(px,py,dpv(2.2f),p);for(int k=0;k<5;k++){double a=k*Math.PI*2/5;c.drawCircle(px+(float)Math.cos(a)*dpv(3.7f),py+(float)Math.sin(a)*dpv(3.7f),dpv(1.1f),p);}
   if(eventText.contains("슈팅")||eventText.contains("GOAL")){p.setTextSize(dpv(18));p.setColor(Color.WHITE);p.setFakeBoldText(true);c.drawText(eventText,l+fw*.43f,t+dpv(28),p);p.setFakeBoldText(false);}
  }
  float dpv(float x){return x*getResources().getDisplayMetrics().density;}
 }
}