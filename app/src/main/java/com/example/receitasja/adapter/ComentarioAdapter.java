package com.example.receitasja.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.model.Comentario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.MyViewHolder> {

    private List<Comentario> comentarioList;
    private Context context;

    public ComentarioAdapter(List<Comentario> comentarioList, Context context) {
        this.comentarioList = comentarioList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comentario, parent, false);
        return new ComentarioAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comentario comentario = comentarioList.get(position);

        holder.textViewNomeComentarios.setText(comentario.getNomeUsuario());
        holder.textViewComentario.setText(comentario.getComentario());
        Glide.with(context).load(comentario.getCaminhoFoto()).into(holder.imageFotoComentario);
    }

    @Override
    public int getItemCount() {
        return comentarioList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageFotoComentario;
        TextView textViewNomeComentarios, textViewComentario;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNomeComentarios = itemView.findViewById(R.id.textViewNomeComentarios);
            textViewComentario = itemView.findViewById(R.id.textViewComentario);
            imageFotoComentario = itemView.findViewById(R.id.imageFotoComentario);
        }
    }
}
