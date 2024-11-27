package com.example.rebobine04.ActivityClasses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rebobine04.R;

public class FilmeActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private TextView textNomeFilme, textCategoriaFilme, textAnoLancamento, textDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);

        // Inicializando os TextViews
        textNomeFilme = findViewById(R.id.textNomeFilme);
        textCategoriaFilme = findViewById(R.id.textCategoriaFilme);
        textAnoLancamento = findViewById(R.id.textAnoLancamento);
        textDescricao = findViewById(R.id.textDescricao); // Adicionei para mostrar a descrição

        // Configurar o banco de dados
        configurarBancoDados();

        // Receber o ID do filme da tela anterior
        long filmeId = getIntent().getLongExtra("filmeId", -1);

        if (filmeId != -1) {
            mostrarDetalhesFilme(filmeId);
        }
    }

    private void configurarBancoDados() {
        bancoDados = openOrCreateDatabase("rebobine", MODE_PRIVATE, null);
    }

    private void mostrarDetalhesFilme(long filmeId) {
        Cursor cursor = bancoDados.rawQuery("SELECT * FROM filmes WHERE id = ?", new String[]{String.valueOf(filmeId)});

        if (cursor.moveToFirst()) {
            String nomeFilme = cursor.getString(cursor.getColumnIndex("nome"));
            String diretor = cursor.getString(cursor.getColumnIndex("diretor"));
            int anoLancamento = cursor.getInt(cursor.getColumnIndex("ano_lancamento"));
            String descricao = cursor.getString(cursor.getColumnIndex("descricao"));

            // Exibir os dados na interface
            textNomeFilme.setText("Nome do Filme: " + nomeFilme);
            textCategoriaFilme.setText("Diretor: " + diretor);
            textAnoLancamento.setText("Ano de Lançamento: " + anoLancamento);
            textDescricao.setText("Descrição: " + descricao);
        }
        cursor.close();
    }
}
