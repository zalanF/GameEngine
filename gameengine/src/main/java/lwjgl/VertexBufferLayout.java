package lwjgl;

public class VertexBufferLayout {
	
	private final int size;
	private final int type;
	private final boolean normalized;
	private final int stride;
	
	public VertexBufferLayout(int size, int type, boolean normalized) {
		
		this.size = size;
		this.type = type;
		this.normalized = normalized;
		//this.stride = size * sizeResolver(type);
		this.stride = sizeResolver(type)*2;
	}
	
	private static int sizeResolver(int n) {
		
		//This is the lazy way - not necessarily most efficient since there are lot of unnecessary / repeat values		
		final int[] JAVA_TYPES = {
				Byte.BYTES, Byte.BYTES, Short.BYTES, Short.BYTES, Integer.BYTES, Integer.BYTES,
				Float.BYTES, Byte.BYTES*2, Byte.BYTES*3, Byte.BYTES*4, Double.BYTES
		};
				
		return JAVA_TYPES[n - 0x1400];
		
	}

	public int getSize() {
		return size;
	}

	public int getType() {
		return type;
	}
	
	public int getStride() {
		return stride;
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
