
class AbstractTab{
    int size;
    int getSize(){
        return size;
    }
}

class TabInt extends AbstractTab{

    int[] tab;
    
    // modifier TabInt modifie aussi le tableau si declare explicitement
    void setInit(int[] tab, int size){
        this.size = size;
        this.tab = tab;
        return;
    }
    // cree une copie renvoi un erreur si pas bonne dimension
    void setInitCopy(int[] tab, int size){
        this.tab = new int[size];
        int i = 0;
        while(i < size){
            this.tab[i] = tab[i];
            i = i + 1;
        }
        this.size = size;
        return;
    }
     
    int getCase(int indice){
        return this.tab[indice];
    }
    
    // ajoute un element a l'indice i
    // il faut que i < this.size + 1
    boolean add(int element, int indice) {
    	if (indice > this.size || indice < 0) {
    		return false;
    	}
    	this.size = this.size + 1;
    	int[] tabnew = new int[this.size];
    	int i = 0;
    	
    	while(i < indice) {
    		tabnew[i] = this.tab[i];
    		i = i + 1;
    	}
    	tabnew[i] = element;
    	i = i + 1;
    	
    	
    	while(i < this.size) {
    		tabnew[i] = this.tab[i - 1];
    		i = i + 1;
    	}
    	this.tab = tabnew;
    	return true;
    }
    
    // ajoute un element en tete du tableau
    void addFirst(int valeur){
    	this.add(valeur, 0);
    	return;
    }
    
    // ajoute a la fin du tableau
    void addLast(int valeur){
    	this.add(valeur, this.size);
    	return;
    }
    
    // supprime la case i du tableau i < size renvoi true si ok false sinon
    boolean delete(int indice){
        if (indice >= size || indice < 0){
        	return false;
        }
        this.size = this.size - 1;
        int[] tabnew = new int[size];
        int i = 0;
        while (i <indice){
            tabnew[i] = this.tab[i];
            i = i + 1;
        }
        while (i < size){
            tabnew[i] = this.tab[i+1];
            i = i + 1;
        }
        this.tab = tabnew;
        return true;
    }

    void deleteFirst(){
        this.delete(0);
        return;
    }
    void deleteLast(){
        this.delete(this.size - 1);
        return;
    }
    
    // somme terme a terme
    boolean sumTab(TabInt tab){
        if (this.size !=tab.size ){
            return false;
        }
        int i = 0;
        while (i < size){
            this.tab[i] = this.tab[i] + tab.getCase(i);
            i = i + 1;
        }
        return true;
    }
 
    // multiplie terme a terme
    boolean multTab(TabInt tab){
        if (this.size !=tab.size ){
            return false;
        }
        int i = 0;
        while (i < size){
            this.tab[i] = this.tab[i] * tab.getCase(i);
            i = i + 1;
        }
        return true;
    }
    
    void print() {
    	int i = 0;
    	System.out.print("[");
    	while(i < this.size) {
    		System.out.print(this.getCase(i));
    		i = i + 1;
    		if (i != this.size) {
    			System.out.print(",");
    		}
    	}
    	System.out.print("]");
    	return;
    }
    
    void mergeSortAscending(){
    	triFusion(0, this.size - 1);
    	return;
    }
	
