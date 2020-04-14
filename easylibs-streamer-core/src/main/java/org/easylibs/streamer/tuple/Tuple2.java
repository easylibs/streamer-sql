package org.easylibs.streamer.tuple;

public class Tuple2<T0, T1> extends AbstractTuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8359243062238860241L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T0, T1> Class<Tuple2<T0, T1>> cast(Class<T0> v0, Class<T1> v1) {
		return (Class) Tuple2.class;
	}

	private T0 v0;
	private T1 v1;

	public Tuple2() {
		super(2);
	}

	public Tuple2(T0 v0, T1 v1) {
		super(2);
		this.v0 = v0;
		this.v1 = v1;
	}

	@Override
	public Object get(int index) {
		checkBounds(index);

		switch (index) {
		case 0:
			return v0;
		case 1:
			return v1;

		}

		throw new IllegalStateException("invalid switch statement encountered while getting a tuple");
	}

	public T0 get0() {
		return v0;
	}

	public T1 get1() {
		return v1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void set(int index, T v) {
		switch (checkBounds(index)) {
		case 0:
			v0 = (T0) v;
			break;
		case 1:
			v1 = (T1) v;
			break;

		}

		throw new IllegalStateException("invalid switch statement encountered while setting a tuple");
	}

	public void set0(T0 v0) {
		this.v0 = v0;
	}

	public void set1(T1 v1) {
		this.v1 = v1;
	}

}
