
public class TestMatAffichage {

	public static void main(String[] args) {
		int [][] tab1 = {{3,2}};
		MatriceInt mat1 = new MatriceInt();
		mat1.setInit(tab1, 1, 2);
		mat1.print();
		
		int [][] tab2 = {{3},{2}};
		MatriceInt mat2 = new MatriceInt();
		mat2.setInit(tab2, 2, 1);
		mat2.print();
		
		int [][] tab3 = {{3,3},{2,3}};
		MatriceInt mat3 = new MatriceInt();
		mat3.setInit(tab3, 2, 2);
		mat3.print();
		
		int [][] tab4 = {{3,3,3},{2,3,2}};
		MatriceInt mat4 = new MatriceInt();
		mat4.setInit(tab4, 2, 3);
		mat4.print();
		
		int [][] tab5 = {{3,3},{2,3},{2,1}};
		MatriceInt mat5 = new MatriceInt();
		mat5.setInit(tab5, 3, 2);
		mat5.print();
	}

}
