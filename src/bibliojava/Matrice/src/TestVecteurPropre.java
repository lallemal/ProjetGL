
public class TestVecteurPropre {
	public static void main(String[] args) {
		float [][] mat2 = {{-3,0,0},{17,13,-7},{16,14,-8}};
		MatriceFloat m2 = new MatriceFloat();
		MatriceFloat vect = new MatriceFloat();
		m2.setOneVector(2);
		//System.out.println(m2.normeVect());
		m2.multScalaire(32).print();
		
		// test vecteur propre
		m2.setInitFloat(mat2, 3, 3);
		vect = m2.Puissancevpvectp();
		System.out.println("AX=");
		m2.prodMat(vect).print();
		System.out.println("lX=");
		vect.multScalaire(6).print();;
		
	}
}
