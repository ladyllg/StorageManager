package com.example.storagemanager.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.storagemanager.StorageModel;

import java.util.ArrayList;

public class StorageAdapter extends ArrayAdapter<StorageModel> {

        private Context context;
        private ArrayList<StorageModel> values;

    public StorageAdapter(Context context, int textViewResourceId,
                          ArrayList<StorageModel> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.size();
        }

        @Override
        public StorageModel getItem(int position){
            return values.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setText(values.get(position).getString());
            return label;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setText(values.get(position).getString());
            return label;
        }

}
