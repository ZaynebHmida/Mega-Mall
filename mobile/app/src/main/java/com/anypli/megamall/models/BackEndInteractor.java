package com.anypli.megamall.models;


import android.net.Uri;
import android.os.Bundle;

import com.anypli.megamall.contracts.ARContract;
import com.anypli.megamall.contracts.AccountContract;
import com.anypli.megamall.contracts.CommonContract;
import com.anypli.megamall.contracts.FileNetContract;
import com.anypli.megamall.contracts.LoginContract;
import com.anypli.megamall.contracts.NavigationContract;
import com.anypli.megamall.contracts.SignUpContract;
import com.anypli.megamall.presenters.ARPresenterImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import androidx.annotation.NonNull;

public class BackEndInteractor {

    private static BackEndInteractor instance=null ;
    public static BackEndInteractor getInstance() {
        if(instance==null){
            instance= new BackEndInteractor();
        }
        return instance ;
    }
    // necessary objects for communicating with the backend
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mDataStorage ;
    private FirebaseStorage mFileStorageRef;
    private AccountData mUserdata;
    private ArrayList<String> mBoutiqueID ;


    private BackEndInteractor(){
        mFirebaseAuth= FirebaseAuth.getInstance();
        mDataStorage= FirebaseFirestore.getInstance();
        mFileStorageRef= FirebaseStorage.getInstance();
        mUserdata=null;
        mBoutiqueID=new ArrayList<>();
    }
    public  AccountData SignUp(final SignUpContract.SignUpPresenterItf presenterItf , final String username, final String email, final  String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("username",username);
                    mDataStorage.collection("Users").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                presenterItf.setResultMessage("success");
                            }else{
                                presenterItf.setResultMessage("failed");
                            }
                        }
                    });
                }
            }
        });
        return  null ;
    }
    public void SignIn(final LoginContract.LoginPresenterItf presenter, final String Email, final String Password){
        if(mUserdata==null) {
            mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        mDataStorage.collection("Users").whereEqualTo("email", Email)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.getResult().isEmpty()) {
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    mUserdata = new AccountData(documentSnapshot.getId(),
                                            documentSnapshot.getString("username"),
                                            Password,
                                            Email,
                                            documentSnapshot.getString("firstname"),
                                            documentSnapshot.getString("lastname"),
                                            documentSnapshot.getString("adress"),
                                            documentSnapshot.getString("iconurl"),
                                            documentSnapshot.getString("phone"));
                                    //get Boutique ids while we still can
                                    mDataStorage.collection("Boutiques")
                                            .whereEqualTo("ownerid", mUserdata.getId())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (!task.getResult().isEmpty()) {
                                                        for (int i = 0; i < task.getResult().getDocuments().size(); ++i) {
                                                            mBoutiqueID.add(task.getResult().getDocuments().get(i).getId());
                                                        }
                                                    }
                                                }
                                            });
                                    presenter.Login(mUserdata);

                                }
                            }
                        });
                    }else{
                        presenter.showErrorMessage("error: "+task.getException().getMessage());
                    }
                }
            });
        }
    }
    public void SignOut(){
        mFirebaseAuth.signOut();
        mUserdata=null;
    }
    public AccountData getAccountData(){
        return mUserdata ;
    }
    public boolean isCurrentUserBoutique(String boutiqueid) {
        if(mBoutiqueID!=null)
            if(mBoutiqueID.contains(boutiqueid)){
                    return true ;
            }
        return false ;

    }

    public  void deleteArticle(final CommonContract.PresenterItf presenter ,String articleid) {
        mFileStorageRef.getReference().child(articleid).delete();
        mDataStorage.collection("Articles").document(articleid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        presenter.dealWithIt("delete");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.dealWithIt("failed");
            }
        });
    }

    public  void deleteBoutique(final CommonContract.PresenterItf presenter ,String boutiqueid) {
        mDataStorage.collection("Boutiques").document(boutiqueid).delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                presenter.dealWithIt("delete");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.dealWithIt("failed");
            }
        });
    }
    public  void SetBoutiqueData(final CommonContract.PresenterItf presenter ,final Bundle boutiquedata) {
        String boutiqueid = boutiquedata.getString("boutiqueid", "");
        final HashMap<String, Object> map = new HashMap<>();
        map.put("adress", boutiquedata.getString("adress"));
        map.put("description", boutiquedata.getString("description"));
        map.put("title", boutiquedata.getString("title"));
        map.put("ownerid",mUserdata.getId());
        map.put("iconurl",boutiquedata.getString("iconurl"));
        map.put("bgimage",boutiquedata.getString("bgimage"));
        if(boutiquedata.getBoolean("haveLocation",false)){
            GeoPoint location = new GeoPoint(boutiquedata.getDouble("lat",0.0),
                                            boutiquedata.getDouble("long",0.0));
            map.put("location",location);
        }
        if (!boutiqueid.equals("")) {
            mDataStorage.collection("Boutiques").document(boutiqueid).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    presenter.dealWithIt("success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    presenter.dealWithIt("failed");
                }
            });
        }else{
            final String newid =UUID.randomUUID().toString();
            mDataStorage.collection("Boutiques").document(newid)
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mBoutiqueID.add(newid);
                            presenter.dealWithIt("success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    presenter.dealWithIt("failed");
                }
            });
        }
    }

    public  void SetArticleData(final CommonContract.PresenterItf presenter, final Bundle articledata,boolean newArticle) {
        String articleid = articledata.getString("articleid","");
        final HashMap<String,Object> map = new HashMap<>();
        map.put("category",articledata.get("category"));
        map.put("description",articledata.get("description"));
        map.put("name",articledata.get("name"));
        map.put("price",articledata.get("price"));
        map.put("boutiqueid",articledata.getString("boutiqueid"));
        map.put ("images",articledata.get("images"));
        if(!newArticle) {
            mDataStorage.collection("Articles").document(articleid)
                    .update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    presenter.dealWithIt("success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    presenter.dealWithIt("failed");
                }
            });
        }else{

            mDataStorage.collection("Articles").document(articleid)
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            presenter.dealWithIt("success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    presenter.dealWithIt("failed");
                }
            });

        }
    }


    public  void GetSearchResult(final NavigationContract.NavigationPresenterItf presenter, String Keywords){
        mDataStorage.collection("Articles").whereEqualTo("name",Keywords)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()) {
                    Bundle[] bundles = new Bundle[task.getResult().size()];
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    for (int i= 0; i<bundles.length;++i){
                        bundles[i].putString("articleid",docs.get(i).getId());
                        bundles[i].putString("name",docs.get(i).getString("name"));
                        bundles[i].putString("description",docs.get(i).getString("description"));
                        bundles[i].putString("category",docs.get(i).getString("category"));
                        bundles[i].putString("boutiqueid",docs.get(i).getString("boutiqueid"));
                        if(docs.get(i).get("images")!=null)
                            bundles[i].putString("imageurl", (String) ((ArrayList) (docs.get(i).get("images"))).get(0));
                    }
                    presenter.ShowSearchResult(bundles);
                }
            }

        });
    }
    public  void getNews(final NavigationContract.NavigationPresenterItf presenter){
        mDataStorage.collection("Articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()) {
                    Bundle[] bundles = new Bundle[task.getResult().size()];
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    for (int i= 0; i<bundles.length;++i){
                        bundles[i]=new Bundle();
                        bundles[i].putString("articleid",docs.get(i).getId());
                        bundles[i].putString("name",docs.get(i).getString("name"));
                        bundles[i].putString("description",docs.get(i).getString("description"));
                        bundles[i].putString("category",docs.get(i).getString("category"));
                        bundles[i].putString("boutiqueid",docs.get(i).getString("boutiqueid"));
                        if(docs.get(i).get("images")!=null)
                            bundles[i].putString("imageurl", (String) ((ArrayList) (docs.get(i).get("images"))).get(0));
                    }
                    presenter.ShowNewFeed(bundles);
                }
            }
        });
    }

    public  void GetBoutiqueData (final CommonContract.PresenterItf presenterItf,String Id) {
        mDataStorage.collection("Boutiques").document(Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    Map<String, Object> data = task.getResult().getData();
                    Bundle bundle =new Bundle();
                    bundle.putString("boutiqueid",task.getResult().getId());
                    bundle.putString("title", (String) data.get("title"));
                    bundle.putString("description",(String) data.get("description"));
                    bundle.putString("iconurl",(String) data.get("iconurl"));
                    bundle.putString("adress",(String) data.get("adress"));
                    bundle.putString("bgimage",(String) data.get("bgimage"));
                    GeoPoint location = (GeoPoint) data.get("location");
                    if(location!=null){
                        bundle.putBoolean("haveLocation",true);
                        bundle.putDouble("lat",location.getLatitude());
                        bundle.putDouble("long",location.getLongitude());
                    }
                    presenterItf.dealWithIt(bundle);
                }
            }
        });
    }

    public void GetArticleData(final CommonContract.PresenterItf presenterItf ,String Id){
        mDataStorage.collection("Articles").document(Id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    if (task.getResult().exists()) {
                        Map<String, Object> data = task.getResult().getData();

                        final Bundle bundle =new Bundle();
                        bundle.putString("articleid",task.getResult().getId());
                        bundle.putString("boutiqueid",(String)data.get("boutiqueid"));
                        bundle.putString("name", (String) data.get("name"));
                        bundle.putString("category", (String) data.get("category"));
                        bundle.putString("description",(String) data.get("description"));
                        bundle.putParcelableArrayList("images", (ArrayList) data.get("images"));
                        mDataStorage.collection("Boutiques")
                                .document(bundle.getString("boutiqueid"))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists()){
                                    bundle.putString("iconurl",(String) task.getResult().get("iconurl"));
                                    bundle.putString("adress",(String) task.getResult().get("adress"));
                                    bundle.putString("boutiquename",(String) task.getResult().get("title"));
                                    presenterItf.dealWithIt(bundle);
                                }
                            }
                        });

                    }
                }
            }
        });
    }

    public void getBoutiqueArticleList(final CommonContract.PresenterItf presenterItf , String id) {
        mDataStorage.collection("Articles").whereEqualTo("boutiqueid",id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()){
                    int numElements = task.getResult().getDocuments().size();
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    Bundle[] bundles = new Bundle[numElements];
                    for (int i=0 ; i< numElements;++i) {
                        bundles[i]=new Bundle();
                        bundles[i].putString("articleid",documents.get(i).getId());
                        bundles[i].putString("name",documents.get(i).getString("name"));
                        if(documents.get(i).get("images")!=null)
                            bundles[i].putString("imageurl", (String) ((ArrayList)documents.get(i).get("images")).get(0));
                        bundles[i].putString("category",documents.get(i).getString("category"));
                    }
                    presenterItf.dealWithIt(bundles);
                }else{
                    presenterItf.dealWithIt((Object) null);
                }
            }
        });
    }

    public void GetArticleReviews(final CommonContract.PresenterItf presenterItf ,String id) {
        mDataStorage.collection("Reviews").whereEqualTo("articleid",id)
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()){
                    int numElements = task.getResult().getDocuments().size();
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    Bundle[] bundles = new Bundle[numElements];
                    for (int i=0 ; i< numElements;++i) {
                        bundles[i]=new Bundle();
                        bundles[i].putString("username",documents.get(i).getString("username"));
                        bundles[i].putString("usericon",documents.get(i).getString("usericon"));
                        bundles[i].putString("comment",documents.get(i).getString("comment"));
                        bundles[i].putFloat("rating",(documents.get(i).getDouble("rating").floatValue()));
                    }
                    presenterItf.dealWithIt(bundles);
                }else{
                    presenterItf.dealWithIt((Object) null);
                }
            }
        });
    }

    public void AddReviewOnArticle(final CommonContract.PresenterItf presenterItf, String articleid, ArticleReview review){
        HashMap<String,Object> map = new HashMap<>();
        map.put("articleid",articleid);
        map.put("username",review.getUsername());
        map.put("usericon",review.getUserIconUrl());
        map.put("comment",review.getComment());
        map.put("rating",review.getRating());
        mDataStorage.collection("Reviews").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                    presenterItf.dealWithIt("success");
                else
                    presenterItf.dealWithIt("failed");
            }
        });
    }

    public void getBoutiquesByUserId(final CommonContract.PresenterItf presenterItf ,String userid) {
        mDataStorage.collection("Boutiques").whereEqualTo("ownerid",userid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()){
                    int numElements = task.getResult().getDocuments().size();
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    Bundle[] bundles = new Bundle[numElements];
                    for (int i=0 ; i< numElements;++i) {
                        bundles[i]=new Bundle();
                        bundles[i].putString("boutiqueid",documents.get(i).getId());
                        bundles[i].putString("title",documents.get(i).getString("title"));
                        bundles[i].putString("iconurl",documents.get(i).getString("iconurl"));
                        bundles[i].putString("adress",documents.get(i).getString("adress"));
                    }
                    presenterItf.dealWithIt(bundles);
                }else{
                    presenterItf.dealWithIt((Object) null);
                }
            }
        });
    }

    public  void UpdateAccoutData(final AccountContract.AccountPresenterItf presenterItf, AccountData newData) {
        /*HashMap data = new HashMap();
        //TODO : fill the map
        mDataStorage.collection("Users").document(mUserdata.getId())
                .update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    presenterItf.NotifyView("success");
                }else{
                    presenterItf.NotifyView("success");
                }
            }
        });*/
        presenterItf.NotifyView("cannot account data , report this to the creator");
    }
    public void uploadImage(final FileNetContract.PresenterItf presenter, final Uri imagelocaluri, final String folderid, final String newname){

        mFileStorageRef.getReference().child(folderid+"/"+newname).putFile(imagelocaluri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mFileStorageRef.getReference().child(folderid+"/"+newname).getDownloadUrl()
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    presenter.notifyViewUploadComplete( task.getResult().toString());
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.notifyViewUploadFailed(imagelocaluri,e.getMessage());
            }
        });
    }

    public void deleteImage(final FileNetContract.PresenterItf presenter, final Uri imageuri ){

        mFileStorageRef.getReference().child(imageuri.toString()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            presenter.notifyViewDeleteComplete(imageuri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.notifyViewDeleteFailed(imageuri,e.getMessage());
            }
        });
    }


    public void getAllBoutiques(final ARContract.PresenterItf presenterItf) {
        mDataStorage.collection("Boutiques").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){
                            int numElements = task.getResult().getDocuments().size();
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            Bundle[] bundles = new Bundle[numElements];
                            for (int i=0 ; i< numElements;++i) {
                                bundles[i]=new Bundle();
                                bundles[i].putString("title",documents.get(i).getString("title"));
                                bundles[i].putString("iconurl",documents.get(i).getString("iconurl"));
                                bundles[i].putString("adress",documents.get(i).getString("adress"));
                                GeoPoint point = documents.get(i).getGeoPoint("location");
                                bundles[i].putDouble("lat",point.getLatitude());
                                bundles[i].putDouble("long",point.getLongitude());
                            }
                            presenterItf.returnAllBoutiques(bundles);
                        }else{
                            presenterItf.returnAllBoutiques(null);
                        }
                    }
                });
    }
}
