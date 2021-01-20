
public class TestProdMat {

	public static void main(String[] args) {
		// Cas ok
		int [][] mat1 = {{1,2},{1,2}};
		MatriceInt m = new MatriceInt();
		m.setInit(mat1, 2, 2);
		int [][] vect = {{1},{2}};
		MatriceInt vect1 = new MatriceInt();
		vect1.setInit(vect, 2, 1);
		MatriceInt prodmat = m.prodMat(vect1);
		prodmat.print();
		//prodmat.print(); sortie {{5},{5}}
		
		// Cas 2 
		int [][] mat2 = {{1,2,3},{1,2,3}};
		MatriceInt m2 = new MatriceInt();
		m2.setInit(mat2, 2, 3);
		int [][] v = {{1},{2},{3}};
		MatriceInt vect2 = new MatriceInt();
		vect2.setInit(v, 3, 1);
		MatriceInt prodmat2 = m2.prodMat(vect2);
		prodmat2.print();
		//prodmat.print(); sortie {{5},{5}}
		
		// Cas matrice de dimension incompatible
		int [][] tab3 = {{1},{2}};
		MatriceInt mat3 = new MatriceInt();
		mat3.setInit(tab3, 1, 2);
		int [][] vec = {{1},{2}};
		MatriceInt vect3 = new MatriceInt();
		vect3.setInit(vec, 1, 2);
		MatriceInt prodmatfalse = mat3.prodMat(vect3);
		if(prodmatfalse == null) {
			//System.out.println("OK");
		}
	}

}
