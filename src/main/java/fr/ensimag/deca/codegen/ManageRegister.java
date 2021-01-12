package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;

public class ManageRegister {

	private int x; //Pour loption -r X de decac limitant le nombre de registre
	private boolean[] freeRegisters;
	
	public ManageRegister(int x) {
		this.x = x;
		freeRegisters = new boolean[x];
		freeRegisters[0] = false;
		freeRegisters[1] = false;
		for (int i = 2; i<x; i++) {
			freeRegisters[i] = true;
		}
	}
	
	public ManageRegister() {
		x = 16;
		freeRegisters = new boolean[x];
		freeRegisters[0] = false;
		freeRegisters[1] = false;
		for (int i = 2; i<x; i++) {
			freeRegisters[i] = true;
		}
	}
	
	public void setFreedom(int i, boolean freedom) {
		if (!(0 <= i && i < x)) {
			System.out.println("error : you can only use register from 0 to " + (x-1) );
		} else {
			freeRegisters[i] = freedom;
		}
	}
	
	public boolean freeRegisterExist() {
		return getFreeRegister()>0;
	}
	
	public int getFreeRegister() {
		for (int i=0; i<x; i++) {
			if (freeRegisters[i]) {
				return i;
			}
		}
		return 0;
	}
	
	public int useFreeRegister() {
		for (int i=0; i<x; i++) {
			if (freeRegisters[i]) {
				this.used(i);
				return i;
			}
		}
		return 0;
	}
	
	public void used(int i) {
		this.setFreedom(i, false);
	}
	
	public void freed(int i) {
		this.setFreedom(i, true);
	}
	
}
