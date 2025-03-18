package es.uem.android_grupo03.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.uem.android_grupo03.MainActivity;
import es.uem.android_grupo03.R;
import java.util.HashMap;
import java.util.Map;

public class PerfilFragment extends Fragment {

    private ImageView fotoPerfil;
    private EditText editarNombreUsuario, editarDireccion, editarCodigoPostal;
    private TextView cargarCorreo;
    private Switch toggleNewsletter;
    private Button botonGuardarCambios, botonCerrarSesion;

    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;

    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Referencias a los elementos de la vista
        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        editarNombreUsuario = view.findViewById(R.id.editarNombreUsuario);
        editarDireccion = view.findViewById(R.id.editarDireccion);
        editarCodigoPostal = view.findViewById(R.id.editarCodigoPostal);
        cargarCorreo = view.findViewById(R.id.cargarCorreo);
        botonGuardarCambios = view.findViewById(R.id.botonGuardarCambios);
        botonCerrarSesion = view.findViewById(R.id.botonCerrarSesion);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference("perfiles").child(currentUser.getUid());
            cargarDatosPerfil();
        } else {
            Toast.makeText(requireContext(), "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            cerrarSesion();
        }

        // Inicializar el lanzador de la cámara
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        fotoPerfil.setImageBitmap(bitmap);
                    }
                });

        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        abrirCamara();
                    } else {
                        Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                    }
                });

        botonGuardarCambios.setOnClickListener(v -> guardarCambiosPerfil());
        botonCerrarSesion.setOnClickListener(v -> cerrarSesion());

        // Botón para guardar cambios en Firebase
        botonGuardarCambios.setOnClickListener(v -> guardarDatosUsuario());

        // Botón para cerrar sesión
        botonCerrarSesion.setOnClickListener(v -> cerrarSesion());

        return view;
    }

    private void cargarDatosPerfil() {
        userRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String nombre = dataSnapshot.child("nombre").getValue(String.class);
                String direccion = dataSnapshot.child("direccion").getValue(String.class);
                String codigoPostal = dataSnapshot.child("codigoPostal").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                editarNombreUsuario.setText(nombre != null ? nombre : "");
                editarDireccion.setText(direccion != null ? direccion : "");
                editarCodigoPostal.setText(codigoPostal != null ? codigoPostal : "");
                cargarCorreo.setText(email != null ? email : "Correo no disponible");
            }
        }).addOnFailureListener(e ->
                Toast.makeText(requireContext(), "Error al cargar perfil", Toast.LENGTH_SHORT).show());
    }

    private void guardarCambiosPerfil() {
        String nuevoNombre = editarNombreUsuario.getText().toString().trim();
        String nuevaDireccion = editarDireccion.getText().toString().trim();
        String nuevoCodigoPostal = editarCodigoPostal.getText().toString().trim();

        if (nuevoNombre.isEmpty() || nuevaDireccion.isEmpty() || nuevoCodigoPostal.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef.child("nombre").setValue(nuevoNombre);
        userRef.child("direccion").setValue(nuevaDireccion);
        userRef.child("codigoPostal").setValue(nuevoCodigoPostal)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Error al actualizar perfil", Toast.LENGTH_SHORT).show());
    }

    private void cerrarSesion() {
        auth.signOut();
        startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finish();
    }

    private void solicitarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            abrirCamara();
        }
    }

    private void abrirCamara() {
        cameraLauncher.launch(null);
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
