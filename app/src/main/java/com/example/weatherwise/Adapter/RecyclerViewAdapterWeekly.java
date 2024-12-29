package com.example.weatherwise.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.weatherwise.Api.Weekly_forecast_data;
import com.example.weatherwise.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewAdapterWeekly extends RecyclerView.Adapter<RecyclerViewAdapterWeekly.ViewHolderD> {
    Context context;
    ArrayList<JSONObject> dataList;

    public RecyclerViewAdapterWeekly(Context context, ArrayList<JSONObject> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public static class ViewHolderD extends RecyclerView.ViewHolder {
        TextView day_name, rain_pop_weekly, temp_min, temp_high;
        LottieAnimationView weather_condition_weekly;

        public ViewHolderD(@NonNull View itemView) {
            super(itemView);
            day_name = itemView.findViewById(R.id.day_name);
            rain_pop_weekly = itemView.findViewById(R.id.rain_pop_weekly);
            temp_min = itemView.findViewById(R.id.temp_min_day);
            temp_high = itemView.findViewById(R.id.temp_high_day);
            weather_condition_weekly = itemView.findViewById(R.id.weather_condition_weekly);
        }
    }

    @NonNull
    @Override
    public ViewHolderD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_for_weekly_details, parent, false);
        return new ViewHolderD(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderD holder, int position) {
        if (!dataList.isEmpty()){
            Weekly_forecast_data data = new Weekly_forecast_data(dataList.get(holder.getAdapterPosition()));
            holder.day_name.setText(data.getDayName());
            setCondition(data.getConditionCode(), holder);
            if (data.getRainOrNot()) {
                holder.rain_pop_weekly.setVisibility(View.VISIBLE);
                holder.rain_pop_weekly.setText(data.getRainChance());
            }
            holder.temp_min.setText(data.getMinTemp());
            holder.temp_high.setText(data.getMaxTemp());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private void setCondition(int code, ViewHolderD holderD) {
        Log.d("code", String.valueOf(code));
        switch (code) {
            case 1000:
                holderD.weather_condition_weekly.setAnimation(R.raw.anim_1d);
                break;

            case 1003:
            case 1006:
            case 1009:
                holderD.weather_condition_weekly.setAnimation(R.raw.anim_2d);
                break;

            case 1030:
            case 1135:
            case 1147:
                holderD.weather_condition_weekly.setAnimation(R.raw.anim_50d);
                break;

            case 1063:
            case 1150:
            case 1153:
            case 1180:
            case 1183:
            case 1186:
            case 1189:
            case 1192:
            case 1195:
                holderD.weather_condition_weekly.setAnimation(R.raw.anim_9d);
                break;

            case 1198:
            case 1201:
            case 1069:
            case 1204:
            case 1207:
            case 1066:
            case 1114:
            case 1117:
            case 1210:
            case 1213:
            case 1216:
            case 1219:
            case 1222:
            case 1225:
            case 1237:
            case 1261:
            case 1264:
                holderD.weather_condition_weekly.setAnimation(R.raw.anim_13d);
                break;

            case 1240:
            case 1243:
            case 1246:
                holderD.weather_condition_weekly.setAnimation(R.raw.anim_10d);
                break;

            case 1087:
            case 1273:
            case 1276:
            case 1279:
            case 1282:
                holderD.weather_condition_weekly.setAnimation(R.raw.anim_11d);
                break;
        }
    }
}
