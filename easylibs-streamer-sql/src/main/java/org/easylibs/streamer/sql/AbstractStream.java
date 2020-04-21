/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.easylibs.streamer.sql;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
abstract class AbstractStream<T> implements Stream<T> {

	private final Stream<T> delegate;

	public AbstractStream() {
		this.delegate = StreamSupport.stream(this::createSpliterator, 0, false);
	}

	public AbstractStream(Spliterator<T> spliterator) {
		this.delegate = StreamSupport.stream(spliterator, false);
	}

	public AbstractStream(Supplier<Spliterator<T>> spliterator) {
		this.delegate = StreamSupport.stream(spliterator, 0, false);
	}

	protected abstract Spliterator<T> createSpliterator();

	public boolean allMatch(Predicate<? super T> predicate) {
		return delegate.allMatch(predicate);
	}

	public boolean anyMatch(Predicate<? super T> predicate) {
		return delegate.anyMatch(predicate);
	}

	public void close() {
		delegate.close();
	}

	public <R, A> R collect(Collector<? super T, A, R> collector) {
		return delegate.collect(collector);
	}

	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		return delegate.collect(supplier, accumulator, combiner);
	}

	public long count() {
		return delegate.count();
	}

	public Stream<T> distinct() {
		return delegate.distinct();
	}

	public Stream<T> filter(Predicate<? super T> predicate) {
		return delegate.filter(predicate);
	}

	public Optional<T> findAny() {
		return delegate.findAny();
	}

	public Optional<T> findFirst() {
		return delegate.findFirst();
	}

	public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return delegate.flatMap(mapper);
	}

	public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		return delegate.flatMapToDouble(mapper);
	}

	public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		return delegate.flatMapToInt(mapper);
	}

	public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		return delegate.flatMapToLong(mapper);
	}

	public void forEach(Consumer<? super T> action) {
		delegate.forEach(action);
	}

	public void forEachOrdered(Consumer<? super T> action) {
		delegate.forEachOrdered(action);
	}

	public boolean isParallel() {
		return delegate.isParallel();
	}

	public Iterator<T> iterator() {
		return delegate.iterator();
	}

	public Stream<T> limit(long maxSize) {
		return delegate.limit(maxSize);
	}

	public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
		return delegate.map(mapper);
	}

	public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		return delegate.mapToDouble(mapper);
	}

	public IntStream mapToInt(ToIntFunction<? super T> mapper) {
		return delegate.mapToInt(mapper);
	}

	public LongStream mapToLong(ToLongFunction<? super T> mapper) {
		return delegate.mapToLong(mapper);
	}

	public Optional<T> max(Comparator<? super T> comparator) {
		return delegate.max(comparator);
	}

	public Optional<T> min(Comparator<? super T> comparator) {
		return delegate.min(comparator);
	}

	public boolean noneMatch(Predicate<? super T> predicate) {
		return delegate.noneMatch(predicate);
	}

	public Stream<T> onClose(Runnable closeHandler) {
		return delegate.onClose(closeHandler);
	}

	public Stream<T> parallel() {
		return delegate.parallel();
	}

	public Stream<T> peek(Consumer<? super T> action) {
		return delegate.peek(action);
	}

	public Optional<T> reduce(BinaryOperator<T> accumulator) {
		return delegate.reduce(accumulator);
	}

	public T reduce(T identity, BinaryOperator<T> accumulator) {
		return delegate.reduce(identity, accumulator);
	}

	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return delegate.reduce(identity, accumulator, combiner);
	}

	public Stream<T> sequential() {
		return delegate.sequential();
	}

	public Stream<T> skip(long n) {
		return delegate.skip(n);
	}

	public Stream<T> sorted() {
		return delegate.sorted();
	}

	public Stream<T> sorted(Comparator<? super T> comparator) {
		return delegate.sorted(comparator);
	}

	public Spliterator<T> spliterator() {
		return delegate.spliterator();
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <A> A[] toArray(IntFunction<A[]> generator) {
		return delegate.toArray(generator);
	}

	public Stream<T> unordered() {
		return delegate.unordered();
	}

}
