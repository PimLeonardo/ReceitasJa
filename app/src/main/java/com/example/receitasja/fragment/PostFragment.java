package com.example.receitasja.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.receitasja.R;
import com.example.receitasja.activity.EditarPerfilActivity;
import com.example.receitasja.activity.PostagemActivity;
import com.example.receitasja.helper.Permissao;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private String cameraGaleria;
    private Button abrirCamera,abrirGaleria;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String[] permissoesPost = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Permissao.validarPermissoes(permissoesPost, getActivity(), 1);

        abrirCamera = view.findViewById(R.id.abrirCamera);
        abrirGaleria = view.findViewById(R.id.abrirGaleria);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if ( result.getResultCode() == getActivity().RESULT_OK && result.getData() != null){

                Bitmap imagem = null;

                try {

                    if (cameraGaleria == "camera") {

                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        imagem = bitmap;
                    }if (cameraGaleria == "galeria") {

                        Uri localImagemSelecionada = result.getData().getData();
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                    }if (imagem != null){

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        imagem.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                        byte[] dadosimagem = byteArrayOutputStream.toByteArray();

                        Intent intent = new Intent(getActivity(), PostagemActivity.class);
                        intent.putExtra("fotoSelecionada",dadosimagem);
                        startActivity(intent);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        abrirCamera.setOnClickListener(v -> {
            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                activityResultLauncher.launch(intent);
                cameraGaleria = "camera";
            }else {
                Toast.makeText(getContext(),"Esse app não tem suporte para essa ação", Toast.LENGTH_SHORT).show();
            }
        });

        abrirGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                activityResultLauncher.launch(intent);
                cameraGaleria = "galeria";
            }else {
                Toast.makeText(getContext(),"Esse app não tem suporte para essa ação", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}