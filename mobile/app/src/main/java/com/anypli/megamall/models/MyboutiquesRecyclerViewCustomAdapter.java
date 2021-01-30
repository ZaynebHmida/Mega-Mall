package com.anypli.megamall.models;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anypli.megamall.R;
import com.anypli.megamall.activities.BoutiqueActivity;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyboutiquesRecyclerViewCustomAdapter extends RecyclerView.Adapter {
    protected class  CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitle =null ;
        public TextView mAdresse=null ;
        public CircleImageView mIcon=null ;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewGroup group = (ViewGroup) itemView;
            for(int i =0 ; i < group.getChildCount(); i++){
                if(group.getChildAt(i).getId()==R.id.boutiquenamemyboutique)
                    mTitle= (TextView) group.getChildAt(i);
                else if(group.getChildAt(i).getId()==R.id.boutiqueadressemyboutiques)
                    mAdresse= (TextView) group.getChildAt(i);
                else if(group.getChildAt(i).getId()==R.id.boutiqueiconmyboutiques)
                    mIcon= (CircleImageView) group.getChildAt(i);
            }
        }
    }


    private Bundle[] mCurrentDataSet  ;

    public MyboutiquesRecyclerViewCustomAdapter(Bundle[] dataset) {
        this.mCurrentDataSet=dataset ;
    }

    public void setDataSet(Bundle[] dataSet){
        mCurrentDataSet=dataSet ;

    }

    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return  new MyboutiquesRecyclerViewCustomAdapter.CustomViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_myboutique_recyclerview_element_layout,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(v.getContext(),BoutiqueActivity.class);
                intent.putExtra("boutiqueid",mCurrentDataSet[i].getString("boutiqueid"));
                v.getContext().startActivity(intent);
            }
        };
        ((MyboutiquesRecyclerViewCustomAdapter.CustomViewHolder)viewHolder).mTitle.setText(mCurrentDataSet[i].getString("title"));
        ((MyboutiquesRecyclerViewCustomAdapter.CustomViewHolder)viewHolder).mTitle.setOnClickListener(listener);
        ((MyboutiquesRecyclerViewCustomAdapter.CustomViewHolder)viewHolder).mAdresse.setText(mCurrentDataSet[i].getString("adress"));
        ((MyboutiquesRecyclerViewCustomAdapter.CustomViewHolder)viewHolder).mAdresse.setOnClickListener(listener);
        if(mCurrentDataSet[i].getString("iconurl")!=null && !mCurrentDataSet[i].getString("iconurl").equals(""))
            Picasso.get().load(mCurrentDataSet[i].getString("iconurl")).into(((MyboutiquesRecyclerViewCustomAdapter.CustomViewHolder)viewHolder).mIcon);
        ((MyboutiquesRecyclerViewCustomAdapter.CustomViewHolder)viewHolder).mIcon.setOnClickListener(listener);


    }

    @Override
    public int getItemCount() {
        if(mCurrentDataSet!=null)
            return mCurrentDataSet.length;
        else
            return 0;
    }
}
