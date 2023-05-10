package com.example.cinemaprovafragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import com.example.cinemaprovafragment.Models.Provincia;

import java.util.ArrayList;
import java.util.Objects;

public class ProvinceAdapter extends ArrayAdapter<Provincia> {
    private LayoutInflater layoutInflater;
    Context context;
    int resource;
    ArrayList<Provincia> provinces, tmp, suggestion;

    public ProvinceAdapter(@NonNull Context context, int resource, ArrayList<Provincia> provinces) {
        super(context, resource, provinces);
        this.context = context;
        this.resource = resource;
        //this.provinces = new ArrayList<>();
        this.provinces = provinces;
        tmp = new ArrayList<>(provinces);
        suggestion = new ArrayList<>();
    }


    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Provincia) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestion.clear();
                for (Provincia p : tmp) {
                    if (Objects.equals(p.getRegion().get_id(), constraint.toString())) {
                        suggestion.add(p);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestion;
                filterResults.count = suggestion.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Provincia> filterList = (ArrayList<Provincia>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Provincia p : filterList) {
                    add(p);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public void setItems(ArrayList<Provincia> emp)
    {
        //provinces.clear();
        //provinces.addAll(emp);
        provinces = emp;
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<Provincia> filterer) {
        provinces = filterer;
        notifyDataSetChanged();
    }
}
