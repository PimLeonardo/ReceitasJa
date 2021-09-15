package com.example.receitasja.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receitasja.R;

public class PostagemAdapter extends RecyclerView.Adapter<PostagemAdapter.MyViewholder> {


    @NonNull
    @Override
    public PostagemAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemPost = LayoutInflater.from(parent.getContext()).inflate(R.layout.postagem_detalhe, parent, false);

        return new MyViewholder(itemPost);

    }

    @Override
    public void onBindViewHolder(@NonNull PostagemAdapter.MyViewholder holder, int position) {

        holder.textNome.setText("Leonardo Pimentel");
        holder.textPostagem.setText("Prato muito delicioso e crocante");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        private TextView textNome, textPostagem;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);
            textPostagem = itemView.findViewById(R.id.textPostagem);
        }
    }
}
