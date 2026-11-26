package com.erp.pedido.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import com.erp.pedido.domain.ItemPedido;
import com.erp.pedido.domain.Pedido;

public class RequestHashUtil {
    public static String gerarHash(Pedido pedido){
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        var conteudo = new StringBuilder();
        conteudo.append("cliente:").append(pedido.getCliente().getId());

        for (ItemPedido item : pedido.getItens()) {
            conteudo.append("|produto:").append(item.getProduto().getId())
                    .append(",quantidade:").append(item.getQuantidade());
        }
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(conteudo.toString().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível", e);
        }
       
    }
}
