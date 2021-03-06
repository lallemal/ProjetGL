class MatriceInt extends AbstractMatrice{
	int[][] matint;
	
	// reference vers tableau
	void setInit(int[][] mat, int nbl, int nbc) {
		this.nbl = nbl;
		this.nbc = nbc;
		this.matint = mat;
		return;
	}

	int getCase(int i, int j) {
		return this.matint[i][j];
	}
	
	
	void setInitCopy(int[][] mat, int nbl, int nbc) {
		this.matint = new int[nbl][nbc];
		this.nbl = nbl;
		this.nbc = nbc;
		int i = 0;
		int j = 0;
		while(i < nbl) {
			while(j < nbc) {
				this.matint[i][j] = mat[i][j];
				j = j + 1;
			}
		j = 0;
		i = i + 1;
		
		}
		return;
	}

	void print() {
		print("[");
		int l = 0;
		int c = 0;
		while(l < this.nbl) {
			while( c < this.nbc){
				if (l == 0 && c == 0) {
					print("[" + this.matint[l][c]);
				}
				else if (l > 0 && c == 0) {
					print(" [" + this.matint[l][c]);
				}
				else{
					print(" " + this.matint[l][c]);
				}
				c = c + 1;
				if (c < nbc) {
					print(",");
				}
			}
			print("]");
			if (l < nbl -1) {
				println(",");
			}
			c = 0;
			l = l + 1;
		}
		println("]");
	}

	// retourne null si dimension incorrecte
	MatriceInt prodMat(MatriceInt m) {
		int [][] tabfinal = new int[this.nbl][m.getNbc()];
		MatriceInt matfinal = new MatriceInt();
		matfinal.setInit(tabfinal, this.nbl, m.getNbc());
		if(this.nbc != m.getNbl()) {
			return null;
		}
		int i = 0;
		int j = 0;
		int k = 0;
		// lignes
		while(i < this.nbl) {
			while( j < m.getNbc()) {
				while(k < this.nbc) {
					tabfinal[i][j] = tabfinal[i][j] + this.matint[i][k]* m.getCase(k, j);
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
	
	MatriceInt transpose() {
		int[][] tab = new int[this.getNbc()][this.getNbl()];
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
		MatriceInt transpose = new MatriceInt();
		transpose.setInit(tab, this.getNbc(), this.getNbl());
		return transpose;
	}
	
	// utilise fonction de MatriceFloat pour algorithme inverse
	// retourne la matrice inverse
	MatriceFloat inverse() {
		MatriceFloat f = new MatriceFloat();
		// transformation en matrice de flottant copie creer
		f.setInitInt(this.matint, this.nbl, this.nbc);
		return f.inverse();
	}
	
	
	float determinant() {
		MatriceFloat f = new MatriceFloat();
		// transformation en matrice de flottant copie creer
		f.setInitInt(this.matint, this.nbl, this.nbc);
		return f.determinant();
	}
	
	
	
	int abs(int number) {
		if( number > 0) {
			return number;
		}else {
			return -number;
		}
	}

	int norme1() {
		int result = 0;
		int l = 0;
		int c = 0;
		while(l < nbl) {
			while( c < nbc) {
				result = result + abs(this.matint[l][c]);
			}
		}
		return result;
	}
	
}