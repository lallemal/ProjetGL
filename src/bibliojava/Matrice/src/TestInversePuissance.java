
public class TestInversePuissance {
	public static void main(String[] args) {
		float [][] mat2 = {{-3,0,0},{17,13,-7},{16,14,-8}};
		MatriceFloat m2 = new MatriceFloat();
		
		// test vecteur propre
		m2.setInitFloat(mat2, 3, 3);
		//m2.sumMat(m2).print(); 
		m2.PuissanceInverse(0);
		//m2.PuissanceInverse(-2).print();
		System.out.println("AX=");
		//m2.prodMat(vect).print();
		System.out.println("lX=");
		//vect.multScalaire(6).print();;
		
	}
}
