package es.uem.android_grupo03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.identity.SignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.uem.android_grupo03.models.PerfilModelo;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private SignInClient oneTapClient;
    private DatabaseReference databaseReference;

    private EditText emailField, passwordField;
    private Button loginButton, googleSignInButton;
    private CheckBox termsCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth y Realtime Database
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("perfiles");

        // Inicializar One Tap Google Sign-In
        oneTapClient = Identity.getSignInClient(this);

        // Referencias a la UI
        emailField = findViewById(R.id.emailOrPhone);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        termsCheckbox = findViewById(R.id.termsCheckbox);

        // Botón de inicio de sesión con Google
        googleSignInButton.setOnClickListener(v -> loginWithGoogle());

        // Botón de inicio de sesión con Email/Contraseña
        loginButton.setOnClickListener(v -> loginWithEmail());
    }

    private void loginWithGoogle() {
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> startActivityForResult(result.getPendingIntent().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0))
                .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();

                if (idToken != null) {
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    mAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        checkUserInDatabase(user);
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Error de autenticación con Google", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } catch (Exception e) {
                Log.e("GoogleSignIn", "Error en la autenticación", e);
            }
        }
    }

    private void checkUserInDatabase(FirebaseUser user) {
        String email = user.getEmail();
        String userId = user.getUid();

        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // Usuario ya registrado, redirigir a PaginaPrincipal
                goToHomePage();
            } else {
                // Nuevo usuario, guardarlo en la base de datos
                databaseReference.child(userId).setValue(new PerfilModelo(user.getDisplayName(), "", "", email, user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : ""))
                        .addOnCompleteListener(saveTask -> {
                            if (saveTask.isSuccessful()) {
                                goToHomePage();
                            } else {
                                Toast.makeText(MainActivity.this, "Error al guardar usuario", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void goToHomePage() {
        Intent intent = new Intent(MainActivity.this, PaginaPrincipal.class);
        startActivity(intent);
        finish();
    }
}
