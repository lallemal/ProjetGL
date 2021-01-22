
public class TestDeterminantInt {
	public static void main(String[] args) {
		int [][] mat2 = {{2,-1,0},{0,-1,2}, {-1,2,-1}};
		MatriceInt m2 = new MatriceInt();
		m2.setInit(mat2, 3, 3);
		System.out.println(m2.determinant());
		// on a -4 en sortie marche pas encore TO DO
	}		
}
