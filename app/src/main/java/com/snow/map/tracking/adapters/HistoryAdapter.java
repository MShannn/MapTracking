package com.snow.map.tracking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snow.map.tracking.R;
import com.snow.map.tracking.Validator.Validations;
import com.snow.map.tracking.models.NavDrawerItem;
import com.snow.map.tracking.models.UserRecord;

import java.util.List;

/**
 * Created by Muhammad Shan on 02/03/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.NavViewHolder> {

    private List<UserRecord> items;
    private Context context;
    private LayoutInflater inflater;


    public HistoryAdapter(Context context, List<UserRecord> items) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
    }



    @Override
    public NavViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.list_history, viewGroup, false);
        return new NavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NavViewHolder holder, final int position) {


        holder.txt_date.setText(items.get(position).getDate());
        holder.txt_miles.setText(""+Validations.roundTwoDecimal(items.get(position).getMiles()));
        holder.txt_time.setText(items.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class NavViewHolder extends RecyclerView.ViewHolder {
        public TextView  txt_date;
        public TextView txt_time;
        public TextView txt_miles;

        public NavViewHolder(View view) {
            super(view);

            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_miles = (TextView) view.findViewById(R.id.txt_miles);
            txt_time = (TextView) view.findViewById(R.id.txt_time);
        }
    }

}