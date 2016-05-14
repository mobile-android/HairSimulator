package com.testapp.hairsimulator;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by bharath.simha on 16/01/16.
 */
public class CustomDialogFragment extends DialogFragment implements
        AdapterView.OnItemClickListener {

    String[] listItems;

    ListView mylist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment, null, false);
        mylist = (ListView) view.findViewById(R.id.list);

        if (getArguments() != null) {
            listItems = getArguments().getStringArray("dialogValues");
        }

        if (Utility.IS_MALE_THEME) {
            mylist.setBackgroundColor(ContextCompat.getColor(HairSimulator.context, R.color.gentsBackgroundColor));
            mylist.setDivider(new ColorDrawable(ContextCompat.getColor(HairSimulator.context, R.color.gentsBrightColor)));
            mylist.setDividerHeight(1);
        } else {
            mylist.setBackgroundColor(ContextCompat.getColor(HairSimulator.context, R.color.ladiesBackgroundColor));
            mylist.setDivider(new ColorDrawable(ContextCompat.getColor(HairSimulator.context, R.color.ladiesBrightColor)));
            mylist.setDividerHeight(1);
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (listItems != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, listItems);

            mylist.setAdapter(adapter);
            mylist.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        dismiss();
        ((HairSimulatorActivity) getActivity()).getHairSimulator().getFragmentPickAndCompare().onAction(position);
    }

}
