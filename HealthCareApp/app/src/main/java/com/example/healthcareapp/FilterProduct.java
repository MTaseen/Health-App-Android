package com.example.healthcareapp;

import android.widget.Filter;

import com.example.healthcareapp.adapter.AdapterIllnessDataAdmin;

import java.util.ArrayList;

public class FilterProduct extends Filter {

    private AdapterIllnessDataAdmin adapter;
    private ArrayList<ModelIllnessData> filterList;

    public FilterProduct(AdapterIllnessDataAdmin adapter, ArrayList<ModelIllnessData> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //validate data for search query
        if (constraint != null && constraint.length() > 0) {
            //only should search when the search field is not empty

            //change to upper case, to make case insensitive
            constraint = constraint.toString().toUpperCase();
            //store our filtered list
            ArrayList<ModelIllnessData> filteredModels = new ArrayList<>();
            for (int i=0; i<filterList.size(); i++) {
                //check, search by illness name or by categories
                if (filterList.get(i).getIllnessName().toUpperCase().contains(constraint) ||
                        filterList.get(i).getIllnessAgeCategory().toUpperCase().contains(constraint)) {
                    //add filtered data to list
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;

        }

        else {
            //when search field is back to empty, return to original state

            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.illnessDataArrayList = (ArrayList<ModelIllnessData>) results.values;
        //refresh com.example.healthcareapp.adapter
        adapter.notifyDataSetChanged();
    }
}
