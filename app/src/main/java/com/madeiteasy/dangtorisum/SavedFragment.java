package com.madeiteasy.dangtorisum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class SavedFragment extends Fragment {
    TextView list_btn, reservations_btn, visited_btn, saved_place_txt;
    ImageView list_selected, reservations_selected, visited_selected;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mfirestoreList, firestore_list2;
    private FirestoreRecyclerAdapter adapter, adapter2;
    ConstraintLayout firestore_list_layout, firestore_list_layout2, firestore_list_layout3;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);


        list_btn = (TextView) view.findViewById(R.id.list_btn);
        reservations_btn = (TextView) view.findViewById(R.id.reservations_btn);
        visited_btn = (TextView) view.findViewById(R.id.visited_btn);
        saved_place_txt = (TextView) view.findViewById(R.id.saved_place_txt);

        list_selected = (ImageView) view.findViewById(R.id.list_selected);
        reservations_selected = (ImageView) view.findViewById(R.id.reservations_selected);
        visited_selected = (ImageView) view.findViewById(R.id.visited_selected);

        mfirestoreList = view.findViewById(R.id.firestore_list);
        firestore_list2 = view.findViewById(R.id.firestore_list2);

        firestore_list_layout = view.findViewById(R.id.firestore_list_layout);
        firestore_list_layout2 = view.findViewById(R.id.firestore_list_layout2);
        firestore_list_layout3 = view.findViewById(R.id.firestore_list_layout3);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        loadINSad();

        list_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                list_selected.setVisibility(View.VISIBLE);
                reservations_selected.setVisibility(View.INVISIBLE);
                visited_selected.setVisibility(View.INVISIBLE);

                list_btn.setTextColor(Color.parseColor("#5180FF"));
                reservations_btn.setTextColor(Color.parseColor("#FF000000"));
                visited_btn.setTextColor(Color.parseColor("#FF000000"));

                mfirestoreList.setVisibility(View.VISIBLE);
                firestore_list_layout3.setVisibility(View.GONE);
                firestore_list2.setVisibility(View.GONE);
            }
        });

        reservations_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                list_selected.setVisibility(View.INVISIBLE);
                reservations_selected.setVisibility(View.VISIBLE);
                visited_selected.setVisibility(View.INVISIBLE);
                list_btn.setTextColor(Color.parseColor("#FF000000"));
                reservations_btn.setTextColor(Color.parseColor("#5180FF"));
                visited_btn.setTextColor(Color.parseColor("#FF000000"));

                mfirestoreList.setVisibility(View.GONE);
                firestore_list_layout3.setVisibility(View.VISIBLE);
                firestore_list2.setVisibility(View.GONE);

            }
        });

        visited_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                list_selected.setVisibility(View.INVISIBLE);
                reservations_selected.setVisibility(View.INVISIBLE);
                visited_selected.setVisibility(View.VISIBLE);

                list_btn.setTextColor(Color.parseColor("#FF000000"));
                reservations_btn.setTextColor(Color.parseColor("#FF000000"));
                visited_btn.setTextColor(Color.parseColor("#5180FF"));

                mfirestoreList.setVisibility(View.GONE);
                firestore_list_layout3.setVisibility(View.GONE);
                firestore_list2.setVisibility(View.VISIBLE);

            }
        });


        firestore_view();
        firestore_view_visit();
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

        InterstitialAd.load(getActivity(), "ca-app-pub-7967640087299394/7420722616", adRequest,
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

    private void firestore_view() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        String value = "Saved";
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //Query

        Query query = firebaseFirestore.collection("Favourites").document(email).collection(value);

        //RecyclerOption

        FirestoreRecyclerOptions<datamodel> options = new FirestoreRecyclerOptions.Builder<datamodel>()
                .setQuery(query, datamodel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<datamodel, dataViewHolder>(options) {
            @NonNull
            @Override
            public dataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
                return new dataViewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull dataViewHolder holder, int position, @NonNull datamodel model) {

                holder.list_place_name.setText(model.getName());
                holder.list_rating_txt.setText(model.getRating());
                try {
                    holder.ratingBar.setRating(Float.parseFloat(model.getRating()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                holder.invisivle_image_url.setText(model.getImage_url());

                try {
                    String input = holder.invisivle_image_url.getText().toString();
                    Picasso.get()
                            .load(input)
                            .centerCrop()
                            .resize(300, 165)
                            .into(holder.list_image);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                }
                String documentId = getSnapshots().getSnapshot(position).getId();

                holder.list_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), information_layout_for_saved.class);
                        setinsAD();
                        i.putExtra("class", value);
                        i.putExtra("item", documentId);
                        startActivity(i);
                    }
                });

            }
        };

        mfirestoreList.setHasFixedSize(true);
        mfirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mfirestoreList.setAdapter(adapter);
    }

    private void firestore_view_visit() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        String value = "Visited";
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //Query

        Query query = firebaseFirestore.collection("Favourites").document(email).collection(value);

        //RecyclerOption

        FirestoreRecyclerOptions<datamodel_visit> options = new FirestoreRecyclerOptions.Builder<datamodel_visit>()
                .setQuery(query, datamodel_visit.class)
                .build();

        adapter2 = new FirestoreRecyclerAdapter<datamodel_visit, dataViewHolderforvisit>(options) {
            @NonNull
            @Override
            public dataViewHolderforvisit onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_visit, parent, false);
                return new dataViewHolderforvisit(view2);
            }

            @Override
            protected void onBindViewHolder(@NonNull dataViewHolderforvisit holder, int position, @NonNull datamodel_visit model) {

                holder.list_place_name.setText(model.getName());
                holder.list_rating_txt.setText(model.getRating());
                try {
                    holder.ratingBar.setRating(Float.parseFloat(model.getRating()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                holder.invisivle_image_url.setText(model.getImage_url());

                try {
                    String input = holder.invisivle_image_url.getText().toString();
                    Picasso.get()
                            .load(input)
                            .centerCrop()
                            .resize(300, 165)
                            .into(holder.list_image);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                }
                String documentId = getSnapshots().getSnapshot(position).getId();

                holder.list_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), information_layout_for_saved.class);
                        setinsAD();
                        i.putExtra("class", value);
                        i.putExtra("item", documentId);
                        startActivity(i);
                    }
                });

            }
        };

        firestore_list2.setHasFixedSize(true);
        firestore_list2.setLayoutManager(new LinearLayoutManager(getContext()));
        firestore_list2.setAdapter(adapter2);
    }


    private class dataViewHolder extends RecyclerView.ViewHolder {

        private ImageView list_image;
        private TextView list_place_name, list_rating_txt, invisivle_image_url;
        private RatingBar ratingBar;

        public dataViewHolder(@NonNull View itemView) {
            super(itemView);

            list_place_name = itemView.findViewById(R.id.list_place_name);
            list_rating_txt = itemView.findViewById(R.id.list_rating_txt);
            invisivle_image_url = itemView.findViewById(R.id.invisivle_image_url);
            ratingBar = itemView.findViewById(R.id.list_rating_info);
            list_image = itemView.findViewById(R.id.list_image1);
        }
    }

    private class dataViewHolderforvisit extends RecyclerView.ViewHolder {

        private ImageView list_image;
        private TextView list_place_name, list_rating_txt, invisivle_image_url;
        private RatingBar ratingBar;

        public dataViewHolderforvisit(@NonNull View itemView) {
            super(itemView);

            list_place_name = itemView.findViewById(R.id.list_place_name);
            list_rating_txt = itemView.findViewById(R.id.list_rating_txt);
            invisivle_image_url = itemView.findViewById(R.id.invisivle_image_url);
            ratingBar = itemView.findViewById(R.id.list_rating_info);
            list_image = itemView.findViewById(R.id.list_image1);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter2.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter2.startListening();
    }
}
