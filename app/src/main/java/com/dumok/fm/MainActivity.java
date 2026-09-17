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
  String name,nation,pos,club,body,nick=""; int age,height,weight,ca,pa,fitness=100,apps,goals,value,wage;
  int[] te=new int[8],me=new int[8],ph=new int[8]; double[] xp=new double[24];
  double growth,professional,physicalRetention; int declineAge; boolean fa,starter;
 }
 static class Team implements java.io.Serializable{String name;int p,w,d,l,gf,ga,pts;Team(String n){name=n;}}
 static class Fixture implements java.io.Serializable{LocalDate date;Team h,a;boolean played;int hg,ag;Fixture(LocalDate d,Team x,Team y){date=d;h=x;a=y;}}
 ArrayList<Player>squad=new ArrayList<>(),pool=new ArrayList<>();ArrayList<Team>teams=new ArrayList<>();ArrayList<Fixture>fixtures=new ArrayList<>();
 Random R=new Random(4152); LocalDate now=LocalDate.of(2026,7,1); String club="DUMOK FC",formation="4-2-3-1",mentality="균형";int budget=650;
 int BG=Color.rgb(8,15,27),CARD=Color.rgb(18,29,45),GREEN=Color.rgb(48,211,145),MUTED=Color.rgb(150,166,186);
 String[] SLOT={"GK","LB","LCB","CB","RCB","RB","LDM","DM","RDM","LCM","CM","RCM","LAM","AM","RAM","LW","LST","ST","RST","RW"};
 Player[] XI=new Player[SLOT.length];
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
protected void onStop() {
    super.onStop();
    if (!squad.isEmpty()) saveGame();
}
 void seed(){
  String[] ns={"DUMOK FC","JEONJU UNITED","SEOUL CITY","BUSAN ATHLETIC","INCHEON BLUE","DAEGU REDS","SUWON KNIGHTS","DAEJEON PHOENIX"};
  for(String n:ns)teams.add(new Team(n));
  for(int i=0;i<25;i++){Player p=gen(false,club,true);p.starter=i<11;squad.add(p);}
  // Default shape only for first launch; user can freely rebuild it on the tactics board.
  int[] ds={0,1,2,4,5,9,11,13,15,17,19}; for(int i=0;i<11;i++)XI[ds[i]]=squad.get(i);
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
  for(int i=0;i<8;i++){p.te[i]=clamp(quality-3+R.nextInt(7));p.me[i]=clamp(quality-3+R.nextInt(7));p.ph[i]=clamp(quality-3+R.nextInt(7));}
  shapePlayer(p);p.ca=calcCA(p);int room=p.age<=20?25+R.nextInt(65):8+R.nextInt(45);p.pa=Math.min(200,Math.max(p.ca,p.ca+room));
  p.value=Math.max(1,(p.ca*p.ca)/80+Math.max(0,p.pa-p.ca)*2);p.wage=Math.max(1,p.ca/4);p.nick=nickname(p);return p;
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
  }
  return v;
 }
 int calcCA(Player p){
  double pts=0,max=0;
  for(int c=0;c<3;c++)for(int i=0;i<8;i++){double ww=w(p,c,i);pts+=cost(c==0?p.te[i]:c==1?p.me[i]:p.ph[i])*ww;max+=cost(20)*ww;}
  return Math.max(1,Math.min(200,(int)Math.round(200*pts/max)));
 }
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
  LinearLayout cols=new LinearLayout(this);cols.setOrientation(LinearLayout.HORIZONTAL);cols.addView(attrCol("기술",TECH,p.te),new LinearLayout.LayoutParams(0,-2,1));cols.addView(attrCol("정신",MENT,p.me),new LinearLayout.LayoutParams(0,-2,1));cols.addView(attrCol("신체",PHYS,p.ph),new LinearLayout.LayoutParams(0,-2,1));body.addView(cols);
  body.addView(tx("※ 플레이 성향 내부점수와 성장속도/프로의식/노쇠 데이터는 공개되지 않습니다.",12,MUTED));
 }
 LinearLayout attrCol(String title,String[] names,int[] a){LinearLayout c=card();c.addView(tx(title,16,GREEN));for(int i=0;i<a.length;i++)c.addView(tx(names[i]+"   "+a[i],13,col(a[i])));return c;}
 int col(int x){if(x>=18)return GREEN;if(x>=15)return Color.rgb(170,235,120);if(x>=11)return Color.rgb(245,210,90);if(x>=6)return Color.WHITE;return MUTED;}
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
  String[][] rows={{"LW","LST","ST","RST","RW"},{"LAM","AM","RAM"},{"LCM","CM","RCM"},{"LDM","DM","RDM"},{"LB","LCB","CB","RCB","RB"},{"GK"}};
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
       else if(from<0){if(XI[idx]!=null)XI[idx].starter=false;XI[idx]=q;}       // squad rail -> slot
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
  v.setOnClickListener(x->pickSlot(idx));return v;
 }
 int slotIndex(String n){for(int i=0;i<SLOT.length;i++)if(SLOT[i].equals(n))return i;return 0;}
 String slotLabel(int i){Player q=XI[i];return SLOT[i]+"\n"+(q==null?"＋":q.name+"\n"+q.pos+(q.pos.equals(SLOT[i])||SLOT[i].contains(q.pos)?"":" ⚠"));}
 void pickSlot(int idx){
  final Dialog d=new Dialog(this);LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(dp(10),dp(10),dp(10),dp(10));box.setBackgroundColor(BG);
  box.addView(tx(SLOT[idx]+" 위치 선수 선택",17,Color.WHITE));
  if(XI[idx]!=null){Button rm=bt("현재 선수 제외 · "+XI[idx].name);rm.setOnClickListener(v->{XI[idx].starter=false;XI[idx]=null;d.dismiss();tactic();});box.addView(rm);}
  ScrollView sv=new ScrollView(this);LinearLayout list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);
  for(Player q:squad){boolean used=false;for(Player z:XI)if(z==q)used=true;if(used&&XI[idx]!=q)continue;Button bb=bt(q.name+"   "+q.pos+"   CA "+q.ca);bb.setOnClickListener(v->{if(XI[idx]!=null)XI[idx].starter=false;for(int i=0;i<XI.length;i++)if(XI[i]==q)XI[i]=null;XI[idx]=q;for(Player z:squad)z.starter=false;for(Player z:XI)if(z!=null)z.starter=true;d.dismiss();tactic();});list.addView(bb);}
  sv.addView(list);box.addView(sv,new LinearLayout.LayoutParams(dp(470),dp(430)));d.setContentView(box);d.show();
 }
 int xiCount(){int n=0;for(Player q:XI)if(q!=null)n++;return n;}
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
  ArrayList<float[]> a=new ArrayList<>();String[][] map={{"GK","0.06","0.50"},{"LB","0.20","0.86"},{"LCB","0.20","0.65"},{"CB","0.19","0.50"},{"RCB","0.20","0.35"},{"RB","0.20","0.14"},{"LDM","0.34","0.66"},{"DM","0.33","0.50"},{"RDM","0.34","0.34"},{"LCM","0.45","0.66"},{"CM","0.45","0.50"},{"RCM","0.45","0.34"},{"LAM","0.57","0.66"},{"AM","0.58","0.50"},{"RAM","0.57","0.34"},{"LW","0.68","0.88"},{"LST","0.70","0.64"},{"ST","0.72","0.50"},{"RST","0.70","0.36"},{"RW","0.68","0.12"}};
  for(String[] m:map){int k=slotIndex(m[0]);if(XI[k]!=null)a.add(new float[]{Float.parseFloat(m[1]),Float.parseFloat(m[2])});}
  while(a.size()<11)a.add(new float[]{.42f+(a.size()%3)*.08f,.14f+(a.size()%6)*.14f});
  float[][] out=new float[11][2];for(int i=0;i<11;i++)out[i]=a.get(i);return out;
 }
 void match(Fixture f){
  if(xiCount()!=11){Toast.makeText(this,"경기 시작 전 선발 11명을 배치해주세요 ("+xiCount()+"/11)",Toast.LENGTH_LONG).show();tactic();return;}

  final Dialog d=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
  LinearLayout all=new LinearLayout(this);all.setOrientation(LinearLayout.HORIZONTAL);all.setBackgroundColor(BG);
  SmoothPitch pitch=new SmoothPitch(this, tacticalCoords());
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
  float[][] xy=new float[22][2], base=new float[22][2], dest=new float[22][2], vel=new float[22][2];
  float bx=.5f,by=.5f,btx=.5f,bty=.5f; int owner=0,receiver=-1,state=0,ticks=0,dribbleDef=-1,dribblePhase=0;
  // states: 0 possession, 1 pass, 2 through ball, 3 cross, 4 shot, 5 save/reset, 6 goal celebration, 7 one-v-one dribble
  boolean ballFlying=false,lastPossessionBlue=true,lastEventBlue=true,shotFlag,onTargetFlag,goalFlag,newLog=true;
  String eventText="KICK OFF";
  Handler h=new Handler(Looper.getMainLooper());
  SmoothPitch(Context c,float[][] userShape){
   super(c);
   float[][] f={{.055f,.50f},{.20f,.14f},{.19f,.38f},{.19f,.62f},{.20f,.86f},{.35f,.38f},{.35f,.62f},{.52f,.18f},{.55f,.50f},{.52f,.82f},{.70f,.50f}};
   for(int i=0;i<11;i++){float ux=(userShape!=null&&i<userShape.length)?userShape[i][0]:f[i][0],uy=(userShape!=null&&i<userShape.length)?userShape[i][1]:f[i][1];base[i][0]=ux;base[i][1]=uy;base[i+11][0]=1-f[i][0];base[i+11][1]=1-f[i][1];}
   for(int i=0;i<22;i++){xy[i][0]=dest[i][0]=base[i][0];xy[i][1]=dest[i][1]=base[i][1];}
   owner=0;bx=xy[0][0];by=xy[0][1];h.post(anim);
  }
  Runnable anim=new Runnable(){public void run(){
   for(int i=0;i<22;i++){float dx=dest[i][0]-xy[i][0],dy=dest[i][1]-xy[i][1];vel[i][0]=vel[i][0]*.86f+dx*.018f;vel[i][1]=vel[i][1]*.86f+dy*.018f;float vm=(float)Math.sqrt(vel[i][0]*vel[i][0]+vel[i][1]*vel[i][1]),mx=.0068f;if(vm>mx){vel[i][0]=vel[i][0]/vm*mx;vel[i][1]=vel[i][1]/vm*mx;}xy[i][0]+=vel[i][0];xy[i][1]+=vel[i][1];}
   if(ballFlying){
    // Normal passes track the receiver continuously. Through balls/crosses deliberately target space.
    if((state==1||state==3)&&receiver>=0){btx=xy[receiver][0];bty=xy[receiver][1];}
    float dx=btx-bx,dy=bty-by,dist=(float)Math.sqrt(dx*dx+dy*dy);float sp=state==4?.036f:state==3?.024f:state==2?.021f:.017f;
    if(dist>sp){bx+=dx/dist*sp;by+=dy/dist*sp;}else{bx=btx;by=bty;}
   }
   else {float lead=(state==7?.016f:.010f);float footX=xy[owner][0]+(owner<11?lead:-lead),footY=xy[owner][1]+.008f;float follow=state==7?.48f:.34f;bx+=(footX-bx)*follow;by+=(footY-by)*follow;}
   if(!ballFlying && state==0 && h.getLooper()!=null){ // micro-adjust continuously between decisions
    boolean blue=owner<11;float carrierX=xy[owner][0],carrierY=xy[owner][1];int b=blue?0:11;
    for(int j=0;j<22;j++)if(j!=owner){boolean same=(j<11)==blue;float sx=(carrierX-.5f)*(same?.30f:.20f);dest[j][0]=clip(base[j][0]+sx);if(!same)dest[j][1]=clip(base[j][1]+(carrierY-base[j][1])*.16f);}
   }
   invalidate();h.postDelayed(this,16);
  }};
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
     boolean on=r.nextFloat()<.68f;
     if(on){onTargetFlag=true;boolean goal=r.nextFloat()<.27f;
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
    if(dribblePhase==1){ // acceleration touch past defender
     float side=(xy[owner][1] <= xy[dribbleDef][1])?-.055f:.055f;
     dest[owner][0]=clip(xy[owner][0]+dir*.105f);dest[owner][1]=clip(xy[owner][1]+side);
     if(dribbleDef>=0){dest[dribbleDef][0]=clip(xy[dribbleDef][0]-dir*.018f);dest[dribbleDef][1]=clip(xy[dribbleDef][1]-side*.35f);}
     dribblePhase=2;eventText="⚡ 수비수를 벗겨내고 치고 나갑니다";newLog=true;return;
    }
    if(dribblePhase==2){float dx=dest[owner][0]-xy[owner][0],dy=dest[owner][1]-xy[owner][1];if(dx*dx+dy*dy<.0012f){state=0;dribbleDef=-1;dribblePhase=0;eventText="돌파 성공 · 전진";newLog=true;shape();}return;}
   }
   if(state==5){if(ticks%12==0){state=0;eventText="골키퍼가 다시 전개합니다";shape();}return;}
   if(state==6){if(ticks>22){resetKickoff(lastEventBlue?11:0);}return;}

   if(ticks%4!=0)return;
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
   // Final third: visible shot only from plausible positions.
   if(progress>.73f && Math.abs(xy[owner][1]-.5f)<.30f && r.nextFloat()<.34f){shoot(blue);return;}
   // Wide attackers/fullbacks cross from wide final-third positions.
   if(progress>.62f && (xy[owner][1]<.28f||xy[owner][1]>.72f) && r.nextFloat()<.42f){cross(blue);return;}
   // Midfielders sometimes send a through ball into space for an attacker to chase.
   if((local==5||local==6||local==7)&&progress>.42f&&r.nextFloat()<.30f){through(blue);return;}
   // Carrier may dribble a short distance; ball remains visibly at feet.
   if(r.nextFloat()<.28f){float dir=blue?.050f:-.050f;dest[owner][0]=clip(x+dir);dest[owner][1]=clip(xy[owner][1]+(r.nextFloat()-.5f)*.030f);eventText="볼을 몰고 전진";return;}
   pass(blue);
  }
  void pass(boolean blue){
   int b=blue?0:11,local=owner-b;int[] opts;
   if(local==0)opts=new int[]{1,2,3,4}; else if(local<=4)opts=new int[]{5,6,7}; else if(local<=7)opts=new int[]{8,9,10}; else opts=new int[]{5,6,7,8,9,10};
   int n=opts[r.nextInt(opts.length)];if(n==local)n=6;receiver=b+n;state=1;lastEventBlue=blue;
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
  void shape(){
   boolean blue=owner<11;float ballx=xy[owner][0],bally=xy[owner][1];
   for(int i=0;i<22;i++){boolean att=(i<11)==blue;float shift=(ballx-.5f)*(att?.32f:.22f);dest[i][0]=clip(base[i][0]+shift);dest[i][1]=clip(base[i][1]+(att?(base[i][1]-.5f)*.05f:(bally-base[i][1])*.18f));}
   // Support triangles, overlapping width and compact defensive block.
   int b=blue?0:11,ob=blue?11:0;
   dest[b+5][0]=clip(dest[b+5][0]+(blue?.035f:-.035f));dest[b+5][1]=clip(bally-.11f);
   dest[b+6][0]=clip(dest[b+6][0]+(blue?.055f:-.055f));dest[b+6][1]=clip(bally+.11f);
   // wide players offer outlets instead of all chasing the ball
   dest[b+8][1]=.14f;dest[b+10][1]=.86f;
   // nearest defender presses, next defender covers
   int n=nearestOpponent(owner);dest[n][0]=clip(xy[owner][0]+(blue?.035f:-.035f));dest[n][1]=clip(xy[owner][1]);
   int cover=ob+5;dest[cover][0]=clip(base[cover][0]+(ballx-.5f)*.18f);dest[cover][1]=clip(.5f+(bally-.5f)*.35f);
  }
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
   for(int i=0;i<22;i++){float px=l+xy[i][0]*fw,py=t+xy[i][1]*fh;p.setColor(Color.argb(70,0,0,0));c.drawCircle(px+2,py+3,dpv(10),p);p.setColor(i<11?Color.rgb(40,130,255):Color.rgb(235,64,64));c.drawCircle(px,py,dpv(9),p);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(2);p.setColor(Color.WHITE);c.drawCircle(px,py,dpv(9),p);p.setStyle(Paint.Style.FILL);p.setColor(Color.WHITE);p.setTextSize(dpv(9));c.drawText(""+(i%11+1),px-dpv(3),py+dpv(3),p);}
   // football: white body + black panels, not a plain dot
   float px=l+bx*fw,py=t+by*fh;p.setColor(Color.WHITE);c.drawCircle(px,py,dpv(6),p);p.setColor(Color.BLACK);c.drawCircle(px,py,dpv(2.2f),p);for(int k=0;k<5;k++){double a=k*Math.PI*2/5;c.drawCircle(px+(float)Math.cos(a)*dpv(3.7f),py+(float)Math.sin(a)*dpv(3.7f),dpv(1.1f),p);}
   if(eventText.contains("슈팅")||eventText.contains("GOAL")){p.setTextSize(dpv(18));p.setColor(Color.WHITE);p.setFakeBoldText(true);c.drawText(eventText,l+fw*.43f,t+dpv(28),p);p.setFakeBoldText(false);}
  }
  float dpv(float x){return x*getResources().getDisplayMetrics().density;}
 }
}
