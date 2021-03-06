package com.example.receitasja.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.activity.AbrirPostagemActivity;
import com.example.receitasja.activity.EditarPerfilActivity;
import com.example.receitasja.activity.ListaActivity;
import com.example.receitasja.activity.PerfilActivity;
import com.example.receitasja.adapter.GridAdapter;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.UsuarioFirebase;
import com.example.receitasja.model.PostagemReceita;
import com.example.receitasja.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    private CircleImageView imagePerfilFoto;
    public GridView gridViewPerfil;
    private TextView textSeguidores, textSeguindo, nomePerfil;
    private Button buttonEditarSeguirPerfil, buttonCriarLista;
    private Usuario usuarioLogado;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference postagensUsuarioRef;
    private ValueEventListener valueEventListenerPerfil;
    private GridAdapter gridAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        usuarioRef = ConfiguracaoFirebase.getFirebase().child("usuarios");
        postagensUsuarioRef = ConfiguracaoFirebase.getFirebase().child("postagens").child(usuarioLogado.getId());

        iniciarComponentes(view);

        buttonEditarSeguirPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
            startActivity(intent);
        });

        buttonCriarLista.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListaActivity.class);
            startActivity(intent);
        });


        iniciarImageLoader();

        carregarPostagem();

        return view;
    }

    public void iniciarImageLoader() {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getActivity()).memoryCache(new LruMemoryCache(2*1024*1024))
                .memoryCacheSize(2*1024*1024).diskCacheSize(50*1024*1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();
        ImageLoader.getInstance().init(configuration);
    }

    public void carregarPostagem() {

        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> urlFotos = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    PostagemReceita postagemReceita = dataSnapshot.getValue(PostagemReceita.class);
                    urlFotos.add(postagemReceita.getCaminhoFoto());
                }
                Collections.reverse(urlFotos);
                gridAdapter = new GridAdapter(getActivity(),R.layout.grid_perfil,urlFotos);
                gridViewPerfil.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void iniciarComponentes(View view) {
        gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        imagePerfilFoto = view.findViewById(R.id.imagePerfilFoto);
        buttonEditarSeguirPerfil = view.findViewById(R.id.buttonEditarSeguirPerfil);
        textSeguidores = view.findViewById(R.id.numeroSeguidoresPerfil);
        textSeguindo = view.findViewById(R.id.numeroSeguindoPerfil);
        nomePerfil = view.findViewById(R.id.nomePerfil);
        buttonCriarLista = view.findViewById(R.id.buttonCriarLista);
    }

    private void recuperarDadosUsuario() {

        usuarioLogadoRef = usuarioRef.child(usuarioLogado.getId());
        valueEventListenerPerfil = usuarioLogadoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue(Usuario.class);
                FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuario();

                String seguindo = String.valueOf(usuario.getSeguindo());
                String seguidores = String.valueOf(usuario.getSeguidores());

                textSeguidores.setText(seguidores);
                textSeguindo.setText(seguindo);
                nomePerfil.setText(usuarioPerfil.getDisplayName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void atualizarFoto() {

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        String caminhoFoto = usuarioLogado.getCaminhoFoto();
        if (!caminhoFoto.isEmpty()) {
            Uri url = Uri.parse(caminhoFoto);
            Glide.with(getActivity()).load(url).into(imagePerfilFoto);
        }else {
            imagePerfilFoto.setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDadosUsuario();
        atualizarFoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioLogadoRef.removeEventListener(valueEventListenerPerfil);
    }
}