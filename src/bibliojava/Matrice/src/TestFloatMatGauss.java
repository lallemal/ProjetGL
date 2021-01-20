
public class TestFloatMatGauss {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float [][] mat2 = {{1,2,0},{4,5,6}, {7,8,-40}};
		MatriceFloat m2 = new MatriceFloat();
		m2.setInit(mat2, 3, 3);
		m2.print();
		m2.echange(0, 1);
		m2.print();
		m2.multScal(0,2);
		m2.print();
		m2.addMultLine(0, 1, 2);
		m2.print();
		m2.setIndentite(4);
		m2.print();
	}
}
