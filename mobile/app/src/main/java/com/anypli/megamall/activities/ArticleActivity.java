package com.anypli.megamall.activities;

import android.content.Intent;
import androidx.annotation.Nullable;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anypli.megamall.R;
import com.anypli.megamall.contracts.ArticleContract.ArticlePresenterItf;
import com.anypli.megamall.contracts.ArticleContract.ArticleViewItf;
import com.anypli.megamall.models.AccountData;
import com.anypli.megamall.models.ArticleGalleryRecyclerViewCustomAdapter;
import com.anypli.megamall.models.ArticleReview;
import com.anypli.megamall.presenters.ArticlePresenterImpl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleActivity extends AppCompatActivity implements ArticleViewItf {

    private ArticlePresenterItf mPresenter ;
    private ArticleGalleryRecyclerViewCustomAdapter mGalleryAdapter ;
    private ArticleGalleryRecyclerViewCustomAdapter.GalleryOnItemClickListener mGalleryListener;
    private RecyclerView mGalleryRecyclerView ;
    private LinearLayout mReviewList ;

    private String mArticleID ;
    private String mBoutiqueID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = findViewById(R.id.articletoolbar);
        setSupportActionBar(toolbar);
        mPresenter = new ArticlePresenterImpl(this);
        Intent intent =this.getIntent();
        mReviewList=findViewById(R.id.articlereviewlist);
        //init Gallery
        mGalleryListener = new ArticleGalleryRecyclerViewCustomAdapter.GalleryOnItemClickListener((ImageView)findViewById(R.id.articleimagepreviewimageview));
        mGalleryAdapter= new ArticleGalleryRecyclerViewCustomAdapter(null,mGalleryListener);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mGalleryRecyclerView = findViewById(R.id.articlegalleryarticlepage);
        mGalleryRecyclerView.setAdapter(mGalleryAdapter);
        mGalleryRecyclerView.setLayoutManager(linearLayoutManager);
        //init review list
        mBoutiqueID=intent.getStringExtra("boutiqueid");
        if((mArticleID=intent.getStringExtra("articleid"))!=null){
            mPresenter.getArticlePageDetails(mArticleID);
            mPresenter.getArticlePageReviews(mArticleID);
        }
    }


    @Override
    public void showArticlePage(final Bundle article) {
        if (article != null) {
            if(mBoutiqueID==null) {
                mBoutiqueID=article.getString("boutiqueid");
            }
            mPresenter.showOrHideConfigButtons(mBoutiqueID);
            if (article.getString("iconurl") != null)
                Picasso.get().load(article.getString("iconurl")).into((ImageView) findViewById(R.id.articleboutiqueiconimageview));

            ((TextView) findViewById(R.id.articlenametitletextview)).setText(article.getString("name"));

            ((TextView) findViewById(R.id.articleboutiquename)).setText("from : " + article.getString("boutiquename"));

            findViewById(R.id.articleboutiquename).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BoutiqueActivity.class);
                    intent.putExtra("boutiqueid", article.getString("boutiqueid"));
                    startActivity(intent);
                }
            });
            String cat = article.getString("category");
            if(cat!=null && !cat.equals(""))
                ((TextView) findViewById(R.id.articleCategoryTextView)).setText("Category :  "+cat);
            ((TextView) findViewById(R.id.articlediscriptionarticlepagetextview)).setText(article.getString("description"));


            if(article.get("images")!=null) {
                ArrayList<String> images = (ArrayList) article.get("images");
                if(!images.get(0).equals("")) {
                    Picasso.get().load(images.get(0)).into((ImageView) findViewById(R.id.articleimagepreviewimageview));
                    mGalleryAdapter.setImageUrls(images.toArray());
                }
            }

        }
    }

    @Override
    public void showArticleReviews(Bundle[] usersReview) {
        if(usersReview!=null){
            float rating =0.0f;
            LayoutInflater inflater =this.getLayoutInflater();
            View view =null;
            ViewGroup group =null;
            TextView usernameview;
            TextView usercommentview;
            TextView usercratingtview;
            mReviewList.removeAllViews();
            for(int i=0; i<usersReview.length;++i){
                rating+=usersReview[i].getFloat("rating",0.0f);
                view= inflater.inflate(R.layout.content_user_review_layout,mReviewList,false);
                group=(ViewGroup)view;

                ImageView imageView= group.findViewById(R.id.articlereviewusericon);
                if(usersReview[i].getString("usericon")!=null && !usersReview[i].get("usericon").equals("") )
                    Picasso.get().load(usersReview[i].getString("usericon")).into(imageView);

                usernameview=group.findViewById(R.id.articlereviewusername);
                usernameview.setText(usersReview[i].getString("username"));

                usercommentview=group.findViewById(R.id.articlereviewusercomment);
                usercommentview.setText(usersReview[i].getString("comment"));

                usercratingtview =group.findViewById(R.id.articlereviewuserrating);
                usercratingtview.setText(String.valueOf(usersReview[i].getFloat("rating")));

                mReviewList.addView(view);
            }
            ((TextView) findViewById(R.id.articlerating)).setText(String.valueOf(rating/usersReview.length));
        }

    }

    @Override
    public void showArticleConfigButton(boolean show) {
        ImageView editbutton = findViewById(R.id.articleeditbutton);
        View ratingview = findViewById(R.id.articleRatingView);

        if(show){
            editbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),AddEditArticleActivity.class);
                    intent.putExtra("boutiqueid",mBoutiqueID);
                    intent.putExtra("articleid",mArticleID);
                    startActivityForResult(intent,0);
                }
            });
            ratingview.setVisibility(View.GONE);


        }else{
            editbutton.setVisibility(View.GONE);
            findViewById(R.id.articlesubmitcommentbutton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountData accountData = mPresenter.getMyAccountData();
                    String comment = ((EditText)findViewById(R.id.articlecommenttextinput)).getText().toString();
                    float rating = ((RatingBar)findViewById(R.id.articleratingbar)).getRating();
                    ArticleReview review =new ArticleReview(mArticleID,accountData.getUsername(),
                            accountData.getIconUrl(),comment,rating);
                    mPresenter.submitComment(mArticleID,review);
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            mPresenter.getArticlePageDetails(mArticleID);
        }else if(resultCode==1){
            finish();
        }
    }

    @Override
    public void dealWithIt(Bundle input) {
        this.showArticlePage(input);
    }

    @Override
    public void dealWithIt(Object input) {
        if(input instanceof String){
            if(input.equals("success")) {
                mPresenter.getArticlePageReviews(mArticleID);
                Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"error placing review",Toast.LENGTH_SHORT).show();
            }
        }else{
            this.showArticleReviews((Bundle[]) input);
        }

    }
}
