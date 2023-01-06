package com.madeiteasy.dangtorisum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.BreakIterator;

public class View_list_Activity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestorelist;
    private FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestorelist = findViewById(R.id.firestore_list);

        TextView textView = findViewById(R.id.textView);

        hideactionbar_notifiactionbar();
        //Query

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("key");
        textView.setText(value);

 firstdata(value);
    }



    private void firstdata(String value) {
        Query query = firebaseFirestore.collection(value);
        FirestoreRecyclerOptions<datamodel> options = new FirestoreRecyclerOptions.Builder<datamodel>()
                .setQuery(query, datamodel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<datamodel, DataViewHolder>(options) {
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DataViewHolder holder, int position, @NonNull datamodel model) {
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
                            .resize(300 , 165)
                            .into(holder.list_image);

                } catch (Exception e) {
                    Toast.makeText(View_list_Activity.this, "fail", Toast.LENGTH_SHORT).show();
                }
                String documentId = getSnapshots().getSnapshot(position).getId();

                holder.list_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(View_list_Activity.this, Information_Layput_For_View_list.class);
                        i.putExtra("Category", value);
                        i.putExtra("documentId", documentId);
                        startActivity(i);
                    }
                });

            }
        };

        //view holder
        mFirestorelist.setHasFixedSize(true);
        mFirestorelist.setLayoutManager(new LinearLayoutManager(this));
        mFirestorelist.setAdapter(adapter);
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {

        private ImageView list_image;
        private TextView list_place_name, list_rating_txt, invisivle_image_url;
        private RatingBar ratingBar;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            list_place_name = itemView.findViewById(R.id.list_place_name);
            list_rating_txt = itemView.findViewById(R.id.list_rating_txt);
            invisivle_image_url = itemView.findViewById(R.id.invisivle_image_url);
            ratingBar = itemView.findViewById(R.id.list_rating_info);
            list_image = itemView.findViewById(R.id.list_image1);


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
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