package es.uem.android_grupo03.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.uem.android_grupo03.R;
import java.util.HashMap;
import java.util.Map;

public class PerfilFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView fotoPerfil;
    private EditText editarNombreUsuario, editarDireccion;
    private TextView cargarCorreo;
    private Switch toggleNewsletter;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String userId;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Referencias a los elementos de la vista
        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        editarNombreUsuario = view.findViewById(R.id.editarNombreUsuario);
        editarDireccion = view.findViewById(R.id.editarDireccion);
        cargarCorreo = view.findViewById(R.id.cargarCorreo);
        toggleNewsletter = view.findViewById(R.id.toggleNewsletter);
        Button botonFoto = view.findViewById(R.id.botonFoto);
        Button botonGuardarCambios = view.findViewById(R.id.botonGuardarCambios);
        Button botonCerrarSesion = view.findViewById(R.id.botonCerrarSesion);

        // Inicializar Firebase Auth y obtener UID del usuario actual
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();  // Obtener ID del usuario autenticado
            cargarCorreo.setText(currentUser.getEmail()); // Mostrar correo en la UI
            databaseReference = FirebaseDatabase.getInstance().getReference("perfiles").child(userId);
            cargarDatosUsuario();
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        // Configurar el ActivityResultLauncher para la cámara
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            fotoPerfil.setImageBitmap(imageBitmap);
                        }
                    }
                });

        // Botón para cambiar foto (Abrir Cámara)
        botonFoto.setOnClickListener(v -> solicitarPermisoCamara());

        // Botón para guardar cambios en Firebase
        botonGuardarCambios.setOnClickListener(v -> guardarDatosUsuario());

        // Botón para cerrar sesión
        botonCerrarSesion.setOnClickListener(v -> cerrarSesion());

        return view;
    }

    // Método para solicitar permisos antes de abrir la cámara
    private void solicitarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            abrirCamara();
        }
    }

    // Método para abrir la cámara sin usar resolveActivity()
    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (requireContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(getActivity(), "No se puede abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    // Manejar la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(getActivity(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Cargar datos del usuario desde Firebase
    private void cargarDatosUsuario() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String direccion = snapshot.child("direccion").getValue(String.class);
                    Boolean newsletter = snapshot.child("newsletter").getValue(Boolean.class);

                    if (nombre != null) editarNombreUsuario.setText(nombre);
                    if (direccion != null) editarDireccion.setText(direccion);
                    if (newsletter != null) toggleNewsletter.setChecked(newsletter);
                } else {
                    Toast.makeText(getContext(), "No se encontraron datos de usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Guardar datos del usuario en Firebase
    private void guardarDatosUsuario() {
        String nombre = editarNombreUsuario.getText().toString().trim();
        String direccion = editarDireccion.getText().toString().trim();
        boolean newsletter = toggleNewsletter.isChecked();

        if (nombre.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> datosActualizados = new HashMap<>();
        datosActualizados.put("nombre", nombre);
        datosActualizados.put("direccion", direccion);
        datosActualizados.put("newsletter", newsletter);

        databaseReference.updateChildren(datosActualizados)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Datos guardados", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al guardar datos", Toast.LENGTH_SHORT).show());
    }

    // Método para cerrar sesión
    private void cerrarSesion() {
        auth.signOut();
        Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        // Aquí puedes redirigir al usuario a la pantalla de inicio de sesión
    }
}
