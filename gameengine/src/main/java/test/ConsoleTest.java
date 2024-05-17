package test;

import zfMath.*;

public class ConsoleTest {

	public static void main(String[] args) {
		
		float[][] a = { {1f, 2f, 3f}, {1f, 2f, 3f}   };
		float[][] b = { {1f, 0f, 3f}, {1f, 2f, 0f}   };
		float[][] d = {
				{1,1,1,1},
				{2,2,2,2},
				{3,3,3,3},
				{4,4,4,4}
		};
		
		Matrix m_a = new Matrix(a);
		Matrix m_b = new Matrix(b);
		Matrix m_d = new Matrix(d);
				
		System.out.println(m_a.toString());
		
		
		System.out.println(m_d.toString());
		
		
		//Matrix c = Matrix.add(m_a, m_b);
		
		
		//System.out.println(c.toString());
		
		//c = Matrix.multiply(m_a, Matrix.transpose(m_a));
		
		//System.out.println(c.toString());
		
		Vec4 v = new Vec4(1, 1, 1, 1);
		
		System.out.println(v.toString());
		
		Vec4 u = Matrix.multiply(m_d, v);
		System.out.println(u.toString());
		
		
		
		
	}

}
