package com.example.receitasja.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.example.receitasja.R;
import com.example.receitasja.activity.PerfilActivity;
import com.example.receitasja.adapter.PesquisaAdapter;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.RecyclerItemClickListener;
import com.example.receitasja.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaFragment extends Fragment {

    private SearchView searchViewPesquisa;
    private RecyclerView recyclerPesquisa;
    private List<Usuario> listaUsuario;
    private DatabaseReference usuarioRef;
    private PesquisaAdapter adapterPesquisa;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaFragment newInstance(String param1, String param2) {
        PesquisaFragment fragment = new PesquisaFragment();
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
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchViewPesquisa = view.findViewById(R.id.searchViewPesquisa);
        recyclerPesquisa = view.findViewById(R.id.recyclerPesquisa);

        listaUsuario = new ArrayList<>();
        usuarioRef = ConfiguracaoFirebase.getFirebase().child("usuarios");

        recyclerPesquisa.setHasFixedSize(true);
        recyclerPesquisa.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterPesquisa = new PesquisaAdapter(listaUsuario, getActivity());
        recyclerPesquisa.setAdapter(adapterPesquisa);

        recyclerPesquisa.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerPesquisa, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Usuario usuarioClick = listaUsuario.get(position);
                Intent intent = new Intent(getActivity(), PerfilActivity.class);
                intent.putExtra("usuarioClick", usuarioClick);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        searchViewPesquisa.setQueryHint("Pesquisar usuários");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("onQueryTextSubmit", "pesquisou: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //.d("onQueryTextChange", "pesquisou: " + newText);
                String textoDigitado = newText.toUpperCase();
                pesquisarUsuarios(textoDigitado);
                return true;
            }
        });

        return view;
    }

    private void pesquisarUsuarios(String texto){

        listaUsuario.clear();

        if (texto.length() >= 2) {

            Query query = usuarioRef.orderByChild("nome").startAt(texto).endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    listaUsuario.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        listaUsuario.add(dataSnapshot.getValue(Usuario.class));
                    }
                    adapterPesquisa.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}