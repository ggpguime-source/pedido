package com.erp.pedido;

import org.springframework.boot.SpringApplication;

public class TestPedidoApplication {

	public static void main(String[] args) {
		SpringApplication.from(PedidoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
