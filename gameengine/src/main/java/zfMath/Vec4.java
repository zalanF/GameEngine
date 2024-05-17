package zfMath;

public class Vec4 {
	public float x, y, z, w;
	
	/**
	 * Vector in R4
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Addition
	 * @param v
	 * @param u
	 * @return
	 */
	public static Vec4 add(Vec4 v, Vec4 u) {
		float x, y, z, w;
		x = v.x + u.x;
		y = v.y + u.y;
		z = v.z + u.z;
		w = v.w + u.w;
		
		return new Vec4(x, y, z, w);
	}
	
	/**
	 * 
	 * @param v
	 * @param u
	 * @return
	 */
	public static Vec4 subtract(Vec4 v, Vec4 u) {
		float x, y, z, w;
		x = v.x - u.x;
		y = v.y - u.y;
		z = v.z - u.z;
		w = v.w - u.w;
		
		return new Vec4(x, y, z, w);
	}
	
	/**
	 * Scalar multiplication
	 * @param scalar
	 * @param v
	 * @return
	 */
	public static Vec4 multiply(float scalar, Vec4 v) {
		float x, y, z, w;
		x = v.x * scalar;
		y = v.y * scalar;
		z = v.z * scalar;
		w = v.w * scalar;
		
		return new Vec4(x, y ,z, w);
		
	}
	
	/**
	 * Dot product / 'inner product'
	 * @param v
	 * @param u
	 * @return
	 */
	public static float dot(Vec4 v, Vec4 u) {
		return v.x*u.x + v.y*u.y + v.z*u.z + v.w*u.w;
	}
	
	/**
	 * Norm or 'length' of vector
	 * @param v
	 * @return
	 */
	public static float norm(Vec4 v) {
		return (float)Math.sqrt(dot(v, v));
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+", "+z+", "+w+")";
	}
}
