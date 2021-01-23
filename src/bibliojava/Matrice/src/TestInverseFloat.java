
public class TestInverseFloat {

	public static void main(String[] args) {
		float [][] mat2 = {{1,2,3},{0,1,4},{5,6,0}};
		MatriceFloat m2 = new MatriceFloat();
		m2.setInit(mat2, 3,3);
		System.out.println("matrice");
		m2.print();
		System.out.println("inverse");
		m2.inverse().print();
		
	}
//	matrice
//	[[1.0, 2.0, 3.0],
//	 [0.0, 1.0, 4.0],
//	 [5.0, 6.0, 0.0]]
//	inverse
//	[[-24.000025, 18.000015, 5.000005],
//	 [20.00002, -15.000013, -4.000004],
//	 [-5.000005, 4.0000033, 1.000001]]
}
