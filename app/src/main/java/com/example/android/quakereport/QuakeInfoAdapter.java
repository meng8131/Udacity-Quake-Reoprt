package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lyman on 2017/6/9.
 */

public class QuakeInfoAdapter extends ArrayAdapter<QuakeInfoClass>{
    public QuakeInfoAdapter(Context context, List<QuakeInfoClass> list){
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        //initialize convertView
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_adapter, parent, false);
        }

        //get current value from ArrayList
        final QuakeInfoClass currentInfo = getItem(position);

        //format mag as 0.0
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        Double mag = currentInfo.getMag();
        String newMag = decimalFormat.format(mag);

        //find TextView and set values
        TextView magTextView = (TextView) listItemView.findViewById(R.id.list_mag);
        magTextView.setText(newMag);

        //split location to distance and city
        String place = currentInfo.getLocation();
        String[] locationArray;
        String locationPrimary;
        String locationOffset;
        if (place.contains("of")){
            locationArray = place.split("(?<=of)");
            locationPrimary = locationArray[1];
            locationOffset = locationArray[0];
        } else {
            locationPrimary = place;
            locationOffset = "Near the";
        }

        TextView mainLocationTextView = (TextView) listItemView.findViewById(R.id.list_location_primary);
        mainLocationTextView.setText(locationPrimary);

        TextView nearLocationTextView = (TextView) listItemView.findViewById(R.id.list_location_offset);
        nearLocationTextView.setText(locationOffset);

        //format milliseconds to date and time
        Date dateObject = new Date(currentInfo.getTime());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String dateM = dateFormatter.format(dateObject);
        String timeM = timeFormatter.format(dateObject);

        TextView date = (TextView) listItemView.findViewById(R.id.list_date);
        date.setText(dateM);

        TextView time = (TextView) listItemView.findViewById(R.id.list_time);
        time.setText(timeM);

        // 为震级圆圈设置正确的背景颜色。
        // 从 TextView 获取背景，该背景是一个 GradientDrawable。
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.list_mag);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // 根据当前的地震震级获取相应的背景颜色
        int magnitudeColor = getMagnitudeColor(currentInfo.getMag());

        // 设置震级圆圈的颜色
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }
    public int getMagnitudeColor(Double mag){
        int color;
        int intMag = (int) Math.floor(mag);
        switch (intMag){
            case 0:
            case 1:
                color = ContextCompat.getColor(getContext(),R.color.magnitude1);
                break;
            case 2:
                color = ContextCompat.getColor(getContext(),R.color.magnitude2);
                break;
            case 3:
                color = ContextCompat.getColor(getContext(),R.color.magnitude3);
                break;
            case 4:
                color = ContextCompat.getColor(getContext(),R.color.magnitude4);
                break;
            case 5:
                color = ContextCompat.getColor(getContext(),R.color.magnitude5);
                break;
            case 6:
                color = ContextCompat.getColor(getContext(),R.color.magnitude6);
                break;
            case 7:
                color = ContextCompat.getColor(getContext(),R.color.magnitude7);
                break;
            case 8:
                color = ContextCompat.getColor(getContext(),R.color.magnitude8);
                break;
            case 9:
                color = ContextCompat.getColor(getContext(),R.color.magnitude9);
                break;
            default:
                color = ContextCompat.getColor(getContext(),R.color.magnitude10plus);
        }
        return color;
    }
}
