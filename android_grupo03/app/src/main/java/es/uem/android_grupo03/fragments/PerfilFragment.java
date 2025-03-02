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

import es.uem.android_grupo03.R;

public class PerfilFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView fotoPerfil;
    private EditText editarNombreUsuario, editarDireccion;
    private TextView cargarCorreo;
    private Switch toggleNewsletter;
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

        // Inicializar el ActivityResultLauncher para la cámara
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

    // Método para abrir la cámara
    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
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
}
