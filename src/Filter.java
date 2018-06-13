import java.lang.Math;
/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 */
public final class Filter {

    /**
     * Get a pixel without accessing out of bounds
     * @param gray a HxW float array
     * @param row Y coordinate
     * @param col X coordinate
     * @return nearest valid pixel color
     */
    public static float at(float[][] gray, int row, int col) {

        /*
         * On test si les valeurs col et row entrent dans le tableau, si non,
         * on prend les valeurs les plus proches
         */
    	if(row < 0){
    		row += 1;
    	} else if(row >= gray.length){
    		row -= 1;
    	}

    	if(col < 0){
    		col += 1;
    	} else if(col >= gray[0].length){
    		col -= 1;
    	}

	    /*
    	 * On retourne la valeur du pixel à la colonne et rangée données (ou la "case" la plus proche)
    	 */
    	return gray[row][col];
    }

    /**
     * Convolve a single-channel image with specified kernel.
     * @param gray a HxW float array
     * @param kernel a MxN float array, with M and N odd
     * @return a HxW float array
     */
    public static float[][] filter(float[][] gray, float[][] kernel) {

    	int r = gray.length, c = gray[0].length;
        /*
    	 * On itère dans gray pour en réévaluer toutes les valeurs
    	 */
    	float[][] grayFiltered = new float[r][c];

    	for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {

				// summ sera la nouvelle valeur de l'emplacement (i;j) dans gray
				float summ = 0.0f;
				int k = 0;

				// On itère dans une sorte de sous-tableau de la taille de kernel, dans gray
				for (int i2 = i-1; i2 < i+2; i2++) {
					int l = 0;
					for (int j2 = j-1; j2 < j+2; j2++) {

						// On additionne les valeurs du tableau pour trouver summ
						summ += at(gray, i2, j2) * at(kernel, k, l);
						l += 1;
					}
					k += 1;
				}

				// On donne la nouvelle valeur donnée a l'emplacement (i;j)
				grayFiltered[i][j] = summ;
			}
		}
        return grayFiltered;
    }

    /**
     * Smooth a single-channel image
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] smooth(float[][] gray) {
    	// On créé le tableau kernel
        float[][] kernel = {
        		{1.0f/10.0f, 1.0f/10.0f, 1.0f/10.0f},
        		{1.0f/10.0f, 2.0f/10.0f, 1.0f/10.0f},
        		{1.0f/10.0f, 1.0f/10.0f, 1.0f/10.0f}
        };
        return filter(gray, kernel);
    }

    /**
     * Compute horizontal Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelX(float[][] gray) {
        // On créé le tableau de sobelX
    	float[][] sobX = {
    			{-1.0f, 0.0f, 1.0f},
    			{-2.0f, 0.0f, 2.0f},
    			{-1.0f, 0.0f, 1.0f}
    	};
    	return filter(gray, sobX);
    }

    /**
     * Compute vertical Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelY(float[][] gray) {
        // On créé le tableau de sobelY
    	float[][] sobY = {
    			{-1.0f, -2.0f, -1.0f},
    			{0.0f, 0.0f, 0.0f},
    			{1.0f, 2.0f, 1.0f}
    	};
    	return filter(gray, sobY);
    }

    /**
     * Compute the magnitude of combined Sobel filters
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobel(float[][] gray) {
    	int r = gray.length, c = gray[0].length;
    	/*
    	 * On itère dans gray par lignes puis par colonnes (?)
    	 */
    	float[][] sobel = new float[r][c];
    	float[][] sobY = sobelY(gray);
    	float[][] sobX = sobelX(gray);
    	float add;

    	for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				add = (float) Math.pow(sobY[i][j], 2) + (float) Math.pow(sobX[i][j], 2);
				sobel[i][j] = (float) Math.sqrt(add);
			}
		}
        return sobel;
    }
}
