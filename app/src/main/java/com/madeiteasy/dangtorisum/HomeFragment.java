package com.madeiteasy.dangtorisum;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {
    TextView sightbtn, waterfallbtn, advanturetbtn, selecteditem, viewall_btn;
    ImageView sightselected, waterfallselected, adventureselected;

    LinearLayout sights_layout, waterfall_layout, adventure_layout;

    RelativeLayout camp_btn, hotel_btn, restaurant_btn;
    private AdView mAdView,mAdView2,mAdView3;
    private InterstitialAd mInterstitialAd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sightbtn = (TextView) view.findViewById(R.id.sights_btn);
        waterfallbtn = (TextView) view.findViewById(R.id.waterfalls_btn);
        advanturetbtn = (TextView) view.findViewById(R.id.adventure_btn);
        selecteditem = (TextView) view.findViewById(R.id.selected_item);
        viewall_btn = (TextView) view.findViewById(R.id.viewall_btn);

        sightselected = (ImageView) view.findViewById(R.id.sights_selected);
        waterfallselected = (ImageView) view.findViewById(R.id.waterfalls_selected);
        adventureselected = (ImageView) view.findViewById(R.id.adventure_selected);


        sights_layout = (LinearLayout) view.findViewById(R.id.sights_layout);
        waterfall_layout = (LinearLayout) view.findViewById(R.id.waterfalls_layout);
        adventure_layout = (LinearLayout) view.findViewById(R.id.adventure_layout);

        camp_btn = (RelativeLayout) view.findViewById(R.id.camp_btn);
        hotel_btn = (RelativeLayout) view.findViewById(R.id.hotel_btn);
        restaurant_btn = (RelativeLayout) view.findViewById(R.id.restaurant_btn);

        HorizontalScrollView h1 = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
        HorizontalScrollView h2 = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView1);
        HorizontalScrollView h3 = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView2);



        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        loadINSad();

        onClick(view);
        onclickinformation(view);
        loadimage(view);
        loadad(view);


        camp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setinsAD();
                Intent i = new Intent(getActivity(), View_list_Activity.class);
                i.putExtra("key", "Camps");
                startActivity(i);
            }
        });
        viewall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sights_layout.getVisibility() == View.VISIBLE) {
                    setinsAD();
                    Intent i = new Intent(getActivity(), View_list_Activity.class);
                    i.putExtra("key", "Sights");
                    startActivity(i);
                } else if (waterfall_layout.getVisibility() == View.VISIBLE) {
                    setinsAD();
                    Intent i = new Intent(getActivity(), View_list_Activity.class);
                    i.putExtra("key", "Waterfalls");
                    startActivity(i);
                } else if (adventure_layout.getVisibility() == View.VISIBLE) {
                    setinsAD();
                    Intent i = new Intent(getActivity(), View_list_Activity.class);
                    i.putExtra("key", "Adventures");
                    startActivity(i);
                }


            }
        });

        sightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhenSightSelected();
            }
        });
        waterfallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhenWaterfallSelected();

            }
        });
        advanturetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhenAdventureSelected();
            }
        });

        hotel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setinsAD();
                String url = "https://www.google.com/maps?q=hotels+in+dang&um=1&ie=UTF-8&sa=X&ved=2ahUKEwi8mfCrt8D3AhX1juYKHRG2DXgQ_AUoAXoECAEQAw";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        restaurant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setinsAD();
                String url = "https://www.google.com/maps/search/Restrurent+in+dang/@20.6755645,73.4840454,11z/data=!3m1!4b1";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        return view;
    }

    private void setinsAD() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity());
        } else {

        }
    }


    private void loadINSad() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getActivity(),"ca-app-pub-7967640087299394/7420722616", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                         mInterstitialAd = null;
                    }
                });

    }


    private void loadad(View view) {
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        mAdView = view.findViewById(R.id.adView);

        mAdView2 = view.findViewById(R.id.adView2);

        mAdView3 = view.findViewById(R.id.adView3);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest);
        mAdView3.loadAd(adRequest);

    }

    private void onclickinformation(View view) {
        ImageView botanical_garden = (ImageView) view.findViewById(R.id.botanical_garden);
        ImageView saputara_hillstation = (ImageView) view.findViewById(R.id.saputara_hillstation);
        ImageView shabari_dham = (ImageView) view.findViewById(R.id.shabari_dham);
        ImageView Purna_Wildlife_Sanctuary = (ImageView) view.findViewById(R.id.Purna_Wildlife_Sanctuary);
        ImageView Don_Hill_Station = (ImageView) view.findViewById(R.id.Don_Hill_Station);

        ImageView Gira_Dhodh = (ImageView) view.findViewById(R.id.Gira_Dhodh);
        ImageView shivghat_waterfall = (ImageView) view.findViewById(R.id.shivghat_waterfall);
        ImageView Bhigu_waterfall = (ImageView) view.findViewById(R.id.Bhigu_waterfall);

        ImageView Vansda_National_Park = (ImageView) view.findViewById(R.id.Vansda_National_Park);
        ImageView Saputara_Boating = (ImageView) view.findViewById(R.id.Saputara_Boating);
        ImageView Danghilla_Paragliding = (ImageView) view.findViewById(R.id.Dangjilla_Paragliding);

        botanical_garden.setOnClickListener(this::onClick);
        saputara_hillstation.setOnClickListener(this::onClick);
        shabari_dham.setOnClickListener(this::onClick);
        Purna_Wildlife_Sanctuary.setOnClickListener(this::onClick);
        Don_Hill_Station.setOnClickListener(this::onClick);

        Gira_Dhodh.setOnClickListener(this::onClick);
        shivghat_waterfall.setOnClickListener(this::onClick);
        Bhigu_waterfall.setOnClickListener(this::onClick);

        Vansda_National_Park.setOnClickListener(this::onClick);
        Saputara_Boating.setOnClickListener(this::onClick);
        Danghilla_Paragliding.setOnClickListener(this::onClick);


    }

    private void loadimage(View view) {
        ImageView botanical_garden = (ImageView) view.findViewById(R.id.botanical_garden);
        ImageView saputara_hillstation = (ImageView) view.findViewById(R.id.saputara_hillstation);
        ImageView shabari_dham = (ImageView) view.findViewById(R.id.shabari_dham);
        ImageView Purna_Wildlife_Sanctuary = (ImageView) view.findViewById(R.id.Purna_Wildlife_Sanctuary);
        ImageView Don_Hill_Station = (ImageView) view.findViewById(R.id.Don_Hill_Station);

        ImageView Gira_Dhodh = (ImageView) view.findViewById(R.id.Gira_Dhodh);
        ImageView shivghat_waterfall = (ImageView) view.findViewById(R.id.shivghat_waterfall);
        ImageView Bhigu_waterfall = (ImageView) view.findViewById(R.id.Bhigu_waterfall);

        ImageView Vansda_National_Park = (ImageView) view.findViewById(R.id.Vansda_National_Park);
        ImageView Saputara_Boating = (ImageView) view.findViewById(R.id.Saputara_Boating);
        ImageView Danghilla_Paragliding = (ImageView) view.findViewById(R.id.Dangjilla_Paragliding);


        //sights images
        Picasso.get()
                .load("https://upload.wikimedia.org/wikipedia/commons/5/57/Lake_Saputara.jpg")
                .centerCrop()
                .resize(240, 330)
                .into(saputara_hillstation);

        Picasso.get()
                .load("https://www.gujarattourism.com/content/dam/gujrattourism/images/flora--fauna/waghai-botanical-gardens/Waghai-Botanical-Gardens-Thumbnail.jpg")
                .centerCrop()
                .resize(240, 330)
                .into(botanical_garden);
        Picasso.get()
                .load("https://lh3.googleusercontent.com/p/AF1QipP6Zop9KWmdU_VGmmqtfqDqjwaXwC04OnmhLJ4l")
                .centerCrop()
                .resize(240, 330)
                .into(shabari_dham);
        Picasso.get()
                .load("https://www.gujarattourism.com/content/dam/gujrattourism/images/flora--fauna/purna-wildlife-sanctuary/Purna-Wildlife-Sanctuary-Thumbnail.jpg")
                .centerCrop()
                .resize(240, 330)
                .into(Purna_Wildlife_Sanctuary);
        Picasso.get()
                .load("https://www.gujarattourism.com/content/dam/gujrattourism/images/flora--fauna/don-hill-station/Don-Hill-Station-Thumbnail.jpg")
                .centerCrop()
                .resize(240, 330)
                .into(Don_Hill_Station);

        //waterfall images

        Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/dangtourism-ceda0.appspot.com/o/map_img%2F279340958_2093616490805252_6291074815373161950_n.jpg?alt=media&token=9d2612f0-86da-406d-9184-fcfc3a29117a")
                .centerCrop()
                .resize(240, 330)
                .into(Gira_Dhodh);

        Picasso.get()
                .load("https://lh5.googleusercontent.com/p/AF1QipOzpnyRxDpp_WS3coPBNXIZc4n-WdM3931UE5-r=w203-h360-k-no")
                .centerCrop()
                .resize(240, 330)
                .into(shivghat_waterfall);
        Picasso.get()
                .load("https://lh5.googleusercontent.com/p/AF1QipNxWoE27F5llfjf6y0tZx2UTvchGabawYLSdUpm=s564-k-no")
                .centerCrop()
                .resize(240, 330)
                .into(Bhigu_waterfall);


        //Advanture images
        Picasso.get()
                .load("https://lh5.googleusercontent.com/p/AF1QipNevlcmj_icu88hFuQn4n_NuCK3oi5TWwdf21WV=w203-h152-k-no")
                .centerCrop()
                .resize(240, 330)
                .into(Vansda_National_Park);


        Picasso.get()
                .load("https://lh5.googleusercontent.com/p/AF1QipMJoVVOSi5gE8Y0yBZcHmkDGuTB4LPkC-oApwfy=s532-k-no")
                .centerCrop()
                .resize(240, 330)
                .into(Saputara_Boating);

        Picasso.get()
                .load("https://lh5.googleusercontent.com/p/AF1QipMgpBvZ7OAXewXVLrt-hn9EbSV0CyQIlk2PnhoR=s532-k-no")
                .centerCrop()
                .resize(240, 330)
                .into(Danghilla_Paragliding);


    }

    private void WhenSightSelected() {
        sightbtn.setTextColor(getResources().getColor(R.color.text_selected, null));
        waterfallbtn.setTextColor(getResources().getColor(R.color.black, null));
        advanturetbtn.setTextColor(getResources().getColor(R.color.black, null));



        selecteditem.setText("-- Sights");
        sightselected.setVisibility(View.VISIBLE);
        waterfallselected.setVisibility(View.GONE);
        adventureselected.setVisibility(View.GONE);

        sights_layout.setVisibility(View.VISIBLE);
        waterfall_layout.setVisibility(View.GONE);
        adventure_layout.setVisibility(View.GONE);


    }

    private void WhenWaterfallSelected() {
        waterfallbtn.setTextColor(getResources().getColor(R.color.text_selected, null));
        sightbtn.setTextColor(getResources().getColor(R.color.black, null));
        advanturetbtn.setTextColor(getResources().getColor(R.color.black, null));

        selecteditem.setText("-- Waterfalls");
        sightselected.setVisibility(View.GONE);
        waterfallselected.setVisibility(View.VISIBLE);
        adventureselected.setVisibility(View.GONE);

        sights_layout.setVisibility(View.GONE);
        waterfall_layout.setVisibility(View.VISIBLE);
        adventure_layout.setVisibility(View.GONE);

    }

    private void WhenAdventureSelected() {
        advanturetbtn.setTextColor(getResources().getColor(R.color.text_selected, null));
        waterfallbtn.setTextColor(getResources().getColor(R.color.black, null));
        sightbtn.setTextColor(getResources().getColor(R.color.black, null));

        selecteditem.setText("-- Adventure");
        sightselected.setVisibility(View.GONE);
        waterfallselected.setVisibility(View.GONE);
        adventureselected.setVisibility(View.VISIBLE);

        sights_layout.setVisibility(View.GONE);
        waterfall_layout.setVisibility(View.GONE);
        adventure_layout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        setinsAD();

        switch (view.getId()) {
            case R.id.botanical_garden:
                String value = "botanical_garden";
                String map_img_url = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279468564_2094790434021191_9101340592612496055_n.jpg?_nc_cat=105&ccb=1-5&_nc_sid=730e14&_nc_ohc=W49D8YCdHsUAX-nOZhC&_nc_ht=scontent.fstv13-1.fna&oh=00_AT9cs2UXWejq_o7h6AXpFwhBUde_c-pwxTaEHKfotKkylQ&oe=6270CC24";
                sendData(value, map_img_url);
                break;
            case R.id.saputara_hillstation:
                String value1 = "saputara_hillstation";
                String map_img_url1 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279459023_2094790860687815_7319540163449476929_n.jpg?_nc_cat=105&ccb=1-5&_nc_sid=730e14&_nc_ohc=jmxV2PFGpp0AX-N2b7j&tn=d7yUAjwZuICrhr-B&_nc_ht=scontent.fstv13-1.fna&oh=00_AT9uyP0l_N8Miv8WBFlm0O69g8MzUpRbaQFd9a3cIv2xqw&oe=6270708C";
                sendData(value1, map_img_url1);
                break;
            case R.id.shabari_dham:
                String value2 = "shabari_dham";
                String map_img_url2 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279313352_2094790997354468_3234600049528908956_n.jpg?_nc_cat=107&ccb=1-5&_nc_sid=730e14&_nc_ohc=ksuvFuIKnfEAX-R8qZg&_nc_ht=scontent.fstv13-1.fna&oh=00_AT9ZiZPiqQUVWsJT-B0rBKqIBnwcfiI3Tx8PgNzXZvwkfQ&oe=6271E969";
                sendData(value2, map_img_url2);
                break;
            case R.id.Purna_Wildlife_Sanctuary:
                String map_img_url3 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279245034_2094790707354497_2448009196669008062_n.jpg?_nc_cat=101&ccb=1-5&_nc_sid=730e14&_nc_ohc=bhogvYD1_lYAX-qUr0S&_nc_ht=scontent.fstv13-1.fna&oh=00_AT9a7FH6pEwf-2xnl8Sa8JWdCgGzCmlbhybNlDfnhgJclg&oe=62714363";
                String value3 = "Purna_Wildlife_Sanctuary";
                sendData(value3, map_img_url3);
                break;
            case R.id.Don_Hill_Station:
                String value4 = "Don_Hill_Station";
                String map_img_url4 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279205444_2094790587354509_7008247475829589645_n.jpg?_nc_cat=109&ccb=1-5&_nc_sid=730e14&_nc_ohc=jcjH3yMzaxAAX8Kv30y&_nc_ht=scontent.fstv13-1.fna&oh=00_AT88Eb4FyO4F-jTW8KAE7S9izZ-PiKVw0LrdZWyL09Af7A&oe=62705772";
                sendData(value4, map_img_url4);
                break;
            case R.id.Gira_Dhodh:
                String value5 = "Gira_Dhodh";
                String map_img_url5 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279386202_2094791970687704_4819500936844620490_n.jpg?_nc_cat=101&ccb=1-5&_nc_sid=730e14&_nc_ohc=YdtsaczmPs8AX-djkhM&_nc_ht=scontent.fstv13-1.fna&oh=00_AT_uPwVxxCtXKVPvX67qwUU3KpQ-M5zwfgKNO8umRXMJ5w&oe=62711E81";
                sendData(value5, map_img_url5);
                break;

            case R.id.shivghat_waterfall:
                String value7 = "shivghat_waterfall";
                String map_img_url7 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279208847_2094792114021023_1023287567162691101_n.jpg?_nc_cat=103&ccb=1-5&_nc_sid=730e14&_nc_ohc=_Z0M1pNoVa4AX_P3z0q&_nc_ht=scontent.fstv13-1.fna&oh=00_AT_eqLEmTDU-YZ-Zr8TU-m2DSAaDN2W4ZGr7CbLaZOKfwA&oe=62709771";
                sendData(value7, map_img_url7);
                break;
            case R.id.Bhigu_waterfall:
                String value8 = "Bhigu_waterfall";
                String map_img_url8 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279592075_2094791560687745_544718898592019899_n.jpg?_nc_cat=110&ccb=1-5&_nc_sid=730e14&_nc_ohc=29MiliixtDsAX8T9AK5&_nc_ht=scontent.fstv13-1.fna&oh=00_AT--JRArOC833o8I3B9tmwdrx8TYaPj8uVrW_flUEjeW-g&oe=6271BA7C";
                sendData(value8, map_img_url8);
                break;

            case R.id.Vansda_National_Park:
                String value10 = "Vansda_National_Park";
                String map_img_url10 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279376881_2094790107354557_675832606045803713_n.jpg?_nc_cat=106&ccb=1-5&_nc_sid=730e14&_nc_ohc=F2UXvBArRSEAX_E_qxB&_nc_oc=AQkCB0tVJ8rpf7bQCUWSpKJyd8x7qi0Bsr61jeJ_OHawfT2j2ytNKyFcfCfcKB8qkic&_nc_ht=scontent.fstv13-1.fna&oh=00_AT-w5WgCo2Vjcy7Aavae68gElUhaqIaFYMU-UslmwTHcQg&oe=627049FD";
                sendData(value10, map_img_url10);
                break;

            case R.id.Saputara_Boating:
                String value12 = "Saputara_Boating";
                String map_img_url12 = "https://scontent.fstv13-1.fna.fbcdn.net/v/t39.30808-6/279360209_2094789847354583_1038085164014301151_n.jpg?_nc_cat=109&ccb=1-5&_nc_sid=730e14&_nc_ohc=tQDps8J92G8AX_H81A3&_nc_ht=scontent.fstv13-1.fna&oh=00_AT8XQ9z1419tlhGI7lMJyxhe8Y6wPtUMN-YMvImOWtzaIg&oe=62717986";
                sendData(value12, map_img_url12);
                break;

            case R.id.Dangjilla_Paragliding:
                String value14 = "Dangjilla_Paragliding";
                String map_img_url14 = "https://www.google.com/maps/uv?pb=!1s0x3bde13e33ff578c9%3A0x4946aea591703b73!3m1!7e115!4shttps%3A%2F%2Flh5.googleusercontent.com%2Fp%2FAF1QipOny7_01fceIWh49HlwHmxhBO26otx9btnDz2EG%3Dw358-h241-n-k-no!5sadventures%20in%20dang%20-%20Google%20Search!15sCgIgAQ&imagekey=!1e10!2sAF1QipOny7_01fceIWh49HlwHmxhBO26otx9btnDz2EG&hl=en";
                sendData(value14, map_img_url14);
                break;

        }
    }

    private void sendData(String value, String map_img_url) {
        Intent i = new Intent(getActivity(), Information_layout.class);
        i.putExtra("key", value);
        i.putExtra("map_img_url", map_img_url);
        startActivity(i);
    }
}