package com.miraclemakers.pitshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miraclemakers.pitshop.NewCarActivity;
import com.miraclemakers.pitshop.R;
import com.miraclemakers.pitshop.SearchActivity;
import com.miraclemakers.pitshop.model.NewCarListModel;
import com.miraclemakers.pitshop.model.SearchModel;

import java.util.ArrayList;

/**
 * Created by nihalpradeep on 20/10/16.
 */
public class NewCarAdapter extends BaseAdapter implements View.OnClickListener{

    Activity activity;
    ArrayList data;
    NewCarListModel tempValues;
    private static LayoutInflater inflater=null;
    public NewCarAdapter(Activity activity,ArrayList data){
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

    public class ViewHolder{
        TextView text1,text2,text3;
        ImageView image1,image2,image3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.new_car_lists, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.new_car_list_text1);
            holder.text2 = (TextView) vi.findViewById(R.id.new_car_list_text2);
            holder.text3 = (TextView) vi.findViewById(R.id.new_car_list_text3);
            holder.image1 = (ImageView) vi.findViewById(R.id.new_car_list_image1);
            holder.image2 = (ImageView) vi.findViewById(R.id.new_car_list_image2);
            holder.image3 = (ImageView) vi.findViewById(R.id.new_car_list_image3);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text1.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (NewCarListModel) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.text1.setText(tempValues.getImage1());
            holder.text2.setText(tempValues.getImage2());
            holder.text3.setText(tempValues.getImage3());

            String s1,s2,s3;
            s1 = tempValues.getImage1().replaceAll(" ","_").toLowerCase();
            s2 = tempValues.getImage2().replaceAll(" ","_").toLowerCase();
            s3 = tempValues.getImage3().replaceAll(" ","_").toLowerCase();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.image1.setImageDrawable(activity.getDrawable(activity.getResources().getIdentifier(s1, "drawable", activity.getPackageName())));

                holder.image2.setImageDrawable(activity.getDrawable(activity.getResources().getIdentifier(s2, "drawable", activity.getPackageName())));
                holder.image3.setImageDrawable(activity.getDrawable(activity.getResources().getIdentifier(s3, "drawable", activity.getPackageName())));
                /******** Set Item Click Listner for LayoutInflater for each row *******/
            }
            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
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


            NewCarActivity sct = (NewCarActivity) activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
