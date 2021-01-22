
public class TestInverseInt {
	
	public static void main(String[] args) {
		int [][] mat2 = {{1,2,3},{0,1,4},{5,6,0}};
		
		MatriceInt m2 = new MatriceInt();
		m2.setInit(mat2, 3, 3);
		System.out.println("matrice");
		m2.print();
		System.out.println("inverse");
		m2.inverse().print();
		
	}	
	// erreur pour l'instant
// attendu 	
//	matrice
//	[[1, 2, 3],
//	 [0, 1, 4],
//	 [5, 6, 0]]
//	inverse
//	[[-24.000025, 18.000015, 5.000005],
//	 [20.00002, -15.000013, -4.000004],
//	 [-5.000005, 4.0000033, 1.000001]]
}
