package no.twomonkeys.sneek.app.components.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import no.twomonkeys.sneek.R;

/**
 * 27/09/16 by chridal
 * Copyright 2MONKEYS AS
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    String[] myDataSet;

    public FriendsAdapter(String[] dataSet){
        myDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;

        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(myDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return myDataSet.length;
    }

}
