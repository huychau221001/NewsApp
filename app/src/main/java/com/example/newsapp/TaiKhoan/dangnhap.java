package com.example.newsapp.TaiKhoan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newsapp.R;
import com.example.newsapp.TrangChu.trangchu;
import com.example.newsapp.TruyenDuLieu;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Arrays;

public class dangnhap extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://newsapp-a5dc3-default-rtdb.firebaseio.com/");

    TextView btn_quenmk,btn_taiday;
    Button btn_dangnhap;
    EditText sdt,matkhau;
    String st_sdt;
    String st_matkhau;
    CountryCodePicker countryCodePicker;
    ImageView img_check, imageView5, imageView4;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private FirebaseAuth auth;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        FacebookSdk.sdkInitialize(getApplicationContext());

        auth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });

        khaibao();

        //kiểm tra định dạng số điện thoại ở từng quốc gia
        countryCodePicker.registerCarrierNumberEditText(sdt);
        countryCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    img_check.setImageResource(R.drawable.ic_checkok);
                } else {
                    img_check.setImageResource(R.drawable.ic_checkx);
                }
            }
        });
        sdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString().trim();
                if (input.length() > 0) {
                    img_check.setVisibility(View.VISIBLE);
                } else {
                    img_check.setVisibility(View.GONE);
                }
            }
        });

        btn_quenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangnhap.this, quenmatkhau1.class);
                startActivity(intent);
                finish();
            }
        });

        btn_taiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangnhap.this, dangky.class);
                startActivity(intent);
                finish();
            }
        });

        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gangiatri();
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(st_sdt)) {
                            final String getMatkhau = snapshot.child(st_sdt).child("Mật khẩu").getValue(String.class);
                            if (getMatkhau.equals(st_matkhau)) {
                                Intent intent = new Intent(dangnhap.this, trangchu.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(dangnhap.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(dangnhap.this, "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(dangnhap.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(dangnhap.this, Arrays.asList("public_profile"));
                Intent intent = new Intent(dangnhap.this, trangchu.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);


        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();


            }
        });
    }

    private void SignIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Home();
            } catch (ApiException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }


    }
    private void Home() {
        finish();
        Intent intent = new Intent(getApplicationContext(), trangchu.class);
        startActivity(intent);
    }

    private void khaibao(){
        btn_quenmk = findViewById(R.id.btn_quenmatkhau);
        btn_taiday = findViewById(R.id.btn_taiday_dnhap);
        btn_dangnhap = findViewById(R.id.btn_dangnhap_dnhap);

        sdt = findViewById(R.id.ed_sdt_dnhap);
        matkhau = findViewById(R.id.ed_matkhau_dnhap);

        img_check = findViewById(R.id.img_check_dnhap);
        countryCodePicker = findViewById(R.id.ccp_dnhap);

        imageView5 = findViewById(R.id.imageView5);
        imageView4 = findViewById(R.id.imageView4);
    }

    private void gangiatri() {
        st_sdt = "+" + countryCodePicker.getFullNumber();
        st_matkhau = matkhau.getText().toString().trim();
        TruyenDuLieu.Tr_sdt = st_sdt;
    }
}