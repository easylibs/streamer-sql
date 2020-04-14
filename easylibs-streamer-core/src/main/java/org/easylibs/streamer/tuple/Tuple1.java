package org.easylibs.streamer.tuple;

public class Tuple1<T0> extends AbstractTuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1174776483449378006L;
	private T0 v0;

	public Tuple1() {
		super(1);
	}

	public Tuple1(T0 v0) {
		super(1);
		this.v0 = v0;
	}

	@Override
	public Object get(int index) {
		checkBounds(index);

		switch (index) {
		case 0:
			return v0;

		}

		throw new IllegalStateException("invalid switch statement encountered while getting a tuple");
	}

	public T0 get0() {
		return v0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void set(int index, T v) {
		switch (checkBounds(index)) {
		case 0:
			v0 = (T0) v;
			break;

		}

		throw new IllegalStateException("invalid switch statement encountered while setting a tuple");
	}

}
