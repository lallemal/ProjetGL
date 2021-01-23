#!/usr/bin/env python3
def generate():
    result = "class A{"
    result += "void main( int field "
    for i in range(100):
        result += ",int field" + str(i)
    result += "){}}"
    print(result)
generate()
	void printPlateau() {

		int i = 0;
		int j = 0;
		int number;
		while( i < 3) {
			while(j < 3) {
				print("|");
				if(plateau[i][j] == 1) {
					print("x");
				}else if(plateau[i][j] == 2) {
					print("o");
				}else {
					number = i * 3 + (j + 1);
					print(number);
				}
				j = j + 1;
			}
			j = 0;
			i = i + 1;
			print("|");
			println(" ");
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
	    boolean succeedSet = false;
	    int numeroCase;
	    int nbJoueur = 0;
		boolean finish = false;

		println(" xxxxxxxxx Bienvenue dans le jeu du morpion. ooooooooo");
		println(" Pour désigner une case, entrez le numéro correspondant à cette case.");
		println(" Le joueur1, celui qui commence aura les x et l'autre les o.");
		println(" Que le meilleur gagne!");

		nbJoueur = (nbJoueur + 1) % 2;
		println("Félicitation au joueur ", (nbJoueur + 1), " pour sa victoire");
		this.printPlateau();
	}
