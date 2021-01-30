package com.anypli.megamall.activities;

import android.content.Intent;
import androidx.annotation.Nullable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anypli.megamall.R;
import com.anypli.megamall.contracts.BoutiqueContract;
import com.anypli.megamall.contracts.BoutiqueContract.BoutiqueViewItf;
import com.anypli.megamall.models.ArticleListElementListener;
import com.anypli.megamall.presenters.BoutiquePresenterImpl;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BoutiqueActivity extends AppCompatActivity implements BoutiqueViewItf {

    private LinearLayout mArticleList ;
    private BoutiqueContract.BoutiquePresenterItf mPresenter ;
    private String mID ;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);
        Toolbar toolbar = findViewById(R.id.boutiquepagetoolbar);
        setSupportActionBar(toolbar);
        mArticleList= findViewById(R.id.boutiquearticlelist);
        mPresenter= new BoutiquePresenterImpl(this);
        mID=this.getIntent().getStringExtra("boutiqueid");
        mPresenter.GetBoutiqueData(mID);
        mPresenter.GetBoutiqueArticles(mID);
        mPresenter.ShowOrHideConfigButtons(mID);


    }
    @Override
    public void showBoutiqueDetails(Bundle boutique) {

        if(boutique!=null) {
            mBundle=boutique ;
            if (boutique.get("bgimage") != null && !boutique.get("bgimage").equals("")) {
                String image= (String) boutique.get("bgimage");
                Picasso.get()
                        .load(image)
                        .into((ImageView) findViewById(R.id.boutiquebackgroundimage));
            }
            if (boutique.getString("iconurl") != null && !boutique.getString("iconurl").equals("") ) {
                Picasso.get()
                        .load(boutique.getString("iconurl"))
                        .into((ImageView) findViewById(R.id.boutiqueiconedit));
            }
            ((TextView) findViewById(R.id.boutiquepagenametextinputedit)).setText(boutique.getString("title"));
            //boutique details
            ((TextView) findViewById(R.id.boutiquediscriptiontextview)).setText(boutique.getString("description"));
            ((TextView) findViewById(R.id.boutiqueadressetextview)).setText(boutique.getString("adress"));

        }
    }

    @Override
    public void showBoutiqueArticles(Bundle[] articles) {
        LayoutInflater inflater =this.getLayoutInflater();
        View view ;
        ViewGroup group ;
        if(articles!=null) {
            for (int i = 0; i < articles.length; ++i) {
                view = inflater.inflate(R.layout.boutique_list_element_layout, mArticleList, false);
                group = (ViewGroup) view;

                ImageView imageView= group.findViewById(R.id.articlepreviewimageboutiquelistelement);
                if (articles[i].get("imageurl") != null ) {
                    String image= (String) articles[i].get("imageurl");
                    Picasso.get().load(image).into(imageView);
                }
                TextView title=group.findViewById( R.id.articlenameboutiquelistelement) ;
                title.setText(articles[i].getString("name"));
                TextView catview =group.findViewById(R.id.articlecategoryboutiquelistelement);
                catview.setText(articles[i].getString("category",""));

                view.setOnClickListener(new ArticleListElementListener(mID, articles[i].getString("articleid")));
                mArticleList.addView(view);
            }
        }

    }

    @Override
    public void showBoutiqueConfigButton(boolean show) {
        ImageView editbutton = findViewById(R.id.boutiqueeditbutton);
        ImageView addArticleButton = findViewById(R.id.addnewarticlebutton);
        if(show){

            editbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),AddEditBoutiqueActivity.class);
                    intent.putExtra("boutiqueid",mID);
                    startActivityForResult(intent,0);
                }
            });
            addArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),AddEditArticleActivity.class);
                    intent.putExtra("boutiqueid",mID);
                    intent.putExtra("iconurl",mBundle.getString("iconurl"));
                    intent.putExtra("title",mBundle.getString("title"));
                    intent.putExtra("adress",mBundle.getString("adress"));
                    startActivityForResult(intent,0);
                }
            });
        }else{
            editbutton.setVisibility(View.GONE);
            addArticleButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            mPresenter.GetBoutiqueData(mID);
        }else if(resultCode==1){
            setResult(1);
            finish();
        }
    }

    @Override
    public void dealWithIt(Bundle input) {
        this.showBoutiqueDetails(input);
    }

    @Override
    public void dealWithIt(Object input) {
        Bundle[] articles = (Bundle[]) input;
        showBoutiqueArticles(articles);
    }
}
