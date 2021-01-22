
public class Racine {
	// calcul la racine carre par methode de Héron
	//a doit etre positif
	float absolue(float t) {
		if (t < 0) {
			return -t;
		}
		else {
			return t;
		}
	}
	
	
	
	
	float sqrt(float a) {
		float xinit = 1;
		int i = 0;
		float y;
		float seuil =(float) 1.0E-4;
			// calcul final
		y = (xinit + a/xinit)/2;
		while(this.absolue(y - xinit) > seuil) {
			xinit = y;
			y = (xinit + a/xinit)/2;
		}
		return y;
	}
}
