package com.example.receitasja.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.activity.AbrirPostagemActivity;
import com.example.receitasja.activity.ComentariosActivity;
import com.example.receitasja.model.Feed;
import com.example.receitasja.model.PostagemReceita;
import com.example.receitasja.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostagemAdapter extends RecyclerView.Adapter<PostagemAdapter.MyViewholder> {

    private  List<Feed> listFeed;
    private Context context;

    public PostagemAdapter(List<Feed> postagensFeed, Context context) {
        this.listFeed = postagensFeed;
        this.context = context;
    }

    @NonNull
    @Override
    public PostagemAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_postagem, parent, false);
        return new PostagemAdapter.MyViewholder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull PostagemAdapter.MyViewholder holder, int position) {

        Feed feed = listFeed.get(position);

        Uri uriFotoUsuario = Uri.parse(feed.getFotoUsuario());
        Uri uriFotoPostagem = Uri.parse(feed.getFotoPostagem());

        Glide.with(context).load(uriFotoUsuario).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(holder.fotoPerfil);
        Glide.with(context).load(uriFotoPostagem).into(holder.imagePostagem);

        holder.nome.setText(feed.getNomeUsuario());
        holder.ingredientes.setText(feed.getIngredientes());
        holder.titulo.setText(feed.getNomeReceita());

        holder.comentar.setOnClickListener(v -> {
            Intent intent = new Intent(context,ComentariosActivity.class);
            intent.putExtra("idPostagem", feed.getId());
            context.startActivity(intent);
        });

        Usuario usuario = new Usuario();
        usuario.setCaminhoFoto(feed.getFotoUsuario());
        usuario.setNome(feed.getNomeUsuario());

        PostagemReceita postagemReceita = new PostagemReceita();
        postagemReceita.setCaminhoFoto(feed.getFotoPostagem());
        postagemReceita.setTextReceita(feed.getReceita());
        postagemReceita.setTextIngredientes(feed.getIngredientes());
        postagemReceita.setTextNomeReceita(feed.getNomeReceita());
        postagemReceita.setId(feed.getId());
        postagemReceita.setIdUsuario(feed.getIdUsuario());

        holder.adicionar.setOnClickListener(v -> {
            postagemReceita.adicionarLista();
        });

        holder.imagePostagem.setOnClickListener(v -> {
            Intent intent = new Intent(context, AbrirPostagemActivity.class);
            intent.putExtra("postagem", postagemReceita);
            intent.putExtra("usuario", usuario);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listFeed.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        CircleImageView fotoPerfil;
        TextView nome, ingredientes, titulo;
        ImageView imagePostagem;
        Button comentar, adicionar;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            fotoPerfil = itemView.findViewById(R.id.imagePerfilFeed);
            nome = itemView.findViewById(R.id.textNomeFeed);
            ingredientes = itemView.findViewById(R.id.textIngredientesFeed);
            titulo = itemView.findViewById(R.id.textTituloReceitaFeed);
            imagePostagem = itemView.findViewById(R.id.imagePostagemFeed);
            comentar = itemView.findViewById(R.id.buttonComentarFeed);
            adicionar = itemView.findViewById(R.id.buttonAdicionarFeed);
        }
    }
}
