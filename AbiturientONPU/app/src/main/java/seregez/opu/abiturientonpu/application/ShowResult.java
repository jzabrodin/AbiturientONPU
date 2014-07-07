package seregez.opu.abiturientonpu.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import seregez.opu.abiturientonpu.R;
import seregez.opu.abiturientonpu.service.MenuHelper;


public class ShowResult extends ActionBarActivity {

    //private ListView    resultsList;

    //Забродин 19-06-2014----------
    //вдруг поменять скажут
    static int capacity    =   15;
    //-----------------------------

    static ArrayList<String> placeDX       = new ArrayList<String>(capacity);
    static ArrayList<String> originplaceDX = new ArrayList<String>(capacity);

    static String person = ""; // Так как всегда будет один
    static ArrayList<String> info          = new ArrayList<String>(capacity);
    static ArrayList<String> department    = new ArrayList<String>(capacity);
    static ArrayList<String> qualification = new ArrayList<String>(capacity);
    static ArrayList<String> licence       = new ArrayList<String>(capacity);
    static ArrayList<String> speciality    = new ArrayList<String>(capacity);
    static ArrayList<String> budget        = new ArrayList<String>(capacity);
    static ArrayList<String> place         = new ArrayList<String>(capacity);
    static ArrayList<String> originplace   = new ArrayList<String>(capacity);
    static ArrayList<String> rating        = new ArrayList<String>(capacity);
    static ArrayList<String> privilege     = new ArrayList<String>(capacity);
    static ArrayList<String> docstate      = new ArrayList<String>(capacity);
    static ArrayList<String> title         = new ArrayList<String>(capacity);
    static ArrayList<String> comment       = new ArrayList<String>(capacity);
    private ExpandableListView exList;
    private static final String TAG    =   " === ShowResult === ";

    //коллекция для групп
    ArrayList<Map<String, String>> groupData;
    //коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;
    //общая коллекция для коллекций элементов
    static ArrayList<ArrayList<Map<String, String>>> childData;
    //список атрибутов группы или элементов
    Map<String,String> m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Log.d(TAG,"activity started!");

        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<ArrayList<Map<String, String>>>();
        int t1;
        int t = 1;
        int countOfSpecInDep;
        for (int i = 0; i < ShowResult.department.size(); i++){
            t1 = i;
            countOfSpecInDep = 1;
            while (i < ShowResult.department.size() - 1) {
                if (ShowResult.department.get(i).equals(ShowResult.department.get(i + 1))) {
                    countOfSpecInDep++;
                    t++;
                    i++;
                } else
                    break;
            }
            m = new HashMap<String, String>();
            m.put("groupName", ShowResult.department.get(i) + " (" + countOfSpecInDep + " " + ( countOfSpecInDep == 1 ? "заявление" : countOfSpecInDep < 5 ?
            "заявления" : "заявлений") + ")");
            groupData.add(m);
            pushToCollection(t1, t);
            t = 1;
        }
        Log.d("myLogs", "" + department.size());
        String groupForm[] = new String[] {"groupName"};
        int groupTo[] = new int[] {android.R.id.text1};
        String childFrom[] = new String[] {""};
        int childTo[]      = new int[] {android.R.id.text1};
        exList = (ExpandableListView) findViewById(R.id.ex_list);
        exList.setAdapter(new exListAdapter(this,
                groupData,
                R.layout.ex_list_parent,
                groupForm,
                groupTo,
                childData,
                R.layout.activity_results_row,
                childFrom,
                childTo));
        exList.expandGroup(0);
        exList.expandGroup(1);
        //exList.expandGroup(2);
        exList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int n = 0;
                for (int i = 0; i < groupPosition; i++) {
                    n += childData.get(i).size();
                }
                n += childPosition;
                Intent intent = new Intent(getApplicationContext(), ShowFullResult.class);
                intent.putExtra("key", n);
                startActivity(intent);
                return false;
            }
        });
    }

    private void pushToCollection(int id, int dx) {
        childDataItem = new ArrayList<Map<String, String>>();
        for (int i = 0; i < dx; i++) {
            m = new HashMap<String, String>();
            m.put("", ShowResult.speciality.get(i + id) + ": " + ShowResult.place.get(i + id));
            //tv11.append(originplace.get(n));
            childDataItem.add(m);
        }
        childData.add(childDataItem);
    }



//    static public String[] toArr(ArrayList<String> s) {
//        return Arrays.copyOf(s.toArray(), s.size(), String[].class);
//    }

    static public void updateArrays() {
        speciality.clear();
        department.clear();
        qualification.clear();
        licence.clear();
        speciality.clear();
        budget.clear();
        place.clear();
        originplace.clear();
        rating.clear();
        privilege.clear();
        docstate.clear();
        title.clear();
        comment.clear();
    }

    static public void updateXD() {
        placeDX.clear();
        originplaceDX.clear();
    }

//    static void fillXD() {//todo Когда будет кнопка добавлений, то вызывать этот метод, перед updateXD
//        //zabrodin 01.07.14
//        updateXD();
//        //zabrodin 01.07.14
//        for (String aPlace : place) {
//            placeDX.add(aPlace);
//        }
//        for (String anOriginplace : originplace) {
//            originplaceDX.add(anOriginplace);
//        }
//    }

    static void getData(ArrayList<String> element, ArrayList<String> value) {

        for (int i = 0; i < element.size(); i++) {
            //Log.d(LOG_TAG, element.get(i)+ ": " + value.get(i));
            String s = element.get(i);
            if (s.equals("person")) {
                ShowResult.person = value.get(i);


            } else if (s.equals("department")) {
                ShowResult.department.add(value.get(i));


            } else if (s.equals("qualification")) {
                ShowResult.qualification.add(value.get(i));


            } else if (s.equals("licence")) {
                ShowResult.licence.add(value.get(i));


            } else if (s.equals("speciality")) {
                ShowResult.speciality.add(value.get(i));


            } else if (s.equals("budget")) {
                ShowResult.budget.add(value.get(i));


            } else if (s.equals("place")) {
                ShowResult.place.add(value.get(i));


            } else if (s.equals("originplace")) {
                ShowResult.originplace.add(value.get(i));


            } else if (s.equals("rating")) {
                ShowResult.rating.add(value.get(i));


            } else if (s.equals("privilege")) {
                ShowResult.privilege.add(value.get(i));


            } else if (s.equals("docstate")) {
                ShowResult.docstate.add(value.get(i));


            } else if (s.equals("title")) {
                ShowResult.title.add(value.get(i));


            } else if (s.equals("comment")) {
                ShowResult.comment.add(value.get(i));


            } else if (s.equals("message")) {
                ShowResult.info.add(value.get(i));

            } //06-07-2014 JZabrodin
              else if(s.equals("placeDx")) {
                ShowResult.placeDX.add(value.get(i));
            } else if(s.equals("originplaceDX")){
                ShowResult.originplaceDX.add(value.get(i));
            }
        }//end of for
    }//end of getData

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id                        = item.getItemId();
        MenuHelper menuHelper         =   new MenuHelper();
        menuHelper.setContext(this);
        Intent result = menuHelper.start(id);
        if (result != null){
            startActivity(result);
        }
        return super.onOptionsItemSelected(item);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
