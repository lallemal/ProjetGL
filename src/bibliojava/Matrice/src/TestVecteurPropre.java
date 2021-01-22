
public class TestVecteurPropre {
	public static void main(String[] args) {
		float [][] mat2 = {{-3,0,0},{17,13,-7},{16,14,-8}};
		MatriceFloat m2 = new MatriceFloat();
		m2.setOneVector(2);
		System.out.println(m2.normeVect());
		m2.multScalaire(32).print();
		m2.setInit(mat2, 3, 3);
		m2.print();
		float f = 0;
		f = m2.Puissancevpvectp();
		System.out.println("vlp= " + f);
	}
}
