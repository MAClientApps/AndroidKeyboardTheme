package com.cmalergun.photokeyboard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cmalergun.photokeyboard.R;
import com.cmalergun.photokeyboard.database.DBManager;

import java.util.ArrayList;

public class SearchWordAdapter extends RecyclerView.Adapter<SearchWordAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> pDataset;
    private String phSubType;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView WordTextView;
        public Button crossBtn;

        public ViewHolder(View v) {
            super(v);
            WordTextView = v.findViewById(R.id.keywordTxtV);
            crossBtn = v.findViewById(R.id.crossButton);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchWordAdapter(ArrayList<String> myDataset, Context context, String subType) {
        pDataset = myDataset;
        this.context = context;
        this.phSubType = subType;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchWordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_keybaord_words_list, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.WordTextView.setText(pDataset.get(position));
        holder.crossBtn.setOnClickListener((View v) -> {
            DBManager db = new DBManager(context);
            try {
                db.delete(pDataset.get(position), phSubType);
            } catch (Exception e) {
                Log.d("Exception Error", String.valueOf(e));
            }

            pDataset.remove(position);
            this.notifyDataSetChanged();
            Toast.makeText(context, R.string.removed_word, Toast.LENGTH_LONG).show();
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pDataset.size();
    }
}