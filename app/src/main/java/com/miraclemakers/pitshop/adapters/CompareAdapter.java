package com.miraclemakers.pitshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miraclemakers.pitshop.R;
import com.miraclemakers.pitshop.model.CompareModel;

import java.util.ArrayList;

/**
 * Created by nihalpradeep on 20/10/16.
 */
public class CompareAdapter extends BaseAdapter {
    Activity activity;
    ArrayList data;
    CompareModel tempValues;
    private static LayoutInflater inflater=null;

    public CompareAdapter(Activity activity, ArrayList data){
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        TextView tvSpec1,tvSpec2,tvParameter;
        ImageView iSpec1,iSpec2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        tempValues=null;
        tempValues = (CompareModel) data.get( position );
        String type = tempValues.getType();
        if(convertView==null){

            holder = new ViewHolder();
            if(type.equals(activity.getString(R.string.text_list))||type.equals(activity.getString(R.string.title_list))) {
                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                vi = inflater.inflate(R.layout.compare_list_text, null);
                holder.tvSpec1 = (TextView) vi.findViewById(R.id.compare_list_spec1);
                holder.tvSpec2 = (TextView) vi.findViewById(R.id.compare_list_spec2);
                holder.tvParameter = (TextView) vi.findViewById(R.id.compare_list_parameter);
            }
            else if(type.equals(activity.getString(R.string.check_list))){
                vi = inflater.inflate(R.layout.compare_list_check, null);
                holder.iSpec1 = (ImageView) vi.findViewById(R.id.compare_list_check1);
                holder.iSpec2 = (ImageView) vi.findViewById(R.id.compare_list_check2);
                holder.tvParameter = (TextView) vi.findViewById(R.id.compare_list_parameter_check);
            }

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.tvParameter.setText("No Data");

        }
        else
        {

            if(type.equals(activity.getString(R.string.text_list))) {
                holder.tvSpec1.setText(tempValues.getSpec1());
                holder.tvSpec2.setText(tempValues.getSpec2());
                holder.tvParameter.setText(tempValues.getParamter());
            }else if(type.equals(activity.getString(R.string.title_list))){
                holder.tvParameter.setText(tempValues.getParamter());
            }else if(type.equals(activity.getString(R.string.check_list))){
                holder.tvParameter.setText(tempValues.getParamter());
                if(tempValues.getSpec1().equals("1")){
                    holder.iSpec1.setImageDrawable(activity.getDrawable(R.drawable.checked));
                }
                if(tempValues.getSpec2().equals("1")){
                    holder.iSpec2.setImageDrawable(activity.getDrawable(R.drawable.checked));
                }
            }
        }
        return vi;
    }
}
