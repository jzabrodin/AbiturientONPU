package seregez.opu.abiturientonpu.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import seregez.opu.abiturientonpu.R;

/**
 * Created by evgez
 * on 04 Май  2014
 */
public class customArrayAdapter extends BaseAdapter {

    SimpleExpandableListAdapter s;
    private static final String LOG_TAG = "myLogs";
    Context     context;
    Map<Integer, ArrayList<String>> map;
    int resource;

    public customArrayAdapter(Context context, Map<Integer, ArrayList<String>> map) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context;
        this.map = map;
        this.resource = R.layout.activity_results_row;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int i) {
        return map.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater =   (LayoutInflater)
                                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row                =   inflater.inflate(resource,viewGroup,false);
        if (row == null)
            throw new NullPointerException();
        setTextView(row, i);
        return row;
    }

    protected void setTextView(View row, int a){
        int i = 1;
        ((TextView) row.findViewById(R.id.speciality)) .setText(ShowResult.department.get(a) +
                "\n"+ ShowResult.speciality.get(a));
        ((TextView) row.findViewById(R.id.license))    .setText(map.get(i).get(a) + " ");i++;
        ((TextView) row.findViewById(R.id.budget))     .setText(map.get(i).get(a) + " ");i++;
        ((TextView) row.findViewById(R.id.place))      .setText(map.get(i).get(a) + " ");i++;
        ((TextView) row.findViewById(R.id.originplace)).setText(map.get(i).get(a) + " ");
    }
}