package com.dumok.fm;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainActivity extends Activity {
 static class Player{
  String name,nation,pos,club; int age,ca,pa,value,wage,fitness=100,apps,goals; boolean starter,fa;
  Player(String n,String nat,String p,int a,int c,int pot,String cl,boolean free){
   name=n;nation=nat;pos=p;age=a;ca=c;pa=pot;club=cl;fa=free;
   value=Math.max(1,(ca*ca)/90 + Math.max(0,pa-ca)*2); wage=Math.max(1,ca/5);
  }
 }
 static class Team{String name;int p,w,d,l,gf,ga,pts;Team(String n){name=n;}}
 static class Fixture{
  LocalDate date; Team home,away; boolean played; int hg,ag;
  Fixture(LocalDate d,Team h,Team a){date=d;home=h;away=a;}
 }
 ArrayList<Player> squad=new ArrayList<>(), pool=new ArrayList<>();
 ArrayList<Team> teams=new ArrayList<>(); ArrayList<Fixture> fixtures=new ArrayList<>();
 Random rng=new Random(4152); LocalDate now=LocalDate.of(2026,7,1);
 String club="DUMOK FC", formation="4-2-3-1", mentality="균형";
 int budget=650, green=Color.rgb(48,211,145), bg=Color.rgb(8,15,27), card=Color.rgb(18,29,45), muted=Color.rgb(155,170,190);
 LinearLayout root,content,bottom; SharedPreferences prefs;

 String[][] NAMES={
  {"대한민국","김","이","박","최","정","강","조","윤","장","민준","서준","도윤","지훈","현우","재민"},
  {"잉글랜드","James","Oliver","George","Harry","Jack","Smith","Brown","Wilson","Taylor","Walker"},
  {"스페인","Alejandro","Pablo","Diego","Javier","Carlos","Garcia","Ruiz","Torres","Navarro","Santos"},
  {"브라질","Lucas","Gabriel","Matheus","Rafael","Joao","Silva","Costa","Santos","Oliveira","Pereira"},
  {"아르헨티나","Mateo","Thiago","Lautaro","Nicolas","Julian","Gomez","Romero","Alvarez","Fernandez","Diaz"},
  {"프랑스","Hugo","Theo","Lucas","Enzo","Mathis","Martin","Dubois","Bernard","Laurent","Petit"},
  {"독일","Leon","Jonas","Felix","Lukas","Max","Muller","Schmidt","Fischer","Weber","Wagner"},
  {"이탈리아","Luca","Marco","Matteo","Andrea","Davide","Rossi","Romano","Ricci","Conti","Moretti"},
  {"일본","Haruto","Ren","Takumi","Kaito","Riku","Sato","Suzuki","Takahashi","Tanaka","Watanabe"},
  {"포르투갈","Joao","Tiago","Diogo","Rui","Andre","Silva","Santos","Costa","Pereira","Fernandes"},
  {"네덜란드","Daan","Sem","Luuk","Finn","Jesse","De Jong","Van Dijk","Visser","Smit","Bakker"},
  {"나이지리아","Chinedu","Victor","Samuel","Ibrahim","Emeka","Okafor","Balogun","Musa","Adebayo","Eze"},
  {"미국","Ethan","Noah","Liam","Mason","Logan","Johnson","Miller","Davis","Moore","Clark"}
 };
 String[] POS={"GK","RB","CB","CB","LB","DM","CM","AM","RW","LW","ST"};

 @Override public void onCreate(Bundle b){
  super.onCreate(b); prefs=getSharedPreferences("dumok_v2",MODE_PRIVATE);
  getWindow().setNavigationBarColor(bg); getWindow().setStatusBarColor(bg);
  seedWorld(); home();
 }
 int dp(int x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
 TextView tx(String s,int z,int c){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(c);v.setPadding(dp(14),dp(9),dp(14),dp(9));return v;}
 Button btn(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(13);return b;}
 LinearLayout box(){LinearLayout x=new LinearLayout(this);x.setOrientation(LinearLayout.VERTICAL);x.setPadding(dp(8),dp(7),dp(8),dp(7));x.setBackgroundColor(card);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(dp(10),dp(6),dp(10),dp(6));x.setLayoutParams(p);return x;}

 void seedWorld(){
  String[] tn={"DUMOK FC","JEONJU UNITED","SEOUL CITY","BUSAN ATHLETIC","INCHEON BLUE","DAEGU REDS","SUWON KNIGHTS","DAEJEON PHOENIX"};
  for(String s:tn)teams.add(new Team(s));
  // user squad: globally generated, stronger than average
  for(int i=0;i<24;i++){Player p=genPlayer(false,club);p.ca=Math.max(p.ca,95+rng.nextInt(45));p.pa=Math.max(p.ca,Math.min(200,p.ca+rng.nextInt(45)));p.value=Math.max(5,(p.ca*p.ca)/90);p.starter=i<11;squad.add(p);}
  // broad world pool: contracted + free agents
  for(int i=0;i<3500;i++){
   boolean fa=rng.nextInt(100)<18; String owner=fa?"FA":tn[1+rng.nextInt(tn.length-1)];
   pool.add(genPlayer(fa,owner));
  }
  generateSchedule();
 }
 Player genPlayer(boolean fa,String owner){
  String[] set=NAMES[rng.nextInt(NAMES.length)]; String nation=set[0];
  String name;
  if(nation.equals("대한민국")) name=set[1+rng.nextInt(8)]+set[9+rng.nextInt(set.length-9)];
  else name=set[1+rng.nextInt(5)]+" "+set[6+rng.nextInt(set.length-6)];
  int age=16+rng.nextInt(30);
  // Most players ordinary; elite CA/PA deliberately rare.
  double u=rng.nextDouble();
  int ca=(int)(35+145*Math.pow(u,1.75)); ca=Math.min(190,Math.max(25,ca));
  int upside;
  if(age<=21) upside=rng.nextInt(70); else if(age<=27) upside=rng.nextInt(38); else upside=rng.nextInt(15);
  int pa=Math.min(200,Math.max(ca,ca+upside));
  String pos=POS[rng.nextInt(POS.length)];
  return new Player(name,nation,pos,age,ca,pa,owner,fa);
 }
 void generateSchedule(){
  ArrayList<Team> arr=new ArrayList<>(teams); int n=arr.size(); LocalDate start=LocalDate.of(2026,8,15);
  ArrayList<Team> rot=new ArrayList<>(arr);
  int round=0;
  for(int leg=0;leg<2;leg++){
   for(int r=0;r<n-1;r++){
    LocalDate d=start.plusWeeks(round++);
    for(int i=0;i<n/2;i++){
     Team a=rot.get(i), b=rot.get(n-1-i);
     boolean flip=((r+i+leg)%2==1);
     Team h=flip?b:a, aw=flip?a:b;
     if(leg==1){Team tmp=h;h=aw;aw=tmp;}
     fixtures.add(new Fixture(d,h,aw));
    }
    Team fixed=rot.get(0); Team last=rot.remove(rot.size()-1);rot.add(1,last);
   }
  }
  fixtures.sort(Comparator.comparing(f->f.date));
 }
 boolean marketOpen(){
  int y=now.getYear();
  LocalDate s1=LocalDate.of(y,6,30),e1=LocalDate.of(y,9,1),s2=LocalDate.of(y,1,1),e2=LocalDate.of(y,2,1);
  return (!now.isBefore(s1)&&!now.isAfter(e1))||(!now.isBefore(s2)&&!now.isAfter(e2));
 }
 Fixture nextMyFixture(){
  for(Fixture f:fixtures)if(!f.played&&!f.date.isBefore(now)&&(f.home.name.equals(club)||f.away.name.equals(club)))return f;
  return null;
 }
 void frame(String title){
  root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(bg);
  TextView h=tx("⚽ "+club+"     "+now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),18,Color.WHITE);h.setBackgroundColor(Color.rgb(11,24,38));root.addView(h);
  root.addView(tx(title+"     예산 "+budget+"억",14,green));
  ScrollView sv=new ScrollView(this);content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);sv.addView(content);root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));
  bottom=new LinearLayout(this);bottom.setOrientation(LinearLayout.HORIZONTAL);bottom.setPadding(0,0,0,dp(10));bottom.setBackgroundColor(Color.rgb(11,24,38));
  nav("홈","H");nav("선수단","S");nav("전술","T");nav("이적","M");nav("리그","L");root.addView(bottom,new LinearLayout.LayoutParams(-1,dp(68)));
  setContentView(root);
  root.setOnApplyWindowInsetsListener((v,in)->{int b=in.getSystemWindowInsetBottom();bottom.setPadding(0,0,0,Math.max(dp(8),b));return in;});
 }
 void nav(String s,String id){Button b=btn(s);b.setOnClickListener(v->{if(id.equals("H"))home();else if(id.equals("S"))squadPage();else if(id.equals("T"))tactic();else if(id.equals("M"))marketPage();else leaguePage();});bottom.addView(b,new LinearLayout.LayoutParams(0,-1,1));}

 void home(){
  frame("감독 대시보드");
  Fixture f=nextMyFixture(); LinearLayout x=box();x.addView(tx("다음 일정",14,muted));
  if(f!=null){String opp=f.home.name.equals(club)?f.away.name:f.home.name;x.addView(tx(f.date+"  "+club+" vs "+opp,21,Color.WHITE));x.addView(tx("리그 경기 · "+(f.date.toEpochDay()-now.toEpochDay())+"일 후",14,green));
   if(f.date.equals(now)){Button p=btn("▶ 2D 경기 시작");p.setOnClickListener(v->matchScreen(f));x.addView(p);}
  }else x.addView(tx("예정된 경기가 없습니다",18,Color.WHITE));content.addView(x);

  LinearLayout cal=box();cal.addView(tx("다가오는 14일",16,Color.WHITE));
  LocalDate end=now.plusDays(14);boolean any=false;
  for(Fixture q:fixtures)if(!q.played&&!q.date.isBefore(now)&&!q.date.isAfter(end)&&(q.home.name.equals(club)||q.away.name.equals(club))){cal.addView(tx("⚽ "+q.date+"  "+q.home.name+" vs "+q.away.name,14,green));any=true;}
  if(!any)cal.addView(tx("경기 없음 · 훈련/회복 기간",14,muted));
  if(now.getMonthValue()==6&&now.getDayOfMonth()==30)cal.addView(tx("💼 여름 이적시장 개장",14,green));
  content.addView(cal);

  LinearLayout a=box();a.addView(tx("시간 진행",16,Color.WHITE));
  Button d=btn("하루 진행");d.setOnClickListener(v->advanceTo(false));a.addView(d);
  Button e=btn("다음 중요 이벤트까지");e.setOnClickListener(v->advanceTo(true));a.addView(e);content.addView(a);

  LinearLayout m=box();m.addView(tx("이적시장",16,Color.WHITE));
  m.addView(tx(marketOpen()?"🟢 OPEN · 소속 선수/FA 영입 가능":"🔴 CLOSED · FA 선수만 계약 가능",15,marketOpen()?green:muted));
  m.addView(tx("여름 6/30~9/1 · 겨울 1/1~2/1 · 월드 선수 DB "+pool.size()+"명",13,muted));content.addView(m);
 }
 void advanceTo(boolean event){
  Fixture n=nextMyFixture(); LocalDate target=now.plusDays(1);
  if(event&&n!=null&&n.date.isAfter(now))target=n.date;
  while(now.isBefore(target)){now=now.plusDays(1);for(Player p:squad)p.fitness=Math.min(100,p.fitness+2);}
  home();
 }
 void squadPage(){
  frame("선수단");
  for(Player p:squad){LinearLayout x=box();x.addView(tx((p.starter?"★ ":"")+p.name+"  "+p.nation+"  "+p.pos+"  "+p.age+"세",16,Color.WHITE));x.addView(tx("CA "+p.ca+" / PA "+p.pa+"   체력 "+p.fitness+"%   가치 "+p.value+"억",14,p.ca>=140?green:muted));Button b=btn(p.starter?"선발 제외":"선발 등록");b.setOnClickListener(v->{toggle(p);squadPage();});x.addView(b);content.addView(x);}
 }
 void toggle(Player p){if(p.starter){p.starter=false;return;}int c=0;for(Player q:squad)if(q.starter)c++;if(c>=11){Toast.makeText(this,"선발은 11명까지",Toast.LENGTH_SHORT).show();return;}p.starter=true;}
 void tactic(){
  frame("전술");
  LinearLayout f=box();f.addView(tx("포메이션",16,Color.WHITE));for(String s:new String[]{"4-2-3-1","4-3-3","4-4-2","3-4-2-1"}){Button b=btn((formation.equals(s)?"✓ ":"")+s);b.setOnClickListener(v->{formation=s;tactic();});f.addView(b);}content.addView(f);
  LinearLayout m=box();m.addView(tx("멘탈리티",16,Color.WHITE));for(String s:new String[]{"수비","균형","공격"}){Button b=btn((mentality.equals(s)?"✓ ":"")+s);b.setOnClickListener(v->{mentality=s;tactic();});m.addView(b);}content.addView(m);
 }
 void marketPage(){
  frame("이적시장 · 선수 검색");
  LinearLayout status=box();status.addView(tx(marketOpen()?"🟢 이적시장 OPEN":"🔴 이적시장 CLOSED",17,marketOpen()?green:Color.LTGRAY));status.addView(tx(marketOpen()?"모든 선수와 협상할 수 있습니다.":"현재는 무소속(FA) 선수만 계약할 수 있습니다.",14,muted));content.addView(status);
  EditText search=new EditText(this);search.setHint("이름/국적/포지션 검색");search.setTextColor(Color.WHITE);search.setHintTextColor(muted);content.addView(search);
  LinearLayout list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);content.addView(list);
  Button go=btn("검색");go.setOnClickListener(v->renderMarket(list,search.getText().toString()));content.addView(go,1);
  renderMarket(list,"");
 }
 void renderMarket(LinearLayout list,String query){
  list.removeAllViews();String q=query.trim().toLowerCase();int shown=0;
  for(Player p:pool){if(!marketOpen()&&!p.fa)continue;if(!q.isEmpty()&&!(p.name+" "+p.nation+" "+p.pos).toLowerCase().contains(q))continue;
   LinearLayout x=box();x.addView(tx((p.fa?"[FA] ":"")+p.name+" · "+p.nation+" · "+p.pos+" · "+p.age+"세",15,Color.WHITE));x.addView(tx("CA "+p.ca+" / PA "+p.pa+" · "+(p.fa?"무소속":p.club)+" · 가치 "+p.value+"억",13,p.pa>=170?green:muted));
   Button b=btn(p.fa?"자유계약":"영입 제안 "+p.value+"억");b.setOnClickListener(v->sign(p));x.addView(b);list.addView(x);if(++shown>=80)break;
  }
  if(shown==0)list.addView(tx("조건에 맞는 선수가 없습니다.",15,muted));
 }
 void sign(Player p){
  if(!p.fa&&!marketOpen()){Toast.makeText(this,"이적시장 기간이 아닙니다.",Toast.LENGTH_SHORT).show();return;}
  int fee=p.fa?0:p.value;if(budget<fee){Toast.makeText(this,"예산 부족",Toast.LENGTH_SHORT).show();return;}
  budget-=fee;p.club=club;p.fa=false;squad.add(p);pool.remove(p);Toast.makeText(this,p.name+" 영입 완료",Toast.LENGTH_SHORT).show();marketPage();
 }
 void leaguePage(){
  frame("리그");
  ArrayList<Team> t=new ArrayList<>(teams);Collections.sort(t,(a,b)->b.pts!=a.pts?b.pts-a.pts:(b.gf-b.ga)-(a.gf-a.ga));int r=1;
  for(Team q:t){LinearLayout x=box();x.addView(tx(r+"  "+q.name,16,q.name.equals(club)?green:Color.WHITE));x.addView(tx("경기 "+q.p+"  "+q.w+"승 "+q.d+"무 "+q.l+"패  "+q.gf+":"+q.ga+"  승점 "+q.pts,13,muted));content.addView(x);r++;}
  LinearLayout fx=box();fx.addView(tx("최근/예정 일정",16,Color.WHITE));int c=0;for(Fixture f:fixtures)if((f.home.name.equals(club)||f.away.name.equals(club))&&!f.date.isBefore(now.minusDays(30))){fx.addView(tx(f.date+"  "+f.home.name+" "+(f.played?f.hg+"-"+f.ag:"vs")+" "+f.away.name,13,f.played?muted:green));if(++c>=12)break;}content.addView(fx);
 }

 void matchScreen(Fixture f){
  int starters=0;for(Player p:squad)if(p.starter)starters++;if(starters<11){Toast.makeText(this,"선발 11명을 구성하세요",Toast.LENGTH_SHORT).show();return;}
  final Dialog d=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen); LinearLayout wrap=new LinearLayout(this);wrap.setOrientation(LinearLayout.VERTICAL);wrap.setBackgroundColor(bg);
  TextView score=tx(f.home.name+"   0 - 0   "+f.away.name+"\n0'",20,Color.WHITE);score.setGravity(Gravity.CENTER);wrap.addView(score);
  PitchView pitch=new PitchView(this);wrap.addView(pitch,new LinearLayout.LayoutParams(-1,0,1));
  TextView log=tx("경기 시작",14,Color.WHITE);wrap.addView(log);
  Button fast=btn("▶▶ 경기 진행");wrap.addView(fast);d.setContentView(wrap);d.show();
  final int[] min={0},hg={0},ag={0}; Handler h=new Handler(Looper.getMainLooper());
  Runnable tick=new Runnable(){public void run(){
   min[0]+=5;pitch.step();
   int my=0;for(Player p:squad)if(p.starter)my+=p.ca*p.fitness/100;my/=11;
   if(rng.nextInt(100)<18+(my-100)/8){if(f.home.name.equals(club))hg[0]++;else ag[0]++;log.setText(min[0]+"'  ⚽ "+club+" 득점!");}
   if(rng.nextInt(100)<18){if(f.home.name.equals(club))ag[0]++;else hg[0]++;log.setText(min[0]+"'  상대팀 득점");}
   score.setText(f.home.name+"   "+hg[0]+" - "+ag[0]+"   "+f.away.name+"\n"+Math.min(90,min[0])+"'");
   if(min[0]>=90){finishFixture(f,hg[0],ag[0]);fast.setText("경기 종료 · 확인");fast.setOnClickListener(v->{d.dismiss();home();});}
   else h.postDelayed(this,450);
  }};
  fast.setOnClickListener(v->{fast.setEnabled(false);h.post(tick);});
 }
 void finishFixture(Fixture f,int hg,int ag){
  f.played=true;f.hg=hg;f.ag=ag;applyResult(f.home,f.away,hg,ag);
  // simulate all other matches on same date
  for(Fixture q:fixtures)if(!q.played&&q.date.equals(f.date)){int a=rng.nextInt(4),b=rng.nextInt(4);q.played=true;q.hg=a;q.ag=b;applyResult(q.home,q.away,a,b);}
  for(Player p:squad)if(p.starter){p.apps++;p.fitness=Math.max(55,p.fitness-7-rng.nextInt(10));}
  now=now.plusDays(1);
 }
 void applyResult(Team h,Team a,int x,int y){h.p++;a.p++;h.gf+=x;h.ga+=y;a.gf+=y;a.ga+=x;if(x>y){h.w++;a.l++;h.pts+=3;}else if(x<y){a.w++;h.l++;a.pts+=3;}else{h.d++;a.d++;h.pts++;a.pts++;}}

 static class PitchView extends View{
  Paint p=new Paint(1);Random r=new Random();float ballX=.5f,ballY=.5f;float[][] dots=new float[20][2];
  PitchView(Context c){super(c);for(int i=0;i<20;i++){dots[i][0]=.08f+r.nextFloat()*.84f;dots[i][1]=.08f+r.nextFloat()*.84f;}}
  void step(){ballX=.1f+r.nextFloat()*.8f;ballY=.1f+r.nextFloat()*.8f;for(float[] d:dots){d[0]=Math.max(.05f,Math.min(.95f,d[0]+(r.nextFloat()-.5f)*.12f));d[1]=Math.max(.05f,Math.min(.95f,d[1]+(r.nextFloat()-.5f)*.12f));}invalidate();}
  protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight();c.drawColor(Color.rgb(28,115,68));p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4);p.setColor(Color.WHITE);c.drawRect(12,12,w-12,h-12,p);c.drawLine(w/2,12,w/2,h-12,p);c.drawCircle(w/2,h/2,Math.min(w,h)*.12f,p);p.setStyle(Paint.Style.FILL);
   for(int i=0;i<dots.length;i++){p.setColor(i<10?Color.rgb(70,150,255):Color.rgb(245,75,75));c.drawCircle(dots[i][0]*w,dots[i][1]*h,10,p);}p.setColor(Color.WHITE);c.drawCircle(ballX*w,ballY*h,7,p);}
 }
}