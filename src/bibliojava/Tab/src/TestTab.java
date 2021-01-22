
public class TestTab {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] t = {1,2,3};
		int [] ts = {2,3,4,5};
		TabInt tab = new TabInt();
		tab.setInit(t, 3);
		//System.out.println(tab.getSize());// expected 3
		TabInt tabsum = new TabInt();
		tabsum.setInit(ts,4);
		//tab.print(); // expectet [1,2,3]
		tab.print();
		tab.addFirst(0);
		tab.print();
		tab.addLast(4);
		tab.print();
		tab.add(60,1);
		tab.print();
		tab.deleteFirst();
		tab.print();
		tab.deleteLast();
		tab.print();
		tab.sumTab(tabsum);
		tab.print();
		tab.multTab(tabsum);
		tab.print();
		tab.delete(2);
		tab.print();
	}

}
