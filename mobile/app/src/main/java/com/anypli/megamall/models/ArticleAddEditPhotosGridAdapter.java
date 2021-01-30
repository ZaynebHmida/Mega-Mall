package com.anypli.megamall.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.anypli.megamall.R;
import com.squareup.picasso.Picasso;

public class ArticleAddEditPhotosGridAdapter extends BaseAdapter {
    private String[] mImagesUrls ;



    public ArticleAddEditPhotosGridAdapter(String[] dataset){
        mImagesUrls=dataset ;
    }
    public void ArticleAddEditPhotosGridAdapter(Object[] dataset) {
        this.setImagesUrls(dataset);
    }


    public void setImagesUrls(String[] dataset) {
        this.mImagesUrls = dataset;
    }

    public void setImagesUrls(Object[] dataset) {

        if(dataset!=null){
            mImagesUrls = new String[dataset.length];
            for (int i=0; i<dataset.length;i++){
                mImagesUrls[i]=dataset[i].toString();
            }
        }
    }

    @Override
    public int getCount() {
        if(mImagesUrls!=null)
            return mImagesUrls.length;
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mImagesUrls!=null)
            return mImagesUrls[position];
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_photos_grid_element_layout,parent,false);
        Picasso.get().load(mImagesUrls[position]).into((ImageView) convertView.findViewById(R.id.photopreview));

        return convertView ;
    }
}
