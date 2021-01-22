
	// Matrice de flottant identique presque que int sauf quelque valeur en plus
class MatriceFloat extends AbstractMatrice{
	float[][] matfloat;
	
	// initialise le tableau, le tableau est en référence avec la classe creer
	void setInit(float[][] mat, int nbl, int nbc) {
		this.nbl = nbl;
		this.nbc = nbc;
		this.matfloat = mat;
		return;
	}
	
	// initialise a partir d'un tableau de int creer une copie
	void setInitInt(int[][] mat, int nbl, int nbc) {
		this.matfloat = new float[nbl][nbc];
		this.nbl = nbl;
		this.nbc = nbc;
		int i = 0;
		int j = 0;
		while(i < nbl) {
			while(j < nbc) {
				this.matfloat[i][j] = mat[i][j];
				j = j + 1;
			}
		j = 0;
		i = i + 1;
		
		}
		return;
	}
	
	// initialise a partir d'un tableau de float, une copie du tableau est creer
	void setInitFloat(float[][] mat, int nbl, int nbc) {
		this.matfloat = new float[nbl][nbc];
		this.nbl = nbl;
		this.nbc = nbc;
		int i = 0;
		int j = 0;
		while(i < nbl) {
			while(j < nbc) {
				this.matfloat[i][j] = mat[i][j];
				j = j + 1;
			}
		j = 0;
		i = i + 1;
		}
		return;
	}

	// initialise la matrice indentite (une copie) 
	void setIndentite(int n) {
		float[][] t = new float[n][n];
		this.nbl = n;
		this.nbc = n;
		int i = 0;
		int j = 0;
		while(i < n) {
			while(j < n) {
				if (i == j) {
					t[i][j] = 1;
				}
				else {
					t[i][j] = 0;
				}
				j = j + 1;
			}
			j = 0;
			i = i + 1;
		}
		this.matfloat = t;
		
	}
	
	
	float getCase(int i, int j) {
		return this.matfloat[i][j];
	}
	
	void print() {
		System.out.print("[");
		int l = 0;
		int c = 0;
		while(l < this.nbl) {
			while( c < this.nbc){
				if (l == 0 && c == 0) {
					System.out.print("[" + this.matfloat[l][c]);
				}
				else if (l > 0 && c == 0) {
					System.out.print(" [" + this.matfloat[l][c]);
				}
				else{
					System.out.print(" " + this.matfloat[l][c]);
				}
				c = c + 1;
				if (c < nbc) {
					System.out.print(",");
				}
			}
			System.out.print("]");
			if (l < nbl -1) {
				System.out.println(",");
			}
			c = 0;
			l = l + 1;
		}
		System.out.println("]");
		return;
	}

	// produit matriciel retourne null si echoue 
	MatriceFloat prodMat(MatriceFloat m) {
		
		// erreur retourne null
		if(this.nbc != m.getNbl()) {
			return null;
		}
		
		int i = 0;
		int j = 0;
		int k = 0;
		float [][] tabfinal = new float[this.nbl][m.getNbc()];
		MatriceFloat matfinal = new MatriceFloat();
		
		matfinal.setInit(tabfinal, this.nbl, m.getNbc());
		
		while(i < this.nbl) {
			while( j < m.getNbc()) {
				while(k < this.nbc) {
					tabfinal[i][j] = tabfinal[i][j] + this.matfloat[i][k]* m.getCase(k, j);
					k = k + 1;
				}
				k = 0;
				j = j + 1;
			}
			j = 0;
			i = i + 1;
		}
		return matfinal;
	}
	
	// creer une nouvelle matrice transpose
	MatriceFloat transpose() {
		float[][] tab = new float[this.getNbc()][this.getNbl()];
		int i = 0;
		int j = 0;
		while (i < this.nbc) {
			while(j < this.nbl) {
				tab[i][j] = this.getCase(j, i);
				j = j + 1;
			}
			j = 0;
			i = i + 1;
		}
		MatriceFloat transpose = new MatriceFloat();
		transpose.setInit(tab, this.getNbc(), this.getNbl());
		return transpose;
	}
	
	/* algorithme utilse pivot de Gauss transforme la matrice directement*/
	
