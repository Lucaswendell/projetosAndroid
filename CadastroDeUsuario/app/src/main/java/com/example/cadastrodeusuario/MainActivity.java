package com.example.cadastrodeusuario;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Transition;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView lista;
    private List<Pessoa> p;
    private String layout_atual = "lista";
    private RecyclerView.ItemDecoration itemDecoration;
    private LinearLayoutManager layoutManager;
    private BD bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Usuarios cadastrados"); //Título da activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        //pega o recyclerview
        lista = (RecyclerView)findViewById(R.id.pessoas);
        bd = new BD(MainActivity.this);
        p = bd.selecinar(); //pega a lista de pessoas no banco
        layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false); //configura o layout
        itemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL); //configura o divider decoration
        lista.addItemDecoration(itemDecoration);
        lista.setLayoutManager(layoutManager);
        lista.setAdapter(new NossoAdapter(p, this));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CadastrarUser.class));
            }
        });
    }


   @Override
    protected void onResume() {
        super.onResume();
        p = bd.selecinar();
        if(!layout_atual.equals("grid")){
            lista.setAdapter(new NossoAdapter(p,MainActivity.this));
            lista.setLayoutManager(layoutManager);
        }else{
            lista.removeItemDecoration(itemDecoration);
            lista.setAdapter(new NossoAdapter_grid(p,MainActivity.this));
            lista.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(layout_atual.equals("grid")){
                item.setIcon(R.drawable.ic_grid_on_black_24dp);
                layout_atual = "lista";
                lista.setAdapter(new NossoAdapter(p,MainActivity.this));
                lista.addItemDecoration(itemDecoration);
                lista.setLayoutManager(layoutManager);
            }else{
                item.setIcon(R.drawable.ic_view_list_black_24dp);
                layout_atual = "grid";
                lista.setAdapter(new NossoAdapter_grid(p,MainActivity.this));
                lista.removeItemDecoration(itemDecoration);
                lista.setLayoutManager(new GridLayoutManager(this, 2));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