    protected void triFusion(int deb, int fin) {
    	if (deb!=fin){
    		int milieu=(fin+deb)/2;
    		triFusion(deb,milieu);
    		triFusion(milieu+1,fin);
    		fusion(deb,milieu,fin);
        }
    	return;
    }
    // tableau = deux tableau trie, modifie le tableau directement pour fusionner evite maximum perte de memoire
    protected void fusion(int deb1, int fin1, int fin2) {
    	int deb2 = fin1 + 1;
    	// on fait une copie du tableau1 au gauche 
    	// car si tout tab1 > tab2, on recopie tab2 et on ecrase donc tab1, au contraire, on ne change pas l'ordre
    	// d'ou une copie de tab1
    	int[] copietab1 = new int[fin1 - deb1 + 1];
    	int i = deb1;
    	
    	while(i <= fin1) {
    		copietab1[i - deb1] = this.tab[i];
    		i = i + 1;
    	}
    	
    	int compt1 = deb1;
    	int compt2 = deb2;
    	int j = deb1;
    	// parcours des deux tableaux
    	while(j <= fin2) {
    		if(compt1 == deb2) {
    			// tous les elements de tab1 ont été classe donc tous classe
    			// on sort de la boucle
    			j = fin2;
    		}
    		// tous les elements de tab2 ont ete classe, on rajoute les element de tab1
    		else if(compt2 == (fin2 + 1)){
    			this.tab[j] = copietab1[compt1 - deb1];
    			compt1 = compt1 + 1;
    		}
    		// utilise copie car peut etre deja ecrase ici tab1 < tab2, on ajoute tab1 
    		else if (copietab1[compt1 - deb1] < this.tab[compt2]){
    			this.tab[j] = copietab1[compt1 - deb1];
    			compt1 = compt1 + 1;
    		}
    		// ajoute element du second tableau
    		else {
    			this.tab[j] = this.tab[compt2];
    			compt2 = compt2 + 1;
    		}
    		j = j + 1;
    	}
    	return;
    }	
}

class TabFloat extends AbstractTab{

    float[] tab;
    
    // modifier TabInt modifie aussi le tableau si declare explicitement
    void setInit(float[] tab, int size){
    	this.tab = tab;
        this.size = size;
        this.tab = tab;
        return;
    }
    // cree une copie
    void setInitCopy(float[] tab, int size){
        this.tab = new float[size];
        int i = 0;
        while(i < size){
            this.tab[i] = tab[i];
            i = i + 1;
        }
        this.size = size;
        return;
    }
    
    
    float getCase(int indice){
        return this.tab[indice];
    }
    
    boolean add(float valeur, int indice){
        if (indice > valeur || indice < 0){
            return false;
        }
        this.size = this.size + 1;
        int i = 0;
        float[] tabnew = new float[size];
        while (i < indice){
            tabnew[i] = this.tab[i];
            i = i + 1;
        }
        // i est a l'indice 
        tabnew[i] = valeur;
        while(i < size){
            tabnew[i+1] = this.tab[i];
        }
        this.tab = tabnew;

        return true;
    }

    void addFirst(float valeur){
        this.add(valeur, 0);
        return;
    }
    
    void addLast(float valeur){
        this.add(valeur, size);
        return;
    }
   
    
    // supprime la case i du tableau
    boolean delete(int indice){
        if (indice >= size || indice < 0){
            return false;
        }
        this.size = this.size - 1;
        float[] tabnew = new float[size];
        int i = 0;
        while (i <indice){
            tabnew[i] = this.tab[i];
            i = i + 1;
        }
        while (i < size){
            tabnew[i] = this.tab[i+1];
            i = i + 1;
        }
        return true;
    }

    void deleteFirst(){
        this.delete(0);
        return;
    }
    void deleteLast(){
        this.delete(this.size - 1);
        return;
    }
    
    // somme terme a terme
    boolean sumTab(TabFloat tab){
        int i = 0;
        if (this.size !=tab.size){
            return false;
        }
        while (i < size){
            this.tab[i] = this.tab[i] + tab.getCase(i);
            i = i + 1;
        }
        return true;
    }
 
    // multiplie terme a terme
    boolean multTab(TabInt tab){
        int i = 0;
        if (this.size != tab.size){
            return false;
        }
        while (i < size){
            this.tab[i] = this.tab[i] * tab.getCase(i);
            i = i + 1;
        }
        return true;
    }
    
    
    void print() {
    	int i = 0;
    	System.out.print("[");
    	while(i < this.size) {
    		System.out.print(this.getCase(i));
    		i = i + 1;
    		if (i != this.size) {
    			System.out.print(",");
    		}
    	}
    	System.out.print("]");
    	
    	return;
    }

}



