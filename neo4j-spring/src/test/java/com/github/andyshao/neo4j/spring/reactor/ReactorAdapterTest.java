package com.github.andyshao.neo4j.spring.reactor;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReactorAdapterTest {

	@Test
	void convertMono() {
		CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
			return "123";
		});
		
		String ret = ReactorAdapter.convertMono(cf)
			.block();
		Assertions.assertThat(ret).isEqualTo("123");
	}

}
