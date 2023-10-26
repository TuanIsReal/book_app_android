package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.common.ApiAddress;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityLoginBinding;
import com.anhtuan.bookapp.request.GoogleLoginRequest;
import com.anhtuan.bookapp.response.LoginData;
import com.anhtuan.bookapp.response.LoginResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.RegisterData;
import com.anhtuan.bookapp.response.RegisterResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private ProgressDialog progressDialog;

    private ShapeableImageView googleLogin;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Xin chờ");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        configLoginGoogle();
        onLoginGoogle();

        binding.forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
            }
        });
    }

    private String email, password;

    private void validateData(){
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if (email.isBlank() || password.isBlank()){
            Toast.makeText(this, "Chưa nhập email/password", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email chưa đúng định dạng", Toast.LENGTH_SHORT).show();
        } else {
            loginUser();
        }
    }

    private void validateEmail(){
        email = binding.emailEt.getText().toString().trim();

        if (email.isBlank()){
            Toast.makeText(this, "Chưa nhập email", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email chưa đúng định dạng", Toast.LENGTH_SHORT).show();
        } else {
            actionForgotPassword();
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();
        userApi.login(email, password, new ApiAddress().getIPAddress()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.setMessage("Đang kiểm tra...");
                LoginResponse loginResponse = response.body();
                if (loginResponse.getCode() == 102) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Email không tồn tại trên hệ thống", Toast.LENGTH_SHORT).show();
                } else if (loginResponse.getCode() == 123) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Mật khẩu chưa đúng", Toast.LENGTH_SHORT).show();
                } else if (loginResponse.getCode() == 100) {
                    LoginData loginData = (LoginData) loginResponse.getData();
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", loginData.getUserId());
                    editor.putInt("userRole", loginData.getRole());
                    editor.putString("theme", "light");
                    editor.apply();
                    int role = loginData.getRole();
                    progressDialog.dismiss();
                    sendDeviceToken(loginData.getUserId(), role);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+ t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendDeviceToken(String userId, int role){
        Log.d("---sendDeviceToken", "---");
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.d("Token LOG--", "No Token");
                }

                String token = task.getResult();
                Log.d("---Token", token);
                userApi.loginDevice(userId, token).enqueue(new Callback<NoDataResponse>() {
                    @Override
                    public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                        if (response.body() != null){
                            NoDataResponse responseBody = response.body();
                            if (responseBody.getCode() == 106){

                            } else if (responseBody.getCode() == 100) {
                                if (role == 2){
                                    Log.d("---Start ", "Admin activity");
                                    startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                                }
                                else {
                                    Log.d("---Start ", "User activity");
                                    startActivity(new Intent(LoginActivity.this, DashboardUserActivity.class));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NoDataResponse> call, Throwable t) {
                        if (role == 2){
                            startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, DashboardUserActivity.class));
                        }
                    }
                });
            }
        });
    }

    private void onLoginGoogle(){
        binding.loginGoogle.setOnClickListener(view -> {
            signIn();
        });
    }
    private void configLoginGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                Log.d(TAG, "code " + account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");


                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this,"Login Success ",Toast.LENGTH_SHORT).show();
                        GoogleLoginRequest request=new GoogleLoginRequest(user.getEmail(),"",user.getDisplayName(),new ApiAddress().getIPAddress(), user.getPhotoUrl().toString());
                        userApi.loginGoogle(request).enqueue(new Callback<>() {
                            @Override
                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                                    if (response.body() != null && response.body().getCode() == 100) {
                                        RegisterData registerData = response.body().getData();
                                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("userId", registerData.getUserId());
                                        editor.putInt("userRole", registerData.getRole());
                                        editor.putString("theme", "light");
                                        editor.apply();
                                        int role = registerData.getRole();
                                        mGoogleSignInClient.signOut();
                                        sendDeviceToken(registerData.getUserId(), role);
                                    }
                            }
                            @Override
                            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this,"Email is not authorized",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void actionForgotPassword(){
        progressDialog.setMessage("Đang kiểm tra...");
        progressDialog.show();
        userApi.forgotPassword(email).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                progressDialog.dismiss();
                if (!Objects.isNull(response.body())){
                    if (response.body().getCode() == 106){
                        Toast.makeText(LoginActivity.this, "Email không tồn tại trên hệ thống", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().getCode() == 100){
                        Intent intent = new Intent(LoginActivity.this, CreateNewPasswordActivity.class);
                        intent.putExtra("authenType", Constant.VERIFY_CODE_TYPE.FORGOT_PASS);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}