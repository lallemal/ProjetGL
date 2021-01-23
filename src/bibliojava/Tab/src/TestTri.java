
public class TestTri {

	public static void main(String[] args) {
		
		int [] t = { 10, 4, 2, 50,5, 8};
		TabInt tab = new TabInt();
		tab.setInit(t, 6);
		tab.print();
		tab.mergeSortAscending();
		tab.print();
	}

}