	// echange deux lignes
	void echange(int li, int  lj) {
		float[] copieligne = new float[this.getNbc()];
		int i = 0;
		while (i < this.nbc) {
			copieligne[i] = this.matfloat[li][i];
			i = i + 1;
		}
		i = 0;
		// copie de lj dans li
		while (i < this.nbc) {
			this.matfloat[li][i] = this.matfloat[lj][i];
			i = i + 1;
		}
		i = 0;
		// copie de li dans lj
		while (i < this.nbc) {
			this.matfloat[lj][i] = copieligne[i];
			i = i + 1;
		}
		return;
	}
	
	// multiplication par un scalaire non nul renvoi false si echoue
	boolean multScal(int li, float scalaire) {
		if(scalaire == 0) {
			return false;
		}
		int i = 0;
		while(i < this.nbc) {
			this.matfloat[li][i] = this.matfloat[li][i]* scalaire;
			i = i + 1;
		}
		return true;
	}
	
	// ajout du multiple par une autre ligne ex: L1 <- L1 + x*L2
	void addMultLine(int lmodif, int lscal, float scalaire) {
		int i = 0;
		while(i < this.nbc) {
			this.matfloat[lmodif][i] = this.matfloat[lmodif][i] + this.matfloat[lscal][i] * scalaire;
			i = i + 1;
		}
		return;
	}
	
	
	// retourne la valeur absolue du flottant
	float absolue (float i) {
		if (i < 0) {
			return (-1)*i;
		}
		return i;
	}
	
	// tourve la ligne  dans colonne i avec la valeur aboslue la plus grand 
	//retourne l'indice de ligne le plus grand
	int indicePivot(int icolonne, int r) {
		int i = r+1;
		// indice du max
		int indice = i;
		float max = this.absolue(this.matfloat[i][icolonne]);
		float compare;
		while(i < this.nbl) {
			compare = this.absolue(this.matfloat[i][icolonne]);
			if(max < compare) {
				indice = i;
			}
			i = i + 1;
		}
	return indice;
	}
	
	// calcul la matrice inverse
	MatriceFloat inverse() {
		int r = -1; // indice dernier pivot
		int j = 0;
		int i = 0;
		int k;
		// creation de l'inverse copie car algo modifie la matrice
		MatriceFloat inverse = new MatriceFloat();
		// creation de l'identite
		inverse.setIndentite(this.nbl);
		while(j < this.nbc) {
			k = indicePivot(j,r);
			
			// si pivot nul on change de colonne
			if(this.absolue(this.matfloat[k][j]) > 0.00000001) {
				r = r + 1;
			
				// on effectue les meme operation sur l'identite et la matrice
				inverse.multScal(k, 1/matfloat[k][j]);
				this.multScal(k, 1/matfloat[k][j]);
				if (k != r) {
					// on  place le pivot a la ligne r si pas encore le cas
					inverse.echange(k, r);
					this.echange(k, r);
				}
				while(i < this.nbl) {
					// simplifie les autres lignes
					if (i != r) {
						inverse.addMultLine(i, r, (-1)*this.matfloat[i][j]);
						this.addMultLine(i, r, (-1)*this.matfloat[i][j]);
					}
					i = i + 1;
				}
				i = 0;
			}
			else {
				// on a trouve une colonne null => non inversible
				return null;
			}
			j = j + 1;
		}
		return inverse;
	}
	
	// Calcul du determinant par pivot de gauss
	float determinant() {
		int r = -1; // indice dernier pivot
		int j = 0;
		int i = 0;
		int k = 0;
		float determinant = 1;
		// copie de matrice on fait calcul dessus
		MatriceFloat copie = new MatriceFloat();
		copie.setInitFloat(this.matfloat,this.nbl, this.nbc);
		
		// trigonalise la matrice
		while(j < this.nbc) {
			k = copie.indicePivot(j,r);
			if(copie.absolue(copie.matfloat[k][j]) > 0.0000001) {
				// diviser la ligne par la valeur du pivot
				r = r + 1;
				determinant = determinant * copie.matfloat[k][j];
				copie.multScal(k, 1/copie.matfloat[k][j]);
				if (k != r) {
					// on  place le pivot a la ligne r si pas encore le cas
					copie.echange(k, r);
					// permutation => *(-1)
					determinant = determinant * (-1);
				}

				while(i < this.nbl) {
					// simplifie les autres lignes
					if (i != r) {
						copie.addMultLine(i, r, (-1)*copie.matfloat[i][j]);
					}
					i = i + 1;
				}
				i = 0;
			}
			else {
				// pivot nul => det = 0
				return 0;
			}
			j = j + 1;
		}
		return determinant;
	}
	
	
}
