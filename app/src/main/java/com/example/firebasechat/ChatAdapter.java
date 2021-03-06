package com.example.firebasechat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    AppCompatActivity appCompatActivity;
    ArrayList<ChatMessage> lista;
    Context contexto;
    private static LayoutInflater inflater= null;


    public ChatAdapter(ChatActivity chatActivity, ArrayList<ChatMessage> lista) {
        this.lista = lista;
        this.contexto = chatActivity;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView nombre;
        TextView fechaHora;
        TextView texto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        Log.i("nombre",user.getDisplayName());
        String nombreUsuarioActual = user.getDisplayName();

        Holder holder = new Holder();
        View fila;
        if(nombreUsuarioActual.equals(lista.get(position).getMessageUser())) {
            fila = inflater.inflate(R.layout.message_right,null);
        }
        else {
            fila = inflater.inflate(R.layout.message_left,null);
        }

        holder.nombre = (TextView) fila.findViewById(R.id.message_user);
        holder.fechaHora = (TextView) fila.findViewById(R.id.message_time);
        holder.texto = (TextView) fila.findViewById(R.id.message_text);
        holder.nombre.setText(lista.get(position).getMessageUser());
        holder.fechaHora.setText(lista.get(position).getMessageTimeString());
        holder.texto.setText(lista.get(position).getMessageText());

        return fila;
    }
}
