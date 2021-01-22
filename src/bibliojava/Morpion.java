
import java.util.Scanner;

public class Morpion {
	int[][] plateau = new int[3][3];
	
	void printPlateau() {
		int i = 0;
		int j = 0;
		int number;
		while( i < 3) {
			while(j < 3) {
				System.out.print("|");
				if(plateau[i][j] == 1) {
					System.out.print("x");
				}else if(plateau[i][j] == 2) {
					System.out.print("o");
				}else {
					number = i * 3 + (j + 1);
					System.out.print(number);
				}
				j = j +1;
			}
			j = 0;
			i = i + 1;
			System.out.print("|\n");
		}
	}
	
	boolean setPlateau(int numeroCase, int nbJoueur) {
		int i = (numeroCase - 1) / 3;
		int j = (numeroCase - 1) % 3;
		if(plateau[i][j] != 0){
			return false;
		}else {
			plateau[i][j] = nbJoueur + 1;
			return true;
		}
	}
	
	boolean someoneWon() {
		int i = 0;
		boolean b = false;
		while(i < 3) {
			b = b || (plateau[i][0] == plateau[i][1] && plateau[i][1] ==  plateau[i][2] && plateau[i][0] != 0);
			b = b || (plateau[0][i] == plateau[1][i] && plateau[1][i] ==  plateau[2][i] && plateau[0][i] != 0);
			i = i + 1;
		}
		b = b || (plateau[0][0] == plateau[1][1] && plateau[1][1] == plateau[2][2] && plateau[0][0] != 0);
		b = b || (plateau[0][2] == plateau[1][1] && plateau[1][1] == plateau[2][0] && plateau[2][0] != 0);
		return b;
	}
	
	void play() {
	    Scanner sc = new Scanner(System.in);
	    boolean succeedSet = false;
	    char numeroCase;
	    int nbJoueur = 0;
		boolean finish = false;
		System.out.println(" xxxxxxxxx Bienvenue dans le jeu du morpion. ooooooooo");
		System.out.println(" Pour désigner une case, entrez le numéro correspondant à cette case.");
		System.out.println(" Le joueur1, celui qui commence aura les x et l'autre les o.");
		System.out.println(" Que le meilleur gagne!");
		while(!finish) {
			succeedSet = false;
			while(!succeedSet) {
				this.printPlateau();
				System.out.println("Joeur " + (nbJoueur + 1) + ", entrez un numéro:");
				numeroCase = sc.nextLine().charAt(0);
				
				succeedSet = this.setPlateau(Character.getNumericValue(numeroCase), nbJoueur);
				if(!succeedSet){
					System.out.println("Vous devez entrer un numéro correspondant à une case libre.");
				}
			}
			nbJoueur = (nbJoueur + 1) % 2;
			finish = this.someoneWon();
		}
		nbJoueur = (nbJoueur + 1) % 2;
		System.out.println("Félicitation au joueur " + (nbJoueur + 1) + " pour sa victoire");
		this.printPlateau();
	}
}
