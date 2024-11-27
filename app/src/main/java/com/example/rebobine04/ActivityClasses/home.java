package com.example.rebobine04.ActivityClasses;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rebobine04.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private EditText edtBuscarFilme;
    private Button btnCadastrarFilme, btnCadastrarCliente, btnBuscar;
    private GridView listViewFilmes;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaFilmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edtBuscarFilme = findViewById(R.id.edtBuscarFilme);
        btnCadastrarFilme = findViewById(R.id.btnCadastrarFilme);
        btnCadastrarCliente = findViewById(R.id.btnCadastrarCliente);
        btnBuscar = findViewById(R.id.btnBuscar);
        listViewFilmes = findViewById(R.id.listviewfilmes);

        configurarBancoDados();

        // Configurando o clique do botão de cadastrar filme
        btnCadastrarFilme.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CadastrarFilmeActivity.class);
            startActivity(intent);
        });

        // Configurando o clique do botão de cadastrar cliente
        btnCadastrarCliente.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CadastrarClienteActivity.class);
            startActivity(intent);
        });

        // Configurando o botão de busca
        btnBuscar.setOnClickListener(v -> buscarFilmes(edtBuscarFilme.getText().toString()));

        // Listando os filmes ao iniciar a tela
        listarFilmes();
    }

    private void configurarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("rebobine", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS filmes (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, ano_lancamento INTEGER, diretor TEXT, descricao TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarFilmes() {
        try {
            listaFilmes = new ArrayList<>();
            Cursor cursor = bancoDados.rawQuery("SELECT id, nome FROM filmes", null);

            while (cursor.moveToNext()) {
                listaFilmes.add(cursor.getString(1)); // Nome do filme
            }
            cursor.close();

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaFilmes);
            listViewFilmes.setAdapter(adapter);

            // Configurando o clique no item da lista
            listViewFilmes.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(HomeActivity.this, FilmeActivity.class);
                // Passa o ID do filme para a próxima activity
                intent.putExtra("filmeId", id);
                startActivity(intent);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarFilmes(String termoBusca) {
        try {
            listaFilmes.clear();
            Cursor cursor = bancoDados.rawQuery("SELECT id, nome FROM filmes WHERE nome LIKE ?", new String[]{"%" + termoBusca + "%"});

            while (cursor.moveToNext()) {
                listaFilmes.add(cursor.getString(1));
            }
            cursor.close();

            if (listaFilmes.isEmpty()) {
                Toast.makeText(this, "Nenhum filme encontrado!", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
