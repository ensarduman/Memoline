package com.ensarduman.memoline.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ensarduman.memoline.R;

import java.util.List;

/**
 * Created by duman on 25/02/2018.
 */

public class AutoCompleteAdapter<T> extends ArrayAdapter {

    Context context;

    public AutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView)inflater.inflate(R.layout.hashtag_autocomplete_textview, null);
        convertView = textView;

        return super.getView(position, convertView, parent);
    }
}
