package com.example.aulapoo2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {
    private LinearLayout container;
    private ApiClient apiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        container = findViewById(R.id.containerProdutos);
        Button btnNovoProduto = findViewById(R.id.btnNovoProduto);
        apiClient = new ApiClient(this);

        btnNovoProduto.setOnClickListener(v -> abrirFormulario(null));
        carregarProdutos();
    }

    private void carregarProdutos(){
        mostrarMensagem("Carregando produtos...");
        apiClient.listarProdutos(this::criarLista, this::mostrarErro);
    }

    private void criarLista(List<Produto> produtos){
        container.removeAllViews();
        if(produtos.isEmpty()){
            mostrarMensagem("Nenhum produto cadastrado");
            return;
        }
        for(Produto produto : produtos){
            View item = getLayoutInflater().inflate(R.layout.item_produto, container, false);
            ImageView img = item.findViewById(R.id.imgProduto);
            TextView txtNome = item.findViewById(R.id.txtNomeProduto);
            TextView txtPreco = item.findViewById(R.id.txtPrecoProduto);

            img.setImgResource(R.drawable.hamburguer);
            txtNome.setText(produto.getNome() + "(" produto.getQuantidade() + ")");
            txtPreco.setText(String.format(Locale.getDefault(), "R$ %2.f", produto.getPreco()));

            item.setOnClickListener( v-> apiClient.buscarProduto(produto.getId(), this::abrirFormulario, this::mostrarErro));

            item.setOnLongClickListener( v-> {
                confirmarExclusao(produto);
                return true;
            });
            container.addView(item);
        }
    }

}