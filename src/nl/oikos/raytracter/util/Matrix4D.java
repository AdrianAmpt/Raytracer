package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 1-8-2017.
 */
public class Matrix4D
{

	public static final Matrix4D I = new Matrix4D(1,0,0,0,
												  0,1,0,0,
												  0,0,1,0,
												  0,0,0,1);

	public final double[][] m;

	public Matrix4D(double m00, double m01, double m02, double m03,
					double m10, double m11, double m12, double m13,
					double m20, double m21, double m22, double m23,
					double m30, double m31, double m32, double m33)
	{
		m = new double[][]{{m00, m01, m02, m03},
						   {m10, m11, m12, m13},
						   {m20, m21, m22, m23},
						   {m30, m31, m32, m33}};
	}

	public Matrix4D(double[][] m)
	{
		this.m = new double[4][4];
		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 4; y++)
				this.m[x][y] = m[x][y];

	}

	public Matrix4D(Matrix4D m)
	{
		this.m = m.m;
	}

	public Matrix4D multiply(Matrix4D m)
	{
		double[][] d = new double[4][4];
		for (int y = 0; y < 4; y++)
		{
			for (int x = 0; x < 4; x++)
			{
				double sum = 0.0;

				for (int i = 0; i < 4; i++)
					sum += this.m[x][i] * m.m[i][y];

				d[x][y] = sum;
			}
		}

		return new Matrix4D(d);
	}

	/**
	 * Returns this * s
	 * @param s
	 * @return this * s
	 */
	public Matrix4D multiply(final double s)
	{
		double[][] m = new double[4][4];
		for (int y = 0; y < 4; y++)
			for (int x = 0; x < 4; x++)
				m[x][y] = this.m[x][y] * s;

		return new Matrix4D(m);
	}

	/**
	 * Return this / s
	 * @param s
	 * @return this / s
	 */
	public Matrix4D divide(final double s)
	{
		double[][] m = new double[4][4];
		for (int y = 0; y < 4; y++)
			for (int x = 0; x < 4; x++)
				m[x][y] = this.m[x][y] / s;

		return new Matrix4D(m);
	}

	/**
	 * return this * v
	 * @param v
	 * @return this * v
	 */
	public Vector3D multiply(final Vector3D v)
	{
		return new Vector3D(m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3],
							m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3],
							m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3]);
	}

	/**
	 * return this * n
	 * @param n
	 * @return this * n
	 */
	public Normal3D multiply(final Normal3D n)
	{
		return new Normal3D(this.multiply((Vector3D) n));
	}

	/**
	 * return this * p
	 * @param p
	 * @return this * p
	 */
	public Point3D multiply(final Point3D p)
	{
		return new Point3D(this.multiply((Vector3D) p));
	}

}
