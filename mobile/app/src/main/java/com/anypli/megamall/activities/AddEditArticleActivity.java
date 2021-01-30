package com.anypli.megamall.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anypli.megamall.R;
import com.anypli.megamall.contracts.AddEditArticleContract.AddEditArticlePresenterItf;
import com.anypli.megamall.contracts.AddEditArticleContract.AddEditArticleViewItf;

import com.anypli.megamall.contracts.FileNetContract;
import com.anypli.megamall.models.ArticleAddEditPhotosGridAdapter;
import com.anypli.megamall.presenters.AddEditArticlePresenterImpl;
import com.anypli.megamall.presenters.FileNetPresenterImpl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddEditArticleActivity extends AppCompatActivity implements AddEditArticleViewItf,DialogInterface.OnClickListener ,FileNetContract.ViewItf {

    private static final int STOPPED =0 ;
    private static final int STARTED =1 ;
    private static final int COMPLETED =2 ;


    private AddEditArticlePresenterItf mAddEditPresenter;
    private FileNetContract.PresenterItf mFileNetPresenter;
    private String mArticleID ;
    private String mBoutiqueID;
    private ArticleAddEditPhotosGridAdapter mNewPhotosAdapter ;
    private ArticleAddEditPhotosGridAdapter mExistingPhotosAdapter ;

    private GridView mNewPhotosGrid ;
    private GridView mExistingPhotosGrid ;
    private Bundle mBundle ;
    private ArrayList<Uri> mPhotosToUpload ;
    private ArrayList<String> mUploadedPhotos;
    private int             mNumUploadedFiles ;
    private int             mUploadStatus ;

    private ArrayList<String>  mPhotosToRemove ;
    private int             mNumDeletedFiles;
    private int             mRemoveStatus ;
    private boolean         mNewArticle=false ;

    private ArrayList<String> mAllPhotos ;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_article);
        Toolbar toolbar = findViewById(R.id.addeditarticletoolbar);
        setSupportActionBar(toolbar);
        mAddEditPresenter = new AddEditArticlePresenterImpl(this);
        mFileNetPresenter= new FileNetPresenterImpl(this);
        mArticleID =this.getIntent().getStringExtra("articleid");
        if(mArticleID==null || mArticleID.equals(""))
            mNewArticle=true ;
        mBoutiqueID =this.getIntent().getStringExtra("boutiqueid");
        mNewPhotosAdapter=new ArticleAddEditPhotosGridAdapter(null);
        mExistingPhotosAdapter=new ArticleAddEditPhotosGridAdapter(null);
        mNewPhotosGrid=findViewById(R.id.articlephotolistartcileedit);
        mNewPhotosGrid.setAdapter(mNewPhotosAdapter);
        mExistingPhotosGrid=findViewById(R.id.existingPhotosListEdit);
        mExistingPhotosGrid.setAdapter(mExistingPhotosAdapter);
        if(!mNewArticle)
            mAddEditPresenter.GetArticleData(mArticleID);
        else {
            this.SetPageData(null);
            CircleImageView boutiqueicon = findViewById(R.id.boutiqueiconarticleedit);
            String url =getIntent().getStringExtra("iconurl");
            if (url != null && !url.equals("")) {
                Picasso.get().load(url).into(boutiqueicon);
            }
            TextView boutiquename = findViewById(R.id.boutiquenamearticleedit);
            boutiquename.setText(getIntent().getStringExtra("title"));
            TextView boutiqueadress = findViewById(R.id.boutiqueadressearticleedit);
            boutiqueadress.setText(getIntent().getStringExtra("adress"));

        }
        ImageView returnbutton = findViewById(R.id.articleaddeditreturnbutton);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        final ImageView confirmbutton = findViewById(R.id.articleaddeditconfirmbutton);
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadStatus==STOPPED) {
                    UploadImages();
                }
                /*if(mRemoveStatus==STOPPED)
                    RemoveImages();*/
                if(mUploadStatus != COMPLETED /*|| mRemoveStatus!=COMPLETED*/)
                    Toast.makeText(v.getContext(),"Wait until Operation complete",Toast.LENGTH_SHORT).show();
                else if(mUploadStatus==COMPLETED)
                    setArticleData();

            }
        });
        mPhotosToUpload=new ArrayList<>();
        mUploadedPhotos=new ArrayList<>();
        mAllPhotos= new ArrayList<>();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void SetPageData(Bundle article) {
        Button deletearticleButton ;
        if(article!=null) {
            CircleImageView boutiqueicon = findViewById(R.id.boutiqueiconarticleedit);
            if (article.getString("iconurl") != null) {
                Picasso.get().load(article.getString("iconurl")).into(boutiqueicon);
            }
            TextView boutiquename = findViewById(R.id.boutiquenamearticleedit);
            boutiquename.setText(article.getString("boutiquename"));
            TextView boutiqueadress = findViewById(R.id.boutiqueadressearticleedit);
            boutiqueadress.setText(article.getString("adress"));

            EditText name = findViewById(R.id.articlenameedittextfield);
            name.setText(article.getString("name"));
            EditText category = findViewById(R.id.articleCategoryEdittextfield);
            category.setText(article.getString("category"));
            EditText discription = findViewById(R.id.articledescriptionedittextfield);
            discription.setText(article.getString("description"));


            if(article.get("images")!=null) {
                ArrayList<String> images = (ArrayList) article.get("images");
                mAllPhotos.addAll(images);
                mExistingPhotosAdapter.setImagesUrls(images.toArray());
                mExistingPhotosAdapter.notifyDataSetChanged();

            }

            // Setup the delete button
            Button deleteButton = findViewById(R.id.articleEditDeleteButton);
            deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Your currenty trying to delete this article. Are you sure? ");
                    builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(v.getContext()));
                    builder.setNegativeButton("No",null);
                    builder.show();

                    return true;
                }
            });

            mBundle=article ;
        }else{
            //remove the delete Button because we are treating the new article case
            deletearticleButton=findViewById(R.id.articleEditDeleteButton);
            deletearticleButton.setVisibility(View.GONE);
            mBundle=new Bundle();
            mArticleID=UUID.randomUUID().toString();
            mBundle.putString("boutiqueid",mBoutiqueID);
            mBundle.putString("articleid",mArticleID);
        }
        ImageView photoselection = findViewById(R.id.takephotosarticleedit);
        photoselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imagechooser = new Intent(Intent.ACTION_PICK);
                imagechooser.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                imagechooser.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                imagechooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                // Launching the Intent
                startActivityForResult(imagechooser,2);//pick from gallery
            }
        });


    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mAddEditPresenter.requestDeleteArticle(mArticleID);
    }

    @Override
    public void dealWithIt(Bundle input) {
        this.SetPageData(input);
    }

    @Override
    public void dealWithIt(Object input) {
        if(input instanceof String){
            this.showMessage((String) input);
            if(input.equals("delete")){
                setResult(1);
                finish();
            }
            else if(input.equals("success")){
                setResult(RESULT_OK);
                finish();
            }else if(input.equals("failed")){
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK ) {
            if (requestCode == 2) {
                if (data.getClipData() != null) {
                    for (int i = 0; i < data.getClipData().getItemCount(); ++i) {
                        mPhotosToUpload.add(data.getClipData().getItemAt(i).getUri());
                    }
                    mNewPhotosAdapter.setImagesUrls(mPhotosToUpload.toArray());
                    mNewPhotosAdapter.notifyDataSetChanged();
                } else if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    mPhotosToUpload.add(selectedImage);
                    mNewPhotosAdapter.setImagesUrls(mPhotosToUpload.toArray());
                    mNewPhotosAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void setArticleData() {
        mBundle.putString("name",((EditText)findViewById(R.id.articlenameedittextfield)).getText().toString());
        mBundle.putString("description",((EditText)findViewById(R.id.articledescriptionedittextfield)).getText().toString());
        //TODO: it s seem creating this causes a problem
        mBundle.putSerializable("images",mAllPhotos);
        // mBundle.putString("category",((EditText)findViewById()).getText().toString());
        //mBundle.putString("price",((EditText)findViewById()).getText().toString());

        mAddEditPresenter.SetArticleData(mBundle,mNewArticle);
    }
    public void UploadImages(){
        mUploadStatus=STARTED ;
        for(int i= 0;i< mPhotosToUpload.size();++i){
            mFileNetPresenter.uploadImage(mPhotosToUpload.get(i),mArticleID,UUID.randomUUID().toString());
        }
    }
    public void RemoveImages(){
        mRemoveStatus=STARTED ;
        for(int i= 0;i< mPhotosToRemove.size();++i){
            mFileNetPresenter.deleteImage(mPhotosToUpload.get(i));
        }
    }

    @Override
    public void onUploadCompleteNotification(String link) {
        mNumUploadedFiles++;
        Toast.makeText(this,link,Toast.LENGTH_LONG).show();
        mUploadedPhotos.add(link);
        mAllPhotos.add(link);
        if(mNumUploadedFiles==mPhotosToUpload.size())
            mUploadStatus=COMPLETED;
    }

    @Override
    public void onUploadFailedNotification(Uri link, String message) {
        mNumUploadedFiles++;
        if(mNumUploadedFiles==mPhotosToUpload.size())
            mUploadStatus=COMPLETED;
    }

    @Override
    public void onDeleteCompleteNotification(Uri link) {
        mNumDeletedFiles++;
        mAllPhotos.remove(link.toString());
        if(mNumDeletedFiles==mPhotosToUpload.size())
            mRemoveStatus=COMPLETED;
    }

    @Override
    public void onDeleteFailedNotification(Uri link, String message) {
        mNumDeletedFiles++;
        if(mNumDeletedFiles==mPhotosToUpload.size())
            mRemoveStatus=COMPLETED;
    }
}
