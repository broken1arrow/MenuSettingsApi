package org.brokenarrow.library.menusettings.utillity;

public class Tuple<K, V> {

	private final K firstValue;
	private final V secondValue;


	public Tuple(K firstValue, V secondValue) {

		this.firstValue = firstValue;
		this.secondValue = secondValue;
	}


	public K getFirst() {
		return firstValue;
	}

	public V getSecond() {
		return secondValue;
	}

	@Override
	public String toString() {
		return firstValue + "_" + secondValue;
	}
}
