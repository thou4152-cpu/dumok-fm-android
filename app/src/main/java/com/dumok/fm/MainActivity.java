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
 static class Player{
  String name,nation,pos,club,body,nick=""; int age,height,weight,ca,pa,fitness=100,apps,goals,value,wage;
  int[] te=new int[8],me=new int[8],ph=new int[8]; double[] xp=new double[24];
  double growth,professional,physicalRetention; int declineAge; boolean fa,starter;
 }
 static class Team{String name;int p,w,d,l,gf,ga,pts;Team(String n){name=n;}}
 static class Fixture{LocalDate date;Team h,a;boolean played;int hg,ag;Fixture(LocalDate d,Team x,Team y){date=d;h=x;a=y;}}
 ArrayList<Player>squad=new ArrayList<>(),pool=new ArrayList<>();ArrayList<Team>teams=new ArrayList<>();ArrayList<Fixture>fixtures=new ArrayList<>();
 Random R=new Random(4152); LocalDate now=LocalDate.of(2026,7,1); String club="DUMOK FC",formation="4-2-3-1",mentality="균형";int budget=650;
 int BG=Color.rgb(8,15,27),CARD=Color.rgb(18,29,45),GREEN=Color.rgb(48,211,145),MUTED=Color.rgb(150,166,186);
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

 public void onCreate(Bundle b){super.onCreate(b);getWindow().setNavigationBarColor(BG);getWindow().setStatusBarColor(BG);seed();home();}
 int dp(int x){return(int)(x*getResources().getDisplayMetrics().density+.5f);}
 TextView tx(String s,int z,int c){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(c);v.setPadding(dp(10),dp(7),dp(10),dp(7));return v;}
 Button bt(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(12);return b;}
 LinearLayout card(){LinearLayout x=new LinearLayout(this);x.setOrientation(LinearLayout.VERTICAL);x.setPadding(dp(8),dp(6),dp(8),dp(6));x.setBackgroundColor(CARD);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(dp(6),dp(5),dp(6),dp(5));x.setLayoutParams(p);return x;}

 void seed(){
  String[] ns={"DUMOK FC","JEONJU UNITED","SEOUL CITY","BUSAN ATHLETIC","INCHEON BLUE","DAEGU REDS","SUWON KNIGHTS","DAEJEON PHOENIX"};
  for(String n:ns)teams.add(new Team(n));
  for(int i=0;i<25;i++){Player p=gen(false,club,true);p.starter=i<11;squad.add(p);}
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
 void tactic(){frame("전술");LinearLayout c=card();c.addView(tx("포메이션 "+formation+" / 멘탈리티 "+mentality,18,Color.WHITE));c.addView(tx("V3 매치엔진은 선수 CA 하나가 아니라 패스·시야·판단력, 주력·가속도·오프더볼, 헤딩·점프·몸싸움 등의 조합을 사용합니다.",13,MUTED));body.addView(c);}
 void calendar(){frame("일정");for(Fixture f:fixtures)if(!f.date.isBefore(now.minusDays(14))&&(f.h.name.equals(club)||f.a.name.equals(club))){LinearLayout c=card();c.addView(tx((f.played?"✓ ":"⚽ ")+f.date+"   "+f.h.name+" "+(f.played?f.hg+" - "+f.ag:"vs")+" "+f.a.name,14,f.played?MUTED:GREEN));body.addView(c);}}
 void market(){frame("이적시장");body.addView(tx(open()?"🟢 OPEN — 소속 선수와 FA 계약 가능":"🔴 CLOSED — FA만 계약 가능",15,open()?GREEN:MUTED));int shown=0;for(Player p:pool){if(!open()&&!p.fa)continue;LinearLayout r=card();r.setOrientation(LinearLayout.HORIZONTAL);TextView n=tx((p.fa?"[FA] ":"")+p.name+"\n"+p.nick,12,Color.WHITE);n.setOnClickListener(v->detail(p));r.addView(n,new LinearLayout.LayoutParams(0,-2,2));r.addView(tx(p.nation+" "+p.pos,12,MUTED),new LinearLayout.LayoutParams(0,-2,1));r.addView(tx(p.age+"세 "+p.height+"cm",12,MUTED),new LinearLayout.LayoutParams(0,-2,1));r.addView(tx("CA "+p.ca+" / PA "+p.pa,12,GREEN),new LinearLayout.LayoutParams(0,-2,1));Button b=bt(p.fa?"자유계약":"영입 "+p.value+"억");b.setOnClickListener(v->sign(p));r.addView(b,new LinearLayout.LayoutParams(0,-2,1));body.addView(r);if(++shown>=70)break;}}
 void sign(Player p){if(!p.fa&&!open()){Toast.makeText(this,"이적시장 기간이 아닙니다",0).show();return;}int fee=p.fa?0:p.value;if(budget<fee){Toast.makeText(this,"예산 부족",0).show();return;}budget-=fee;p.fa=false;p.club=club;squad.add(p);pool.remove(p);market();}
 void league(){frame("리그");ArrayList<Team>a=new ArrayList<>(teams);Collections.sort(a,(x,y)->y.pts!=x.pts?y.pts-x.pts:(y.gf-y.ga)-(x.gf-x.ga));int i=1;for(Team t:a){body.addView(tx(i+++"  "+t.name+"   "+t.p+"경기   "+t.w+"승 "+t.d+"무 "+t.l+"패   "+t.gf+":"+t.ga+"   "+t.pts+"점",14,t.name.equals(club)?GREEN:Color.WHITE));}}
 void advance(boolean event){
  Fixture f=next();if(f!=null&&f.date.equals(now)){match(f);return;}LocalDate target=now.plusDays(1);if(event&&f!=null)target=f.date;
  while(now.isBefore(target)){now=now.plusDays(1);dailyGrowth();}
  f=next();if(f!=null&&f.date.equals(now))match(f);else home();
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
 void match(Fixture f){
  final Dialog d=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);LinearLayout all=new LinearLayout(this);all.setOrientation(LinearLayout.HORIZONTAL);all.setBackgroundColor(BG);
  SmoothPitch pitch=new SmoothPitch(this);all.addView(pitch,new LinearLayout.LayoutParams(0,-1,3));
  LinearLayout panel=new LinearLayout(this);panel.setOrientation(LinearLayout.VERTICAL);TextView score=tx(f.h.name+"\n0 - 0\n"+f.a.name+"\n0'",19,Color.WHITE);score.setGravity(Gravity.CENTER);panel.addView(score);TextView log=tx("킥오프",13,MUTED);panel.addView(log);Button speed=bt("속도 x1");panel.addView(speed);all.addView(panel,new LinearLayout.LayoutParams(0,-1,1));d.setContentView(all);d.show();
  int[] min={0},hg={0},ag={0},delay={600};Handler H=new Handler(Looper.getMainLooper());
  Runnable tick=new Runnable(){public void run(){
   min[0]+=2;pitch.newPlay();
   double attack=teamAttack();
   if(R.nextDouble()<.055+attack/5000.0){if(f.h.name.equals(club))hg[0]++;else ag[0]++;log.setText(min[0]+"'  ⚽ DUMOK FC 득점 장면");awardMatchXP(true);}
   if(R.nextDouble()<.055){if(f.h.name.equals(club))ag[0]++;else hg[0]++;log.setText(min[0]+"'  상대팀 득점");}
   score.setText(f.h.name+"\n"+hg[0]+" - "+ag[0]+"\n"+f.a.name+"\n"+Math.min(90,min[0])+"'");
   if(min[0]>=90){finish(f,hg[0],ag[0]);speed.setText("경기 종료");speed.setOnClickListener(v->{d.dismiss();home();});}
   else H.postDelayed(this,delay[0]);
  }};
  speed.setOnClickListener(v->{delay[0]=delay[0]==600?220:delay[0]==220?90:600;speed.setText(delay[0]==600?"속도 x1":delay[0]==220?"속도 x2":"속도 x3");});H.postDelayed(tick,700);
 }
 double teamAttack(){double s=0;int n=0;for(Player p:squad)if(p.starter){double run=p.ph[0]*1.4+p.ph[1]*1.5+p.me[3]*1.3;double tech=p.te[0]*1.5+p.te[1]+p.te[2]+p.me[0];s+=run+tech;n++;}return n==0?0:s/n;}
 void awardMatchXP(boolean attack){for(Player p:squad)if(p.starter){int[] cand=p.pos.equals("ST")||p.pos.equals("RW")||p.pos.equals("LW")?new int[]{0,1,2,8,11,16,17,18}:new int[]{3,10,9,14,22};int idx=cand[R.nextInt(cand.length)];if(idx>=16&&p.age>20)idx=R.nextInt(16);p.xp[idx]+=2.0*p.growth*p.professional;p.apps++;}}
 void finish(Fixture f,int x,int y){f.played=true;f.hg=x;f.ag=y;result(f.h,f.a,x,y);for(Fixture q:fixtures)if(!q.played&&q.date.equals(f.date)){int a=R.nextInt(4),b=R.nextInt(4);q.played=true;q.hg=a;q.ag=b;result(q.h,q.a,a,b);}for(Player p:squad)if(p.starter)p.fitness=Math.max(55,p.fitness-6-R.nextInt(10));now=now.plusDays(1);}
 void result(Team h,Team a,int x,int y){h.p++;a.p++;h.gf+=x;h.ga+=y;a.gf+=y;a.ga+=x;if(x>y){h.w++;a.l++;h.pts+=3;}else if(x<y){a.w++;h.l++;a.pts+=3;}else{h.d++;a.d++;h.pts++;a.pts++;}}

 static class SmoothPitch extends View{
  Paint p=new Paint(1); Random r=new Random();
  // 0-9 blue: GK,RB,RCB,LCB,LB,DM,CM,RW,AM,LW/ST hybrid; 10-19 red mirror
  float[][] xy=new float[20][2], home=new float[20][2], target=new float[20][2];
  int owner=0, phase=0, receiver=-1; float bx,by,btx,bty; boolean ballFlying=false;
  Handler h=new Handler(Looper.getMainLooper());
  SmoothPitch(Context c){
   super(c);
   float[][] shape={{.07f,.50f},{.22f,.18f},{.20f,.38f},{.20f,.62f},{.22f,.82f},{.35f,.43f},{.43f,.58f},{.57f,.15f},{.60f,.50f},{.68f,.78f}};
   for(int i=0;i<10;i++){home[i][0]=shape[i][0];home[i][1]=shape[i][1];home[i+10][0]=1-shape[i][0];home[i+10][1]=1-shape[i][1];}
   for(int i=0;i<20;i++){xy[i][0]=target[i][0]=home[i][0];xy[i][1]=target[i][1]=home[i][1];}
   owner=0; bx=xy[owner][0];by=xy[owner][1]; h.post(anim);
  }
  Runnable anim=new Runnable(){public void run(){
   for(int i=0;i<20;i++){xy[i][0]+=(target[i][0]-xy[i][0])*.055f;xy[i][1]+=(target[i][1]-xy[i][1])*.055f;}
   if(ballFlying){bx+=(btx-bx)*.16f;by+=(bty-by)*.16f;if(Math.abs(bx-btx)<.012&&Math.abs(by-bty)<.012){owner=receiver;receiver=-1;ballFlying=false;}}
   else {bx+=(xy[owner][0]-bx)*.30f;by+=(xy[owner][1]-by)*.30f;}
   invalidate();h.postDelayed(this,16);
  }};
  void newPlay(){
   // Every match tick advances a coherent possession phase rather than teleporting 20 random dots.
   phase++;
   int team=owner<10?0:1, base=team*10;
   if(phase%7==0 && r.nextFloat()<.28f){ // turnover: nearest-ish opponent wins it
    int k=base==0?10:0; owner=k+1+r.nextInt(8); ballFlying=false; shapeAroundBall(); return;
   }
   int local=owner-base;
   int nextLocal;
   if(local==0) nextLocal=2+r.nextInt(3);             // GK -> CB/FB
   else if(local<=4) nextLocal=r.nextBoolean()?5:6;   // back line -> midfield
   else if(local<=6) nextLocal=7+r.nextInt(3);        // midfield -> attacking line
   else { // final third: recycle, combine, or drive toward goal
    if(r.nextFloat()<.42f){target[owner][0]=team==0?.88f:.12f;target[owner][1]=.35f+r.nextFloat()*.30f;shapeAroundBall();return;}
    nextLocal=7+r.nextInt(3);
   }
   if(nextLocal==local)nextLocal=6;
   receiver=base+nextLocal;btx=xy[receiver][0];bty=xy[receiver][1];ballFlying=true;shapeAroundBall();
  }
  void shapeAroundBall(){
   boolean blue=owner<10;float ballX=bx;
   for(int i=0;i<20;i++){
    boolean same=(i<10)==blue;
    float hx=home[i][0],hy=home[i][1];
    float shift=(ballX-.5f)*(same?.34f:.24f);
    // Team in possession expands; defending team compresses around ball.
    target[i][0]=clip(hx+shift);
    if(same) target[i][1]=clip(hy+(hy-.5f)*.08f);
    else target[i][1]=clip(hy+(by-hy)*.20f);
   }
   // Ball carrier and nearby support preserve local triangles.
   target[owner][0]=clip(bx+(blue?.045f:-.045f));target[owner][1]=clip(by);
   int b=blue?0:10;
   target[b+5][1]=clip(by-.12f);target[b+6][1]=clip(by+.12f);
  }
  float clip(float v){return Math.max(.045f,Math.min(.955f,v));}
  protected void onDraw(Canvas c){
   float w=getWidth(),h=getHeight();c.drawColor(Color.rgb(32,116,67));
   p.setStyle(Paint.Style.FILL);p.setColor(Color.argb(28,255,255,255));for(int i=0;i<10;i+=2)c.drawRect(i*w/10,0,(i+1)*w/10,h,p);
   p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3);p.setColor(Color.WHITE);c.drawRect(10,10,w-10,h-10,p);c.drawLine(w/2,10,w/2,h-10,p);c.drawCircle(w/2,h/2,Math.min(w,h)*.12f,p);
   c.drawRect(10,h*.27f,w*.16f,h*.73f,p);c.drawRect(w*.84f,h*.27f,w-10,h*.73f,p);
   c.drawRect(10,h*.38f,w*.07f,h*.62f,p);c.drawRect(w*.93f,h*.38f,w-10,h*.62f,p);
   p.setStyle(Paint.Style.FILL);
   for(int i=0;i<20;i++){p.setColor(i<10?Color.rgb(65,145,255):Color.rgb(245,75,75));c.drawCircle(xy[i][0]*w,xy[i][1]*h,10,p);p.setColor(Color.WHITE);p.setTextSize(11);c.drawText(""+(i%10+1),xy[i][0]*w-4,xy[i][1]*h+4,p);}
   p.setColor(Color.WHITE);c.drawCircle(bx*w,by*h,5,p);
  }
 }
}