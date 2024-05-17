package zfMath;

public class Matrix {
	
	private float[][] matrix;
	
	public Matrix(int rows, int cols) {
		matrix = new float[rows][cols];
	}
	
	public Matrix(float[][] matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * Error checking for member functions
	 * @param row
	 * @param col
	 */
	//Eventually, export this into it's own class, then have it as an import?? maybe
	//Using boolean to avoid using stupid exceptions and throwables (they SUCK!)
	//Exceptions be like sry you can't use this function without a try catch! SHUT UP. 
	// Let me not use try catch it a dont want.
	private void errorOutOfBounds(int row, int  col) {
		
		
		if (row >= this.rows() || col >= this.cols() || row < 0 || col < 0) {
			System.err.println("Index out of bounds");
		}
	}
	
	/**
	 * Get the data stored at the indexed location of the matrix following (row,col) notation
	 * @param row row of specified data
	 * @param col column of specified data
	 * @return value stored at the index
	 */
	public float get(int row, int col) {
		errorOutOfBounds(row, col);
		
		return matrix[row][col];
	}
	
	/**
	 * Set a new value to be stored at he indexed location of the matrix following (row,col) notation
	 * @param row row of the value to be changed
	 * @param col column of the value to be changed
	 * @param value the new value to replace existing data
	 */
	public void set(int row, int col, float value) {
		errorOutOfBounds(row, col);
		
		matrix[row][col] = value;
		
	}
	
	/**
	 * Get the number of rows of the matrix
	 * @return the number of rows
	 */
	public int rows() {
		return matrix.length;
	}
	
	/**
	 * Get the number of columns of the matrix
	 * @return the number of columns
	 */
	public int cols() {
		return matrix[0].length;
	}
	
	/**
	 * Modifies existing (<b><i>this</i></b>) matrix by scalar multiplication
	 * @param scalar amount of scaling
	 */
	public void scale(float scalar) {
		
		for (int r = 0; r < this.rows(); r++) {
			for (int c = 0; c < this.cols(); c++) {
				
				matrix[r][c] = scalar*matrix[r][c];
				
			}
		}
	}
	
	/**
	 * Special case of scale(). Mainly for syntax. (Not sure how useful, might delete)
	 */
	public void negate() {
		scale(-1);
	}
	
	/**
	 * General algorithm for that works for any two matrices. Includes checking for validity of operation
	 * @param a
	 * @param b
	 * @return null if operation is invalid, otherwise a new matrix containing the sum
	 */
	public static Matrix add(Matrix a, Matrix b) {

		if (a.equalOrder(b)) {
			Matrix sum = new Matrix(a.rows(), a.cols());
			
			for (int r = 0; r < sum.rows(); r++) {
				for (int c = 0; c < sum.cols(); c++) {


					sum.set(r, c, a.get(r, c) + b.get(r, c));
					
				}
				
			}
			
			return sum;
			
		}
		
		return null;
	}
	
	/**
	 * General algorithm for subtraction that works for any two matrices. Includes checking for validity of operation
	 * @param a
	 * @param b
	 * @return null if operation is invalid, otherwise a new matrix containing the difference
	 */
	public static Matrix subtract(Matrix a, Matrix b) {
		if (a.equalOrder(b)) {
			Matrix sum = new Matrix(a.rows(), a.cols());
			
			for (int r = 0; r < sum.rows(); r++) {
				for (int c = 0; c < sum.cols(); c++) {


					sum.set(r, c, a.get(r, c) - b.get(r, c));
					
				}
				
			}
			
			return sum;
			
		}
		return null;
	}
	
	/**
	 * General algorithm to multiply any two valid matrices.
	 * Checks for validity and returns null if invalid.
	 * [NOTE] Not the fastest algorithm for any given matrix, but is guaranteed to work for ANY VALID operation
	 * @param a
	 * @param b
	 * @return null if operation is invalid, otherwise new product matrix
	 */
	public static Matrix multiply(Matrix a, Matrix b) {
		int sum;
		if (a.cols() == b.rows()) {
			
			Matrix product = new Matrix(a.rows(), b.cols());
			
			for (int r = 0; r < a.rows(); r++) {
				for (int c = 0; c < b.cols(); c++) {
					
					sum = 0;
					for (int i = 0; i < a.cols(); i++) {
						sum += a.get(r, i) * b.get(i,c);
					}
					
					product.set(r, c, sum);
					
				}
			}
			
			return product;
		}
		
		return null;
	}
	
	/**
	 * General algorithm for scalar multiplication of a matrix
	 * @param scalar
	 * @param a
	 * @return new matrix
	 */
	public static Matrix multiply(float scalar, Matrix a) {
		
		Matrix product = a;
		
		for (int r = 0; r < a.rows(); r++) {
			for (int c = 0; c < a.cols(); c++) {
				
				product.set(r, c, scalar*product.get(r, c));
			}
		}
		
		return product;
		
	}
	
	public static Vec4 multiply(Matrix a, Vec4 v) {
		if (a.cols() == 4 && a.rows() == 4) {
			
			float x, y, z, w;
			
			x = a.get(0, 0)*v.x + a.get(0, 1)*v.x + a.get(0, 2)*v.x + a.get(0, 3)*v.x;
			y = a.get(1, 0)*v.y + a.get(1, 1)*v.y + a.get(1, 2)*v.y + a.get(1, 3)*v.y;
			z = a.get(2, 0)*v.z + a.get(2, 1)*v.z + a.get(2, 2)*v.z + a.get(2, 3)*v.z;
			w = a.get(3, 0)*v.w + a.get(3, 1)*v.w + a.get(3, 2)*v.w + a.get(3, 3)*v.w;
			
			return new Vec4(x, y, z, w);
			
			
		}
		
		
		return null;
	}
	
	public static Matrix transpose(Matrix a) {
		
		Matrix matrixTransposed = new Matrix(a.cols(), a.rows());
		
		for (int r = 0; r < matrixTransposed.rows(); r++) {
			for (int c = 0; c < matrixTransposed.cols(); c++) {
				matrixTransposed.set(r, c, a.get(c, r));
			}
		}
		return matrixTransposed;
	}
	
	public boolean equalOrder(Matrix m) {
		return this.rows() == m.rows() && this.cols() == m.cols();
	}
	
	/**
	 *Convert to 2D array containing the matrix data
	 * @return matrix data in the form of <b>float</b> array
	 */
	public float[][] toArray() {
		return this.matrix;
	}
	
	@Override
	public String toString() {
		String out_string = "";
		
		for (int r = 0; r < this.rows(); r++) {
			out_string+="|  ";
			for (int c = 0; c < this.cols(); c++) {
				
				out_string += matrix[r][c]+"  ";
				
			}
			out_string+= "|\n";
		}
		
		return out_string;
	}
	
	//TODO: Refactor into seperate utils class
	private String floatPadding(int amount, float f) {
		
		String paddedNum = "";
		
		
		
		
		return paddedNum; 
		
	}
	
	//TODO: Refactor into seperate utils class
	private int digitCounter(float f) {
		int p = 0;
		
		if (f == 0) {
			p = 0;
		}  else if (f < 1) {
			while (f * Math.pow(10, p) <= 1) {
				++p;
			}
			p *= -1;
		} else {
			while (f / Math.pow(10, p) >= 1) {
				++p;	
			}
		}
		
		return p;
	}

}
