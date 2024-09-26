// Started - 2024-09-15  Author - Mishel Fernando StudentID - IM/2021/115
package com.example.easipe_mobileapplicationdevelopment.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextfirstname, editTextlastname, editTextemail, editTextusername,
            editTextpassword, editTextpassword2;
    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toast.makeText(this, "You can register now", Toast.LENGTH_SHORT).show();

        editTextfirstname = findViewById(R.id.firstNameInput);
        editTextlastname = findViewById(R.id.lastNameInput);
        editTextemail = findViewById(R.id.emailInput);
        editTextusername = findViewById(R.id.usernameInput);
        editTextpassword = findViewById(R.id.editTextTextPassword);
        editTextpassword2 = findViewById(R.id.editTextTextPassword2);

        Button ButtonSignup = findViewById(R.id.signupBtn);
        ButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtain the entered data
                String firstname = editTextfirstname.getText().toString();
                String lastname = editTextlastname.getText().toString();
                String email = editTextemail.getText().toString();
                String username = editTextusername.getText().toString();
                String password = editTextpassword.getText().toString();
                String confirmedPassword = editTextpassword2.getText().toString();

                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter the first name", Toast.LENGTH_SHORT).show();
                    editTextfirstname.setError("First name is required");
                    editTextfirstname.requestFocus();
                } else if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter the last name", Toast.LENGTH_SHORT).show();
                    editTextlastname.setError("Last name is required");
                    editTextlastname.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter the email", Toast.LENGTH_SHORT).show();
                    editTextemail.setError("Email is required");
                    editTextemail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegistrationActivity.this, "Please re-enter the email", Toast.LENGTH_SHORT).show();
                    editTextemail.setError("Valid email is required");
                    editTextemail.requestFocus();
                } else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter the username", Toast.LENGTH_SHORT).show();
                    editTextusername.setError("Username is required");
                    editTextusername.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    editTextpassword.setError("Password is required");
                    editTextpassword.requestFocus();
                } else if (TextUtils.isEmpty(confirmedPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Please re-enter the confirmed password", Toast.LENGTH_SHORT).show();
                    editTextpassword2.setError("Confirmed password is required");
                    editTextpassword2.requestFocus();
                } else if (!password.equals(confirmedPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    editTextpassword2.setError("Password confirmation is required");
                    editTextpassword2.requestFocus();
                    editTextpassword.clearComposingText();
                    editTextpassword2.clearComposingText();
                } else if (password.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    editTextpassword.setError("Password is too weak");
                    editTextpassword.requestFocus();
                } else if (confirmedPassword.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    editTextpassword2.setError("Password is too weak");
                    editTextpassword2.requestFocus();
                } else {
                    registerUser(firstname, lastname, email, username, password);
                }
            }
        });
    }


    private void registerUser(String firstname, String lastname, String email, String username, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //send the verification email
                            firebaseUser.sendEmailVerification();

                            //open login page after successful registration
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            //to prevent from returning back to register activity on pressing the back button
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e){
                                editTextpassword.setError("Your password is too weak. Kindly use a mix of alphabet, numbers and special characters");
                                editTextpassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                editTextpassword.setError("Your email is invalid or already in use. Kindly re-enter ");
                                editTextpassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e){
                                editTextpassword.setError("User already with this email");
                                editTextpassword.requestFocus();
                            } catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // Redirect to the login page
    public void redirectToLogin(View view) {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // Handle the back button click to redirect to the login page
    public void onBackClick(View view) {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}



// Started - 2024-09-15  Author - Mishel Fernando StudentID - IM/2021/115
