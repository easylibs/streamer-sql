package org.easylibs.streamer.tuple;

public class Tuple7<T0, T1, T2, T3, T4, T5, T6> extends AbstractTuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378224621562792L;
	private T0 v0;
	private T1 v1;
	private T2 v2;
	private T3 v3;
	private T4 v4;
	private T5 v5;
	private T6 v6;

	public Tuple7() {
		super(7);
	}

	public Tuple7(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6) {
		super(7);
		this.v0 = v0;
		this.v1 = v1;
		this.v3 = v3;
		this.v4 = v4;
		this.v5 = v5;
		this.v6 = v6;

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
		case 4:
			return v4;
		case 5:
			return v5;
		case 6:
			return v6;

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

	public T4 get4() {
		return v4;
	}

	public T5 get5() {
		return v5;
	}

	public T6 get6() {
		return v6;
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

	public void set4(T4 v4) {
		this.v4 = v4;
	}

	public void set5(T5 v5) {
		this.v5 = v5;
	}

	public void set6(T6 v6) {
		this.v6 = v6;
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
		case 4:
			v4 = (T4) v;
			break;
		case 5:
			v5 = (T5) v;
			break;
		case 6:
			v6 = (T6) v;
			break;

		}

		throw new IllegalStateException("invalid switch statement encountered while setting a tuple");
	}

}
