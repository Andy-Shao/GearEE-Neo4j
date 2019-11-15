package com.github.andyshao.spring.reactor;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import reactor.core.publisher.Mono;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Nov 15, 2019<br>
 * Encoding: UNIX UTF-8
 * 
 * @author Andy.Shao
 *
 */
public final class MonoOperation {
	private MonoOperation() {}

	public static final <T> Mono<T> fromCompletionStage(CompletionStage<T> it) {
		return Mono.create(ms -> {
			it.whenCompleteAsync((ret, ex) -> {
				if(Objects.nonNull(ex)) ms.error(ex);
				else ms.success(ret);
			});
		});
	}
	
	public static final <T> Mono<T> fromOptional(Optional<T> op) {
		return op.isPresent() ? Mono.create(ms -> ms.success(op.get())) : Mono.empty();
	}
}
