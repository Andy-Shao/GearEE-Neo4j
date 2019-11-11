package com.github.andyshao.neo4j.spring.reactor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.github.andyshao.util.CollectionOperation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Nov 11, 2019<br>
 * Encoding: UNIX UTF-8
 * 
 * @author Andy.Shao
 *
 */
public final class ReactorAdapter {
	private ReactorAdapter() {}
	
	public static final <T> Mono<T> convertMono(CompletionStage<T> it) {
		return Mono.create(ms -> {
			it.whenCompleteAsync((ret, ex) -> {
				if(Objects.nonNull(ex)) ms.error(ex);
				else ms.success(ret);
			});
		});
	}
	
	public static final <T> Flux<T> convertFlux(CompletionStage<T> it) {
		return convertMono(it).flux();
	}
	
	public static final <T> Flux<T> convertList(CompletionStage<List<T>> it) {
		return Flux.create(fs -> {
			it.whenCompleteAsync((ls, ex) -> {
				if(Objects.nonNull(ex)) fs.error(ex);
				else {
					if(CollectionOperation.isEmptyOrNull(ls)) {
						fs.complete();
					} 
					else {
						ls.forEach(ret -> fs.next(ret));
						fs.complete();
					}
				}
			});
		});
	}
	
	public static final <T> Mono<T> convertOptionalMono(CompletionStage<Optional<T>> it) {
		return Mono.create(ms -> {
			it.whenCompleteAsync((ret, ex) -> {
				if(Objects.nonNull(ex)) ms.error(ex);
				else {
					if(ret.isPresent()) {
						ms.success(ret.get());
					}
					else ms.success();
				}
			});
		});
	}
	
	public static final <T> Flux<T> convertOptionalFlux(CompletionStage<Optional<T>> it) {
		return convertOptionalMono(it).flux();
	}
}
