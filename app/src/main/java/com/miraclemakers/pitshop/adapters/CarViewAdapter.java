package com.miraclemakers.pitshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.miraclemakers.pitshop.CarView;
import com.miraclemakers.pitshop.R;
import com.miraclemakers.pitshop.SearchActivity;
import com.miraclemakers.pitshop.model.SearchModel;
import com.miraclemakers.pitshop.model.SpecificationModel;
import com.miraclemakers.pitshop.model.VariantModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by nihalpradeep on 19/10/16.
 */
public class CarViewAdapter extends BaseAdapter implements View.OnClickListener{

    Activity activity;
    ArrayList data;
    VariantModel variantValues;
    SpecificationModel specValues;
    String listType;
    private static LayoutInflater inflater=null;

    public CarViewAdapter(Activity activity, ArrayList arrayList, String listType){
        this.activity = activity;
        data = arrayList;
        this.listType = listType;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(listType.equals(activity.getString(R.string.variant_list))){
            VariantViewHolder holder;
            if(convertView==null){

                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                vi = inflater.inflate(R.layout.text_list, null);

                /****** View Holder Object to contain tabitem.xml file elements ******/

                holder = new VariantViewHolder();
                holder.text_name = (TextView) vi.findViewById(R.id.text_list_title);
                holder.text_price = (TextView) vi.findViewById(R.id.text_list_text);


                /************  Set holder with LayoutInflater ************/
                vi.setTag( holder );
            }
            else
                holder=(VariantViewHolder)vi.getTag();

            if(data.size()<=0)
            {
                holder.text_name.setText("No Data");

            }
            else
            {
                /***** Get each Model object from Arraylist ********/
                variantValues=null;
                variantValues = (VariantModel) data.get( position );

                /************  Set Model values in Holder elements ***********/

                holder.text_name.setText(variantValues.getName());
                holder.text_price.setText("Rs. " + variantValues.getPrice());

                /******** Set Item Click Listner for LayoutInflater for each row *******/

                vi.setOnClickListener(new OnItemClickListener( position ));
            }
        }

        else if(listType.equals(activity.getString(R.string.text_list))){
            VariantViewHolder holder;
            if(convertView==null){

                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                vi = inflater.inflate(R.layout.text_list, null);

                /****** View Holder Object to contain tabitem.xml file elements ******/

                holder = new VariantViewHolder();
                holder.text_name = (TextView) vi.findViewById(R.id.text_list_title);
                holder.text_price = (TextView) vi.findViewById(R.id.text_list_text);


                /************  Set holder with LayoutInflater ************/
                vi.setTag( holder );
            }
            else
                holder=(VariantViewHolder)vi.getTag();

            if(data.size()<=0)
            {
                holder.text_name.setText("No Data");

            }
            else
            {
                /***** Get each Model object from Arraylist ********/
                specValues=null;
                specValues = (SpecificationModel) data.get( position );

                /************  Set Model values in Holder elements ***********/

                holder.text_name.setText(specValues.getName());
                holder.text_price.setText(specValues.getSpec());

            }
        }

        else if(listType.equals(activity.getString(R.string.check_list))){
            CheckSpecViewHolder holder;
            if(convertView==null){

                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                vi = inflater.inflate(R.layout.check_list, null);

                /****** View Holder Object to contain tabitem.xml file elements ******/

                holder = new CheckSpecViewHolder();
                holder.text_title = (TextView) vi.findViewById(R.id.check_list_title);
                holder.image_check = (ImageView) vi.findViewById(R.id.check_list_image);


                /************  Set holder with LayoutInflater ************/
                vi.setTag( holder );
            }
            else
                holder=(CheckSpecViewHolder)vi.getTag();

            if(data.size()<=0)
            {
                holder.text_title.setText("No Data");

            }
            else
            {
                /***** Get each Model object from Arraylist ********/
                specValues=null;
                specValues = (SpecificationModel) data.get( position );

                /************  Set Model values in Holder elements ***********/

                holder.text_title.setText(specValues.getName());
                if(specValues.getSpec().equals("1")){
                    holder.image_check.setImageDrawable(activity.getDrawable(R.drawable.checked));
                }

            }
        }
        return vi;

    }

    @Override
    public void onClick(View v) {

    }

    public static class VariantViewHolder{

        public TextView text_name,text_price;
    }
    public static class CheckSpecViewHolder{
        public TextView text_title;
        public ImageView image_check;
    }

    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            CarView sct = (CarView) activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
