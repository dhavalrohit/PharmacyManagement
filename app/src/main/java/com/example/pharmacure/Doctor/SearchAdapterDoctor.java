package com.example.pharmacure.Doctor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pharmacure.Model.DoctorModel;
import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapterDoctor extends ArrayAdapter<DoctorModel> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<DoctorModel> items, tempItems, suggestions;

    public static String docselected="";


    public SearchAdapterDoctor(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<DoctorModel> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.resource=resource;
        this.textViewResourceId=textViewResourceId;
        this.items=objects;
        this.tempItems= (ArrayList<DoctorModel>) objects.clone();
        this.suggestions=new ArrayList<DoctorModel>();



    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autotext_list, parent, false);
        }
        final DoctorModel model = items.get(position);
        if (model != null) {

            TextView item = (TextView) view.findViewById(R.id.item_name);

            if (item != null)

                item.setText(model.getDoctorName() + " (" + model.getSpecialisation() + ")");

            //Log.d("selected doc from list",item.getText().toString());

        }




        return view;
    }
    public Filter getFilter() {

        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DoctorModel) resultValue).getDoctorName() + " (" + ((DoctorModel) resultValue).getSpecialisation() + ")";
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DoctorModel model : tempItems) {
                    if (model.getDoctorName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(model);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<DoctorModel> filterList = (ArrayList<DoctorModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (DoctorModel model : filterList) {
                    add(model);
                    notifyDataSetChanged();
                }
            }
        }
    };



}
