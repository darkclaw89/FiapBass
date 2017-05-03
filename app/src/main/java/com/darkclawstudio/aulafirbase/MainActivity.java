package com.darkclawstudio.aulafirbase;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    final Context context = this;

    final FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
    DatabaseReference database = databaseInstance.getReference().child("Compras");
    DatabaseReference token   = databaseInstance.getReference().child("UserToken");
    FirebaseAnalytics analyticsInstance;

    //Interface
    FloatingActionMenu famMenu;
    FloatingActionButton fabAdicionar;
    FloatingActionButton fabConfigurar;

    EditText edtText;


    RecyclerView recyclerView;
    AdapterListView adapterRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    double timeStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeStart = System.currentTimeMillis();
        recyclerView = (RecyclerView) findViewById(R.id.recView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapterRecyclerView = new AdapterListView();
        recyclerView.setAdapter(adapterRecyclerView);
        analyticsInstance = FirebaseAnalytics.getInstance(this);
        String tokeId = FirebaseInstanceId.getInstance().getToken();
        if(tokeId != null){
            token.child(FirebaseInstanceId.getInstance().getToken()).setValue(true);
        }
        //edtText = (EditText) findViewById(R.id.edtEntrada);
        famMenu = (FloatingActionMenu) findViewById(R.id.famMenu);
        fabAdicionar = (FloatingActionButton) findViewById(R.id.fabAdicionar);
        fabConfigurar = (FloatingActionButton) findViewById(R.id.fabConfigurar);

        fabAdicionar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog addDialog = new Dialog(context);
                addDialog.setContentView(R.layout.layout_dialog_add);
                addDialog.setTitle("Adicionar");
                addDialog.setCanceledOnTouchOutside(true);


                final EditText edtNome = (EditText) addDialog.findViewById(R.id.edtNome);
                final EditText edtQuantidade = (EditText) addDialog.findViewById(R.id.edtQuantidade);

                Button btnOk = (Button) addDialog.findViewById(R.id.btnUpdate);
                Button btnCancel = (Button) addDialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onCreateData(edtNome.getText().toString(), edtQuantidade.getText().toString());
                        famMenu.close(true);
                        addDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDialog.dismiss();
                    }
                });

                addDialog.show();

            }
        });

        fabConfigurar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent config = new Intent(context,ConfigurationActivity.class);
                startActivity(config);
            }
        });

        onRetrieveData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        double timePassed = System.currentTimeMillis() - timeStart;
        Bundle params = new Bundle();
        params.putDouble("TimeOnApp",timePassed);
        analyticsInstance.logEvent("User_On",params);
    }

    public void onClickSearch(View v) {

    }


    private void onRetrieveData() {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapterRecyclerView.clearData();
                Iterator it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    ItemCompraDAO item = new ItemCompraDAO();
                    DataSnapshot data = (DataSnapshot) it.next();
                    item.id = data.getKey();
                    item.nome = (String) data.child("nome").getValue();
                    item.quantidade = (String) data.child("quantidade").getValue();
                    adapterRecyclerView.insertData(item);

                }
               adapterRecyclerView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void onCreateData(String nome, String quantidade) {

        String key = database.push().getKey();
        database.child(key).child("nome").setValue(nome);
        database.child(key).child("quantidade").setValue(quantidade);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Item Adicionado!").setTitle("Concluído");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertFinish = builder.create();
        alertFinish.show();

    }

}
