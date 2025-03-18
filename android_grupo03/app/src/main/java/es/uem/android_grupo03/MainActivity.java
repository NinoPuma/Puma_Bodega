package es.uem.android_grupo03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton, btnGoogle;
    private TextView createAccount;
    private CheckBox termsCheckbox;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Vincular elementos de la UI
        emailField = findViewById(R.id.emailOrPhone);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        btnGoogle = findViewById(R.id.btn_google);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        createAccount = findViewById(R.id.createAccount); // Botón de Crear Cuenta Nueva

        // Evento de clic para iniciar sesión con correo y contraseña
        loginButton.setOnClickListener(v -> iniciarSesionConCorreo());

        // Evento de clic para iniciar sesión con Google
        btnGoogle.setOnClickListener(v -> iniciarSesionConGoogle());

        // Evento de clic para ir a la pantalla de registro
        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);
            startActivity(intent);
        });
    }

    private void iniciarSesionConCorreo() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que el usuario acepte los términos y condiciones
        if (!termsCheckbox.isChecked()) {
            Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iniciar sesión con Firebase Auth
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(this, "Bienvenido, " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        // Redirigir a la pantalla principal
                        Intent intent = new Intent(MainActivity.this, PaginaPrincipal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Error: Verifica tus credenciales", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void iniciarSesionConGoogle() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Redirigir a la pantalla principal
            Intent intent = new Intent(MainActivity.this, PaginaPrincipal.class);
            startActivity(intent);
            finish();
        } else {
            if (response != null && response.getError() != null) {
                int errorCode = response.getError().getErrorCode();
                System.out.println("Error de autenticación: " + errorCode);
            }
        }
    }
}
