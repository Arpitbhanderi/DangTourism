package com.madeiteasy.dangtorisum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class SettingsFragment extends Fragment {
    Button log_out, reset;
    CardView beautiful_dang_btn, exploring_dang_btn, soul_travelers_btn;
    ImageView backbtn, savebtn;
    ConstraintLayout mainlayout, accountlayout;
    LinearLayout account_layer, privacy_policy_layer, terms_and_services_layer, contact_us_layer, admin_area;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        log_out = (Button) view.findViewById(R.id.Log_out);
        reset = (Button) view.findViewById(R.id.reset_password);

        beautiful_dang_btn = (CardView) view.findViewById(R.id.beautiful_dang_cardView);
        exploring_dang_btn = (CardView) view.findViewById(R.id.exploring_dang_cardView);
        soul_travelers_btn = (CardView) view.findViewById(R.id.soul_travelers_cardView);

        backbtn = (ImageView) view.findViewById(R.id.back_press_btn);
        savebtn = (ImageView) view.findViewById(R.id.saved_btn);

        mainlayout = (ConstraintLayout) view.findViewById(R.id.main_layout);
        accountlayout = (ConstraintLayout) view.findViewById(R.id.account_layout);

        account_layer = (LinearLayout) view.findViewById(R.id.account_layer);
        privacy_policy_layer = (LinearLayout) view.findViewById(R.id.privacy_policy_layer);
        terms_and_services_layer = (LinearLayout) view.findViewById(R.id.terms_and_services_layer);
        contact_us_layer = (LinearLayout) view.findViewById(R.id.contact_us_layer);
        admin_area = (LinearLayout) view.findViewById(R.id.Admin_area);


        showemail(view);
        getAdmin(view);

        soul_travelers_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/soultravelersofficial/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });


        beautiful_dang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/beautiful.dang/?hl=en";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        exploring_dang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/exploringdang/?hl=en";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Password Reset Email Sent!", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Logged Out!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainlayout.setVisibility(View.VISIBLE);
                accountlayout.setVisibility(View.GONE);
            }
        });

        account_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainlayout.setVisibility(View.GONE);
                accountlayout.setVisibility(View.VISIBLE);
            }
        });
        privacy_policy_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://pages.flycricket.io/dangtourism/privacy.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        terms_and_services_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://pages.flycricket.io/dangtourism/terms.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        contact_us_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chooserTitle = "DangTourism";
                String email = "madeiteasy4you@gmail.com";
                Uri uri = Uri.parse("mailto:" + email)
                        .buildUpon()
                        .build();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(Intent.createChooser(emailIntent, chooserTitle));
            }
        });


        admin_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Admin_Layout.class);
                startActivity(i);
            }
        });


        return view;


    }

    private void getAdmin(View view) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        try {
            if (email.matches("arpitbhanderi8@gmail.com")) {
                admin_area.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            admin_area.setVisibility(View.GONE);
        }


    }

    private void showemail(View view) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        TextView showemail = (TextView) view.findViewById(R.id.show_email);
        showemail.setText(email);


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}