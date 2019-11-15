package com.github.andyshao.neo4j.spring.reactor;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.andyshao.spring.reactor.MonoOperation;

class ReactorAdapterTest {

	@Test
	void convertMono() {
		CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
			return "123";
		});
		
		String ret = MonoOperation.fromCompletionStage(cf)
			.block();
		Assertions.assertThat(ret).isEqualTo("123");
	}

}
