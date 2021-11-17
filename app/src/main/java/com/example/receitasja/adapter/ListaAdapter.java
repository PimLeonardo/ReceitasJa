package com.example.receitasja.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.activity.AbrirPostagemActivity;
import com.example.receitasja.activity.CadastroActivity;
import com.example.receitasja.model.Lista;
import com.example.receitasja.model.PostagemReceita;
import com.example.receitasja.model.Usuario;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MyViewHolder> {

    private List<Lista> minhaLista;
    private Context context;

    public ListaAdapter(List<Lista> minhaLista, Context context) {
        this.minhaLista = minhaLista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);
        return new ListaAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Lista lista = minhaLista.get(position);

        holder.textViewNomeReceitaLista.setText(lista.getNomeReceita());
        Glide.with(context).load(lista.getFotoPostagem()).into(holder.imageFotoReceitaLista);

        Usuario usuario = new Usuario();
        usuario.setCaminhoFoto(lista.getFotoUsuario());
        usuario.setNome("");

        PostagemReceita postagemReceita = new PostagemReceita();
        postagemReceita.setCaminhoFoto(lista.getFotoPostagem());
        postagemReceita.setTextReceita(lista.getReceita());
        postagemReceita.setTextIngredientes(lista.getIngredientes());
        postagemReceita.setTextNomeReceita(lista.getNomeReceita());
        postagemReceita.setId(lista.getId());
        postagemReceita.setIdUsuario(lista.getIdUsuario());

        holder.imageFotoReceitaLista.setOnClickListener(v -> {
            Intent intent = new Intent(context, AbrirPostagemActivity.class);
            intent.putExtra("postagem", postagemReceita);
            intent.putExtra("usuario", usuario);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return minhaLista.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFotoReceitaLista;
        TextView textViewNomeReceitaLista;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFotoReceitaLista = itemView.findViewById(R.id.imageFotoReceitaLista);
            textViewNomeReceitaLista = itemView.findViewById(R.id.textViewNomeReceitaLista);
        }
    }
}
