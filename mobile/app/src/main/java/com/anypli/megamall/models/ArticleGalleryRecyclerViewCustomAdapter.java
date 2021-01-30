package com.anypli.megamall.models;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anypli.megamall.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;


public class ArticleGalleryRecyclerViewCustomAdapter extends RecyclerView.Adapter {


    public static class GalleryOnItemClickListener implements View.OnClickListener {
        private ImageView mImageViewTarget;

        public GalleryOnItemClickListener(@NonNull ImageView imageViewTarget) {
            this.mImageViewTarget = imageViewTarget;
        }

        @Override
        public void onClick(View v) {

            mImageViewTarget.setImageDrawable(((ImageView)v).getDrawable());
        }
    }



    protected class  CustomViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView ;


        public CustomViewHolder(@NonNull View view) {
            super(view);
            imageView=((ViewGroup)view).findViewById(R.id.galleryimageview) ;

        }
    }




    private String[] mImageUrls ;
    private GalleryOnItemClickListener mListener;

    public ArticleGalleryRecyclerViewCustomAdapter(String[] imageUrls,@NonNull GalleryOnItemClickListener listener) {
        mImageUrls = imageUrls;
        mListener=listener ;
    }

    public void setImageUrls(String[] imageUrls) {
        mImageUrls = imageUrls;
    }

    public void setImageUrls(Object[] imageUrls) {

        if(imageUrls!=null){
            mImageUrls = new String[imageUrls.length];
            for (int i=0; i<imageUrls.length;i++){
                mImageUrls[i]=imageUrls[i].toString();
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_gallery_element_layout,parent,false);
        ImageView imageView=((ViewGroup)view).findViewById(R.id.galleryimageview);
        imageView.setOnClickListener(mListener);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder ;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Picasso.get().load(mImageUrls[i]).into(((CustomViewHolder)holder).imageView);
    }

    @Override
    public int getItemCount() {
        if(mImageUrls!=null)
            return mImageUrls.length ;
        else
            return 0 ;
    }
}
