package com.izajul.testprojectforloginwithgoogle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText email,passsword;
    TextView showText;
    Button button;
    boolean loginisTrue;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        email = (EditText) findViewById( R.id.emailInput );
        passsword = (EditText) findViewById( R.id.password );
        showText = (TextView) findViewById( R.id.showText_TV );
        button = (Button) findViewById( R.id.submit_Button );
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser!=null){
                    Toast.makeText( MainActivity.this, "Hello "+firebaseUser.getEmail(), Toast.LENGTH_SHORT ).show();
                    Log.e( "TAG","<<<<<<UserChange>>>>>:"+firebaseUser.getEmail() );
                }else{
                    Log.e( "TAG","<<<<<<<<<<User Change>>>>>>> :"+"Signed Out........." );
                }
            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener( authStateListener );
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener( authStateListener );
    }

    public void SubmitLogin(View view) {
        String emailAdd = email.getText().toString();
        String pass = passsword.getText().toString();
        if (loginisTrue){
            auth.signInWithEmailAndPassword( emailAdd,pass )
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.e( "TAG","::::IsLogingComplate:::::"+task.isSuccessful() );
                            if (!task.isSuccessful()){
                                Toast.makeText( MainActivity.this, "Authentication login Failed!!", Toast.LENGTH_SHORT ).show();
                            }else{
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String  user = firebaseUser.getEmail();
                                Toast.makeText( MainActivity.this, "Welcome "+user, Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
        }else{
            auth.createUserWithEmailAndPassword( emailAdd,pass )
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.e( "TAG","::::IsSignInComplate:::::"+task.isSuccessful() );
                            if (!task.isSuccessful()){
                                Toast.makeText( MainActivity.this, "Authentication Failed!!", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
        }
    }

    public void onStateChange(View view) {
        if (loginisTrue){
            button.setText( "SignUp" );
            showText.setText( "Already Have an account | login" );
            loginisTrue = false;
        }else{
            button.setText( "Login" );
            showText.setText( "Dont Have an account | SignUp" );
            loginisTrue = true;
        }
    }
}
