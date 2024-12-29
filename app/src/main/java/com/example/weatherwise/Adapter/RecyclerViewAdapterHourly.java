package com.example.weatherwise.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.weatherwise.Api.Hourly_forecast_data;
import com.example.weatherwise.Api.Weekly_forecast_data;
import com.example.weatherwise.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewAdapterHourly extends RecyclerView.Adapter<RecyclerViewAdapterHourly.ViewHolderH> {
    Context context;
    ArrayList<JSONObject> dataList;
    public static class ViewHolderH extends RecyclerView.ViewHolder {
        TextView time, rain_pop_hourly,temp_hourly;
        ImageView weather_condition_hourly;

        public ViewHolderH(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            rain_pop_hourly = itemView.findViewById(R.id.rain_pop_hourly);
            temp_hourly = itemView.findViewById(R.id.temp_hourly);
            weather_condition_hourly = itemView.findViewById(R.id.weather_condition_hourly);
        }
    }

    public RecyclerViewAdapterHourly(Context context, ArrayList<JSONObject> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public ViewHolderH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_for_hourly_details, parent, false);
        return new ViewHolderH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderH holder, int position) {
        if (!dataList.isEmpty()){
            Hourly_forecast_data data=new Hourly_forecast_data(dataList.get(holder.getAdapterPosition()));
            holder.time.setText(data.getTime());
            Glide.with(context).load(data.getConditionUrl()).into(holder.weather_condition_hourly);
            if(data.getRainOrNot()){
                holder.rain_pop_hourly.setVisibility(View.VISIBLE);
                holder.rain_pop_hourly.setText(data.getRainChance());
            }
            holder.temp_hourly.setText(data.getTemp());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
