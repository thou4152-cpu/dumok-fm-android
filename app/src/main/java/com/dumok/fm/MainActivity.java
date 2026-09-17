package com.dumok.fm;
import android.app.*; import android.os.*; import android.graphics.Color; import android.widget.*; import java.util.Random;
public class MainActivity extends Activity{
 LinearLayout body; int day=1;
 public void onCreate(Bundle b){super.onCreate(b);home();}
 TextView t(String s,int z){TextView v=new TextView(this);v.setText(s);v.setTextColor(Color.WHITE);v.setTextSize(z);v.setPadding(24,18,24,18);return v;}
 Button b(String s){Button x=new Button(this);x.setText(s);x.setAllCaps(false);return x;}
 void base(String h){ScrollView s=new ScrollView(this);body=new LinearLayout(this);body.setOrientation(LinearLayout.VERTICAL);body.setPadding(22,25,22,35);body.setBackgroundColor(Color.rgb(9,17,31));s.addView(body);body.addView(t("⚽ DUMOK FM | "+h,24));body.addView(t("2026년 7월 "+day+"일",15));setContentView(s);}
 void nav(String n){Button x=b(n);x.setOnClickListener(v->section(n));body.addView(x);}
 void home(){base("감독실");body.addView(t("다음 경기\nDUMOK FC vs JEONJU UNITED",22));for(String n:new String[]{"경기 진행","선수단","이적시장","리그","챔피언스리그","전술","날짜 진행"})nav(n);}
 void section(String s){if(s.equals("날짜 진행")){day++;home();return;}base(s);
 if(s.equals("선수단"))body.addView(t("김도윤 ST  CA 74 / PA 86\n이준혁 CM  CA 71 / PA 82\n박시우 CB  CA 69 / PA 84\n최민재 GK  CA 73 / PA 79",19));
 else if(s.equals("이적시장"))body.addView(t("Mateo Silva ST  CA 77 / PA 91  85억\nLuca Moretti CM  CA 73 / PA 86  62억",19));
 else if(s.equals("리그"))body.addView(t("1 DUMOK FC  0점\n2 JEONJU UNITED  0점\n3 SEOUL CITY  0점\n4 BUSAN ATHLETIC  0점",19));
 else if(s.equals("챔피언스리그"))body.addView(t("리그 페이즈 · 9월 시작\n상위 팀 → 토너먼트",19));
 else if(s.equals("전술"))body.addView(t("4-2-3-1\n공격적 · 높은 압박 · 높은 수비라인",20));
 else {Random r=new Random();body.addView(t("경기 종료\nDUMOK FC "+r.nextInt(4)+" : "+r.nextInt(4)+" JEONJU UNITED",24));}
 Button x=b("← 감독실");x.setOnClickListener(v->home());body.addView(x);}
}