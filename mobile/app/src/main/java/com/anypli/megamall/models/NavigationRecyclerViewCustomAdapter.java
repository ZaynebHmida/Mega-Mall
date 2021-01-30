package com.anypli.megamall.models;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anypli.megamall.R;
import com.anypli.megamall.activities.ArticleActivity;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

public class NavigationRecyclerViewCustomAdapter extends RecyclerView.Adapter {

    protected class  CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitle  ;
        public TextView mDescription ;
        public ImageView mPreview  ;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewGroup group = (ViewGroup) itemView;
            mTitle=  group.findViewById(R.id.titletext);
            mDescription=  group.findViewById(R.id.descriptiontext);
            mPreview=  group.findViewById(R.id.navigationimagepreview);

        }
    }


    private Bundle[] mCurrentDataSet  ;
    private Intent intent ;

    public NavigationRecyclerViewCustomAdapter(Bundle[] dataset) {
        this.mCurrentDataSet=dataset ;

    }

    public void setDataSet(Bundle[] dataSet){
        mCurrentDataSet=dataSet ;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return  new CustomViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.navigation_list_element_layout,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        ((CustomViewHolder)viewHolder).mTitle.setText(mCurrentDataSet[i].getString("name"));
        ((CustomViewHolder)viewHolder).mDescription.setText(mCurrentDataSet[i].getString("description"));
        String image=  mCurrentDataSet[i].getString("imageurl");
        if(image!=null && !image.equals("")) {
            Picasso.get().load(image).into(((CustomViewHolder)viewHolder).mPreview);
        }
        //setListener
        ((CustomViewHolder)viewHolder).mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(),ArticleActivity.class);
                intent.putExtra("articleid",mCurrentDataSet[i].getString("articleid"));
                if(intent!=null)
                    v.getContext().startActivity(intent);

            }
        });

        ((CustomViewHolder)viewHolder).mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(v.getContext(),ArticleActivity.class);
                intent.putExtra("articleid",mCurrentDataSet[i].getString("articleid"));
                v.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(mCurrentDataSet!=null)
            return mCurrentDataSet.length;
        else
            return 0;
    }
}
