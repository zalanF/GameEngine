package zfMath;

public class Vec2 {
	public float x, y;
	
	/**
	 * Vector in R2
	 * @param x
	 * @param y
	 */
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Addition
	 * @param v
	 * @param u
	 * @return
	 */
	public static Vec2 add(Vec2 v, Vec2 u) {
		float x, y;
		x = v.x + u.x;
		y = v.y + u.y;
		
		return new Vec2(x, y);
	}
	
	/**
	 * 
	 * @param v
	 * @param u
	 * @return
	 */
	public static Vec2 subtract(Vec2 v, Vec2 u) {
		float x, y;
		x = v.x - u.x;
		y = v.y - u.y;
		
		return new Vec2(x, y);
	}
	
	/**
	 * Scalar multiplication
	 * @param scalar
	 * @param v
	 * @return
	 */
	public static Vec2 multiply(float scalar, Vec2 v) {
		float x, y;
		x = v.x * scalar;
		y = v.y * scalar;

		
		return new Vec2(x, y);
		
	}
	
	/**
	 * Dot product / 'inner product'
	 * @param v
	 * @param u
	 * @return
	 */
	public static float dot(Vec2 v, Vec2 u) {
		return v.x*u.x + v.y*u.y;
	}
	
	/**
	 * Norm or 'length' of vector
	 * @param v
	 * @return
	 */
	public static float norm(Vec2 v) {
		return (float)Math.sqrt(dot(v, v));
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}
}
