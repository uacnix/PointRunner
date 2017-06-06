package pj.uacnix.pointrunner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button loginButton,registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    registerButton.setEnabled(false);
                    Log.d("AUTH:", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    registerButton.setEnabled(true);
                    Log.d("AUTH:", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        registerButton = (Button) findViewById(R.id.regButton);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setEnabled(false);
                signIn();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(){
        Intent regActivity = new Intent(getApplicationContext(), RegisterActivity.class);
        regActivity.putExtra("mail", mEmailView.getText().toString());
        regActivity.putExtra("pwd", mPasswordView.getText().toString());
        startActivity(regActivity);
    }

    private void signIn(){
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String pwd = mPasswordView.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOGIN", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("LOGIN", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.login_fuckup,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                       //     Toast.makeText(LoginActivity.this,"User login successful", Toast.LENGTH_SHORT).show();

                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            //finish();
                            startActivity(main);
                            loginButton.setEnabled(true);
                        }
                    }
                });
    }
}

