package fr.ensimag.deca.tree;

public class ElementArray extends AbstractElementArray{
	final private AbstractIdentifier var;
	final private ElementArray next;
	
	public ElementArray(AbstractIdentifier var, ElementArray next) {
		this.var = var;
		this.next = next;
	}
}
