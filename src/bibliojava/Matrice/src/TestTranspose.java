
public class TestTranspose {

	public static void main(String[] args) {
		int[][] t = {{1}, {2}};
		MatriceInt tm = new MatriceInt();
		tm.setInit(t, 2, 1);
		tm.transpose().print();
		
		int[][] t2 = {{1,2}, {3,4}};
		MatriceInt tm2 = new MatriceInt();
		tm2.setInit(t2, 2, 2);
		tm2.transpose().print();
	}

}
