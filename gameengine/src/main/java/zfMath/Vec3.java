package zfMath;

/**
 * Vectors in R3
 */
public class Vec3 {
	//typically not advised to set to public but in this case it makes sense
	//since we are just making plain data structure
	//Also means I dont have to implement 6 get and set functions XD
	public float x, y, z;
	
	/**
	 * Vector in R3
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Addition
	 * @param v
	 * @param u
	 * @return
	 */
	public static Vec3 add(Vec3 v, Vec3 u) {
		float x, y, z;
		x = v.x + u.x;
		y = v.y + u.y;
		z = v.z + u.z;
		
		return new Vec3(x, y, z);
	}
	
	/**
	 * 
	 * @param v
	 * @param u
	 * @return
	 */
	public static Vec3 subtract(Vec3 v, Vec3 u) {
		float x, y, z;
		x = v.x - u.x;
		y = v.y - u.y;
		z = v.z - u.z;
		
		return new Vec3(x, y, z);
	}
	
	/**
	 * Scalar multiplication
	 * @param scalar
	 * @param v
	 * @return
	 */
	public static Vec3 multiply(float scalar, Vec3 v) {
		float x, y, z;
		x = v.x * scalar;
		y = v.y * scalar;
		z = v.z * scalar;
		
		return new Vec3(x, y ,z);
		
	}
	
	/**
	 * Dot product / 'inner product'
	 * @param v
	 * @param u
	 * @return
	 */
	public static float dot(Vec3 v, Vec3 u) {
		return v.x*u.x + v.y*u.y + v.z*u.z;
	}
	
	/**
	 * Norm or 'length' of vector
	 * @param v
	 * @return
	 */
	public static float norm(Vec3 v) {
		return (float)Math.sqrt(dot(v, v));
	}
	
	/**
	 * Cross product
	 * @param v
	 * @param u
	 * @return
	 */
	public static Vec3 cross(Vec3 v, Vec3 u) {
		float x, y, z;
		x = v.y*u.z - v.z*u.y;
		y = v.z*u.x - v.x*u.z;
		z = v.x*u.y - v.y*u.x;
		
		return new Vec3(x, y ,z);
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
	
	//TODO: vector projections? or makes a seperate class for all kinds of projections
}
