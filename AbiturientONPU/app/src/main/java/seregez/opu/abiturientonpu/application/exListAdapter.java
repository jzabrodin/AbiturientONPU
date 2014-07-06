package seregez.opu.abiturientonpu.application;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import seregez.opu.abiturientonpu.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 23.05.14.
 */

public class exListAdapter extends SimpleExpandableListAdapter {
    Context context;
    public exListAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        this.context = context;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
        int n = 0;
        for (int i = 0; i < groupPosition; i++) {
            n += ShowResult.childData.get(i).size();
        }
        n += childPosition;
        setTextView(childView, n);
        return childView;
    }

    protected void setTextView(View view, int n) {
        int a = 0;
        int b = 0;
        if (!ShowResult.placeDX.isEmpty()) {
            //zabrodin 01/07/2014
            //a = Integer.parseInt(ShowResult.placeDX.get(n)) - Integer.parseInt(ShowResult.place.get(n));
            //b = Integer.parseInt(ShowResult.originplaceDX.get(n)) - Integer.parseInt(ShowResult.originplace.get(n));
        }
        //a = 20 - Integer.parseInt(ShowResult.place.get(n));
        //b = 20 - Integer.parseInt(ShowResult.originplace.get(n));
        if (a >= 0)
            ((TextView) view.findViewById(R.id.dxplace)).setTextColor(Color.GREEN);
        else
            ((TextView) view.findViewById(R.id.dxplace)).setTextColor(Color.RED);
        if (b >= 0)
            ((TextView) view.findViewById(R.id.dxoriginplace)).setTextColor(Color.GREEN);
        else
        ((TextView) view.findViewById(R.id.dxoriginplace)).setTextColor(Color.RED);
        ((TextView) view.findViewById(R.id.speciality)) .setText(ShowResult.speciality .get(n));
        ((TextView) view.findViewById(R.id.license))    .setText(ShowResult.licence    .get(n) + " ");
        ((TextView) view.findViewById(R.id.budget))     .setText(ShowResult.budget     .get(n) + " ");
        ((TextView) view.findViewById(R.id.place))      .setText(ShowResult.place      .get(n) + " ");
        ((TextView) view.findViewById(R.id.originplace)).setText(ShowResult.originplace.get(n) + " ");
        //dx
        ((TextView) view.findViewById(R.id.dxplace))    .setText(a + " ");
        ((TextView) view.findViewById(R.id.dxoriginplace)).setText(b + " ");

    }
}
