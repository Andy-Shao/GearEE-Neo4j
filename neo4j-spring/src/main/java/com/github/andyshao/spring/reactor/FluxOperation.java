package com.github.andyshao.spring.reactor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.andyshao.lang.ArrayWrapper;

import reactor.core.publisher.Flux;

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
public final class FluxOperation {
	private FluxOperation() {}

	public static final <T> Flux<T> fromCompletionStage(CompletionStage<T> it) {
		return MonoOperation.fromCompletionStage(it).flux();
	}
	
	public static final <T> Flux<T> fromStream(Stream<T> stream) {
		return Flux.create(fs -> {
			stream.forEach(it -> fs.next(it));
			fs.complete();
		});
	}
	
	public static final <T> Flux<T> fromIterator(Iterator<T> iterator) {
		return fromStream(StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false));
	}
	
	public static final <T> Flux<T> fromCollection(Collection<T> collection) {
		return fromStream(collection.stream());
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> Flux<T> fromArray(ArrayWrapper array) {
		return (Flux<T>) fromStream(array.stream());
	}
	
	public static final <T> Flux<T> fromArray(T[] array) {
		return fromArray(ArrayWrapper.wrap(array));
	}
	
	public static final Flux<Byte> fromArray(byte[] bs) {
		return fromArray(ArrayWrapper.wrap(bs));
	}
	
	public static final Flux<Character> fromArray(char[] cs) {
		return fromArray(ArrayWrapper.wrap(cs));
	}
	
	public static final Flux<Short> fromArray(short[] array) {
		return fromArray(ArrayWrapper.wrap(array));
	}
	
	public static final Flux<Integer> fromArray(int[] is) {
		return fromArray(ArrayWrapper.wrap(is));
	}
	
	public static final Flux<Long> fromArray(long[] array) {
		return fromArray(ArrayWrapper.wrap(array));
	}
	
	public static final Flux<Float> fromArray(float[] array) {
		return fromArray(ArrayWrapper.wrap(array));
	}
	
	public static final Flux<Double> fromArray(double[] array) {
		return fromArray(ArrayWrapper.wrap(array));
	}
}
