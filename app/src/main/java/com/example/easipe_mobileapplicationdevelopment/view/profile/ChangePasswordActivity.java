package com.example.easipe_mobileapplicationdevelopment.view.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private EditText editTextCurrentPassword, editTextNewPassword, editTextConfirmPassword;
    private TextView header2;
    private Button btnChangePassword, btnReAuthenticate;
    private String userPasswordCurrent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        editTextCurrentPassword = findViewById(R.id.editTextCurrentPasssword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        header2 = findViewById(R.id.header2);
        btnReAuthenticate = findViewById(R.id.authenticateBtn);
        btnChangePassword = findViewById(R.id.changePasswordBtn);

        editTextNewPassword.setEnabled(false);
        editTextConfirmPassword.setEnabled(false);
        btnChangePassword.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        
        if(firebaseUser.equals("")){
            Toast.makeText(this, "Something's went wrong, User's details not available", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePasswordActivity.this, NavigationBar.class);
            startActivity(intent);
            finish();
        } else {
            reAuthenticatedUser(firebaseUser);
        }

        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(ChangePasswordActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ChangePasswordActivity.this, NavigationBar.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();  // Finish the current activity
            }
        });
    }

    private void reAuthenticatedUser(FirebaseUser firebaseUser) {
        btnReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPasswordCurrent = editTextCurrentPassword.getText().toString();
                
                if (TextUtils.isEmpty(userPasswordCurrent)){
                    Toast.makeText(ChangePasswordActivity.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    editTextCurrentPassword.setError("Please enter your current password to authenticate");
                    editTextCurrentPassword.requestFocus();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPasswordCurrent);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //dissable the cureent password. Enable editText for the new password and confirm new password
                                editTextCurrentPassword.setEnabled(false);
                                editTextNewPassword.setEnabled(true);
                                editTextConfirmPassword.setEnabled(true);

                                //enable change password button, disable authenticate bbutton
                                btnChangePassword.setEnabled(true);
                                btnReAuthenticate.setEnabled(false);

                                //set textview to show User is Authenticated/verified
                                header2.setText("You are now authenticated/verified." + " Change the password now!");
                                Toast.makeText(ChangePasswordActivity.this, "Password has  been verified." +
                                        "Change the password now", Toast.LENGTH_SHORT).show();

//                                //update the color of the password button
//                                btnChangePassword.setBackgroundTintList(ContextCompat.getColorStateList(
//                                        ChangePasswordActivity.this,));

                                btnChangePassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        changePassword(firebaseUser);
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e){
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePassword(FirebaseUser firebaseUser){
        String userPasswordNew = editTextNewPassword.getText().toString();
        String userPasswordConfirmed = editTextConfirmPassword.getText().toString();
        String userPasswordCurrent = editTextCurrentPassword.getText().toString();

        if (TextUtils.isEmpty(userPasswordNew)) {
            Toast.makeText(this, "New Password is needed", Toast.LENGTH_SHORT).show();
            editTextNewPassword.setError("Please enter your new password");
            editTextNewPassword.requestFocus();
        } else if (TextUtils.isEmpty(userPasswordConfirmed)) {
            Toast.makeText(this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
            editTextConfirmPassword.setError("Please re-enter your password");
            editTextConfirmPassword.requestFocus();
        } else if (!userPasswordNew.matches(userPasswordConfirmed)) {
            Toast.makeText(this, "Passwords did not match", Toast.LENGTH_SHORT).show();
            editTextConfirmPassword.setError("Please re-enter same password");
            editTextConfirmPassword.requestFocus();
        } else if (userPasswordCurrent.matches(userPasswordNew)) {
            Toast.makeText(this, "New password cannot be same as old password", Toast.LENGTH_SHORT).show();
            editTextConfirmPassword.setError("Please enter a new password");
            editTextConfirmPassword.requestFocus();
        } else {
            firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePasswordActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, NavigationBar.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    // Method to redirect to home page
    public void redirectToHomePage(View view) {
        startActivity(new Intent(this, NavigationBar.class));
        finish();
    }
}