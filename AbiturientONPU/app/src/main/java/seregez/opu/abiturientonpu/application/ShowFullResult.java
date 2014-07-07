package seregez.opu.abiturientonpu.application;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import seregez.opu.abiturientonpu.R;
import seregez.opu.abiturientonpu.service.MenuHelper;

/*
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);


        Map<Integer ,ArrayList<String>> map = new HashMap<>();

//        map.put("department" , department);
        //String[] departmentArr  = department.toArray(new String[department.size()]);
        map.put(1 , licence);
        map.put(2 , budget);
        map.put(3 , place);
        map.put(4 , originplace);

        Map<String ,ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

//        map.put("department" , department);
        String[] departmentArr  = department.toArray(new String[department.size()]);
        map.put("licence"    , licence);
        map.put("budget"     , budget);
        map.put("place"      , place);
        map.put("originplace", originplace);

resultsList = (ListView) findViewById(R.id.results);

        customArrayAdapter adapter = new customArrayAdapter(this, map);

        resultsList.setAdapter(adapter);

        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
        //Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
        Intent intent = new Intent(getApplicationContext(), ShowFullResult.class);
        intent.putExtra("key", position);
        startActivity(intent);
        }
        });

        }
 */

/**
 * Created by Admin on 12.05.14.
 */
public class ShowFullResult extends ActionBarActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //нужно задать контекст и ид
        MenuHelper menuHelper = new MenuHelper();
        menuHelper.setContext(this);
        Intent result = menuHelper.start(id);
        if (result != null){
            startActivity(result);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int n = intent.getIntExtra("key", 0);
        //StringBuilder sb = new StringBuilder();
        SpannableStringBuilder b = new SpannableStringBuilder();
        TextView tv = (TextView) findViewById(R.id.all);
        b.append("Абитуриент: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), 0, "Абитуриент: ".length(), 0);
        b.append(ShowResult.person);
        b.append("\nСпециальность: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), (b.length() - "Специальность: ".length()), b.length(), 0);
        b.append(ShowResult.speciality.get(n));
        b.append("\nСтепень: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - "Степень: ".length(), b.length(), 0);
        b.append(ShowResult.qualification.get(n));
        b.append("\nБюджет: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - " Бюджет:".length(), b.length(), 0);
        b.append(ShowResult.budget.get(n));
        b.append("  Лицензия: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - "  Лицензия: ".length(), b.length(), 0);
        b.append(ShowResult.licence.get(n));
        b.append("\n\nРейтинг: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - " Рейтинг:".length(), b.length(), 0);
        b.append(ShowResult.rating.get(n));
        b.setSpan(new SuperscriptSpan(), b.length() - ShowResult.rating.get(n).length(), b.length(), 0);
        tv.setText(b);

        SpannableString ss;
        int num1 = 0;
        int num2 = 0;
        String dx;
        String dy;
        if (!ShowResult.placeDX.isEmpty()) {
            //*todo здесь почему-то вылетает массивы placeDX и originplaceDX задаются в классе UpdateInformation
            num1 = Integer.parseInt(ShowResult.placeDX.get(n));
                    //- Integer.parseInt(ShowResult.place.get(n));
            num2 = Integer.parseInt(ShowResult.originplaceDX.get(n));
                    //- Integer.parseInt(ShowResult.originplace.get(n));
        }//todo -
        //num1 = 777;
        //num2 = -99;
        dx = num1 > 0 ? "+" + num1 : "" + num1;
        dy = num2 > 0 ? "+" + num2 : "" + num2;
        Spanned sx = Html.fromHtml("X <small><sub>2</sub></small> ");
        Spanned sy = Html.fromHtml("<sup><small>" + dy + " </small></sup>");
//"\n\nРейтинг: " + ShowResult.rating.get(n) +
        ss = new SpannableString("Позиция в рейтинге: " + ShowResult.place.get(n) + " " + dx);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, "Позиция в рейтинге: ".length(), 0);
        ss.setSpan(new SuperscriptSpan(), ss.length()-dx.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new TextAppearanceSpan(this, R.style.SpecialTextAppearance), ss.length() - dx.length(), ss.length(), 0);
        ss.setSpan(new ForegroundColorSpan(num1 >= 0 ? Color.GREEN : Color.RED), ss.length() - dx.length(), ss.length(), 0);
                ((TextView) findViewById(R.id.place)).setText(ss);
        ss = null;
        ss = new SpannableString("Позиция по оригиналам: " + ShowResult.place.get(n) + " " + dy);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, "Позиция по оригиналам: ".length(), 0);
        ss.setSpan(new SuperscriptSpan(), ss.length()-dy.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new TextAppearanceSpan(this, R.style.SpecialTextAppearance), ss.length() - dy.length(), ss.length(), 0);
        ss.setSpan(new ForegroundColorSpan(num1 >= 0 ? Color.GREEN : Color.RED), ss.length() - dy.length(), ss.length(), 0);
        ((TextView) findViewById(R.id.originplace)).setText(ss);
        //b.append("\n\nПозиция в рейтинге: ");
        //b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - "Позиция в рейтинге: ".length(), b.length(), 0);
        //b.append(ShowResult.place.get(n)).append(" ");
        //tv.setText(b);
        //b.clear();
        //b.clearSpans();

        //b.append(dx);
        //b.setSpan(new SuperscriptSpan(), b.length()-dx.length(), b.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //b.setSpan(new TextAppearanceSpan(this, R.style.SpecialTextAppearance), b.length()-dx.length(), b.length(), 0);
        //b.setSpan(new ForegroundColorSpan(num1 >= 0 ? Color.GREEN : Color.RED), b.length()-dx.length(), b.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //((TextView) findViewById(R.id.placeDX)) .setText("123");
        //b.clear();
        //b.clearSpans();

        //b.append("\nПозиция по оригиналам: ");
        //b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - "Позиция по оригиналам: ".length(), b.length(), 0);
        //b.append(ShowResult.originplace.get(n)).append(" ");
        //((TextView) findViewById(R.id.all2)) .setText("\nПозиция по оригиналам: ");
        //b.clear();
        //b.clearSpans();
        //b.append(dy);
        //b.setSpan(new TextAppearanceSpan(this, R.style.SpecialTextAppearance), b.length()-dy.length(), b.length(), 0);
        //b.setSpan(new SuperscriptSpan(), b.length()-dy.length(), b.length(), 0);
        // b.setSpan(new ForegroundColorSpan(num2 >= 0 ? Color.GREEN : Color.RED), b.length()-dy.length(), b.length(), 0);
        //((TextView) findViewById(R.id.originplaceDX)) .setText("22");
        //b.clear();
        //b.clearSpans();
        //TextView all2 = (TextView) findViewById(R.id.all2);
        //html
        //TextView all3 = (TextView) findViewById(R.id.all3);

        TextView all3 = (TextView) findViewById(R.id.all3);
        /*SpannableString sss = new SpannableString("TEST EXAMPLE HAHA");
        sss.setSpan(new TextAppearanceSpan(this, R.style.SpecialTextAppearance), 6,10, 0);
        sss.setSpan(new SuperscriptSpan(), 6,10, 0);
        sss.setSpan(new ForegroundColorSpan(Color.RED), 6, 10, 0);
        //all3.setText(sss);*/

        b.clear();
        b.clearSpans();
        b.append("\nПодано: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - "Подано: ".length(), b.length(), 0);
        b.append(ShowResult.docstate.get(n));
        b.append("\nПривилегии: ");
        b.setSpan(new StyleSpan(Typeface.BOLD), b.length() - "Привилегии: ".length(), b.length(), 0);
        b.append(ShowResult.privilege.get(n));
        b.append("\n");
        if ( ShowResult.title.get(n) != null || !ShowResult.title.get(n).equals("")) {
            b.append(ShowResult.title.get(n));
            b.append("\n");
        }
        b.append(ShowResult.comment.get(n));
        //tv.setText(b);
        all3.setText(b);

        //tv.append(sss);
        //sb.delete(0, sb.length());

        /*TextView tv10 = (TextView) findViewById(R.id.place);
        tv10.append(ShowResult.place.get(n));
        TextView tv11 = (TextView) findViewById(R.id.originplace);
        tv11.append(ShowResult.originplace.get(n));
        //Вывести разницу в рейтинге, если есть.
        int num1 = 0;
        int num2 = 0;
        if (!ShowResult.placeDX.isEmpty()) {
            num1 = Integer.parseInt(ShowResult.placeDX.get(n)) - Integer.parseInt(ShowResult.place.get(n));
            num2 = Integer.parseInt(ShowResult.originplaceDX.get(n)) - Integer.parseInt(ShowResult.originplace.get(n));
        }//todo -
        num1 = 777;
        num2 = -99;
        if (num1 >= 0)
            ((TextView) findViewById(R.id.placeDX)).setTextColor(Color.GREEN);
        else
            ((TextView) findViewById(R.id.placeDX)).setTextColor(Color.RED);
        if (num2 >= 0)
            ((TextView) findViewById(R.id.originplaceDX)).setTextColor(Color.GREEN);
        else
            ((TextView) findViewById(R.id.originplaceDX)).setTextColor(Color.RED);
        TextView tv101 = (TextView) findViewById(R.id.placeDX);
        tv101.setText(num1 > 0 ? "+" + num1 : "" + num1);
        TextView tv102 = (TextView) findViewById(R.id.originplaceDX);
        tv102.setText(num2 > 0 ? "+" + num2 : "" + num2);
        //}//todo +
        tv101.setTextSize(12);
        tv102.setTextSize(12);
*/
    }
}

