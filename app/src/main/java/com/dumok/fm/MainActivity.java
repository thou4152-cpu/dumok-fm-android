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
 String pressLine="중간",pressIntensity="보통",pressTrap="중립",lossReaction="즉시 압박";
 String buildupShape="자율",progressionStyle="균형",transitionAttack="균형",restSecurity="균형";
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
 String roleCode(String r){
  if(r==null)return "";
  if(r.equals("타겟맨"))return "TM";if(r.equals("연계형 공격수"))return "DLF";if(r.equals("침투형 공격수"))return "AF";
  if(r.equals("내려오는 공격수"))return "F9";if(r.equals("만능형"))return "CF";if(r.equals("인사이드 포워드"))return "IF";
  if(r.equals("윙어"))return "W";if(r.equals("침투형 윙어"))return "IW";if(r.equals("와이드 플레이메이커"))return "WP";
  if(r.equals("플레이메이커"))return "AP";if(r.equals("세컨드 스트라이커"))return "SS";if(r.equals("공격형 미드필더"))return "AM";
  if(r.equals("박스투박스"))return "BBM";if(r.equals("전진형 미드필더"))return "CM-A";if(r.equals("중앙 미드필더"))return "CM";
  if(r.equals("수비형 미드필더"))return "DM";if(r.equals("딥라잉 플레이메이커"))return "DLP";if(r.equals("볼위닝 미드필더"))return "BWM";
  if(r.equals("풀백"))return "FB";if(r.equals("공격형 풀백"))return "WB";if(r.equals("수비형 풀백"))return "FB-D";
  if(r.equals("센터백"))return "CD";if(r.equals("스토퍼"))return "STP";if(r.equals("커버"))return "COV";if(r.equals("빌드업 센터백"))return "BPD";
  if(r.equals("골키퍼"))return "GK";if(r.equals("스위퍼 키퍼"))return "SK";return r;
 }
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
  Button ch=bt("선수 변경");ch.setOnClickListener(v->{d.dismiss();pickSlot(idx);});b.addView(ch);ScrollView rs=new ScrollView(this);rs.setFillViewport(true);rs.addView(b);d.setContentView(rs);d.show();if(d.getWindow()!=null)d.getWindow().setLayout(-1,(int)(getResources().getDisplayMetrics().heightPixels*.82f));
 }
 String[] matchRoles(){ArrayList<String>a=new ArrayList<>();for(int i=0;i<XI.length;i++)if(XI[i]!=null)a.add(roleAt(i));return a.toArray(new String[0]);}
 String[] matchInst(){ArrayList<String>a=new ArrayList<>();for(int i=0;i<XI.length;i++)if(XI[i]!=null)a.add(instAt(i));return a.toArray(new String[0]);}
 String cycle(String cur,String[] a){for(int i=0;i<a.length;i++)if(a[i].equals(cur))return a[(i+1)%a.length];return a[0];}
 Button tacticCycle(String label,String value,String[] options,int kind){Button b=bt(label+" · "+value);b.setOnClickListener(v->{String cur=kind==0?pressLine:kind==1?pressIntensity:kind==2?pressTrap:kind==3?lossReaction:kind==4?buildupShape:kind==5?progressionStyle:kind==6?transitionAttack:restSecurity;String nv=cycle(cur,options);if(kind==0)pressLine=nv;else if(kind==1)pressIntensity=nv;else if(kind==2)pressTrap=nv;else if(kind==3)lossReaction=nv;else if(kind==4)buildupShape=nv;else if(kind==5)progressionStyle=nv;else if(kind==6)transitionAttack=nv;else restSecurity=nv;saveTacticPrefs();tactic();});return b;}
 void saveTacticPrefs(){getSharedPreferences("dumok_tactic",0).edit().putString("line",pressLine).putString("intensity",pressIntensity).putString("trap",pressTrap).putString("loss",lossReaction).putString("buildShape",buildupShape).putString("progression",progressionStyle).putString("transitionAttack",transitionAttack).putString("restSecurity",restSecurity).apply();}
 void loadPressPrefs(){android.content.SharedPreferences sp=getSharedPreferences("dumok_tactic",0);pressLine=sp.getString("line",pressLine);pressIntensity=sp.getString("intensity",pressIntensity);pressTrap=sp.getString("trap",pressTrap);lossReaction=sp.getString("loss",lossReaction);buildupShape=sp.getString("buildShape",buildupShape);progressionStyle=sp.getString("progression",progressionStyle);transitionAttack=sp.getString("transitionAttack",transitionAttack);restSecurity=sp.getString("restSecurity",restSecurity);}
 void tactic(){
  loadPressPrefs();frame("전술 · 드래그 배치");
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
  LinearLayout pc=card();pc.addView(tx("압박 전술 · 경기 엔진 직접 적용",15,Color.WHITE));pc.addView(tx("강한 압박은 높은 위치 탈취를 노리지만 실패하면 라인 사이 공간과 체력 부담이 커집니다.",11,MUTED));
  pc.addView(tacticCycle("압박 라인",pressLine,new String[]{"낮음","중간","높음"},0));
  pc.addView(tacticCycle("압박 강도",pressIntensity,new String[]{"소극적","보통","적극적","매우 적극적"},1));
  pc.addView(tacticCycle("압박 유도",pressTrap,new String[]{"중립","측면 유도","중앙 유도"},2));
  pc.addView(tacticCycle("공을 잃은 뒤",lossReaction,new String[]{"즉시 압박","재정비"},3));
   pc.addView(tx("공격 전술 · 기본 축구지능의 판단 성향을 조절",15,Color.WHITE));
   pc.addView(tx("전술은 정해진 패턴을 재생하지 않습니다. 후방 구조·전진 성향·전환 속도·역습 대비의 우선순위를 바꿉니다.",11,MUTED));
   pc.addView(tacticCycle("후방 구조",buildupShape,new String[]{"자율","3-2 지향","2-3 지향"},4));
   pc.addView(tacticCycle("전진 방식",progressionStyle,new String[]{"안정적","균형","직선적"},5));
   pc.addView(tacticCycle("공 탈취 후",transitionAttack,new String[]{"점유 재정비","균형","빠른 역습"},6));
   pc.addView(tacticCycle("역습 대비",restSecurity,new String[]{"공격적","균형","안정적"},7));body.addView(pc);
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
 String[] matchSlots(){
  ArrayList<String>a=new ArrayList<>();for(int i=0;i<XI.length;i++)if(XI[i]!=null)a.add(SLOT[i]);
  String[] out=new String[11];for(int i=0;i<11;i++)out[i]=i<a.size()?a.get(i):"CM";return out;
 }
 Player[] matchProfiles(){
  ArrayList<Player>a=new ArrayList<>();for(Player q:XI)if(q!=null)a.add(q);
  Player[] out=new Player[11];for(int i=0;i<11&&i<a.size();i++)out[i]=a.get(i);return out;
 }
 void match(Fixture f){
  if(xiCount()!=11){Toast.makeText(this,"경기 시작 전 선발 11명을 배치해주세요 ("+xiCount()+"/11)",Toast.LENGTH_LONG).show();tactic();return;}

  final Dialog d=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
  LinearLayout all=new LinearLayout(this);all.setOrientation(LinearLayout.HORIZONTAL);all.setBackgroundColor(BG);
  loadPressPrefs();SmoothPitch pitch=new SmoothPitch(this, tacticalCoords(), matchProfiles(), matchRoles(), matchInst(), matchSlots(),pressLine,pressIntensity,pressTrap,lossReaction,buildupShape,progressionStyle,transitionAttack,restSecurity);
  all.addView(pitch,new LinearLayout.LayoutParams(0,-1,4));
  LinearLayout panel=new LinearLayout(this);panel.setOrientation(LinearLayout.VERTICAL);panel.setPadding(dp(8),dp(8),dp(8),dp(8));panel.setBackgroundColor(Color.rgb(10,20,32));
  TextView score=tx(f.h.name+"\n0 - 0\n"+f.a.name,20,Color.WHITE);score.setGravity(Gravity.CENTER);panel.addView(score);
  TextView clock=tx("00:00",18,GREEN);clock.setGravity(Gravity.CENTER);panel.addView(clock);
  TextView action=tx("KICK OFF",15,Color.WHITE);action.setGravity(Gravity.CENTER);panel.addView(action);
  TextView stats=tx("슈팅 0 - 0\n유효슈팅 0 - 0\n점유율 50% - 50%",13,MUTED);panel.addView(stats);
  TextView log=tx("경기가 시작됩니다.",12,MUTED);panel.addView(log,new LinearLayout.LayoutParams(-1,0,1));
  Button manage=bt("전술 · 교체");panel.addView(manage);Button speed=bt("속도 x1");panel.addView(speed);all.addView(panel,new LinearLayout.LayoutParams(0,-1,1));
  d.setContentView(all);d.show();

  final int[] sec={0},hg={0},ag={0},shotsH={0},shotsA={0},sotH={0},sotA={0},possH={0},possA={0},delay={95};
  final boolean homeIsBlue=f.h.name.equals(club);final boolean[] paused={false};final int[] subsUsed={0},subWindows={0},stoppageEvents={0};final boolean[] windowOpen={false};
  final java.util.HashSet<Player> appeared=new java.util.HashSet<>();final java.util.HashMap<Player,Integer> minutes=new java.util.HashMap<>();
  for(int i=0;i<11;i++)if(pitch.simPlayer[i]!=null)appeared.add(pitch.simPlayer[i]);
  Handler H=new Handler(Looper.getMainLooper());

  Runnable game=new Runnable(){public void run(){
    if(paused[0]){H.postDelayed(this,120);return;}
    // Engine runs in small football-time steps. Score can ONLY change after pitch reports a real goal event.
    sec[0]+=4;
    for(int i=0;i<11;i++){Player mp=pitch.simPlayer[i];if(mp!=null)minutes.put(mp,minutes.getOrDefault(mp,0)+4);}
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

    int rawMin=sec[0]/60,mm=Math.min(90,rawMin),ss=sec[0]%60;
    score.setText(f.h.name+"\n"+hg[0]+" - "+ag[0]+"\n"+f.a.name);
    clock.setText(rawMin<=90?String.format(java.util.Locale.US,"%02d:%02d",mm,ss):String.format(java.util.Locale.US,"90+%d:%02d",rawMin-90,ss));
    action.setText(pitch.eventText);
    int tot=possH[0]+possA[0],ph=tot==0?50:(100*possH[0]/tot);
    stats.setText("슈팅 "+shotsH[0]+" - "+shotsA[0]+"\n유효슈팅 "+sotH[0]+" - "+sotA[0]+"\n점유율 "+ph+"% - "+(100-ph)+"%");
    if(pitch.newLog){String ev=pitch.eventText;if(ev.contains("파울")||ev.contains("경고")||ev.contains("오프사이드")||ev.contains("GOAL")||ev.contains("페널티"))stoppageEvents[0]++;log.setText(ev+"\n\n"+log.getText());pitch.newLog=false;}

    int addedMin=Math.min(5,Math.max(1,(subWindows[0]+stoppageEvents[0]/3)));int fullTimeSec=5400+addedMin*60;
    if(sec[0]>=fullTimeSec){
      finishMatchParticipants(f,hg[0],ag[0],appeared,minutes,pitch);action.setText("FULL TIME");
      speed.setText("경기 종료");speed.setOnClickListener(v->{d.dismiss();home();});
    }else H.postDelayed(this,delay[0]);
  }};
  manage.setOnClickListener(v->{paused[0]=true;final Dialog md=new Dialog(this);LinearLayout mb=new LinearLayout(this);mb.setOrientation(LinearLayout.VERTICAL);mb.setPadding(dp(12),dp(12),dp(12),dp(12));mb.setBackgroundColor(BG);windowOpen[0]=false;mb.addView(tx("경기 중 관리 · 교체 "+subsUsed[0]+"/5 · 교체 기회 "+subWindows[0]+"/3",18,Color.WHITE));mb.addView(tx("현재 선수 · 평점",13,GREEN));
   for(int i=0;i<11;i++){final int idx=i;Player q=pitch.simPlayer[i];if(q==null)continue;Button pb=bt(q.name+"   "+pitch.ratingText(i)+"   "+pitch.simRole[i]+(pitch.sentOff[i]?" 🟥":pitch.yellowCards[i]>0?" 🟨":""));pb.setOnClickListener(x->{if(subsUsed[0]>=5){Toast.makeText(this,"교체 5명을 모두 사용했습니다",Toast.LENGTH_SHORT).show();return;}if(subWindows[0]>=3&&!windowOpen[0]){Toast.makeText(this,"교체 기회 3회를 모두 사용했습니다",Toast.LENGTH_SHORT).show();return;}ArrayList<Player> bench=new ArrayList<>();for(Player z:squad){boolean on=false;for(int k=0;k<11;k++)if(pitch.simPlayer[k]==z)on=true;if(!on)bench.add(z);}if(bench.isEmpty()){Toast.makeText(this,"교체 가능한 선수가 없습니다",Toast.LENGTH_SHORT).show();return;}final Dialog sd=new Dialog(this);LinearLayout sb=new LinearLayout(this);sb.setOrientation(LinearLayout.VERTICAL);sb.setPadding(dp(10),dp(10),dp(10),dp(10));sb.setBackgroundColor(BG);sb.addView(tx(q.name+" OUT → 교체 선수",16,Color.WHITE));ScrollView sv=new ScrollView(this);LinearLayout sl=new LinearLayout(this);sl.setOrientation(LinearLayout.VERTICAL);for(Player z:bench){Button zz=bt(z.name+" · "+z.pos+" · CA "+z.ca+" · 체력 "+z.fitness);zz.setOnClickListener(y->{if(!windowOpen[0]){subWindows[0]++;windowOpen[0]=true;}pitch.substitute(idx,z);appeared.add(z);subsUsed[0]++;sd.dismiss();});sl.addView(zz);}sv.addView(sl);sb.addView(sv,new LinearLayout.LayoutParams(-1,dp(420)));sd.setContentView(sb);sd.show();});mb.addView(pb);}
   mb.addView(tx("포지션 · 역할 · 경기 중 즉시 변경",13,GREEN));
   for(int i=0;i<11;i++){final int idx=i;if(pitch.simPlayer[i]==null)continue;LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);Button role=bt((pitch.simSlot[i]==null?"?":pitch.simSlot[i])+" · "+pitch.matchRoleCode(i));role.setOnClickListener(x->{pitch.cycleMatchRole(idx);role.setText((pitch.simSlot[idx]==null?"?":pitch.simSlot[idx])+" · "+pitch.matchRoleCode(idx));});row.addView(role,new LinearLayout.LayoutParams(0,-2,1));Button move=bt("자리교환");move.setOnClickListener(x->{final Dialog pd=new Dialog(this);LinearLayout pbx=new LinearLayout(this);pbx.setOrientation(LinearLayout.VERTICAL);pbx.setPadding(dp(10),dp(10),dp(10),dp(10));pbx.setBackgroundColor(BG);pbx.addView(tx(pitch.simPlayer[idx].name+" ↔ 교환할 선수",16,Color.WHITE));for(int k=0;k<11;k++){if(k==idx||pitch.simPlayer[k]==null)continue;final int kk=k;Button q=bt(pitch.simPlayer[k].name+" · "+pitch.simSlot[k]);q.setOnClickListener(y->{pitch.swapTacticalSlots(idx,kk);pd.dismiss();md.dismiss();paused[0]=false;});pbx.addView(q);}ScrollView psv=new ScrollView(this);psv.addView(pbx);pd.setContentView(psv);pd.show();});row.addView(move);mb.addView(row);}
   mb.addView(tx("팀 지침 · 변경 즉시 매치엔진 반영",13,GREEN));Button pl=bt("압박 라인 · "+pitch.pressLine);pl.setOnClickListener(x->{pitch.pressLine=cycle(pitch.pressLine,new String[]{"낮음","중간","높음"});pl.setText("압박 라인 · "+pitch.pressLine);});mb.addView(pl);Button pi=bt("압박 강도 · "+pitch.pressIntensity);pi.setOnClickListener(x->{pitch.pressIntensity=cycle(pitch.pressIntensity,new String[]{"소극적","보통","적극적","매우 적극적"});pi.setText("압박 강도 · "+pitch.pressIntensity);});mb.addView(pi);Button pt=bt("압박 유도 · "+pitch.pressTrap);pt.setOnClickListener(x->{pitch.pressTrap=cycle(pitch.pressTrap,new String[]{"중립","측면 유도","중앙 유도"});pt.setText("압박 유도 · "+pitch.pressTrap);});mb.addView(pt);Button lr=bt("공을 잃은 뒤 · "+pitch.lossReaction);lr.setOnClickListener(x->{pitch.lossReaction=cycle(pitch.lossReaction,new String[]{"즉시 압박","재정비"});lr.setText("공을 잃은 뒤 · "+pitch.lossReaction);});mb.addView(lr);Button bs=bt("후방 구조 · "+pitch.buildupShape);bs.setOnClickListener(x->{pitch.buildupShape=cycle(pitch.buildupShape,new String[]{"자율","3-2 지향","2-3 지향"});bs.setText("후방 구조 · "+pitch.buildupShape);pitch.requestTacticalRescan();});mb.addView(bs);
   Button ps=bt("전진 방식 · "+pitch.progressionStyle);ps.setOnClickListener(x->{pitch.progressionStyle=cycle(pitch.progressionStyle,new String[]{"안정적","균형","직선적"});ps.setText("전진 방식 · "+pitch.progressionStyle);pitch.requestTacticalRescan();});mb.addView(ps);
   Button ta=bt("공 탈취 후 · "+pitch.transitionAttack);ta.setOnClickListener(x->{pitch.transitionAttack=cycle(pitch.transitionAttack,new String[]{"점유 재정비","균형","빠른 역습"});ta.setText("공 탈취 후 · "+pitch.transitionAttack);pitch.requestTacticalRescan();});mb.addView(ta);
   Button rs=bt("역습 대비 · "+pitch.restSecurity);rs.setOnClickListener(x->{pitch.restSecurity=cycle(pitch.restSecurity,new String[]{"공격적","균형","안정적"});rs.setText("역습 대비 · "+pitch.restSecurity);pitch.requestTacticalRescan();});mb.addView(rs);Button resume=bt("적용하고 경기 재개");resume.setOnClickListener(x->{getSharedPreferences("dumok_tactic",0).edit().putString("line",pitch.pressLine).putString("intensity",pitch.pressIntensity).putString("trap",pitch.pressTrap).putString("loss",pitch.lossReaction).apply();md.dismiss();paused[0]=false;});mb.addView(resume);ScrollView msv=new ScrollView(this);msv.addView(mb);md.setContentView(msv);md.setOnCancelListener(x->paused[0]=false);md.show();});
  speed.setOnClickListener(v->{delay[0]=delay[0]==95?55:delay[0]==55?28:95;speed.setText(delay[0]==95?"속도 x1":delay[0]==55?"속도 x2":"속도 x3");});
  H.postDelayed(game,500);
 }
 double teamAttack(){double s=0;int n=0;for(Player p:squad)if(p.starter){double run=p.ph[0]*1.4+p.ph[1]*1.5+p.me[3]*1.3;double tech=p.te[0]*1.5+p.te[1]+p.te[2]+p.me[0];s+=run+tech;n++;}return n==0?0:s/n;}
 void awardMatchXP(boolean attack){for(Player p:squad)if(p.starter){int[] cand=p.pos.equals("ST")||p.pos.equals("RW")||p.pos.equals("LW")?new int[]{0,1,2,8,11,16,17,18}:new int[]{3,10,9,14,22};int idx=cand[R.nextInt(cand.length)];if(idx>=16&&p.age>20)idx=R.nextInt(16);p.xp[idx]+=2.0*p.growth*p.professional;}}
 void finishMatchParticipants(Fixture f,int x,int y,java.util.Set<Player> appeared,java.util.Map<Player,Integer> seconds,SmoothPitch pitch){
  f.played=true;f.hg=x;f.ag=y;result(f.h,f.a,x,y);
  for(Fixture q:fixtures)if(!q.played&&q.date.equals(f.date)){int a=R.nextInt(4),b=R.nextInt(4);q.played=true;q.hg=a;q.ag=b;result(q.h,q.a,a,b);}
  for(Player p:appeared){int sec=seconds.getOrDefault(p,0);if(sec<=0)continue;p.apps++;double load=Math.min(1.0,sec/5400.0);p.fitness=Math.max(55,p.fitness-(int)Math.round((6+R.nextInt(10))*load));int idx=R.nextInt(p.age<=20?24:16);p.xp[idx]+=1.2*load*p.growth*p.professional;}
  now=now.plusDays(1);saveGame();
 }
 void finish(Fixture f,int x,int y){f.played=true;f.hg=x;f.ag=y;result(f.h,f.a,x,y);for(Fixture q:fixtures)if(!q.played&&q.date.equals(f.date)){int a=R.nextInt(4),b=R.nextInt(4);q.played=true;q.hg=a;q.ag=b;result(q.h,q.a,a,b);}for(Player p:squad)if(p.starter)p.fitness=Math.max(55,p.fitness-6-R.nextInt(10));now=now.plusDays(1);saveGame();}
 void result(Team h,Team a,int x,int y){h.p++;a.p++;h.gf+=x;h.ga+=y;a.gf+=y;a.ga+=x;if(x>y){h.w++;a.l++;h.pts+=3;}else if(x<y){a.w++;h.l++;a.pts+=3;}else{h.d++;a.d++;h.pts++;a.pts++;}}

 static class SmoothPitch extends View{
  Paint p=new Paint(1); Random r=new Random();
  Player[] simPlayer=new Player[22]; String[] simRole=new String[22],simInst=new String[22],simSlot=new String[22]; String pressLine,pressIntensity,pressTrap,lossReaction,buildupShape,progressionStyle,transitionAttack,restSecurity; int primaryPress=-1,coverPress=-1,blockPress=-1,pressTicks=0; boolean pressActive=false;
  float[][] xy=new float[22][2], base=new float[22][2], dest=new float[22][2], vel=new float[22][2];
   float[] thinkOffset=new float[22], runBias=new float[22], laneBias=new float[22], aggression=new float[22];
   float[] stridePhase=new float[22],headingX=new float[22],headingY=new float[22];
   float[][] stepTarget=new float[22][2];int[] stepHold=new int[22],turnHold=new int[22];
   float[][] prevXY=new float[22][2];float[] visualStep=new float[22];
   float[][] renderXY=new float[22][2];float[] renderHeadingX=new float[22],renderHeadingY=new float[22],moveBlend=new float[22];
   int[] roleState=new int[22],moveIntent=new int[22],intentHold=new int[22],yellowCards=new int[22]; boolean[] sentOff=new boolean[22]; int animFrame=0;float[] matchRating=new float[22];int lastPasser=-1;float lastActionThreat=0f;
  int restartType=0,restartTaker=-1;float restartX=.5f,restartY=.5f,offsideLineSnapshot=.5f,offsideBallSnapshot=.5f; // 1 indirect FK, 2 direct FK, 3 penalty
  float[][] principleTarget=new float[22][2];float[] perceptionLag=new float[22],riskMemory=new float[22];int transitionTicks=0;boolean previousBlue=true;int lastScanOwner=-2,lastScanState=-1;boolean forceTacticalScan=true;
  float[] perceivedPressure=new float[22],supportNeed=new float[22],scanQuality=new float[22],decisionConfidence=new float[22];
  float[][] perceivedBall=new float[22][2];int[] scanAge=new int[22];
  float bx=.5f,by=.5f,btx=.5f,bty=.5f,ballVX=0f,ballVY=0f,ballFriction=.982f; int owner=0,receiver=-1,state=0,ticks=0,dribbleDef=-1,dribblePhase=0;
  float[] angularVel=new float[22],locomotionSpeed=new float[22];int[] recoveryUntil=new int[22];
  int[] teamDuty=new int[22];float[][] coordinatedTarget=new float[22][2];
  int pendingActor=-1,pendingAction=0,pendingReceiver=-1,pendingContactFrame=-1;float pendingX=.5f,pendingY=.5f,pendingPower=0f;
  float lastBallX=.5f,lastBallY=.5f;int ballIdleFrames=0,dribbleTouchClock=0;float dribbleTouchX=.5f,dribbleTouchY=.5f;
  // states: 0 possession, 1 pass, 2 through ball, 3 cross, 4 shot, 5 save/reset, 6 goal celebration, 7 one-v-one dribble
  boolean ballFlying=false,lastPossessionBlue=true,lastEventBlue=true,shotFlag,onTargetFlag,goalFlag,newLog=true;
  String eventText="KICK OFF";
  Handler h=new Handler(Looper.getMainLooper());
  SmoothPitch(Context c,float[][] userShape,Player[] userPlayers,String[] userRoles,String[] userInst,String[] userSlots,String pl,String pi,String pt,String lr,String bs,String ps,String ta,String rs){
   super(c);pressLine=pl;pressIntensity=pi;pressTrap=pt;lossReaction=lr;buildupShape=bs;progressionStyle=ps;transitionAttack=ta;restSecurity=rs;
   if(userPlayers!=null)for(int i=0;i<11&&i<userPlayers.length;i++)simPlayer[i]=userPlayers[i];if(userRoles!=null)for(int i=0;i<11&&i<userRoles.length;i++)simRole[i]=userRoles[i];if(userInst!=null)for(int i=0;i<11&&i<userInst.length;i++)simInst[i]=userInst[i];if(userSlots!=null)for(int i=0;i<11&&i<userSlots.length;i++)simSlot[i]=userSlots[i];
   float[][] f={{.055f,.50f},{.20f,.14f},{.19f,.38f},{.19f,.62f},{.20f,.86f},{.35f,.38f},{.35f,.62f},{.52f,.18f},{.55f,.50f},{.52f,.82f},{.70f,.50f}};
   for(int i=0;i<11;i++){float ux=(userShape!=null&&i<userShape.length)?userShape[i][0]:f[i][0],uy=(userShape!=null&&i<userShape.length)?userShape[i][1]:f[i][1];base[i][0]=ux;base[i][1]=uy;base[i+11][0]=1-f[i][0];base[i+11][1]=1-f[i][1];}
   for(int i=0;i<22;i++){xy[i][0]=dest[i][0]=base[i][0];xy[i][1]=dest[i][1]=base[i][1];matchRating[i]=6.0f;}
   for(int i=0;i<22;i++){thinkOffset[i]=r.nextFloat()*60f;runBias[i]=.78f+r.nextFloat()*.48f;laneBias[i]=(r.nextFloat()-.5f)*.12f;aggression[i]=.72f+r.nextFloat()*.55f;}
   for(int i=0;i<22;i++){stridePhase[i]=r.nextFloat()*6.28f;headingX[i]=1f;}
   for(int i=0;i<22;i++){stepTarget[i][0]=xy[i][0];stepTarget[i][1]=xy[i][1];stepHold[i]=r.nextInt(3);turnHold[i]=0;}
   for(int i=0;i<22;i++){prevXY[i][0]=xy[i][0];prevXY[i][1]=xy[i][1];visualStep[i]=r.nextFloat()*6.28f;}
   for(int i=0;i<22;i++){renderXY[i][0]=xy[i][0];renderXY[i][1]=xy[i][1];renderHeadingX[i]=1f;moveBlend[i]=0f;perceptionLag[i]=.72f+r.nextFloat()*.38f;riskMemory[i]=r.nextFloat();scanQuality[i]=.45f+.35f*norm(A(i,"dec"))+.20f*norm(A(i,"vis"));perceivedBall[i][0]=bx;perceivedBall[i][1]=by;scanAge[i]=r.nextInt(5);}
   owner=0;bx=xy[0][0];by=xy[0][1];dribbleTouchX=bx;dribbleTouchY=by;h.post(anim);
  }
  // V6 Collective Coordination + physical contact actions.
  float pressCandidateScore(int i,boolean blue,float ballX,float ballY){
   float dx=ballX-xy[i][0],dy=ballY-xy[i][1],d=(float)Math.sqrt(dx*dx+dy*dy),sp=Math.max(.0008f,speedStateMax(i,locomotionSpeed[i])),eta=d/sp;
   float dl=Math.max(.001f,d),approach=(headingX[i]*dx/dl+headingY[i]*dy/dl+1f)*.5f,goal=blue?.055f:.945f;
   float goalSide=blue?(xy[i][0]<=ballX?1f:0f):(xy[i][0]>=ballX?1f:0f),cover=0f;
   int lo=blue?0:11,hi=lo+11;for(int k=lo;k<hi;k++)if(k!=i&&active(k)){float cx=xy[k][0]-(ballX+(goal-ballX)*.28f),cy=xy[k][1]-ballY;if(cx*cx+cy*cy<.018f){cover=1f;break;}}
   return -eta*.012f+.25f*approach+.23f*goalSide+.18f*cover+.10f*norm(A(i,"work"));
  }
  void assignDefensiveDuties(boolean blue,float ballX,float ballY){
   int lo=blue?0:11,hi=lo+11,p1=-1,p2=-1;float a=-999f,b=-999f;
   for(int i=lo;i<hi;i++){if(!active(i)||i%11==0)continue;float sc=pressCandidateScore(i,blue,ballX,ballY);if(sc>a){b=a;p2=p1;a=sc;p1=i;}else if(sc>b){b=sc;p2=i;}}
   for(int i=lo;i<hi;i++){if(!active(i))continue;if(animFrame<recoveryUntil[i])teamDuty[i]=6;else if(i==p1)teamDuty[i]=1;else if(i==p2&&pressIntensity.equals("높음"))teamDuty[i]=2;else{float bp=blue?base[i][0]:1f-base[i][0],c=Math.abs(base[i][1]-.5f);teamDuty[i]=bp<.31f?5:c<.20f?3:4;}}
  }
  float teammateIntentCrowd(int j,float tx,float ty,boolean blue){int lo=blue?0:11,hi=lo+11;float v=0;for(int k=lo;k<hi;k++){if(k==j||!active(k))continue;float dx=coordinatedTarget[k][0]-tx,dy=coordinatedTarget[k][1]-ty,d=(float)Math.sqrt(dx*dx+dy*dy);if(d<.13f)v+=1f-d/.13f;}return Math.min(1f,v/2.2f);}
  void coordinateTeamShape(boolean blue,float ballX,float ballY){int lo=blue?0:11,hi=lo+11;for(int i=lo;i<hi;i++){coordinatedTarget[i][0]=dest[i][0];coordinatedTarget[i][1]=dest[i][1];}
   for(int i=lo;i<hi;i++){if(!active(i)||i==owner)continue;float tx=dest[i][0],ty=dest[i][1],crowd=teammateIntentCrowd(i,tx,ty,blue);if(crowd>.20f){float side=base[i][1]<.5f?-1f:1f;ty=clip(ty+side*(.055f+.075f*crowd));}
    if(Math.abs(base[i][1]-.5f)>.25f&&Math.abs(ballY-.5f)<.30f)ty=clip(.72f*ty+.28f*base[i][1]);if(teamDuty[i]==5)tx=clip(blue?Math.min(tx,ballX-.09f):Math.max(tx,ballX+.09f));coordinatedTarget[i][0]=tx;coordinatedTarget[i][1]=ty;dest[i][0]=tx;dest[i][1]=ty;}}
  void applyDutyTarget(int j,boolean blue,float ballX,float ballY,float[] out){float dir=attackDir(blue),ownGoal=blue?.055f:.945f;if(teamDuty[j]==1){out[0]=ballX-dir*.018f;out[1]=ballY;}else if(teamDuty[j]==2){out[0]=ballX-dir*.075f;out[1]=clip(.65f*ballY+.35f*.5f);}else if(teamDuty[j]==3){out[0]=clip(ballX-dir*.13f);out[1]=clip(.58f*ballY+.42f*.5f);}else if(teamDuty[j]==5){out[0]=clip(.72f*out[0]+.28f*ownGoal);out[1]=clip(.82f*out[1]+.18f*.5f);}}
  void queueKick(int actor,int action,int recv,float tx,float ty,float power){if(actor<0||owner!=actor)return;pendingActor=actor;pendingAction=action;pendingReceiver=recv;pendingX=tx;pendingY=ty;pendingPower=power;float dx=tx-xy[actor][0],dy=ty-xy[actor][1],a=(float)Math.atan2(dy,dx);headingX[actor]=(float)Math.cos(a);headingY[actor]=(float)Math.sin(a);pendingContactFrame=animFrame+5+(int)(4*(1f-norm(A(actor,"tech"))));roleState[actor]=7;}
  void resolvePhysicalAction(){if(pendingActor<0||animFrame<pendingContactFrame)return;if(pendingActor>=22){pendingActor=-1;pendingAction=0;pendingContactFrame=-1;return;}int a=pendingActor;if(owner!=a||!active(a)){pendingActor=-1;pendingAction=0;return;}float footReach=.018f+.004f*norm(A(a,"tech")),footX=xy[a][0]+headingX[a]*footReach,footY=xy[a][1]+headingY[a]*footReach;
   float dx=bx-footX,dy=by-footY,d=(float)Math.sqrt(dx*dx+dy*dy);if(d>.0215f){pendingActor=-1;pendingAction=0;requestTacticalRescan();return;}
   receiver=(pendingReceiver>=0&&pendingReceiver<22)?pendingReceiver:-1;state=pendingAction;lastPasser=a;lastEventBlue=a<11;kickBall(pendingX,pendingY,pendingPower);pendingActor=-1;pendingAction=0;pendingContactFrame=-1;}
  // V5 locomotion + ball physics. Football brain chooses intent; this layer alone moves bodies and ball.
  float angleDiff(float a,float b){float d=b-a;while(d>(float)Math.PI)d-=6.2831853f;while(d<-(float)Math.PI)d+=6.2831853f;return d;}
  float speedStateMax(int i,float current){
   float pace=norm(A(i,"pace")); // walk / jog / run / sprint bands
   if(current<.00055f)return .00072f+.00028f*pace;
   if(current<.00115f)return .00128f+.00048f*pace;
   if(current<.00185f)return .00192f+.00072f*pace;
   return .00255f+.00100f*pace;
  }
  float maxTurnPerFrame(int i,float speed){
   float ag=.45f+.55f*norm(A(i,"agi"));
   // At high speed the body cannot instantly point at a new destination.
   float t=speed<.00065f?.115f:speed<.00135f?.082f:speed<.00215f?.052f:.034f;
   return t*ag;
  }
  void applyLocalAvoidance(int i,float[] desired){
   float ax=0f,ay=0f;for(int j=0;j<22;j++){if(i==j||!active(j))continue;
    float px=(xy[i][0]+vel[i][0]*7f)-(xy[j][0]+vel[j][0]*7f),py=(xy[i][1]+vel[i][1]*7f)-(xy[j][1]+vel[j][1]*7f);
    float d2=px*px+py*py;if(d2>.000001f&&d2<.00145f){float d=(float)Math.sqrt(d2),w=(.038f-d)/.038f;if(w>0){ax+=px/d*w;ay+=py/d*w;}}
   }
   desired[0]+=ax*.42f;desired[1]+=ay*.42f;
  }
  void integratePlayer(int i){
   float dx=dest[i][0]-xy[i][0],dy=dest[i][1]-xy[i][1],dl=(float)Math.sqrt(dx*dx+dy*dy);
   float speed=(float)Math.sqrt(vel[i][0]*vel[i][0]+vel[i][1]*vel[i][1]),accN=norm(A(i,"acc")),agiN=norm(A(i,"agi"));
   float hx=headingX[i],hy=headingY[i],ha=(float)Math.atan2(hy,hx);
   if(dl>.0015f){
    float[] wish={dx/dl,dy/dl};applyLocalAvoidance(i,wish);float wl=(float)Math.sqrt(wish[0]*wish[0]+wish[1]*wish[1]);if(wl>.001f){wish[0]/=wl;wish[1]/=wl;}
    float wanted=(float)Math.atan2(wish[1],wish[0]),da=angleDiff(ha,wanted),turn=maxTurnPerFrame(i,speed);float step=Math.max(-turn,Math.min(turn,da));ha+=step;angularVel[i]=step;
    headingX[i]=(float)Math.cos(ha);headingY[i]=(float)Math.sin(ha);
    float max=speedStateMax(i,speed);boolean urgent=roleState[i]==1||roleState[i]==2||roleState[i]==4||animFrame<recoveryUntil[i];if(!urgent)max*=.76f;
    // Braking distance from v²/2a; sharp turns require deceleration before re-acceleration.
    float accel=.000038f+.000075f*accN,decel=.000060f+.000105f*agiN;
    float brakeDist=(speed*speed)/(2f*Math.max(.00001f,decel));float target=max;if(dl<brakeDist+.010f)target*=Math.max(.10f,dl/(brakeDist+.010f));
    if(Math.abs(da)>.72f)target*=.38f;if(Math.abs(da)>1.45f)target*=.16f;
    float ns=speed;if(ns<target)ns=Math.min(target,ns+accel);else ns=Math.max(target,ns-decel);
    vel[i][0]=headingX[i]*ns;vel[i][1]=headingY[i]*ns;locomotionSpeed[i]=ns;
   }else{
    float decel=.000065f+.00011f*agiN,ns=Math.max(0f,speed-decel);vel[i][0]=speed>.00001f?vel[i][0]/speed*ns:0;vel[i][1]=speed>.00001f?vel[i][1]/speed*ns:0;locomotionSpeed[i]=ns;
   }
   xy[i][0]=clip(xy[i][0]+vel[i][0]);xy[i][1]=clip(xy[i][1]+vel[i][1]);visualStep[i]+=.07f+Math.min(.60f,locomotionSpeed[i]*170f);separateKeeper(i);
  }
  void kickBall(float tx,float ty,float power){
   float dx=tx-bx,dy=ty-by,d=(float)Math.sqrt(dx*dx+dy*dy);if(d<.001f){dx=owner<11?1f:-1f;dy=0;d=1;}
   ballVX=dx/d*power;ballVY=dy/d*power;ballFlying=true;owner=-1;
  }
  void integrateBall(){
   if(!ballFlying)return;
   bx+=ballVX;by+=ballVY;ballVX*=ballFriction;ballVY*=ballFriction;
   // Grass friction is continuous; no target snapping/guided-ball tracking.
   float sp=(float)Math.sqrt(ballVX*ballVX+ballVY*ballVY);if(sp<.00125f){ballVX*=.88f;ballVY*=.88f;}
   if(by<.035f||by>.965f){by=clip(by);ballVY*=-.38f;}
   if(bx<.010f||bx>.990f){bx=Math.max(.01f,Math.min(.99f,bx));ballVX*=-.28f;}
  }
  void beginRecovery(int defender,boolean defendingBlue){
   if(defender<0)return;recoveryUntil[defender]=animFrame+80+(int)(45*(1f-norm(A(defender,"acc"))));roleState[defender]=4;requestTacticalRescan();
  }
  void applyRecoveryTarget(int j,boolean blue,float ballX,float ballY,float[] out){
   if(animFrame>=recoveryUntil[j])return;float ownGoal=blue?.055f:.945f,dir=blue?1f:-1f;
   // Recover goal-side first, then curve toward the carrier/central lane instead of freezing after being beaten.
   float goalSideX=ballX-dir*(.075f+.035f*norm(A(j,"pos")));out[0]=clip((goalSideX+ownGoal*.22f)/1.22f);
   out[1]=clip(.68f*ballY+.32f*.5f);
  }
  Runnable anim=new Runnable(){public void run(){
   animFrame++;
   for(int i=0;i<22;i++){prevXY[i][0]=xy[i][0];prevXY[i][1]=xy[i][1];}
   for(int i=0;i<22;i++){if(active(i))integratePlayer(i);}
   sanitizeGoalAreas();
   resolvePhysicalAction();
   if(ballFlying){integrateBall();}
   else if(owner>=0&&active(owner)){float spd=(float)Math.sqrt(vel[owner][0]*vel[owner][0]+vel[owner][1]*vel[owner][1]);float hx=headingX[owner],hy=headingY[owner],hl=(float)Math.sqrt(hx*hx+hy*hy);if(hl<.01f){hx=owner<11?1f:-1f;hy=0;hl=1;}hx/=hl;hy/=hl;
    dribbleTouchClock++;int touchEvery=state==7?5:(spd>.0021f?7:10);if(dribbleTouchClock>=touchEvery){dribbleTouchClock=0;float lead=state==7?.024f:(.012f+Math.min(.012f,spd*4.2f));dribbleTouchX=clip(xy[owner][0]+hx*lead);dribbleTouchY=clip(xy[owner][1]+hy*lead);}
    float follow=state==7?.30f:.22f;bx+=(dribbleTouchX-bx)*follow;by+=(dribbleTouchY-by)*follow;}
   if(state==0 && h.getLooper()!=null && animFrame%3==0) updateIndependentMovement();
   ballContinuityWatch();
   invalidate();h.postDelayed(this,16);
  }};
  void playerSpacing(){ /* V6.1.1: removed legacy direct coordinate pushing. */ }

  boolean inGoalMouth(int i){boolean b=i<11;return (b?xy[i][0]>.925f:xy[i][0]<.075f)&&xy[i][1]>.39f&&xy[i][1]<.61f;}
  boolean inBox(int i){boolean b=i<11;return (b?xy[i][0]>.80f:xy[i][0]<.20f)&&xy[i][1]>.25f&&xy[i][1]<.75f;}
  void separateKeeper(int i){if(i%11==0)return;boolean b=i<11;int g=b?11:0;float dx=xy[i][0]-xy[g][0],dy=xy[i][1]-xy[g][1],d=(float)Math.sqrt(dx*dx+dy*dy);if(d<.035f){float nx=d>.001f?dx/d:(b?-1f:1f),ny=d>.001f?dy/d:0f;xy[i][0]=clip(xy[g][0]+nx*.037f);xy[i][1]=clip(xy[g][1]+ny*.037f);}}
  void sanitizeGoalAreas(){
   for(int i=0;i<22;i++){
    if(!active(i)||i%11==0)continue;
    boolean blue=i<11;boolean activeAttacker=(i==owner)&&((blue&&xy[i][0]>.82f)||(!blue&&xy[i][0]<.18f));
    // Nobody except the keeper should camp inside the goal mouth. The ball carrier may enter the box,
    // but is forced to finish before physically running through the keeper/goal line.
    if(!activeAttacker&&xy[i][1]>.385f&&xy[i][1]<.615f){
      if(xy[i][0]<.085f)xy[i][0]=.085f;if(xy[i][0]>.915f)xy[i][0]=.915f;
    }
   }
  }
  float looseBallClaimScore(int i){
   if(!active(i))return -99f;float dx=bx-xy[i][0],dy=by-xy[i][1],d=(float)Math.sqrt(dx*dx+dy*dy);
   float sp=Math.max(.0007f,speedStateMax(i,locomotionSpeed[i])),eta=d/sp;
   float dl=Math.max(.001f,d),toward=(headingX[i]*dx/dl+headingY[i]*dy/dl+1f)*.5f;
   boolean blue=i<11;float contest=spaceAt(bx,by,blue),duty=(teamDuty[i]==1||teamDuty[i]==6)?.16f:teamDuty[i]==5?-.10f:0f;
   return -eta*.012f+.22f*toward+.18f*contest+duty+.08f*norm(A(i,"ant"));
  }
  int bestLooseBallClaimant(){
   int best=-1;float score=-999f;for(int i=0;i<22;i++){float v=looseBallClaimScore(i);if(v>score){score=v;best=i;}}return best;
  }
  void ballContinuityWatch(){
   if(pendingActor>=0)return;
   float dx=bx-lastBallX,dy=by-lastBallY,m=dx*dx+dy*dy;lastBallX=bx;lastBallY=by;
   if(m<.0000007f)ballIdleFrames++;else ballIdleFrames=0;
   if(ballFlying){if(ballIdleFrames>90){ballFlying=false;ballVX=ballVY=0f;receiver=-1;state=0;requestTacticalRescan();}return;}
   if(owner<0){int n=bestLooseBallClaimant();if(n>=0){dest[n][0]=bx;dest[n][1]=by;if(distToBall(n)<.030f){owner=n;state=0;receiver=-1;ballFlying=false;ballVX=ballVY=0f;ballIdleFrames=0;requestTacticalRescan();}}return;}
   // A stationary keeper must distribute; an outfield carrier must make a football action.
   if(ballIdleFrames>75&&state==0){boolean blue=owner<11;if(owner%11==0)pass(blue);else if(inBox(owner)&&(owner%11)>=8)shoot(blue);else pass(blue);ballIdleFrames=0;}
  }
  float distToBall(int i){float dx=xy[i][0]-bx,dy=xy[i][1]-by;return (float)Math.sqrt(dx*dx+dy*dy);}
  int nearestToBall(){int best=0;float bd=99;for(int i=0;i<22;i++){float dx=xy[i][0]-bx,dy=xy[i][1]-by,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  void rescueDeadBall(){
   if(ballFlying||owner>=0)return;
   int n=bestLooseBallClaimant();if(n<0)return;
   dest[n][0]=bx;dest[n][1]=by;
   if(distToBall(n)<.030f){owner=n;receiver=-1;state=0;ballFlying=false;ballVX=ballVY=0f;requestTacticalRescan();}
  }
  void rate(int i,float d){if(i<0||i>=22)return;matchRating[i]=Math.max(1f,Math.min(10f,matchRating[i]+d));}
  String ratingText(int i){return String.format(java.util.Locale.US,"%.1f",matchRating[i]);}
  String matchRoleCode(int i){String r=R(i);if(r==null)return "";if(r.contains("타겟"))return "TM";if(r.contains("인사이드"))return "IF";if(r.contains("윙어"))return "W";if(r.contains("플레이메이커"))return "AP";if(r.contains("박스투박스"))return "BBM";if(r.contains("수비형 미드"))return "DM";if(r.contains("풀백"))return "FB";if(r.contains("센터백"))return "CD";if(r.contains("스위퍼"))return "SK";if(r.contains("골키퍼"))return "GK";if(r.contains("공격수")||r.contains("스트라이커"))return "AF";return r.length()>7?r.substring(0,7):r;}
  void swapTacticalSlots(int a,int b){if(a<0||b<0||a>=11||b>=11||a==b)return;float x=base[a][0],y=base[a][1];base[a][0]=base[b][0];base[a][1]=base[b][1];base[b][0]=x;base[b][1]=y;String z=simSlot[a];simSlot[a]=simSlot[b];simSlot[b]=z;shape();invalidate();}
  void cycleMatchRole(int i){if(i<0||i>=11)return;String sl=simSlot[i]==null?"CM":simSlot[i];String[] rr;if(sl.equals("GK"))rr=new String[]{"골키퍼","스위퍼 키퍼"};else if(sl.contains("CB"))rr=new String[]{"센터백","스토퍼","커버","빌드업 센터백"};else if(sl.equals("LB")||sl.equals("RB"))rr=new String[]{"풀백","공격형 풀백","수비형 풀백"};else if(sl.contains("DM"))rr=new String[]{"수비형 미드필더","딥라잉 플레이메이커","볼위닝 미드필더"};else if(sl.contains("CM"))rr=new String[]{"중앙 미드필더","박스투박스","전진형 미드필더","플레이메이커"};else if(sl.contains("AM"))rr=new String[]{"공격형 미드필더","플레이메이커","세컨드 스트라이커"};else if(sl.equals("LW")||sl.equals("RW"))rr=new String[]{"윙어","인사이드 포워드","침투형 윙어","와이드 플레이메이커"};else rr=new String[]{"침투형 공격수","타겟맨","연계형 공격수","내려오는 공격수","만능형"};String cur=R(i);int k=0;for(int n=0;n<rr.length;n++)if(rr[n].equals(cur)){k=(n+1)%rr.length;break;}simRole[i]=rr[k];shape();invalidate();}

  String shortName(int i){Player q=simPlayer[i];if(q==null)return "P"+(i%11+1);String n=q.name;return n.length()>7?n.substring(0,7):n;}
  void substitute(int idx,Player q){if(idx<0||idx>=11||q==null)return;simPlayer[idx]=q;matchRating[idx]=6.0f;eventText=q.name+" 교체 투입";newLog=true;invalidate();}
  void step(){
   if(restartType>0){takeRestart();return;}
   ticks++; lastPossessionBlue=owner<11;
   if(state==1){if(closeBall()){int recv=receiver;float gain=Math.max(-.12f,laneThreat(xy[recv][0],xy[recv][1],recv<11)-lastActionThreat);rate(lastPasser,.015f+Math.max(0f,gain)*.16f);owner=recv;receiver=-1;ballFlying=false;ballVX=ballVY=0f;state=0;dribbleTouchX=clip(xy[owner][0]+headingX[owner]*(.006f+.008f*norm(A(owner,"touch"))));dribbleTouchY=clip(xy[owner][1]+headingY[owner]*(.006f+.008f*norm(A(owner,"touch"))));requestTacticalRescan();eventText=stateName(owner)+" 패스 성공";newLog=true;shape();}return;}
   if(state==2){
    if(receiver>=0){dest[receiver][0]=btx;dest[receiver][1]=bty;float dx=bx-(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][0]:bx):bx),dy=by-(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][1]:by):by);
     if(dx*dx+dy*dy<.00075f){float gain=Math.max(0f,laneThreat((receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][0]:bx):bx),(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][1]:by):by),receiver<11)-lastActionThreat);rate(lastPasser,.07f+.18f*gain);owner=receiver;receiver=-1;ballFlying=false;state=0;eventText="스루패스 연결";newLog=true;shape();}}
    return;
   }
   if(state==3){if(closeBall()){float gain=Math.max(0f,laneThreat((receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][0]:bx):bx),(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][1]:by):by),receiver<11)-lastActionThreat);rate(lastPasser,.055f+.16f*gain);owner=receiver;receiver=-1;ballFlying=false;state=0;eventText="크로스 연결";newLog=true;shape();}return;}
   if(state==4){ // shot must physically reach goal before outcome exists
    if(closeBall()){
     boolean blue=lastEventBlue;int shooter=owner;shotFlag=true;rate(shooter,.06f);
     float fq=finishQ(owner);boolean on=r.nextFloat()<(.48f+.40f*fq);
     if(on){onTargetFlag=true;boolean goal=r.nextFloat()<(.10f+.32f*fq);
       if(goal){rate(shooter,1.05f);if(lastPasser>=0&&lastPasser!=shooter)rate(lastPasser,.38f);goalFlag=true;state=6;eventText="⚽ GOAL!";newLog=true;ballFlying=false;bx=blue?.985f:.015f;by=.5f;ticks=0;}
       else{rate(shooter,.08f);rate(blue?11:0,.22f);state=5;eventText="🧤 골키퍼 선방";newLog=true;owner=blue?11:0;receiver=-1;ballFlying=false;ticks=0;}
     }else{rate(shooter,-.05f);state=5;eventText="슛이 골문을 벗어납니다";newLog=true;owner=blue?11:0;receiver=-1;ballFlying=false;ticks=0;}
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
     if(owner<0||owner>=22||dribbleDef<0||dribbleDef>=22||!active(dribbleDef)){state=0;dribbleDef=-1;dribblePhase=0;requestTacticalRescan();return;}
     float side=(xy[owner][1] <= xy[dribbleDef][1])?-.055f:.055f;
     float aq=attack1v1(owner),dq=defend1v1(dribbleDef),win=Math.max(.18f,Math.min(.82f,.50f+(aq-dq)*.65f));
     if(r.nextFloat()<win){
      float burst=.075f+.065f*norm(A(owner,"acc"));dest[owner][0]=clip(xy[owner][0]+dir*burst);dest[owner][1]=clip(xy[owner][1]+side*(.65f+.55f*norm(A(owner,"agi"))));
      if(dribbleDef>=0){beginRecovery(dribbleDef,dribbleDef<11);}
      dribblePhase=2;eventText="⚡ "+styleOf(owner)+" · 수비수를 벗겨냅니다";newLog=true;
     }else{owner=dribbleDef;state=0;dribbleDef=-1;dribblePhase=0;eventText="수비가 1대1을 막아냅니다";newLog=true;shape();}
     return;
    }
    if(dribblePhase==2){float dx=dest[owner][0]-xy[owner][0],dy=dest[owner][1]-xy[owner][1];if(dx*dx+dy*dy<.0012f){int beaten=dribbleDef;state=0;dribbleDef=-1;dribblePhase=0;rate(owner,.08f+.08f*laneThreat(xy[owner][0],xy[owner][1],owner<11));if(beaten>=0)rate(beaten,-.035f);eventText="돌파 성공 · 전진";newLog=true;shape();}return;}
   }
   if(state==5){if(ticks>=8){state=0;eventText="골키퍼가 빠르게 다시 전개합니다";newLog=true;pass(owner<11);}return;}
   if(state==6){if(ticks>22){resetKickoff(lastEventBlue?11:0);}return;}

   if(ticks%4!=0)return;
   int pressing=nearestOpponent(owner);if(pressing<0){requestTacticalRescan();return;}float pressChance=pressActive&&pressing==primaryPress?(.16f+.34f*pressIntensityN()+.18f*pressureIQ(pressing)):.10f;if(dist(owner,pressing)<.052f && r.nextFloat()<Math.min(.72f,pressChance)){tackle(pressing);return;}
   shape();
   boolean blue=owner<11;int b=blue?0:11,local=owner-b;float x=xy[owner][0],progress=blue?x:1-x;

   // Defenders can actually win the ball when close to carrier.
   int opp=nearestOpponent(owner);if(opp<0){requestTacticalRescan();return;}float dx=xy[opp][0]-x,dy=xy[opp][1]-xy[owner][1];
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
   if(inBox(owner)&&ownLocal!=0){int enemyGK=blue?11:0;float gd=dist(owner,enemyGK);if(gd<.14f||(blue?x>.89f:x<.11f)){shoot(blue);return;}}
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
   // Universal decision: an unpressed carrier with open grass can advance and force the block to react.
   float carryValue=carrierAdvanceValue(owner,blue),bestPassValue=-99f;int probe=chooseUniversalPass(owner,blue);if(probe>=0)bestPassValue=passOptionValue(owner,probe,blue);
   if(local<=7&&carryValue>bestPassValue+.06f&&r.nextFloat()<Math.min(.82f,.38f+.42f*carryValue)){float dir=blue?.050f:-.050f;dest[owner][0]=clip(x+dir*(.75f+.50f*norm(A(owner,"acc"))));dest[owner][1]=clip(xy[owner][1]+(r.nextFloat()-.5f)*.018f);eventText="공간을 보고 전진 운반";requestTacticalRescan();return;}
   // Carrier may dribble a short distance; ball remains visibly at feet.
   float action=r.nextFloat();
   if(local>=8 && action<.46f){ // attackers default to advancing, not recycling
    float dir=blue?.070f:-.070f;dest[owner][0]=clip(x+dir);dest[owner][1]=clip(xy[owner][1]+(r.nextFloat()-.5f)*.040f);eventText="공격수가 골문을 향해 전진";return;
   }
   if(local>=5 && action<.27f){float dir=blue?.052f:-.052f;dest[owner][0]=clip(x+dir);eventText="미드필더 전진 운반";return;}
   pass(blue);
  }
  float dist(int a,int b){if(a<0||a>=22||b<0||b>=22)return 99f;float dx=xy[a][0]-xy[b][0],dy=xy[a][1]-xy[b][1];return (float)Math.sqrt(dx*dx+dy*dy);}
  void startDribbleDuel(int defender){state=7;dribbleDef=defender;dribblePhase=0;ballFlying=false;eventText="1대1 돌파 시도";newLog=true;}
  boolean active(int i){if(i<0||i>=22)return false;return i>=0&&i<22&&!sentOff[i];}
  int nearestActiveTo(float x,float y,boolean blue){int lo=blue?0:11,hi=lo+11,best=-1;float bd=99f;for(int i=lo;i<hi;i++){if(!active(i))continue;float dx=xy[i][0]-x,dy=xy[i][1]-y,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  void dismiss(int i,String why){if(i<0)return;sentOff[i]=true;rate(i,-.45f);if(owner==i)owner=nearestActiveTo(bx,by,i>=11);dest[i][0]=xy[i][0]=i<11?.01f:.99f;dest[i][1]=xy[i][1]=.98f;eventText="🟥 "+why+" · 퇴장";newLog=true;}
  void book(int i){if(i<0)return;yellowCards[i]++;rate(i,-.06f);if(yellowCards[i]>=2)dismiss(i,"두 번째 경고");}
  boolean inPenaltyArea(float x,float y,boolean blueAttack){return blueAttack?x>.84f&&Math.abs(y-.5f)<.22f:x<.16f&&Math.abs(y-.5f)<.22f;}
  void setupRestart(boolean attackingBlue,float x,float y,int type){requestTacticalRescan();restartType=type;restartX=clip(x);restartY=clip(y);restartTaker=nearestActiveTo(restartX,restartY,attackingBlue);owner=restartTaker;receiver=-1;ballFlying=false;state=0;bx=restartX;by=restartY;if(restartTaker>=0){xy[restartTaker][0]=restartX;xy[restartTaker][1]=restartY;dest[restartTaker][0]=restartX;dest[restartTaker][1]=restartY;}shape();}
  void takePenalty(boolean blue){
   owner=restartTaker;int keeper=blue?11:0;float kick=.52f*finishQ(owner)+.28f*norm(A(owner,"comp"))+.20f*norm(A(owner,"fin")),save=.55f*norm(A(keeper,"agi"))+.45f*norm(A(keeper,"dec"));
   shotFlag=true;onTargetFlag=true;lastEventBlue=blue;float goalP=Math.max(.55f,Math.min(.91f,.76f+(kick-save)*.22f));
   if(r.nextFloat()<goalP){rate(owner,.72f);goalFlag=true;state=6;eventText="⚽ 페널티킥 GOAL!";ballFlying=false;bx=blue?.985f:.015f;by=.5f;ticks=0;}
   else{rate(owner,-.12f);rate(keeper,.42f);state=5;eventText="🧤 페널티킥 선방!";this.owner=keeper;ballFlying=false;ticks=0;}newLog=true;
  }
  void takeDirectFreeKick(boolean blue){
   owner=restartTaker;int keeper=blue?11:0;float goalX=blue?1f:0f,dist=(float)Math.sqrt((goalX-bx)*(goalX-bx)+(by-.5f)*(by-.5f));
   float technique=.42f*norm(A(owner,"fin"))+.30f*norm(A(owner,"comp"))+.28f*norm(A(owner,"touch")),keeperQ=.55f*norm(A(keeper,"agi"))+.45f*norm(A(keeper,"dec"));
   float distancePenalty=Math.max(0f,(dist-.18f)*1.25f),wallPenalty=(Math.abs(by-.5f)<.20f?.10f:.04f),onP=Math.max(.22f,Math.min(.78f,.62f+technique*.22f-distancePenalty-wallPenalty));
   shotFlag=true;lastEventBlue=blue;if(r.nextFloat()<onP){onTargetFlag=true;float gp=Math.max(.06f,Math.min(.38f,.18f+(technique-keeperQ)*.22f-distancePenalty*.25f));if(r.nextFloat()<gp){rate(owner,.85f);goalFlag=true;state=6;eventText="⚽ 직접 프리킥 GOAL!";ballFlying=false;bx=blue?.985f:.015f;by=.5f;ticks=0;}else{rate(keeper,.24f);state=5;eventText="🧤 프리킥 선방";this.owner=keeper;ballFlying=false;ticks=0;}}else{rate(owner,-.04f);state=5;eventText="프리킥이 골문을 벗어납니다";this.owner=keeper;ballFlying=false;ticks=0;}newLog=true;
  }
  void takeRestart(){
   if(restartType==0||restartTaker<0)return;boolean blue=restartTaker<11;int t=restartType;restartType=0;
   if(t==3){takePenalty(blue);return;}
   if(t==2&&laneThreat(bx,by,blue)>.48f&&r.nextFloat()<.66f){takeDirectFreeKick(blue);return;}
   owner=restartTaker;pass(blue);eventText=t==1?"간접 프리킥 · 재개":"프리킥 · 재개";newLog=true;
  }
  void tackle(int defender){
   if(defender<0)return;
   float aq=attack1v1(owner),dq=defend1v1(defender),org=pressureIQ(defender);
   float foulChance=Math.max(.015f,Math.min(.16f,.075f+.06f*(1f-dq)+.025f*pressIntensityN()));
   if(r.nextFloat()<foulChance){boolean attackingBlue=owner<11;float fx=xy[owner][0],fy=xy[owner][1],danger=laneThreat(fx,fy,attackingBlue);rate(defender,-.05f-.07f*danger);
    float severity=r.nextFloat();boolean penalty=inPenaltyArea(fx,fy,attackingBlue);
    if(severity<.025f+.035f*danger){dismiss(defender,"다이렉트 레드");}
    else if(severity<.24f+.20f*danger){book(defender);if(!sentOff[defender])eventText="🟨 경고 · "+(penalty?"페널티킥":"프리킥");}
    else eventText=penalty?"파울 · 페널티킥":"파울 · 프리킥";
    setupRestart(attackingBlue,penalty?(attackingBlue?.88f:.12f):fx,penalty?.5f:fy,penalty?3:(danger>.52f?2:1));newLog=true;return;}
   // close pressure can win it, but attackers can escape and keep attacking
   float tw=Math.max(.14f,Math.min(.76f,.34f+(dq-aq)*.52f+(pressActive?.12f*org:0f)));
   if(r.nextFloat()<tw){float danger=laneThreat(xy[owner][0],xy[owner][1],owner<11);rate(defender,.10f+.14f*danger);rate(owner,-.05f-.06f*danger);if(pressActive&&primaryPress>=0&&primaryPress!=defender)rate(primaryPress,.035f+.035f*danger);pressActive=false;primaryPress=coverPress=blockPress=-1;owner=defender;receiver=-1;ballFlying=false;state=0;requestTacticalRescan();eventText="태클 성공 · 소유권 전환";newLog=true;shape();}
   else if((owner%11)>=8){startDribbleDuel(defender);}
   else{beginRecovery(defender,defender<11);eventText="압박을 벗어남 · 수비 복귀";newLog=true;}
  }
  float secondLastDefenderLine(boolean blueAttack){int lo=blueAttack?11:0,hi=lo+11;float first=blueAttack?2f:-1f,second=first;for(int i=lo;i<hi;i++){if(!active(i)||i%11==0)continue;float x=xy[i][0];if(blueAttack){if(x<first){second=first;first=x;}else if(x<second)second=x;}else{if(x>first){second=first;first=x;}else if(x>second)second=x;}}return second;}
  void snapshotOffside(boolean blue){offsideLineSnapshot=secondLastDefenderLine(blue);offsideBallSnapshot=bx;}
  boolean isOffsideSnapshot(int passer,int target,boolean blue){if(target<0)return false;float tx=xy[target][0],px=xy[passer][0];if(blue){if(tx<=.5f||tx<=px)return false;return tx>offsideLineSnapshot+.006f&&tx>offsideBallSnapshot+.006f;}else{if(tx>=.5f||tx>=px)return false;return tx<offsideLineSnapshot-.006f&&tx<offsideBallSnapshot-.006f;}}
  void awardOffside(boolean blue){setupRestart(!blue,bx,by,1);eventText="오프사이드 · 간접 프리킥";newLog=true;}
  void pass(boolean blue){
   requestTacticalRescan();snapshotOffside(blue);lastPasser=owner;lastActionThreat=laneThreat(xy[owner][0],xy[owner][1],blue);int best=chooseUniversalPass(owner,blue);
   if(best<0)best=nearestMate(owner,xy[owner][0],xy[owner][1]);if(best<0)return;
   receiver=best;state=1;lastEventBlue=blue;float pd=dist(owner,receiver),lead=.025f+.035f*passQ(owner);btx=clip((receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][0]:bx):bx)+(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?vel[receiver][0]:0f):0f)*lead*900f);bty=clip((receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][1]:by):by)+(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?vel[receiver][1]:0f):0f)*lead*900f);
   float power=.0072f+.0105f*Math.min(1f,pd/.42f)+.0022f*passQ(owner);int actor=owner;queueKick(actor,1,best,btx,bty,power);
   int bypass=opponentsBypassed(actor,best,blue);eventText=bypass>=2?"↗ 전진 패스":"→ 패스";newLog=true;
  }
  void through(boolean blue){
   requestTacticalRescan();snapshotOffside(blue);lastPasser=owner;lastActionThreat=laneThreat(xy[owner][0],xy[owner][1],blue);int b=blue?0:11,best=-1;float bs=-99f;
   for(int i=b;i<b+11;i++){if(!active(i)||i==owner||!isForwardRole(i)||isOffsideSnapshot(owner,i,blue))continue;float sc=laneThreat(xy[i][0],xy[i][1],blue)+spaceAt(xy[i][0],xy[i][1],blue);if(sc>bs){bs=sc;best=i;}}
   if(best<0){awardOffside(blue);return;}receiver=best;float dir=blue?.12f:-.12f;dest[receiver][0]=clip((receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][0]:bx):bx)+dir);btx=dest[receiver][0];bty=dest[receiver][1];lastEventBlue=blue;queueKick(owner,2,best,btx,bty,.0195f+.0035f*passQ(owner));eventText="⇢ 스루패스 준비";newLog=true;
  }
  void cross(boolean blue){
   requestTacticalRescan();snapshotOffside(blue);lastPasser=owner;lastActionThreat=laneThreat(xy[owner][0],xy[owner][1],blue);int b=blue?0:11,best=-1;float bs=-99f;for(int i=b;i<b+11;i++){if(!active(i)||i==owner||isOffsideSnapshot(owner,i,blue))continue;float central=1f-Math.min(1f,Math.abs(xy[i][1]-.5f)*2f),sc=.62f*laneThreat(xy[i][0],xy[i][1],blue)+.38f*central;if(sc>bs){bs=sc;best=i;}}if(best<0){awardOffside(blue);return;}receiver=best;dest[receiver][0]=blue?.83f:.17f;dest[receiver][1]=clip(.5f+((receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][1]:by):by)-.5f)*.35f);btx=dest[receiver][0];bty=dest[receiver][1];lastEventBlue=blue;queueKick(owner,3,best,btx,bty,.0225f+.0030f*norm(A(owner,"cross")));eventText="⤴ 크로스 준비";newLog=true;
  }
  void shoot(boolean blue){
   receiver=-1;lastEventBlue=blue;btx=blue?.985f:.015f;bty=.38f+r.nextFloat()*.24f;queueKick(owner,4,-1,btx,bty,.031f+.008f*finishQ(owner));eventText="💥 슈팅 준비";newLog=true;
  }
  boolean closeBall(){
   if(receiver>=0&&(state==1||state==2||state==3)){float dx=bx-(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][0]:bx):bx),dy=by-(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?xy[receiver][1]:by):by),rvx=ballVX-(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?vel[receiver][0]:0f):0f),rvy=ballVY-(receiver>=0&&receiver<22?(receiver>=0&&receiver<22?vel[receiver][1]:0f):0f);float rel=(float)Math.sqrt(rvx*rvx+rvy*rvy);return dx*dx+dy*dy<(.00042f+.00020f*Math.min(1f,rel/.02f));}
   return Math.abs(bx-btx)<.020f&&Math.abs(by-bty)<.020f;
  }
  boolean consumeShot(){boolean q=shotFlag;shotFlag=false;return q;} boolean consumeOnTarget(){boolean q=onTargetFlag;onTargetFlag=false;return q;} boolean consumeGoal(){boolean q=goalFlag;goalFlag=false;return q;}
  String stateName(int i){return i<11?"BLUE":"RED";}
  void resetKickoff(int team){owner=team+6;receiver=-1;dribbleDef=-1;dribblePhase=0;state=0;ballFlying=false;pendingActor=-1;pendingAction=0;pendingContactFrame=-1;ballVX=ballVY=0f;bx=.5f;by=.5f;xy[owner][0]=dest[owner][0]=.5f;xy[owner][1]=dest[owner][1]=.5f;eventText="센터서클에서 킥오프";newLog=true;ticks=0;shape();}
  int nearestPlayerTo(float x,float y,boolean blueTeam){int lo=blueTeam?0:11,hi=lo+11,best=-1;float bd=99;for(int i=lo;i<hi;i++){if(!active(i))continue;float dx=xy[i][0]-x,dy=xy[i][1]-y,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  int nearestOpponent(int me){if(me<0||me>=22)return -1;int lo=me<11?11:0,hi=lo+11,best=-1;float bd=99;for(int i=lo;i<hi;i++){if(!active(i))continue;float dx=xy[i][0]-xy[me][0],dy=xy[i][1]-xy[me][1],d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
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
  int phase=0,phaseHold=0; // 0 build-up,1 possession,2 attack,3 win transition,4 loss transition
  boolean phaseBlue=true;
  void updatePhase(boolean blue,float ballX){
   if(blue!=phaseBlue){phase=3;phaseBlue=blue;phaseHold=18;return;}
   if(phaseHold>0){phaseHold--;return;}
   float prog=blue?ballX:1-ballX;
   phase=prog<.34f?0:(prog>.66f?2:1);
  }
  float teamShiftX(boolean attacking,boolean blue,float ballX){
   float dir=blue?1f:-1f,rel=(ballX-.5f);
   return attacking?rel*.18f:rel*.28f;
  }
  float teamShiftY(boolean attacking,float ballY){return (ballY-.5f)*(attacking?.24f:.38f);}
  float clampLane(float y,float center,float half){return Math.max(center-half,Math.min(center+half,y));}
  float pressIntensityN(){if(pressIntensity.equals("소극적"))return .28f;if(pressIntensity.equals("적극적"))return .72f;if(pressIntensity.equals("매우 적극적"))return .94f;return .50f;}
  float pressLineX(boolean defendingBlue){float own=defendingBlue?.0f:1f;if(pressLine.equals("높음"))return defendingBlue?.58f:.42f;if(pressLine.equals("낮음"))return defendingBlue?.34f:.66f;return defendingBlue?.46f:.54f;}
  float pressureIQ(int i){return .30f*norm(A(i,"dec"))+.28f*norm(A(i,"pos"))+.24f*norm(A(i,"work"))+.18f*norm(A(i,"con"));}
  float pressureAth(int i){return .36f*norm(A(i,"acc"))+.24f*norm(A(i,"pace"))+.22f*norm(A(i,"sta"))+.18f*norm(A(i,"str"));}
  boolean triggerPress(boolean possessionBlue,float ballX,float ballY){boolean defBlue=!possessionBlue;float intensity=pressIntensityN(),prog=defBlue?ballX:1-ballX;boolean inLine=defBlue?ballX>=pressLineX(true):ballX<=pressLineX(false);boolean touchline=ballY<.20f||ballY>.80f;boolean central=ballY>.36f&&ballY<.64f;float chance=.08f+.34f*intensity;if(pressTrap.equals("측면 유도")&&touchline)chance+=.28f;if(pressTrap.equals("중앙 유도")&&central)chance+=.20f;if(phase==3&&lossReaction.equals("즉시 압박"))chance+=.34f;if(!inLine&&phase!=3)chance*=.30f;return r.nextFloat()<Math.min(.92f,chance);}
  int bestPressureSupport(boolean blueTeam,int exclude,float x,float y){int lo=blueTeam?0:11,hi=lo+11,best=-1;float score=-99;for(int i=lo;i<hi;i++){if(i==exclude||i%11==0)continue;float dx=xy[i][0]-x,dy=xy[i][1]-y,d=(float)Math.sqrt(dx*dx+dy*dy);float sc=pressureIQ(i)*.50f+pressureAth(i)*.25f-d*.55f;if(I(i).equals("압박 적극적"))sc+=.16f;if(sc>score){score=sc;best=i;}}return best;}
  void coordinatePress(boolean possessionBlue,float ballX,float ballY){boolean defBlue=!possessionBlue;if(!pressActive){if(!triggerPress(possessionBlue,ballX,ballY))return;primaryPress=bestPressureSupport(defBlue,-1,ballX,ballY);coverPress=bestPressureSupport(defBlue,primaryPress,ballX,ballY);blockPress=bestPressureSupport(defBlue,coverPress,ballX,ballY);pressActive=primaryPress>=0;pressTicks=8+(int)(10*pressIntensityN());}else if(--pressTicks<=0){pressActive=false;primaryPress=coverPress=blockPress=-1;}}
  float goalX(boolean blue){return blue?.965f:.035f;}
  float attackDir(boolean blue){return blue?1f:-1f;}
  boolean sameTeam(int a,int b){return (a<11)==(b<11);}
  float spaceAt(float x,float y,boolean forBlue){float nearest=2f;int lo=forBlue?11:0,hi=lo+11;for(int i=lo;i<hi;i++){float dx=xy[i][0]-x,dy=xy[i][1]-y;nearest=Math.min(nearest,(float)Math.sqrt(dx*dx+dy*dy));}return Math.min(1f,nearest/.18f);}
  float laneThreat(float x,float y,boolean blue){float prog=blue?x:1-x,central=1f-Math.min(1f,Math.abs(y-.5f)/.5f);return .62f*prog+.38f*central;}
  int nearestMate(int me,float x,float y){int lo=me<11?0:11,hi=lo+11,best=-1;float bd=99;for(int i=lo;i<hi;i++){if(i==me)continue;float dx=xy[i][0]-x,dy=xy[i][1]-y,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  boolean isWideRole(int i){String rr=R(i);return rr.contains("윙어")||rr.contains("풀백")||rr.equals("WB")||rr.equals("W");}
  boolean isInsideRole(int i){String rr=R(i);return rr.contains("인사이드")||rr.contains("세컨드")||rr.contains("공격형 미드");}
  boolean isForwardRole(int i){int l=i%11;String rr=R(i);return l>=8||rr.contains("공격수")||rr.contains("타겟")||rr.contains("포워드")||rr.contains("스트라이커");}
  float supportValue(int i,float tx,float ty,boolean blue){float sp=spaceAt(tx,ty,blue),prog=blue?tx:1-tx,db=(float)Math.sqrt((tx-bx)*(tx-bx)+(ty-by)*(ty-by));return .42f*sp+.30f*prog+.28f*Math.min(1f,db/.22f);}
  int widePartner(int j){boolean blue=j<11;int lo=blue?0:11,hi=lo+11,best=-1;float bd=99;boolean left=base[j][1]>.5f;for(int k=lo;k<hi;k++){if(k==j||k%11==0)continue;if((base[k][1]>.5f)!=left)continue;float dx=base[k][0]-base[j][0],dy=base[k][1]-base[j][1],d=dx*dx+dy*dy;if(d<bd){bd=d;best=k;}}return best;}
  boolean teammateOccupiesWideLane(int j){boolean blue=j<11;int lo=blue?0:11,hi=lo+11;boolean left=base[j][1]>.5f;for(int k=lo;k<hi;k++){if(k==j)continue;if(((xy[k][1]>.5f)==left)&&(xy[k][1]<.16f||xy[k][1]>.84f)&&((blue&&xy[k][0]>xy[j][0]-.03f)||(!blue&&xy[k][0]<xy[j][0]+.03f)))return true;}return false;}



  // V4.6 Player Cognition Layer: perceive first, then decide. No named football pattern is commanded.
  void scanWorld(int j,float ballX,float ballY){
   float awareness=.38f*norm(A(j,"dec"))+.34f*norm(A(j,"vis"))+.28f*norm(A(j,"con"));
   int interval=Math.max(2,8-(int)(5*awareness));if(scanAge[j]++<interval)return;scanAge[j]=0;
   float err=(1f-awareness)*.028f;perceivedBall[j][0]=clip(ballX+(r.nextFloat()-.5f)*err);perceivedBall[j][1]=clip(ballY+(r.nextFloat()-.5f)*err);
   perceivedPressure[j]=oppPressureAt(j);scanQuality[j]=.50f+.50f*awareness;decisionConfidence[j]=.42f+.58f*awareness;
  }
  float laneVisibility(int j,float tx,float ty,boolean blue){
   float vx=tx-xy[j][0],vy=ty-xy[j][1],len=(float)Math.sqrt(vx*vx+vy*vy);if(len<.001f)return 0f;vx/=len;vy/=len;
   float hx=headingX[j],hy=headingY[j],hl=(float)Math.sqrt(hx*hx+hy*hy);if(hl<.01f){hx=attackDir(blue);hy=0;hl=1;}hx/=hl;hy/=hl;
   float facing=Math.max(-1f,Math.min(1f,hx*vx+hy*vy));return Math.max(.15f,.55f+.45f*facing)*scanQuality[j];
  }
  float spaceCreationValue(int j,float tx,float ty,boolean blue){
   float before=oppPressureAt(j),after=1f-spaceAt(tx,ty,blue),separation=Math.max(0f,before-after);
   float occupied=Math.min(1f,nearbyCount(tx,ty,blue,.075f)/2f);return .64f*separation+.36f*(1f-occupied);
  }
  float supportRelationshipValue(int j,float tx,float ty,boolean blue,float ballX,float ballY){
   float db=(float)Math.sqrt((tx-ballX)*(tx-ballX)+(ty-ballY)*(ty-ballY));float angle=Math.min(1f,Math.abs(ty-ballY)/.15f);
   float usable=(db>.055f&&db<.27f)?1f:.35f;return .42f*usable+.30f*angle+.28f*spaceAt(tx,ty,blue);
  }
  float transitionConsequence(int j,float tx,float ty,boolean blue,float ballX,float ballY){
   float dir=attackDir(blue),ahead=Math.max(0f,dir*(tx-ballX)),risk=transitionRisk(blue,ballX,ballY);
   return Math.min(1f,risk*(.45f+.55f*Math.min(1f,ahead/.24f)));
  }
  void chooseEmergentDestination(int j,boolean blue,float ballX,float ballY,float[] out){
   if(j%11==0)return;float dir=attackDir(blue),best=-99f,bx0=out[0],by0=out[1];
   float[] dx={-.13f,-.085f,-.04f,0f,.04f,.085f,.13f},dy={-.16f,-.105f,-.055f,0f,.055f,.105f,.16f};
   for(float ox:dx)for(float oy:dy){float tx=clip(xy[j][0]+dir*ox),ty=clip(xy[j][1]+oy),d=(float)Math.sqrt((tx-xy[j][0])*(tx-xy[j][0])+(ty-xy[j][1])*(ty-xy[j][1]));if(d>.30f)continue;
    float open=spaceAt(tx,ty,blue),relation=supportRelationshipValue(j,tx,ty,blue,ballX,ballY),create=spaceCreationValue(j,tx,ty,blue);
    float progress=Math.max(0f,dir*(tx-xy[j][0])),depth=isForwardRole(j)?Math.min(1f,progress/.15f):.35f;
    float visibility=laneVisibility(j,tx,ty,blue),counter=transitionConsequence(j,tx,ty,blue,ballX,ballY);
    float crowd=Math.min(1f,nearbyCount(tx,ty,blue,.07f)/2f);
    float sc=.24f*open+.21f*relation+.16f*create+.14f*visibility+.13f*depth+.12f*Math.min(1f,progress/.18f)-(.16f+.18f*securityN())*counter-.20f*crowd;
    sc+=(r.nextFloat()-.5f)*(.13f-.07f*decisionConfidence[j]);if(sc>best){best=sc;bx0=tx;by0=ty;}}
   out[0]=bx0;out[1]=by0;
  }
  // V4.5 Tactical Influence Layer: modifies priorities; never commands a fixed passing pattern.
  float directnessN(){return progressionStyle.equals("직선적")?1f:progressionStyle.equals("안정적")?0f:.5f;}
  float transitionN(){return transitionAttack.equals("빠른 역습")?1f:transitionAttack.equals("점유 재정비")?0f:.5f;}
  float securityN(){return restSecurity.equals("안정적")?1f:restSecurity.equals("공격적")?0f:.5f;}
  int desiredRearLine(){return buildupShape.equals("3-2 지향")?3:buildupShape.equals("2-3 지향")?2:0;}
  int desiredPivotLine(){return buildupShape.equals("3-2 지향")?2:buildupShape.equals("2-3 지향")?3:0;}
  int opponentsAheadForCounter(boolean blue,float ballX){int lo=blue?11:0,hi=lo+11,n=0;float dir=attackDir(blue);for(int i=lo;i<hi;i++){if(!active(i))continue;if(dir*(xy[i][0]-ballX)>0)n++;}return n;}
  float transitionRisk(boolean blue,float ballX,float ballY){
   int lo=blue?0:11,hi=lo+11,behind=0;float dir=attackDir(blue);for(int i=lo;i<hi;i++){if(!active(i)||i%11==0)continue;if(dir*(xy[i][0]-ballX)<-.045f)behind++;}
   int opp=opponentsAheadForCounter(blue,ballX);float centralDanger=1f-Math.min(1f,Math.abs(ballY-.5f)*1.8f);
   return Math.max(0f,Math.min(1f,.46f*Math.max(0,opp-behind+2)/5f+.30f*centralDanger+.24f*(1f-securityN())));
  }
  boolean shouldProtectTransition(int j,boolean blue,float ballX,float ballY){
   if(isForwardRole(j)||j%11==0)return false;float risk=transitionRisk(blue,ballX,ballY),dir=attackDir(blue);int lo=blue?0:11,hi=lo+11,behind=0;
   for(int i=lo;i<hi;i++)if(active(i)&&i%11!=0&&dir*(xy[i][0]-ballX)<-.045f)behind++;
   int min=(int)Math.round(2+2*securityN());return risk>.42f&&behind<min;
  }
  void applyBuildStructurePreference(int j,boolean blue,float ballX,float ballY,float[] out){
   if(desiredRearLine()==0||j%11==0)return;float prog=blue?ballX:1f-ballX;if(prog>.58f)return;float dir=attackDir(blue),baseProg=blue?base[j][0]:1f-base[j][0];
   boolean naturallyDeep=baseProg<.31f,naturallyMid=baseProg>=.31f&&baseProg<.50f;
   if(naturallyDeep&&desiredRearLine()==3){out[0]=clip(out[0]-dir*.025f);out[1]=clip(.5f+(out[1]-.5f)*.88f);}
   if(naturallyMid&&desiredPivotLine()>=2){out[0]=clip(out[0]-dir*.018f);}
   if(buildupShape.equals("2-3 지향")&&naturallyDeep&&Math.abs(base[j][1]-.5f)>.18f){out[0]=clip(out[0]+dir*.025f);}
  }
  // V4.4 Universal Football Intelligence.
  // Players do not execute named patterns. They perceive pressure, space, lanes,
  // numerical relationships and risk; football patterns emerge from those choices.
  float segmentPressure(int a,int b,boolean blue){
   float ax=xy[a][0],ay=xy[a][1],cx=xy[b][0],cy=xy[b][1],vx=cx-ax,vy=cy-ay,len2=vx*vx+vy*vy,score=0f;int lo=blue?11:0,hi=lo+11;
   for(int k=lo;k<hi;k++){if(!active(k))continue;float t=len2<.0001f?0f:((xy[k][0]-ax)*vx+(xy[k][1]-ay)*vy)/len2;t=Math.max(0f,Math.min(1f,t));float px=ax+t*vx,py=ay+t*vy,dx=xy[k][0]-px,dy=xy[k][1]-py,d=(float)Math.sqrt(dx*dx+dy*dy);score+=Math.max(0f,1f-d/.085f);}
   return Math.min(1f,score);
  }
  int opponentsBypassed(int from,int to,boolean blue){float dir=attackDir(blue),a=dir*xy[from][0],b=dir*xy[to][0];if(b<=a)return 0;int lo=blue?11:0,hi=lo+11,n=0;for(int k=lo;k<hi;k++){if(!active(k))continue;float q=dir*xy[k][0];if(q>a&&q<b)n++;}return n;}
  int nearbyCount(float x,float y,boolean blue,float radius){int lo=blue?0:11,hi=lo+11,n=0;float rr=radius*radius;for(int i=lo;i<hi;i++){if(!active(i))continue;float dx=xy[i][0]-x,dy=xy[i][1]-y;if(dx*dx+dy*dy<rr)n++;}return n;}
  float localSuperiority(float x,float y,boolean blue){int us=nearbyCount(x,y,blue,.18f),them=nearbyCount(x,y,!blue,.18f);return Math.max(-1f,Math.min(1f,(us-them)/3f));}
  float receiverOrientationValue(int i,boolean blue){float dir=attackDir(blue),ahead=spaceAt(clip(xy[i][0]+dir*.08f),xy[i][1],blue),behind=spaceAt(clip(xy[i][0]-dir*.05f),xy[i][1],blue);return Math.max(0f,Math.min(1f,.5f+.5f*(ahead-behind)));}
  float possessionRisk(int from,int to,boolean blue){
   float lane=segmentPressure(from,to,blue),recv=oppPressureAt(to),d=dist(from,to),ownProg=blue?xy[from][0]:1f-xy[from][0];
   float turnoverCost=(ownProg<.34f?.18f:ownProg>.72f?-.08f:0f);
   return Math.max(0f,Math.min(1f,.44f*lane+.34f*recv+.22f*Math.min(1f,d/.48f)+turnoverCost));
  }
  float progressionValue(int from,int to,boolean blue){
   float dir=attackDir(blue),progress=Math.max(0f,dir*(xy[to][0]-xy[from][0])),bypass=Math.min(1f,opponentsBypassed(from,to,blue)/4f),threat=laneThreat(xy[to][0],xy[to][1],blue);
   return .38f*Math.min(1f,progress/.28f)+.34f*bypass+.28f*threat;
  }
  float passOptionValue(int from,int to,boolean blue){
   float lane=1f-segmentPressure(from,to,blue),space=spaceAt(xy[to][0],xy[to][1],blue),sup=(localSuperiority(xy[to][0],xy[to][1],blue)+1f)*.5f,orient=receiverOrientationValue(to,blue),prog=progressionValue(from,to,blue),risk=possessionRisk(from,to,blue),seen=laneVisibility(from,xy[to][0],xy[to][1],blue);
   float iq=.42f*passQ(from)+.34f*norm(A(from,"vis"))+.24f*norm(A(from,"dec"));
   float direct=directnessN(),trans=(transitionTicks>0?transitionN():.5f),security=securityN();
   float value=(.25f-.08f*direct)*lane+(.20f-.07f*direct)*space+.12f*sup+.11f*orient+.12f*seen+(.20f+.22f*direct+.12f*trans)*prog-(.18f+.18f*security-.08f*direct)*risk;
   return value+(r.nextFloat()-.5f)*(.14f-.08f*iq);
  }
  int chooseUniversalPass(int from,boolean blue){
   snapshotOffside(blue);int lo=blue?0:11,hi=lo+11,best=-1;float bs=-99f;
   for(int i=lo;i<hi;i++){if(i==from||!active(i)||isOffsideSnapshot(from,i,blue))continue;float d=dist(from,i);if(d>.52f)continue;float sc=passOptionValue(from,i,blue);if(sc>bs){bs=sc;best=i;}}
   return best;
  }
  float carrierAdvanceValue(int j,boolean blue){
   float dir=attackDir(blue),frontSpace=spaceAt(clip(xy[j][0]+dir*.09f),xy[j][1],blue),press=oppPressureAt(j),sup=localSuperiority(xy[j][0],xy[j][1],blue),prog=blue?xy[j][0]:1f-xy[j][0];
   float direct=directnessN(),trans=transitionTicks>0?transitionN():.5f;return (.36f+.12f*direct+.10f*trans)*frontSpace+.20f*(1f-press)+.17f*((sup+1f)*.5f)+(.17f+.08f*direct)*(1f-prog);
  }
  void universalSupportDestination(int j,boolean blue,float ballX,float ballY,float[] out){
   float dir=attackDir(blue),bestX=out[0],bestY=out[1],best=-99f;
   // Sample nearby football spaces. No triangle/U/third-man template is prescribed.
   float[] ox={-.11f,-.07f,-.03f,.03f,.07f,.11f},oy={-.16f,-.10f,-.05f,.05f,.10f,.16f};
   for(float dx:ox)for(float dy:oy){float tx=clip(ballX+dir*dx),ty=clip(ballY+dy);float d=(float)Math.sqrt((tx-xy[j][0])*(tx-xy[j][0])+(ty-xy[j][1])*(ty-xy[j][1]));if(d>.34f)continue;
    float space=spaceAt(tx,ty,blue),sup=(localSuperiority(tx,ty,blue)+1f)*.5f,depth=Math.max(0f,dir*(tx-ballX)),width=Math.min(1f,Math.abs(ty-ballY)/.18f),crowd=Math.min(1f,nearbyCount(tx,ty,blue,.08f)/3f);
    float sc=.34f*space+.20f*sup+.18f*width+.16f*depth-.20f*crowd-.10f*Math.min(1f,d/.34f);
    if(sc>best){best=sc;bestX=tx;bestY=ty;}}
   out[0]=bestX;out[1]=bestY;
  }
  // V4.2 Movement Intelligence: tactical action comes before destination.
  // 0 hold, 1 receive, 2 run-behind, 3 support, 4 overlap, 5 underlap,
  // 6 box-run, 7 press, 8 cover, 9 recovery, 10 track/handoff, 11 decoy.
  float oppPressureAt(int j){boolean blue=j<11;int lo=blue?11:0,hi=lo+11;float best=2f;for(int k=lo;k<hi;k++){if(!active(k))continue;float dx=xy[k][0]-xy[j][0],dy=xy[k][1]-xy[j][1];best=Math.min(best,(float)Math.sqrt(dx*dx+dy*dy));}return Math.max(0f,1f-best/.16f);}
  float decisionNoise(int j){float iq=.45f*norm(A(j,"dec"))+.30f*norm(A(j,"off"))+.25f*norm(A(j,"con"));return (r.nextFloat()-.5f)*(.30f-.20f*iq);}
  int nearestForwardMate(int j){int lo=j<11?0:11,hi=lo+11,b=-1;float bd=99;for(int k=lo;k<hi;k++){if(k==j||!active(k)||!isForwardRole(k))continue;float d=dist(j,k);if(d<bd){bd=d;b=k;}}return b;}
  int chooseAttackIntent(int j,boolean blue,float ballX,float ballY){
   float dir=attackDir(blue),space=spaceAt(xy[j][0],xy[j][1],blue),press=oppPressureAt(j),iq=.5f*norm(A(j,"dec"))+.3f*norm(A(j,"off"))+.2f*norm(A(j,"work")),n=decisionNoise(j);
   boolean f=isForwardRole(j),wide=isWideRole(j),inside=isInsideRole(j),box=blue?xy[j][0]>.70f:xy[j][0]<.30f;
   int mate=nearestForwardMate(j);
   if(f){
    // Counter-movement: nearby forwards should not make the same run.
    if(mate>=0&&moveIntent[mate]==2)return 1;          // partner attacks depth -> offer to feet
    if(mate>=0&&(moveIntent[mate]==1||moveIntent[mate]==3||moveIntent[mate]==11)&&space>.27f)return 2; // partner drops/supports -> attack depth
    if(box&&Math.abs(xy[j][1]-.5f)>.13f&&space>.35f)return 6;
    float behind=.36f*space+.32f*iq+.20f*runQ(j)+n;
    float receive=.34f*press+.28f*(1f-space)+.25f*iq-n;
    if(transitionTicks>0)behind+=.08f+.24f*transitionN();behind+=.12f*directnessN();receive+=.10f*(1f-directnessN());
    if(behind>receive+.05f)return 2;
    if(receive>behind+.03f)return 1;
    return 11;
   }
   if(wide){
    int wp=widePartner(j);boolean partnerInside=wp>=0&&isInsideRole(wp),laneTaken=teammateOccupiesWideLane(j);
    if(partnerInside&&!laneTaken&&phase>=1)return 4;
    if(laneTaken&&phase>=1)return 5;
    return space>.48f?1:3;
   }
   if(phase==3)return 3;
   if(space>.50f&&Math.abs(xy[j][1]-ballY)>.10f)return 1;
   return 3;
  }
  int chooseDefendIntent(int j,boolean possessionBlue,float ballX,float ballY){
   boolean defBlue=j<11;int local=j%11;if(local==0)return 0;
   if(pressActive&&j==primaryPress)return 7;
   if(pressActive&&(j==coverPress||j==blockPress))return 8;
   float goalSide=defBlue?xy[j][0]-ballX:ballX-xy[j][0],dBall=(float)Math.sqrt((xy[j][0]-ballX)*(xy[j][0]-ballX)+(xy[j][1]-ballY)*(xy[j][1]-ballY));
   if(phase==3&&goalSide>0)return 9;
   if(local<=4&&dBall<.22f)return 10;
   return 8;
  }
  void applyAttackIntent(int j,int intent,boolean blue,float ballX,float ballY,float[] out){
   float dir=attackDir(blue),tx=out[0],ty=out[1],side=base[j][1]<.5f?-1f:1f;
   if(intent==1){ // move to receive / come short / exploit space
    tx=clip(ballX-dir*(.08f+.05f*oppPressureAt(j)));ty=clip(base[j][1]+(ballY-base[j][1])*.32f);roleState[j]=3;
   }else if(intent==2){ // run in behind, curved away from nearest defender
    float line=secondLastDefenderLine(blue);tx=clip(line+dir*(.055f+.055f*runQ(j)));ty=clip(base[j][1]+side*.035f);roleState[j]=2;
   }else if(intent==3){ // support the current structure by finding an open passing relationship
    float[] uv={tx,ty};universalSupportDestination(j,blue,ballX,ballY,uv);tx=uv[0];ty=uv[1];roleState[j]=3;
   }else if(intent==4){ // overlap outside
    tx=clip(ballX+dir*.075f);ty=side<0?.095f:.905f;roleState[j]=1;
   }else if(intent==5){ // underlap through half-space
    tx=clip(ballX+dir*.06f);ty=side<0?.36f:.64f;roleState[j]=1;
   }else if(intent==6){ // box occupation: don't pile onto keeper/central lane
    tx=blue?.80f:.20f;ty=side<0?.39f:.61f;roleState[j]=2;
   }else if(intent==11){ // decoy/counter movement
    tx=clip(xy[j][0]-dir*.045f);ty=clip(xy[j][1]-side*.055f);roleState[j]=3;
   }
   out[0]=tx;out[1]=ty;
  }
  void applyDefendIntent(int j,int intent,boolean possessionBlue,float ballX,float ballY,float[] out){
   boolean defBlue=j<11;float ddir=defBlue?1f:-1f,tx=out[0],ty=out[1];
   if(intent==7){ // press with angle: curve run to screen central passing lane
    float screenY=.5f+(ballY-.5f)*.55f;tx=clip(ballX-ddir*.018f);ty=clip(ballY+(ballY-screenY)*.18f);roleState[j]=4;
   }else if(intent==8){ // cover: goal-side and slightly inside
    tx=clip(ballX-ddir*.085f);ty=clip(.5f+(ballY-.5f)*.58f);roleState[j]=5;
   }else if(intent==9){ // recovery run
    tx=clip(base[j][0]-ddir*.035f);ty=clip(.5f+(base[j][1]-.5f)*.72f);roleState[j]=5;
   }else if(intent==10){ // track/handoff: stay goal-side, don't chase through line
    int mark=bestMarkFor(j,possessionBlue);if(mark>=0){tx=clip(xy[mark][0]-ddir*.035f);ty=clip(xy[mark][1]*.78f+.5f*.22f);}roleState[j]=5;
   }
   out[0]=tx;out[1]=ty;
  }
  void footballPrinciples(boolean blue,float ballX,float ballY){
   if(blue!=previousBlue){transitionTicks=22;previousBlue=blue;}else if(transitionTicks>0)transitionTicks--;
   updatePhase(blue,ballX);coordinatePress(blue,ballX,ballY);float dir=attackDir(blue);
   for(int j=0;j<22;j++){
    if(j==owner)continue;boolean attacking=(j<11)==blue;int local=j%11;float tx=base[j][0],ty=base[j][1];
    float sx=teamShiftX(attacking,blue,ballX),sy=teamShiftY(attacking,ballY);tx=clip(tx+sx);ty=clip(ty+sy);
    if(attacking){
     // Principle 1: preserve width/depth and create passing angles rather than chase the ball.
     float prog=blue?ballX:1-ballX;boolean wideBase=base[j][1]<.27f||base[j][1]>.73f;boolean forward=isForwardRole(j);
     if(local==0){tx=blue?.075f:.925f;ty=.5f+(ballY-.5f)*.12f;roleState[j]=0;}
     else{
      float depth=phase==2?.10f:phase==1?.05f:.015f;if(transitionTicks>0)depth+=forward?.10f:.025f;
      tx=clip(base[j][0]+sx+dir*depth);ty=clip(base[j][1]+sy*.55f);
      if(wideBase||isWideRole(j)){float edge=base[j][1]<.5f?.12f:.88f;ty=ty*.35f+edge*.65f;roleState[j]=6;}
      // Space-led role interaction: an inside-moving advanced teammate frees the flank for a deeper player.
      int wp=widePartner(j);boolean deeper=wp>=0&&(blue?base[j][0]<base[wp][0]:base[j][0]>base[wp][0]);
      if(wp>=0&&phase>=1&&deeper&&isInsideRole(wp)&&!teammateOccupiesWideLane(j)){ty=base[j][1]<.5f?.10f:.90f;tx=clip(tx+dir*.07f);roleState[j]=1;}
      else if((wideBase||isWideRole(j))&&teammateOccupiesWideLane(j)){ty=clip(.5f+(base[j][1]-.5f)*.58f);tx=clip(tx-dir*.025f);roleState[j]=3;}
      // Forwards keep transition outlet value; one offers to feet while another attacks depth.
      if(forward){int mate=nearestMate(j,ballX,ballY);boolean outlet=transitionTicks>0||phase==3;float rq=runQ(j)+runRole(j);if(outlet){boolean deepest=(j%2)==0;tx=clip(ballX+dir*(deepest?.18f:.08f));ty=clip(.5f+(base[j][1]-.5f)*.55f);roleState[j]=deepest?2:3;}else if(phase==2){tx=clip(tx+dir*(.06f+.06f*rq));roleState[j]=2;}}
      // Tactical structure is a preference, not a coordinate script.
      float[] ts={tx,ty};applyBuildStructurePreference(j,blue,ballX,ballY,ts);tx=ts[0];ty=ts[1];
      // Rest defence is risk-based: more security keeps more cover; aggressive settings accept counter risk.
      int ahead=0;for(int k=(j<11?0:11);k<(j<11?11:22);k++){if(k==j||!active(k))continue;if((blue&&xy[k][0]>ballX)||(!blue&&xy[k][0]<ballX))ahead++;}
      if((ahead>=5||shouldProtectTransition(j,blue,ballX,ballY))&&local>=1&&local<=7&&!forward){float gap=.075f+.055f*securityN();float safe=blue?Math.min(tx,ballX-gap):Math.max(tx,ballX+gap);tx=clip(safe);roleState[j]=5;}
     }
    }else{
     boolean defBlue=j<11;float ownGoal=defBlue?.055f:.945f;float ddir=defBlue?1f:-1f;
     if(local==0){tx=ownGoal;ty=clampLane(.5f+(ballY-.5f)*.22f,.5f,.11f);roleState[j]=0;}
     else if(pressActive&&j==primaryPress){tx=clip(ballX-ddir*(.012f+.018f*(1f-pressIntensityN())));ty=ballY;roleState[j]=4;}
     else if(pressActive&&j==coverPress){tx=clip(ballX-ddir*.065f);ty=clampLane(ballY,.5f,.20f);roleState[j]=5;}
     else if(pressActive&&j==blockPress){tx=clip(ballX-ddir*.10f);ty=clampLane(.5f+(ballY-.5f)*.35f,.5f,.24f);roleState[j]=5;}
     else{
      // Principle 2: protect goal/centre first, shift as a connected block, retain an outlet when appropriate.
      float compact=.58f+.12f*(1f-pressIntensityN());ty=.5f+(base[j][1]-.5f)*compact+(ballY-.5f)*.30f;
      float line;if(local<=4)line=defBlue?.22f:.78f;else if(local<=7)line=defBlue?.37f:.63f;else line=defBlue?.49f:.51f;
      tx=clip(line+(ballX-.5f)*.25f);roleState[j]=5;
      // Counter-attacking forwards do not collapse into their own box: preserve a release point.
      if(isForwardRole(j)&&pressLine.equals("낮음")){tx=defBlue?Math.max(tx,.43f):Math.min(tx,.57f);ty=.5f+(base[j][1]-.5f)*.50f;roleState[j]=3;}
      // Cover the teammate who steps out: nearby defender sits goal-side instead of following the ball.
      int near=nearestPlayerTo(ballX,ballY,defBlue);if(near!=j&&local>=1&&local<=7){float nx=xy[near][0],ny=xy[near][1];if(Math.abs(nx-ballX)<.10f){tx=clip(tx-ddir*.025f);ty=clip(ty+(ny-.5f)*.10f);}}
     }
    }
    if(local!=0){if(j<11&&tx>.915f)tx=.895f;if(j>=11&&tx<.085f)tx=.105f;}
    // Movement Intelligence: keep an intention briefly, then re-scan and choose again.
    if(intentHold[j]<=0){moveIntent[j]=attacking?chooseAttackIntent(j,blue,ballX,ballY):chooseDefendIntent(j,blue,ballX,ballY);float iq0=.45f*norm(A(j,"dec"))+.30f*norm(A(j,"off"))+.25f*norm(A(j,"con"));intentHold[j]=Math.max(2,8-(int)(5*iq0));}else intentHold[j]--;
    float[] mi={tx,ty};float seenX=perceivedBall[j][0],seenY=perceivedBall[j][1];if(attacking)applyAttackIntent(j,moveIntent[j],blue,seenX,seenY,mi);else{applyDefendIntent(j,moveIntent[j],blue,seenX,seenY,mi);applyDutyTarget(j,blue,seenX,seenY,mi);applyRecoveryTarget(j,blue,seenX,seenY,mi);}
    if(attacking&&j!=owner){float[] emerg={mi[0],mi[1]};chooseEmergentDestination(j,blue,seenX,seenY,emerg);float trust=.38f+.42f*decisionConfidence[j];mi[0]=mi[0]*(1f-trust)+emerg[0]*trust;mi[1]=mi[1]*(1f-trust)+emerg[1]*trust;}
    tx=mi[0];ty=mi[1];
    // Perception/decisions alter reaction quality without changing football principles themselves.
    float iq=.45f*norm(A(j,"dec"))+.30f*norm(A(j,"off"))+.25f*norm(A(j,"con"));float react=.58f+.34f*iq;
    principleTarget[j][0]=tx;principleTarget[j][1]=ty;dest[j][0]=clip(dest[j][0]*(1f-react)+tx*react);dest[j][1]=clip(dest[j][1]*(1f-react)+ty*react);
   }
  }
  void requestTacticalRescan(){forceTacticalScan=true;for(int i=0;i<22;i++){intentHold[i]=0;scanAge[i]=99;}}
  void sanitizeMatchIndices(){
   if(owner<-1||owner>=22)owner=-1;
   if(receiver<-1||receiver>=22)receiver=-1;
   if(dribbleDef<-1||dribbleDef>=22){dribbleDef=-1;dribblePhase=0;}
   if(pendingActor<-1||pendingActor>=22){pendingActor=-1;pendingAction=0;pendingReceiver=-1;pendingContactFrame=-1;}
   if(pendingReceiver<-1||pendingReceiver>=22)pendingReceiver=-1;
  }
  void updateIndependentMovement(){
   sanitizeMatchIndices();
   if(owner>=22||receiver>=22||pendingActor>=22){owner=-1;receiver=-1;pendingActor=-1;pendingAction=0;ballFlying=false;ballVX=ballVY=0f;}
   boolean possessionChanged=owner!=lastScanOwner&&owner>=0,actionChanged=state!=lastScanState;
   boolean blue=owner>=0?owner<11:lastPossessionBlue;float ballX=owner>=0?xy[owner][0]:bx,ballY=owner>=0?xy[owner][1]:by;
   boolean dangerousEntry=finalThird(blue,ballX)&&!finalThird(blue,lastBallX);
   if(!forceTacticalScan&&!possessionChanged&&!actionChanged&&!dangerousEntry&&animFrame%7!=0)return;
   if(forceTacticalScan||possessionChanged||actionChanged||dangerousEntry)for(int i=0;i<22;i++)intentHold[i]=0;
   for(int i=0;i<22;i++)if(active(i))scanWorld(i,ballX,ballY);
   assignDefensiveDuties(!blue,ballX,ballY);footballPrinciples(blue,ballX,ballY);
   coordinateTeamShape(true,ballX,ballY);coordinateTeamShape(false,ballX,ballY);lastScanOwner=owner;lastScanState=state;forceTacticalScan=false;
  }
  int secondNearestDefender(float x,float y,boolean blueTeam,int first){int lo=blueTeam?0:11,hi=lo+11,best=lo;float bd=99;for(int i=lo;i<hi;i++){if(!active(i)||i==first||i%11==0)continue;float dx=xy[i][0]-x,dy=xy[i][1]-y,d=dx*dx+dy*dy;if(d<bd){bd=d;best=i;}}return best;}
  int bestMarkFor(int defender,boolean possessionBlue){int atk=possessionBlue?0:11,best=-1;float bs=-99f;boolean defBlue=defender<11;for(int i=atk;i<atk+11;i++){if(!active(i)||i%11==0)continue;float dx=xy[i][0]-xy[defender][0],dy=xy[i][1]-xy[defender][1],d=(float)Math.sqrt(dx*dx+dy*dy);float threat=laneThreat(xy[i][0],xy[i][1],possessionBlue),run=(moveIntent[i]==2||moveIntent[i]==4||moveIntent[i]==5||moveIntent[i]==6)?.24f:0f,central=.10f*(1f-Math.min(1f,Math.abs(xy[i][1]-.5f)*2f));float sc=.48f*threat+.20f*(1f-Math.min(1f,d/.35f))+run+central;if(sc>bs){bs=sc;best=i;}}return best;}
  boolean finalThird(boolean blue,float x){return blue?x>.66f:x<.34f;}
  void shape(){updateIndependentMovement();}
  float clip(float v){return Math.max(.035f,Math.min(.965f,v));}
  void updateRenderPose(){
   for(int i=0;i<22;i++){
    float dx=xy[i][0]-renderXY[i][0],dy=xy[i][1]-renderXY[i][1],d=(float)Math.sqrt(dx*dx+dy*dy),target=d>.00045f?1f:0f;
    moveBlend[i]+=(target-moveBlend[i])*(target>moveBlend[i]?.16f:.10f);
    float follow=.13f+.16f*moveBlend[i];renderXY[i][0]+=dx*follow;renderXY[i][1]+=dy*follow;
    float hx=headingX[i],hy=headingY[i],hl=(float)Math.sqrt(hx*hx+hy*hy);if(hl<.01f){hx=1;hy=0;hl=1;}hx/=hl;hy/=hl;
    renderHeadingX[i]+=(hx-renderHeadingX[i])*(.10f+.12f*moveBlend[i]);renderHeadingY[i]+=(hy-renderHeadingY[i])*(.10f+.12f*moveBlend[i]);
    visualStep[i]+=(.08f+.30f*moveBlend[i])*(.82f+.18f*norm(A(i,"pace")));
   }
  }
  protected void onDraw(Canvas c){updateRenderPose();
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
    if(!active(i))continue;
    float px=l+renderXY[i][0]*fw,py=t+renderXY[i][1]*fh,hx=renderHeadingX[i],hy=renderHeadingY[i],hl=(float)Math.sqrt(hx*hx+hy*hy);if(hl<.01f){hx=1;hy=0;hl=1;}hx/=hl;hy/=hl;
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
    p.setTextSize(dpv(6.5f));p.setColor(Color.rgb(30,30,30));c.drawText(""+(i%11+1),px-dpv(2),py-dpv(5)-bob,p);p.setTextSize(dpv(7.2f));p.setColor(Color.WHITE);p.setFakeBoldText(true);String tag=shortName(i)+" "+ratingText(i);float tw=p.measureText(tag);p.setStyle(Paint.Style.FILL);p.setColor(Color.argb(155,0,0,0));c.drawRoundRect(px-tw/2-dpv(2),py+dpv(12),px+tw/2+dpv(2),py+dpv(21),dpv(2),dpv(2),p);p.setColor(Color.WHITE);c.drawText(tag,px-tw/2,py+dpv(19),p);p.setFakeBoldText(false);
   }
   // football: white body + black panels, not a plain dot
   float px=l+bx*fw,py=t+by*fh;p.setColor(Color.WHITE);c.drawCircle(px,py,dpv(6),p);p.setColor(Color.BLACK);c.drawCircle(px,py,dpv(2.2f),p);for(int k=0;k<5;k++){double a=k*Math.PI*2/5;c.drawCircle(px+(float)Math.cos(a)*dpv(3.7f),py+(float)Math.sin(a)*dpv(3.7f),dpv(1.1f),p);}
   if(eventText.contains("슈팅")||eventText.contains("GOAL")){p.setTextSize(dpv(18));p.setColor(Color.WHITE);p.setFakeBoldText(true);c.drawText(eventText,l+fw*.43f,t+dpv(28),p);p.setFakeBoldText(false);}
  }
  float dpv(float x){return x*getResources().getDisplayMetrics().density;}
 }
}