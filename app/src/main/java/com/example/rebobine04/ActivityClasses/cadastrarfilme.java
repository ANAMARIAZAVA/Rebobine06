package com.example.rebobine04.ActivityClasses;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rebobine04.R;

public class CadastrarFilmeActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;

    // Campos de entrada para cadastrar filmes
    private EditText editTextNomeFilme, editTextCategoriaFilme, editTextDescricaoFilme, editTextAnoLancamento;
    private Button btnSalvarFilme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrarfilme);

        // Inicializando os campos de entrada
        editTextNomeFilme = findViewById(R.id.editTextNomeFilme);
        editTextCategoriaFilme = findViewById(R.id.editTextCategoriaFilme);
        editTextDescricaoFilme = findViewById(R.id.editTextDescricaoFilme);
        editTextAnoLancamento = findViewById(R.id.editTextAnoLancamento);
        btnSalvarFilme = findViewById(R.id.buttonConfirmarCadastro);

        // Configurando evento do botão de salvar
        btnSalvarFilme.setOnClickListener(view -> salvarFilme());
    }

    // Método para salvar o filme no banco de dados
    private void salvarFilme() {
        try {
            // Abre ou cria o banco de dados
            bancoDados = openOrCreateDatabase("rebobine", MODE_PRIVATE, null);

            // Comando SQL para inserir os dados
            String sql = "INSERT INTO filmes (nome, categoria, descricao, ano_lancamento) VALUES (?, ?, ?, ?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            // Pegando os valores dos campos de entrada
            stmt.bindString(1, editTextNomeFilme.getText().toString());
            stmt.bindString(2, editTextCategoriaFilme.getText().toString());
            stmt.bindString(3, editTextDescricaoFilme.getText().toString());
            stmt.bindLong(4, Integer.parseInt(editTextAnoLancamento.getText().toString()));

            // Executando o comando SQL
            stmt.executeInsert();

            // Mostra uma mensagem de sucesso
            Toast.makeText(this, "Filme cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

            // Limpa os campos após o cadastro
            editTextNomeFilme.setText("");
            editTextCategoriaFilme.setText("");
            editTextDescricaoFilme.setText("");
            editTextAnoLancamento.setText("");

            // Fecha o banco de dados
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao cadastrar filme!", Toast.LENGTH_SHORT).show();
        }
    }
}
