/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 */
public final class Color {

    /**
     * Returns red component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(float, float, float)
     */
    public static float getRed(int rgb) {
    	int red = rgb;
    	/*
    	 * On décales de 16 bits vers la droite pour que les 8 derniers bits soient ceux du rouge,
    	 * puis on applique un mask qui ne garde que ces 8 derniers bits
    	 */
    	red =  red >> 16;
    	red = red & 0b11111111;

    	return (float)red/255.0f;
    }

    /**
     * Returns green component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getBlue
     * @see #getRGB(float, float, float)
     */
    public static float getGreen(int rgb) {
    	int green = rgb;
    	/*
    	 * Mêmes opérations que pour le rouge
    	 */
    	green = green >> 8;
    	green = green & 0b11111111;

    	return (float)green/255.0f;
    }

    /**
     * Returns blue component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getGreen
     * @see #getRGB(float, float, float)
     */
    public static float getBlue(int rgb) {
    	int blue = rgb & 0b11111111;

    	return (float)blue/255.0f;
    }

    /**
     * Returns the average of red, green and blue components from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(float)
     */
    public static float getGray(int rgb) {
        /*
         * On fait la moyenne des trois couleurs pour obtenir la teinte de gris correspondant
         */
    	return (getRed(rgb)+getGreen(rgb)+getBlue(rgb))/3.0f;
    }

    /**
     * Returns packed RGB components from given red, green and blue components.
     * @param red a float between 0.0 and 1.0
     * @param green a float between 0.0 and 1.0
     * @param blue a float between 0.0 and 1.0
     * @return 32-bits RGB color
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     */
    public static int getRGB(float red, float green, float blue) {
        /*
         * On converti les trois float en int séparément
         */
    	return doAnInt(floatToInt(red), floatToInt(green), floatToInt(blue));
    }

    /**
     * Returns packed RGB components from given grayscale value.
     * @param red a float between 0.0 and 1.0
     * @param green a float between 0.0 and 1.0
     * @param blue a float between 0.0 and 1.0
     * @return 32-bits RGB color
     * @see #getGray
     */
    public static int getRGB(float gray) {

        int intGray = floatToInt(gray);
        return doAnInt(intGray, intGray, intGray);
    }

    /**
     * Converts packed RGB image to grayscale float image.
     * @param image a HxW int array
     * @return a HxW float array
     * @see #toRGB
     * @see #getGray
     */
    public static float[][] toGray(int[][] image) {
    	int width = image[0].length;
        int height = image.length;
    	float[][] imageGray = new float[height][width];
    	/*
    	 * On rempli le tableau imageGray des nuances de gris obtenues grâce à la méthode getGray
    	 */
    	for(int i = 0; i<width; i++)
        	for(int j=0; j<height; j++){
        		imageGray[j][i] = getGray(image[j][i]);
        	}
        return imageGray;
    }

    /**
     * Converts grayscale float image to packed RGB image.
     * @param channels a HxW float array
     * @return a HxW int array
     * @see #toGray
     * @see #getRGB(float)
     */
    public static int[][] toRGB(float[][] gray) {
    	int width = gray[0].length;
        int height = gray.length;
        int[][] image = new int[height][width];
        /*
         * On remplit le tableau image avec les valeurs obtenues grâce à la méthode getRGB
         */
    	for(int i = 0; i<width; i++)
        	for(int j=0; j<height; j++){
        		image[j][i] = getRGB(gray[j][i]);
        	}
        return image;
    }

    /**
     * Méthode sérvant à convertir un float compris entre 0 et 1 en un int compris entre 0 et 225
     * @param color
     * 			la couleur donnée en float
     * @return intColor
     * 			l'equivalent de la couleur donnée mais en int 
     */
    public static int floatToInt(float color){
    	if(color < 0.0f){
        	return 0;
    	} else if(color > 1.0f){
        	return 255;
        } else{
        	color *= 255.0f;
        }

        return (int)color;
    }

    /**
     * Méthode servant à créer une couleur sur 3 bytes quand on a les valeurs en int de RGB
     * @param color1
     * 			première couleur donnée en un byte
     * @param color2
     * 			deuxième couleur donnée en un byte
     * @param color3
     * 			troisième couleur donnée en un byte
     * @return nouvelle couleur en trois bytes
     */
    public static int doAnInt(int color1, int color2, int color3){
    	int rgb = color1;
        rgb = rgb << 8;
        rgb += color2;
        rgb = rgb << 8;
        rgb += color3;

        return rgb;
    }
}