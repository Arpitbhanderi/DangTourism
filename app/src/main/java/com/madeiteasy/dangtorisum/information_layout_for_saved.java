package com.madeiteasy.dangtorisum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class information_layout_for_saved extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference;

    String uni_name, ret_image, uni_rating, uni_place, uni_info, uni_website, uni_map_url, uni_map_img, uni_multi_img;
    LinearLayout back_btn, share_btn;
    List<String> group;
    TextView place_name, place_location, place_info, place_map_url_img, rating_in_txt, saved_txt, mark_visited;
    CardView map_btn;
    ImageView image, website, map_img, save_btn, visited_btn, unchecked_save_btn, unchecked_visited_btn;
    RatingBar ratingBar;
    ImageView image1, image2, image3, image4, image5, image6;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_layout_for_saved);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        hideactionbar_notifiactionbar();


        back_btn = findViewById(R.id.back_btn);
        image = findViewById(R.id.image_here);
        place_name = findViewById(R.id.place_name);
        place_location = findViewById(R.id.place_loaction);
        place_info = findViewById(R.id.place_information);
        map_btn = findViewById(R.id.map_btn);
        rating_in_txt = findViewById(R.id.rating_in_txt1);
        ratingBar = findViewById(R.id.item_rating_info);
        website = findViewById(R.id.website_btn);
        map_img = findViewById(R.id.map_img);
        save_btn = findViewById(R.id.save_btn);
        visited_btn = findViewById(R.id.visited_btn);
        unchecked_save_btn = findViewById(R.id.unchecked_save_btn);
        saved_txt = findViewById(R.id.saved_txt);
        unchecked_visited_btn = findViewById(R.id.unchecked_visited_btn);
        mark_visited = findViewById(R.id.mark_visited);
        share_btn = findViewById(R.id.share_btn);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);

        Bundle extras = getIntent().getExtras();
        String documentId = extras.getString("item");

        fromhomepage(documentId);
        getimages(documentId);

        loadad();

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.madeiteasy.dangtorisum"));
                startActivity(i);
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedatasaved();
            }
        });

        unchecked_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetodatabase();
            }
        });

        unchecked_visited_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetodatabase_forvisit();
            }
        });

        visited_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedata_forVisit();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void loadad() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void checksaved() {

        try {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            DocumentReference docRef = db.collection("Favourites").document(email).collection("Saved").document(uni_name);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            uni_name = document.getString("name");
                            save_btn.setVisibility(View.VISIBLE);
                            unchecked_save_btn.setVisibility(View.INVISIBLE);
                            saved_txt.setText("Saved");


                        } else {
                            //does not exist
                            save_btn.setVisibility(View.INVISIBLE);
                            unchecked_save_btn.setVisibility(View.VISIBLE);
                            saved_txt.setText("Save");
                        }
                    }
                }
            });


        } catch (Exception e) {
            Toast.makeText(information_layout_for_saved.this, "fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkVisited() {
        try {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            DocumentReference docRef = db.collection("Favourites").document(email).collection("Visited").document(uni_name);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            uni_name = document.getString("name");
                            visited_btn.setVisibility(View.VISIBLE);
                            unchecked_visited_btn.setVisibility(View.INVISIBLE);
                            mark_visited.setText("Visited");


                        } else {
                            //does not exist
                            unchecked_visited_btn.setVisibility(View.VISIBLE);
                            visited_btn.setVisibility(View.INVISIBLE);
                            mark_visited.setText("Mark Visited");

                        }
                    }
                }
            });


        } catch (Exception e) {
            Toast.makeText(information_layout_for_saved.this, "fail", Toast.LENGTH_SHORT).show();
        }

    }

    private void savetodatabase_forvisit() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        try {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, String> data = new HashMap<>();
            data.put("name", uni_name);
            data.put("image_url", ret_image);
            data.put("rating", uni_rating);
            data.put("city", uni_place);
            data.put("info", uni_info);
            data.put("website_url", uni_website);
            data.put("map_url", uni_map_url);
            data.put("img_map_url", uni_map_img);
            data.put("multi_img", uni_multi_img);

            try {
                FirebaseFirestore.getInstance().collection("Favourites").document(email).collection("Visited").document(uni_name).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            } catch (Exception e) {
                Toast.makeText(information_layout_for_saved.this, "FAILED.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(information_layout_for_saved.this, "Something went wrong! Try Again.", Toast.LENGTH_SHORT).show();
        }
        checkVisited();

    }

    private void deletedata_forVisit() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Favourites").document(email).collection("Visited").document(uni_name)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(information_layout_for_saved.this, "Something went Wrong! Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
        checkVisited();
    }

    private void deletedatasaved() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Favourites").document(email).collection("Saved").document(uni_name)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(information_layout_for_saved.this, "Something went Wrong! Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
        checksaved();
    }

    private void savetodatabase() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        try {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, String> data = new HashMap<>();
            data.put("name", uni_name);
            data.put("city", uni_place);
            data.put("image_url", ret_image);
            data.put("rating", uni_rating);
            data.put("info", uni_info);
            data.put("website_url", uni_website);
            data.put("map_url", uni_map_url);
            data.put("img_map_url", uni_map_img);
            data.put("multi_img", uni_multi_img);


            try {
                FirebaseFirestore.getInstance().collection("Favourites").document(email).collection("Saved").document(uni_name).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
            } catch (Exception e) {
                Toast.makeText(information_layout_for_saved.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(information_layout_for_saved.this, "Something went wrong! Try Again.", Toast.LENGTH_SHORT).show();
        }

        checksaved();
    }

    private void getimages(String documentId) {
        try {
            reference = db.collection("Adventures").document(documentId);
            getimagedata();
        } catch (Exception e) {
        }

        try {
            reference = db.collection("Camps").document(documentId);
            getimagedata();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            reference = db.collection("Sights").document(documentId);
            getimagedata();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            reference = db.collection("Waterfalls").document(documentId);
            getimagedata();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getimagedata() {
        try {
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        searchdoc(documentSnapshot);

                    } else {

                    }
                }

                private void searchdoc(DocumentSnapshot documentSnapshot) {
                    List<String> group = (List<String>) documentSnapshot.get("multi_img");
                    int width = image1.getWidth();
                    int height = image1.getHeight();

                    Picasso.get()
                            .load(group.get(0))
                            .centerCrop()
                            .resize(width, height)
                            .into(image1);
                    Picasso.get()
                            .load(group.get(1))
                            .centerCrop()
                            .resize(width, height)
                            .into(image2);
                    Picasso.get()
                            .load(group.get(2))
                            .centerCrop()
                            .resize(width, height)
                            .into(image3);
                    Picasso.get()
                            .load(group.get(3))
                            .centerCrop()
                            .resize(width, height)
                            .into(image4);
                    Picasso.get()
                            .load(group.get(4))
                            .centerCrop()
                            .resize(width, height)
                            .into(image5);
                    Picasso.get()
                            .load(group.get(5))
                            .centerCrop()
                            .resize(width, height)
                            .into(image6);

                    image1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(information_layout_for_saved.this, PhotoView.class);
                            i.putExtra("url", group.get(0));
                            startActivity(i);
                        }
                    });
                    image2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(information_layout_for_saved.this, PhotoView.class);
                            i.putExtra("url", group.get(1));
                            startActivity(i);
                        }
                    });
                    image3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(information_layout_for_saved.this, PhotoView.class);
                            i.putExtra("url", group.get(2));
                            startActivity(i);
                        }
                    });
                    image4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(information_layout_for_saved.this, PhotoView.class);
                            i.putExtra("url", group.get(3));
                            startActivity(i);
                        }
                    });
                    image5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(information_layout_for_saved.this, PhotoView.class);
                            i.putExtra("url", group.get(4));
                            startActivity(i);
                        }
                    });
                    image6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(information_layout_for_saved.this, PhotoView.class);
                            i.putExtra("url", group.get(5));
                            startActivity(i);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(information_layout_for_saved.this, "Failed to Fetch Data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to Load Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void fromhomepage(String documentId) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        reference = db.collection("Favourites").document(email).collection("Saved").document(documentId);
        getdatafirestore();
    }

    public void getdatafirestore() {
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        Bundle extras = getIntent().getExtras();
        String documentId = extras.getString("item");
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    searchdoc(documentSnapshot);


                } else {

                }

            }

            private void searchdoc(DocumentSnapshot documentSnapshot) {
                place_name.setText(documentSnapshot.getString("name"));
                uni_name = documentSnapshot.getString("name");

                checksaved();
                checkVisited();
                place_location.setText(documentSnapshot.getString("city"));
                place_info.setText(documentSnapshot.getString("info"));
                String web_url = documentSnapshot.getString("website_url");
                website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(web_url));
                        startActivity(i);
                    }
                });

                String map_url = documentSnapshot.getString("map_url");
                map_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(map_url));
                        startActivity(i);
                    }
                });


                rating_in_txt.setText(documentSnapshot.getString("rating"));
                uni_rating = documentSnapshot.getString("rating");
                uni_place = documentSnapshot.getString("city");
                uni_info = documentSnapshot.getString("info");
                uni_website = documentSnapshot.getString("website_url");
                uni_map_url = documentSnapshot.getString("map_url");
                uni_map_img = documentSnapshot.getString("img_map_url");
                ratingBar.setRating(Float.parseFloat(documentSnapshot.getString("rating")));


              /*  List<String> group = (List<String>) documentSnapshot.get("multi_img");
                Picasso.get()
                        .load(group.get(1))
                        .centerCrop()
                        .resize(74, 53)
                        .into(image1);
                Picasso.get()
                        .load(group.get(2))
                        .centerCrop()
                        .resize(74, 53)
                        .into(image2);
                Picasso.get()
                        .load(group.get(3))
                        .centerCrop()
                        .resize(74, 53)
                        .into(image3);
                Picasso.get()
                        .load(group.get(4))
                        .centerCrop()
                        .resize(74, 53)
                        .into(image4);
                Picasso.get()
                        .load(group.get(5))
                        .centerCrop()
                        .resize(74, 53)
                        .into(image5);
                Picasso.get()
                        .load(group.get(0))
                        .centerCrop()
                        .resize(74, 53)
                        .into(image6);*/

                ret_image = documentSnapshot.getString("image_url");
                Picasso.get()
                        .load(ret_image)
                        .centerCrop()
                        .resize(widthPixels, 700)
                        .into(image);

                Picasso.get()
                        .load(uni_map_img)
                        .centerCrop()
                        .resize(330, 140)
                        .into(map_img);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(information_layout_for_saved.this, "Failed to Fetch Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideactionbar_notifiactionbar() {
        //hide actionbar and notification bar

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}