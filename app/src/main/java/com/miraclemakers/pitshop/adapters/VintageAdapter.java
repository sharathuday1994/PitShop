package com.miraclemakers.pitshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miraclemakers.pitshop.R;
import com.miraclemakers.pitshop.SearchActivity;
import com.miraclemakers.pitshop.model.CompareModel;
import com.miraclemakers.pitshop.model.SearchModel;
import com.miraclemakers.pitshop.model.VintageModel;
import com.miraclemakers.pitshop.serverfiles.VintageCarsActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by nihalpradeep on 20/10/16.
 */
public class VintageAdapter extends BaseAdapter implements View.OnClickListener {
    Activity activity;
    ArrayList data;
    VintageModel tempValues;
    private static LayoutInflater inflater=null;

    public VintageAdapter(Activity activity, ArrayList data){
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
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
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.vintage_list, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.tvBrand = (TextView) vi.findViewById(R.id.vintage_brand);
            holder.tvDriven = (TextView) vi.findViewById(R.id.vintage_driven);
            holder.tvModel = (TextView) vi.findViewById(R.id.vintage_model);
            holder.tvPrice = (TextView) vi.findViewById(R.id.vintage_price);
            holder.tvYear = (TextView) vi.findViewById(R.id.vintage_year);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.tvBrand.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (VintageModel) data.get( position );

            /************  Set Model values in Holder elements ***********/


            holder.tvBrand.setText( tempValues.getBrand() );
            holder.tvModel.setText(tempValues.getModel());
            holder.tvDriven.setText(tempValues.getDriven());
            holder.tvPrice.setText("Rs. " + tempValues.getPrice() + " onwards");
            holder.tvYear.setText(tempValues.getYear());

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    public class ViewHolder{
        TextView tvName,tvBrand,tvModel,tvDriven,tvYear,tvPrice;
    }
    @Override
    public void onClick(View v) {

    }
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            VintageCarsActivity sct = (VintageCarsActivity) activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
