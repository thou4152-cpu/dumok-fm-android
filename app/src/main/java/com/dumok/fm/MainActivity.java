package com.dumok.fm;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    static class Player {
        String name,pos; int age,ca,pa,value,wage,fitness,goals,apps; boolean starter;
        Player(String n,String p,int a,int c,int potential,int v,int w){
            name=n;pos=p;age=a;ca=c;pa=potential;value=v;wage=w;fitness=100;
        }
    }
    static class Team {
        String name; int pts,gf,ga,w,d,l,p;
        Team(String n){name=n;}
    }

    ArrayList<Player> squad=new ArrayList<>();
    ArrayList<Player> market=new ArrayList<>();
    ArrayList<Team> league=new ArrayList<>();
    LinearLayout page, content, bottom;
    SharedPreferences save;
    Random rng=new Random();
    int day=1, month=7, year=2026, budget=420, round=0;
    String club="DUMOK FC", formation="4-2-3-1", mentality="균형";
    int green=Color.rgb(48,211,145), bg=Color.rgb(8,15,27), card=Color.rgb(18,29,45), muted=Color.rgb(150,165,185);

    @Override public void onCreate(Bundle b){
        super.onCreate(b); save=getSharedPreferences("career",MODE_PRIVATE);
        seed(); if(save.getBoolean("hasSave",false)) load(); home();
    }

    void seed(){
        if(!squad.isEmpty())return;
        String[] names={"김도윤","이준혁","박시우","최민재","한지성","윤태호","강민석","서준","임도현","백승현","송재원","정우진","오현석","문태양","조성민","권혁준","신재민","유승우","장민호","노준서","배지훈","황도현"};
        String[] pos={"GK","GK","RB","CB","CB","LB","DM","CM","CM","AM","RW","LW","ST","ST","CB","RB","LB","DM","AM","RW","LW","ST"};
        for(int i=0;i<names.length;i++){
            int pa=65+rng.nextInt(31), ca=Math.min(pa,52+rng.nextInt(28));
            squad.add(new Player(names[i],pos[i],18+rng.nextInt(15),ca,pa,8+rng.nextInt(75),5+rng.nextInt(35)));
        }
        String[] mn={"Mateo Silva","Luca Moretti","Noah Jensen","Takumi Arai","Daniel Costa","Alex Moreno","Milan Petrovic","Leo Martins"};
        String[] mp={"ST","CM","CB","RW","GK","LB","DM","AM"};
        for(int i=0;i<mn.length;i++){int pa=78+rng.nextInt(20);market.add(new Player(mn[i],mp[i],18+rng.nextInt(11),64+rng.nextInt(16),pa,35+rng.nextInt(120),12+rng.nextInt(45)));}
        String[] teams={"DUMOK FC","JEONJU UNITED","SEOUL CITY","BUSAN ATHLETIC","INCHEON BLUE","DAEGU REDS","SUWON KNIGHTS","DAEJEON CITIZEN"};
        for(String t:teams)league.add(new Team(t));
        for(int i=0;i<11;i++) squad.get(i).starter=true;
    }

    TextView tx(String s,int size,int color){
        TextView v=new TextView(this);v.setText(s);v.setTextSize(size);v.setTextColor(color);v.setPadding(dp(14),dp(10),dp(14),dp(10));return v;
    }
    int dp(int x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
    LinearLayout box(){
        LinearLayout x=new LinearLayout(this);x.setOrientation(LinearLayout.VERTICAL);x.setPadding(dp(10),dp(8),dp(10),dp(8));x.setBackgroundColor(card);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(dp(10),dp(6),dp(10),dp(6));x.setLayoutParams(lp);return x;
    }
    Button button(String s){
        Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(14);return b;
    }
    void frame(String title){
        page=new LinearLayout(this);page.setOrientation(LinearLayout.VERTICAL);page.setBackgroundColor(bg);
        TextView head=tx("⚽ "+club+"     "+date(),18,Color.WHITE);head.setTypeface(null,Typeface.BOLD);head.setBackgroundColor(Color.rgb(11,24,38));page.addView(head);
        TextView sub=tx(title+"     예산 "+budget+"억",14,green);page.addView(sub);
        ScrollView sv=new ScrollView(this);content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);sv.addView(content);
        page.addView(sv,new LinearLayout.LayoutParams(-1,0,1));
        bottom=new LinearLayout(this);bottom.setOrientation(LinearLayout.HORIZONTAL);bottom.setBackgroundColor(Color.rgb(11,24,38));
        nav("홈","HOME");nav("선수단","SQUAD");nav("전술","TACTIC");nav("이적","MARKET");nav("리그","LEAGUE");
        page.addView(bottom);setContentView(page);
    }
    void nav(String label,String id){
        Button b=button(label);b.setOnClickListener(v->go(id));bottom.addView(b,new LinearLayout.LayoutParams(0,dp(54),1));
    }
    String date(){return year+"."+month+"."+day;}
    void go(String id){
        if(id.equals("HOME"))home(); else if(id.equals("SQUAD"))squadPage(); else if(id.equals("TACTIC"))tactic();
        else if(id.equals("MARKET"))market(); else leaguePage();
    }

    void home(){
        frame("감독 대시보드");
        LinearLayout next=box();next.addView(tx("다음 경기",15,muted));
        String opp=league.get((round+1)%league.size()).name;if(opp.equals(club))opp="SEOUL CITY";
        next.addView(tx(club+"  vs  "+opp,23,Color.WHITE));next.addView(tx("리그 "+(round+1)+"R · 3일 후",14,green));
        Button play=button("▶ 경기 진행");play.setOnClickListener(v->playMatch());next.addView(play);content.addView(next);

        LinearLayout stats=box();
        int avg=0,fit=0;for(Player p:squad){avg+=p.ca;fit+=p.fitness;}avg/=squad.size();fit/=squad.size();
        stats.addView(tx("구단 현황",16,Color.WHITE));
        stats.addView(tx("선수 "+squad.size()+"명   평균 CA "+avg+"   평균 컨디션 "+fit+"%",16,muted));
        stats.addView(tx("전술 "+formation+" · "+mentality,16,muted));content.addView(stats);

        LinearLayout actions=box();actions.addView(tx("시간 진행",16,Color.WHITE));
        Button d=button("하루 진행");d.setOnClickListener(v->{advance(1);home();});actions.addView(d);
        Button w=button("일주일 진행");w.setOnClickListener(v->{advance(7);home();});actions.addView(w);content.addView(actions);

        LinearLayout news=box();news.addView(tx("구단 소식",16,Color.WHITE));
        news.addView(tx("• 스카우트: 이적시장 후보 "+market.size()+"명 분석 완료\n• 감독 전술 훈련 진행 중\n• 시즌 목표: 상위 4위",15,muted));content.addView(news);
    }

    void squadPage(){
        frame("선수단");
        for(Player p:squad){
            LinearLayout x=box();
            TextView a=tx((p.starter?"★ ":"")+p.name+"   "+p.pos+"   "+p.age+"세",17,Color.WHITE);x.addView(a);
            x.addView(tx("CA "+p.ca+" / PA "+p.pa+"   컨디션 "+p.fitness+"%   "+p.value+"억   주급 "+p.wage,14,p.ca>=72?green:muted));
            x.addView(tx("출장 "+p.apps+"   골 "+p.goals,13,muted));
            Button st=button(p.starter?"선발 제외":"선발 등록");st.setOnClickListener(v->{toggleStarter(p);squadPage();});x.addView(st);
            content.addView(x);
        }
    }
    void toggleStarter(Player p){
        if(p.starter){p.starter=false;return;}
        int n=0;for(Player q:squad)if(q.starter)n++;
        if(n>=11){Toast.makeText(this,"선발은 11명까지입니다",Toast.LENGTH_SHORT).show();return;}p.starter=true;
    }

    void tactic(){
        frame("전술");
        LinearLayout f=box();f.addView(tx("포메이션",16,Color.WHITE));
        for(String s:new String[]{"4-2-3-1","4-3-3","4-4-2","3-4-2-1"}){Button b=button((formation.equals(s)?"✓ ":"")+s);b.setOnClickListener(v->{formation=s;tactic();});f.addView(b);}content.addView(f);
        LinearLayout m=box();m.addView(tx("멘탈리티",16,Color.WHITE));
        for(String s:new String[]{"수비","균형","공격"}){Button b=button((mentality.equals(s)?"✓ ":"")+s);b.setOnClickListener(v->{mentality=s;tactic();});m.addView(b);}content.addView(m);
    }

    void market(){
        frame("이적시장");
        for(Player p:new ArrayList<>(market)){
            LinearLayout x=box();x.addView(tx(p.name+"   "+p.pos+"   "+p.age+"세",17,Color.WHITE));
            x.addView(tx("CA "+p.ca+" / PA "+p.pa+"   가치 "+p.value+"억   주급 "+p.wage,14,green));
            Button buy=button("영입 제안 "+p.value+"억");buy.setOnClickListener(v->buy(p));x.addView(buy);content.addView(x);
        }
    }
    void buy(Player p){
        if(budget<p.value){Toast.makeText(this,"예산이 부족합니다",Toast.LENGTH_SHORT).show();return;}
        budget-=p.value;squad.add(p);market.remove(p);save();Toast.makeText(this,p.name+" 영입 완료",Toast.LENGTH_SHORT).show();market();
    }

    void leaguePage(){
        frame("리그 순위");
        ArrayList<Team> t=new ArrayList<>(league);
        Collections.sort(t,(a,b)-> b.pts!=a.pts?b.pts-a.pts:(b.gf-b.ga)-(a.gf-a.ga));
        int rank=1;for(Team q:t){LinearLayout x=box();x.addView(tx(rank+"   "+q.name+(q.name.equals(club)?"  ◀":""),17,q.name.equals(club)?green:Color.WHITE));x.addView(tx("경기 "+q.p+"   승 "+q.w+" 무 "+q.d+" 패 "+q.l+"   "+q.gf+":"+q.ga+"   승점 "+q.pts,14,muted));content.addView(x);rank++;}
    }

    void playMatch(){
        int strength=0,n=0;for(Player p:squad)if(p.starter){strength+=p.ca*p.fitness/100;n++;}
        if(n<11){Toast.makeText(this,"선발 11명을 먼저 구성하세요",Toast.LENGTH_SHORT).show();return;}
        strength/=11;if(mentality.equals("공격"))strength+=2;if(mentality.equals("수비"))strength-=1;
        Team me=league.get(0);Team opp=league.get((round%(league.size()-1))+1);
        int hg=Math.max(0,rng.nextInt(4)+(strength-68)/12), ag=rng.nextInt(4);
        me.p++;opp.p++;me.gf+=hg;me.ga+=ag;opp.gf+=ag;opp.ga+=hg;
        if(hg>ag){me.w++;opp.l++;me.pts+=3;}else if(hg<ag){me.l++;opp.w++;opp.pts+=3;}else{me.d++;opp.d++;me.pts++;opp.pts++;}
        for(Player p:squad)if(p.starter){p.apps++;p.fitness=Math.max(55,p.fitness-(5+rng.nextInt(9)));if(p.pos.equals("ST")||p.pos.equals("RW")||p.pos.equals("LW"))if(rng.nextInt(100)<hg*18)p.goals++;}
        round++;advance(3);save();
        new AlertDialog.Builder(this).setTitle("경기 종료").setMessage(club+"  "+hg+" : "+ag+"  "+opp.name+"\n\n승점 "+me.pts+"점").setPositiveButton("확인",(d,w)->home()).show();
    }

    void advance(int days){
        for(int z=0;z<days;z++){day++;if(day>30){day=1;month++;if(month>12){month=1;year++;}}
            for(Player p:squad){p.fitness=Math.min(100,p.fitness+3);if(rng.nextInt(100)<2 && p.ca<p.pa)p.ca++;}
        }save();
    }
    void save(){
        save.edit().putBoolean("hasSave",true).putInt("day",day).putInt("month",month).putInt("year",year).putInt("budget",budget).putInt("round",round).putString("formation",formation).putString("mentality",mentality).apply();
    }
    void load(){day=save.getInt("day",1);month=save.getInt("month",7);year=save.getInt("year",2026);budget=save.getInt("budget",420);round=save.getInt("round",0);formation=save.getString("formation","4-2-3-1");mentality=save.getString("mentality","균형");}
}