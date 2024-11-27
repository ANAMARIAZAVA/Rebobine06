package com.example.rebobine04.ActivityClasses;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rebobine04.R;

public class cadastrocliente extends AppCompatActivity {

    private EditText nomeRegister, campoTelefoneFunc, emailRegister, campoCPFFunc;
    private Button btCadastrar;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ativando a funcionalidade Edge-to-Edge
        EdgeToEdge.enable(this);

        // Definindo o layout da tela
        setContentView(R.layout.activity_cadastrocliente);

        // Ajuste de padding para as barras de sistema (status e navegação)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cadastrocliente), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializando componentes da tela
        nomeRegister = findViewById(R.id.nomeRegister);
        campoTelefoneFunc = findViewById(R.id.campoTelefoneFunc);
        emailRegister = findViewById(R.id.emailRegister);
        campoCPFFunc = findViewById(R.id.campoCPFFunc);
        btCadastrar = findViewById(R.id.btCadastrar);

        // Criar ou abrir o banco de dados
        bancoDados = openOrCreateDatabase("rebobine", MODE_PRIVATE, null);

        // Criar tabela, se ainda não existir
        criarTabelaCliente();

        // Configurando o evento de clique para cadastrar cliente
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarCliente();
            }
        });
    }

    // Método para criar a tabela de clientes
    private void criarTabelaCliente() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "telefone TEXT, " +
                    "email TEXT, " +
                    "cpf TEXT UNIQUE" +
                    ");";
            bancoDados.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao criar tabela!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para cadastrar cliente
    private void cadastrarCliente() {
        // Obtendo os dados inseridos pelo usuário
        String nome = nomeRegister.getText().toString();
        String telefone = campoTelefoneFunc.getText().toString();
        String email = emailRegister.getText().toString();
        String cpf = campoCPFFunc.getText().toString();

        // Verificando se os campos obrigatórios estão preenchidos
        if (nome.isEmpty() || cpf.isEmpty()) {
            Toast.makeText(this, "Nome e CPF são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inserir os dados no banco de dados
        try {
            ContentValues valores = new ContentValues();
            valores.put("nome", nome);
            valores.put("telefone", telefone);
            valores.put("email", email);
            valores.put("cpf", cpf);

            long resultado = bancoDados.insert("clientes", null, valores);

            if (resultado != -1) {
                Toast.makeText(this, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                limparCampos();
            } else {
                Toast.makeText(this, "Erro ao cadastrar cliente.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Método para limpar os campos após cadastro
    private void limparCampos() {
        nomeRegister.setText("");
        campoTelefoneFunc.setText("");
        emailRegister.setText("");
        campoCPFFunc.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bancoDados != null) {
            bancoDados.close();
        }
    }
}
