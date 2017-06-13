package pj.uacnix.pointrunner;

/**
 * Created by Rafał on 2017-06-13.
 */

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class AssignmentsAdapter extends BaseAdapter {
    private final ArrayList mData;
    private static final String TAG = "LISTADAPTER";

    public AssignmentsAdapter(ArrayList<HashMap<String, String>> map) {
        mData = map;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return (Map<String,String>)mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_asgn, parent, false);
        } else {
            result = convertView;
        }

        Map<String, String> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        Log.d(TAG, "getting LISTVIEEW");
        ((TextView) result.findViewById(R.id.item_asgnTitle)).setText(item.get("name"));
        ((TextView) result.findViewById(R.id.item_asgnDesc)).setText(item.get("desc"));

        return result;
    }
}