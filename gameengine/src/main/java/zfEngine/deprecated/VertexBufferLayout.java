package zfEngine.deprecated;

import java.util.ArrayList;

public class VertexBufferLayout {

	private ArrayList<Integer> size;
	private final int type;
	private final boolean normalized;
	private final int stride;

	public VertexBufferLayout(int type, boolean normalized) {

		this.size = new ArrayList<Integer>();
		
		this.type = type;
		this.normalized = normalized;
		// this.stride = size * sizeResolver(type);
		this.stride = 0;
	}
	
	//TODO: ADD ERROR STUFF
	public void pushf(int size) {
		this.size.add(size);
	}

	private static int sizeResolver(int n) {

		// This is the lazy way - not necessarily most efficient since there are lot of
		// unnecessary / repeat values
		final int[] JAVA_TYPES = { Byte.BYTES, Byte.BYTES, Short.BYTES, Short.BYTES, Integer.BYTES, Integer.BYTES,
				Float.BYTES, Byte.BYTES * 2, Byte.BYTES * 3, Byte.BYTES * 4, Double.BYTES };

		return JAVA_TYPES[n - 0x1400];

	}
	
	public int length() {
		return size.size();
	}

	//TODO: RETURN INDEXOUTOFBOUNDS EXCEPTION
	public int getSize(int index) {
		return size.get(index);
	}

	public int getType() {
		return type;
	}

	public int getStride(int index) {
		return sizeResolver(type) * getSize(index);
	}

	public boolean isNormalized() {
		return normalized;
	}

	@Override
	public String toString() {
		return "VertexBufferLayout [size=" + size + ", type=" + type + ", normalized=" + normalized + ", stride="
				+ stride + "]";
	}

}
