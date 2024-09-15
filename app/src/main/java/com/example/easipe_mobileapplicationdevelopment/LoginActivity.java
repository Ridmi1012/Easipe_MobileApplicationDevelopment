//Started - 2024-09-15  Author - Mishel Fernando StudentID - IM/2021/115
package com.example.easipe_mobileapplicationdevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    public void redirectToRegister(View view) {
//        Intent intent = new Intent(this, RegistrationActivity.class);
//        startActivity(intent); // Redirect to NextActivity
//    }

//    public void redirectToForgotPassword(View view) {
//        Intent intent = new Intent(this, ForgotPasswordActivity.class);
//        startActivity(intent);
//    }

//    public void redirectToMyAccount(View view) {
//        Intent intent = new Intent(this, MyAccountActivity.class);
//        startActivity(intent);
//    }
}
//Finished - 2024-09-15  Author - Mishel Fernando StudentID - IM/2021/115