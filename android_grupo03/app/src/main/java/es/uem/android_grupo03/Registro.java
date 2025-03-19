package es.uem.android_grupo03;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput, nombreInput, direccionInput, codigoPostalInput;
    private MaterialButton btnRegistro;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("perfiles");

        // Vincular elementos de la UI con sus respectivos IDs en el layout
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        nombreInput = findViewById(R.id.nombreInput);
        direccionInput = findViewById(R.id.direccionInput);
        codigoPostalInput = findViewById(R.id.codigoPostalInput);
        btnRegistro = findViewById(R.id.btnRegistro);

        // Verificar si el botón de registro está correctamente referenciado
        if (btnRegistro != null) {
            btnRegistro.setOnClickListener(v -> registrarUsuario());
        } else {
            Toast.makeText(this, "Error: No se encontró el botón de registro", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarUsuario() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String nombre = nombreInput.getText().toString().trim();
        String direccion = direccionInput.getText().toString().trim();
        String codigoPostal = codigoPostalInput.getText().toString().trim();

        // Validar campos vacíos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nombre) ||
                TextUtils.isEmpty(direccion) || TextUtils.isEmpty(codigoPostal)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            guardarDatosEnDatabase(user.getUid(), email, nombre, direccion, codigoPostal);
                        }
                    } else {
                        Toast.makeText(this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarDatosEnDatabase(String userId, String email, String nombre, String direccion, String codigoPostal) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("nombre", nombre);
        userData.put("direccion", direccion);
        userData.put("codigoPostal", codigoPostal);

        databaseRef.child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Registro.this, "Registro exitoso, inicia sesión", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registro.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(Registro.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
