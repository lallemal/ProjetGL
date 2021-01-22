
public class TestDeterminantFloat {
	public static void main(String[] args) {
		float [][] mat2 = {{2,-1,0},{0,-1,2}, {-1,2,-1}};
		MatriceFloat m2 = new MatriceFloat();
		m2.setInit(mat2, 3, 3);
		System.out.println(m2.determinant());
		// on a -4 en sortie
	}		

}
