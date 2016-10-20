package com.miraclemakers.pitshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miraclemakers.pitshop.NewCarActivity;
import com.miraclemakers.pitshop.R;
import com.miraclemakers.pitshop.SearchActivity;
import com.miraclemakers.pitshop.model.SearchModel;

import java.util.ArrayList;

/**
 * Created by nihalpradeep on 01/10/16.
 * Adapter is used to set the listview in SearchActivity
 */
public class SearchListAdapter extends BaseAdapter implements View.OnClickListener{
    Activity activity;
    ArrayList data;
    SearchModel tempValues;
    private static LayoutInflater inflater=null;

    public SearchListAdapter(Activity activity, ArrayList data){
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

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder{

        public TextView text_name,text_brand,text_body_type,text_mileage,text_seats,text_year,text_price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.search_list, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text_name = (TextView) vi.findViewById(R.id.search_list_car_name);
            holder.text_brand = (TextView) vi.findViewById(R.id.search_list_car_brand);
            holder.text_seats = (TextView) vi.findViewById(R.id.search_list_seats);
            holder.text_body_type = (TextView) vi.findViewById(R.id.search_list_body_type);
            holder.text_mileage = (TextView) vi.findViewById(R.id.search_list_mileage);
            holder.text_year = (TextView) vi.findViewById(R.id.search_list_year);
            holder.text_price = (TextView) vi.findViewById(R.id.search_list_price);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text_name.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (SearchModel) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.text_name.setText(tempValues.getName());
            holder.text_brand.setText( tempValues.getBrand() );
            holder.text_seats.setText(tempValues.getSeats());
            holder.text_mileage.setText(tempValues.getMileage() + "kmpl");
            holder.text_body_type.setText(tempValues.getBodyType());
            holder.text_price.setText("Rs. " + tempValues.getPrice() + " onwards");
            holder.text_year.setText(tempValues.getYear());

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            SearchActivity sct = (SearchActivity) activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
