package org.easylibs.streamer.tuple;

public class Tuple4<T0, T1, T2, T3> extends AbstractTuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1464671735378339067L;
	private T0 v0;
	private T1 v1;
	private T2 v2;
	private T3 v3;

	public Tuple4() {
		super(4);
	}

	public Tuple4(T0 v0, T1 v1, T2 v2, T3 v3) {
		super(4);
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;

	}

	@Override
	public Object get(int index) {
		checkBounds(index);

		switch (index) {
		case 0:
			return v0;
		case 1:
			return v1;
		case 2:
			return v2;
		case 3:
			return v3;

		}

		throw new IllegalStateException("invalid switch statement encountered while getting a tuple");
	}

	public T0 get0() {
		return v0;
	}

	public T1 get1() {
		return v1;
	}

	public T2 get2() {
		return v2;
	}

	public T3 get3() {
		return v3;
	}

	public void set0(T0 v0) {
		this.v0 = v0;
	}

	public void set1(T1 v1) {
		this.v1 = v1;
	}

	public void set2(T2 v2) {
		this.v2 = v2;
	}

	public void set3(T3 v3) {
		this.v3 = v3;
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
		case 2:
			v2 = (T2) v;
			break;
		case 3:
			v3 = (T3) v;
			break;

		}

		throw new IllegalStateException("invalid switch statement encountered while setting a tuple");
	}

}
