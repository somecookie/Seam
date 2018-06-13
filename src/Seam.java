import java.util.ArrayList;
/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 */
public final class Seam {

    /**
     * Compute shortest path between {@code from} and {@code to}
     * @param successors adjacency list for all vertices
     * @param costs weight for all vertices
     * @param from first vertex
     * @param to last vertex
     * @return a sequence of vertices, or {@code null} if no path exists
     */
    public static int[] path(int[][] successors, float[] costs, int from, int to) {
    	int succsLgth = successors.length;
    	int [] bestPredecessor = new int[succsLgth];
		float[] distance = new float[succsLgth];

		/*
		 * On parcours les differents points et on leur tous une distance infinie (positive)
		 * sauf pour le dernier point dont la distance est de zéro
		 */
		for (int i = 0; i < succsLgth; i++) {
			distance[i] = Float.POSITIVE_INFINITY;
			bestPredecessor[i] = -1;

		}
		distance[from] = 0;
		boolean modified = true;

		/*
		 * Tant que la liste de points bestPredecessors est modifiée, on parcours tous les points et on calcule leur distance avec le point suivant
		 * si cette dernière est plus courte que la distance calculée au tour précédent, on l'ajoute à la liste
		 */
		while(modified){
			modified = false;
			for (int v = 0; v < succsLgth; v++) {
				for (int j = 0; j < successors[v].length; j++) {
					int n = successors[v][j];
					if (distance[n] > distance[v] + costs[n]) {
						distance[n] = distance[v] + costs[n];
						bestPredecessor[n] = v;
						modified = true;
					}
				}
			}
		}

		int n = to;
		boolean bool = true;
		ArrayList<Integer> tab = new ArrayList<Integer>();
		tab.add(to);
		while(bool){
			if(n==from) {
				bool = false;
			} else {
				tab.add( bestPredecessor[n]);
				n = bestPredecessor[n];
			} 
		}

		/*
		 *On calcule si la liste de points (le chemin) est vide, si c'est la cas on retourne un null, sinon on retourne le chemin
		 */
		boolean vide = true;
		for (int i = 0; i < bestPredecessor.length; i++) {
			if(bestPredecessor[i] != -1){
				vide = false;
				break;	
			}
		}
		if(vide == true) {
			return null;
		} else {
			int[] result = new int[tab.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = tab.get((result.length-1)-i);
			}
			return result;
		}
    }

    /**
     * Find best seam
     * @param energy weight for all pixels
     * @return a sequence of x-coordinates (the y-coordinate is the index)
     */
    public static int[] find(float[][] energy) {
    	int row = energy.length;
    	int width = energy[0].length;

    	int[][] successors = new int[row *width+2][];
    	successors(successors, width, row);

    	float[] costs = new float[row * width+2];
    	costs = costs(energy, costs, row, width);

    	int[] indices =  path(successors, costs, width*row, width*row+1);
    	int[] result = new int[indices.length-2];
    	int k = 0;
    	for (int i = 1; i < indices.length-1; i++) {
			result[k] = indices[i]%width;
			++k;
		}

    	return result;
    }

    /**
     * Prend les valeurs d'un tableau adeux dimensions et les met dans un tableau a une
     * @param energy
     * 			tableau a deux dimensions donné
     * @param costs
     * 			tableau a une dimension modifié
     * @param row
     * 			nombre de rangées
     * @param width
     * 			nombre de colonnes
     * @return un nouveau tableau a une dimension
     */
    public static float[] costs(float[][] energy, float[] costs, int row, int width){
    	int k = 0;
    	for (int i = 0; i < row; i++) {
			for (int j = 0; j < width; j++) {
				costs[k] = energy[i][j];
				k += 1;
			}
		}

    	costs[width*row] = 0;
    	costs[width*row + 1] = 0;

    	return costs;
    }

    /**
     * Méthode sérvant à transformer le tableau de successors en deux dimension donné en un tableau à une dimension
     * @param successors
     * 			tableau de successors donné (en deux dimensions (tableau de tableaux))
     * @param width
     * 			largeur du tableau donné
     * @param row
     * 			hauteur du tableau donné
     */
    public static void successors(int[][] successors, int width, int row){

    	for (int i = 0; i < row*width-width; i++) {
    		if(i%width == 0){
    				successors[i] = new int[2];
    				successors[i][0] = (i+width);
    				successors[i][1] = (i+width+1);
    			}
    		else if(i%width == width-1){
    			successors[i] = new int[2];
    			successors[i][0] = (i+width-1);
    			successors[i][1] = (i+width);
    		}
    		else{
    			successors[i] = new int[3];
    			successors[i][0] = i+width-1;
    			successors[i][1] = i + width;
    			successors[i][2] = i+width+1;
    		}
    	}

    	for (int i = row*width-width; i <row*width; i++) {
    		successors[i] = new int[1];
    		successors[i][0]= row*width+1;
    	}

        successors[row*width] = new int[width];
        for (int i = 0; i < width; i++) {
        	successors[row*width][i] = i;
    	}
        successors[row*width + 1] = new int[] {};
    }

    /**
     * Draw a seam on an image
     * @param image original image
     * @param seam a seam on this image
     * @return a new image with the seam in blue
     */
    public static int[][] merge(int[][] image, int[] seam) {
        // Copy image
        int width = image[0].length;
        int height = image.length;
        int[][] copy = new int[height][width];
        for (int row = 0; row < height; ++row)
            for (int col = 0; col < width; ++col)
                copy[row][col] = image[row][col];

        // Paint seam in blue
        for (int row = 0; row < height; ++row)
            copy[row][seam[row]] = 0x0000ff;

        return copy;
    }

    /**
     * Remove specified seam
     * @param image original image
     * @param seam a seam on this image
     * @return the new image (width is decreased by 1)
     */
    public static int[][] shrink(int[][] image, int[] seam) {
        int width = image[0].length;
        int height = image.length;
        int[][] result = new int[height][width - 1];
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < seam[row]; ++col)
                result[row][col] = image[row][col];
            for (int col = seam[row] + 1; col < width; ++col)
                result[row][col - 1] = image[row][col];
        }
        return result;
    }
}
