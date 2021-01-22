
public class TestConvergence {
	public static void main(String[] args) {
		int [][] mat2 = {{2,-1,0},{0,-1,2}, {-1,2,-1}};
		MatriceInt m2 = new MatriceInt();
		m2.setInit(mat2, 3,3);
		
		System.out.println("Norme 1 de la matrice m2: " + m2.norme1());
	}
}
