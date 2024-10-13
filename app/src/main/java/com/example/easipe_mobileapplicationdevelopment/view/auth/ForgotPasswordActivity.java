//Created - 2024-09-15  Author - Mishel Fernando StudentID - IM/2021/115
//Backend updated - 2024-09-25  Author - Mishel Fernando StudentID - IM/2021/115
package com.example.easipe_mobileapplicationdevelopment.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button resetPasswordBtn;
    private EditText editTextemail;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        progressBar = findViewById(R.id.progressBar);

        editTextemail = findViewById(R.id.editTextTextEmailAddress);
        resetPasswordBtn = findViewById(R.id.resetPasswordButton);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextemail.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                    editTextemail.setError("Email is required");
                    editTextemail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    editTextemail.setError("Valid email is required");
                    editTextemail.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your inbox for password reset link",
                            Toast.LENGTH_SHORT).show();

                    // Hide the progress bar
                    progressBar.setVisibility(View.GONE);

                    //open add recipe page after successful reset password
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    //to prevent from returning back to forgot password activity on pressing the back button
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        editTextemail.setError("User does not exist or is no longer valid. Please register again");

                        // Hide the progress bar
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        // Hide the progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //    redirect to login page
    public void onBackClick(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
//Finished - 2024-09-25  Author - Mishel Fernando StudentID - IM/2021/115