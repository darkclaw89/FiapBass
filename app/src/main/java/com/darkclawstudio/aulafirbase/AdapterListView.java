package com.darkclawstudio.aulafirbase;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by leand on 18/03/2017.
 */

public class AdapterListView extends RecyclerView.Adapter<AdapterListView.ViewHolder> {

    private ArrayList<ItemCompraDAO> mDataset;

    @Override
    public AdapterListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recyclerview_row,parent,false);

        return new ViewHolder(inflatedView);
    }

    public AdapterListView(){
        mDataset = new ArrayList<ItemCompraDAO>();
    }

    public void insertData(ItemCompraDAO item){
        mDataset.add(item);
    }

    public void clearData(){
        mDataset = null;
        mDataset = new ArrayList<ItemCompraDAO>();
    }

    @Override
    public void onBindViewHolder(AdapterListView.ViewHolder holder, int position) {
        ItemCompraDAO item = mDataset.get(position);
        holder.bindItemCompra(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imgThumb;
        private TextView lblNome;
        private TextView lblQuantidade;
        public ItemCompraDAO itemSelected;

        final FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference database = databaseInstance.getReference().child("Compras");

        public ViewHolder(View itemView) {
            super(itemView);

            imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
            lblNome = (TextView) itemView.findViewById(R.id.lblNome);
            lblQuantidade = (TextView) itemView.findViewById(R.id.lblQuantidade);
            itemView.setOnClickListener(this);

        }

        public void bindItemCompra(ItemCompraDAO item){
            itemSelected = item;
            lblNome.setText(item.nome);
            lblQuantidade.setText(String.valueOf(item.quantidade));
            Bitmap defaultImg = Bitmap.createBitmap(128,128, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(defaultImg);
            canvas.drawRGB(0,0,255);
            imgThumb.setImageBitmap(defaultImg);


        }
        @Override
        public void onClick(View v) {

            final Dialog updateDialog = new Dialog(v.getContext());
            updateDialog.setContentView(R.layout.layout_dialog_upd);
            updateDialog.setTitle("Atualizar");
            updateDialog.setCanceledOnTouchOutside(true);


            final EditText edtNome = (EditText) updateDialog.findViewById(R.id.edtNome);
            final EditText edtQuantidade = (EditText) updateDialog.findViewById(R.id.edtQuantidade);

            edtNome.setText(itemSelected.nome);
            edtQuantidade.setText(itemSelected.quantidade);

            Button btnUpdate = (Button) updateDialog.findViewById(R.id.btnUpdate);
            Button btnDelete = (Button) updateDialog.findViewById(R.id.btnDelete);
            Button btnCancel = (Button) updateDialog.findViewById(R.id.btnCancel);


            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.child(itemSelected.id).child("nome").setValue(edtNome.getText().toString());
                    database.child(itemSelected.id).child("quantidade").setValue(edtQuantidade.getText().toString());

                    updateDialog.dismiss();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.child(itemSelected.id).setValue(null);
                    updateDialog.dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDialog.dismiss();
                }
            });

            updateDialog.show();
        }

    }
}