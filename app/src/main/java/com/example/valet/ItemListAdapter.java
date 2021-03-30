package com.example.valet;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {

    private MainValetActivity context;
    private static List< DriveInfo> itemsList;

    public ItemListAdapter(MainValetActivity context, List< DriveInfo> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public ItemListAdapter() {

    }

    public void setItemsList(List< DriveInfo> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

//    private class ViewHolder {
//        TextView catName;
//        ImageView catPic;
//    }
//
//    @Override
//    public View getView(final int i, View view, ViewGroup viewGroup) {
//
//        final ViewHolder holder = new ViewHolder();
//        view = View.inflate(context, R.layout.cat_row, null);
//
//        holder.catName = (TextView) view.findViewById(R.id.catName);
//        holder.catPic = (ImageView) view.findViewById(R.id.image);
//
//
//        holder.catName.setText(itemsList.get(i).getCatName());
////        holder.catPic.setImageDrawable(context.getResources().getDrawable(itemsList.get(i).getPic()));
//
//        return view;
//    }




    private class ViewHolder {
        TextView itemName , accept;

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.row_request_recycle, null);
        holder.accept=view.findViewById(R.id.accept);
        holder.itemName =  view.findViewById(R.id.driverName);
        holder.itemName.setText(itemsList.get(i).getDriverName());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,  MapsActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }


}
