package com.example.receitasja.adapter;

import android.content.Context;
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
import com.example.receitasja.model.Feed;
import com.example.receitasja.model.PostagemReceita;

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
        Glide.with(context).load(uriFotoUsuario).into(holder.fotoPerfil);
        Glide.with(context).load(uriFotoPostagem).into(holder.imagePostagem);

        holder.nome.setText(feed.getNomeUsuario());
        holder.ingredientes.setText(feed.getIngredientes());
        holder.titulo.setText(feed.getNomeReceita());
    }

    @Override
    public int getItemCount() {
        return listFeed.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        CircleImageView fotoPerfil;
        TextView nome, ingredientes, titulo;
        ImageView imagePostagem;
        Button favoritar, comentar, adicionar;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            fotoPerfil = itemView.findViewById(R.id.imagePerfilFeed);
            nome = itemView.findViewById(R.id.textNomeFeed);
            ingredientes = itemView.findViewById(R.id.textIngredientesFeed);
            titulo = itemView.findViewById(R.id.textTituloReceitaFeed);
            imagePostagem = itemView.findViewById(R.id.imagePostagemFeed);
            favoritar = itemView.findViewById(R.id.buttonFavoritoFeed);
            comentar = itemView.findViewById(R.id.buttonComentarFeed);
            adicionar = itemView.findViewById(R.id.buttonAdicionarFeed);
        }
    }
}
