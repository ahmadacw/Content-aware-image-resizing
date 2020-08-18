import java.awt.Color;

public class SeamCarving {
	
	public static int getRed(int pixel) {
		return new Color(pixel).getRed();
	}
	
	public static int getBlue(int pixel) {
		return new Color(pixel).getBlue();
	}
	
	public static int getGreen(int pixel) {
		return new Color(pixel).getGreen();
	}
	
	public static void paintRed(int[][] image, int[] seam) {
		for(int i=0;i<seam.length;i++) {
			image[i][seam[i]]=new Color(255,0,0).getRGB();
		}
	}
	
	/**
	 * calculate the energy between the two pixels
	 * @param pixel1
	 * @param pixel2
	 * @return the energy value between the two pixels
	 */
	public static double energyCal(int pixel1,int pixel2) {
		int Ri,R1,Gi,G1,Bi,B1;
		Ri=getRed(pixel1);
		Gi=getGreen(pixel1);
		Bi=getBlue(pixel1);
		R1=getRed(pixel2);
		G1=getGreen(pixel2);
		B1=getBlue(pixel2);
		return (Math.abs(Ri-R1)+Math.abs(Bi-B1)+Math.abs(Gi-G1))/3.;
		
	}
	
	/**
	 * calc Pmn function in (x,y)
	 * @param imgArray
	 * @param m
	 * @param n
	 * @param x
	 * @param y
	 * @return Pmn(x,y)
	 */
	public static double Pmn(int[][] imgArray, int m, int n, int x, int y) {
		double sum=0;
		for(int k=x-4;k<=x+4;k++) {
			for(int l=y-4;l<=y+4;l++) {
				if((k>=0 && k<imgArray[0].length) && (l>=0 && l<imgArray.length)) {
					sum+=GreyScale(imgArray,k,l);
				}
			}
		}
		if(sum==0) {
			return 0;
		}
		return GreyScale(imgArray,m,n)/sum;
	}
	
	
	/**
	 * calc the enropy of (x,y)
	 * @param imgArray
	 * @param x
	 * @param y
	 * @return entropy
	 */
	public static double entropyCal(int[][] imgArray, int x, int y) {
		double sum=0;
		double pmn;
		for(int m=x-4;m<=x+4;m++) {
			for(int n=y-4;n<=y+4;n++) {
				if((m>=0 && m<imgArray[0].length) && (n>=0 && n<imgArray.length)) {
					pmn=Pmn(imgArray,m,n,x,y);
					if(pmn==0)
						sum+=0;
					else {
						sum+=pmn*Math.log(pmn);
					}
				}
			}
		
		}
		return -sum;
	}
	/**
	 * 
	 * @param imgArray
	 * @param energyType - the type of energy calculation with or with not entropy
	 * @param i
	 * @param j
	 * @return the energy value of imgArray[i,j] normalized
	 */
	public static double EnergyFunction(int[][] imgArray, int energyType, int i, int j) {
		double sum=0;
		double norm=0;
		for(int k=i-1;k<=i+1;k++) {
			for(int w=j-1;w<=j+1;w++) {
				if(k!=i || w!=j) {
					if((k>=0 && k<imgArray.length) && (w>=0 && w<imgArray[0].length)) {
						sum+=energyCal(imgArray[i][j],imgArray[k][w]);
						norm++;
					}
				}
			}
		}
		sum = sum/norm;
		if(energyType == 1) {
			sum+= entropyCal(imgArray,j,i);
		}
		return sum;
	}

	/**
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @return color - the greyscale value in image[h,w]
	 */
	public static int GreyScale(int[][] image, int w, int h) {
		int blue=getBlue(image[h][w]);
		int green=getGreen(image[h][w]);
		int red=getRed(image[h][w]);
		int color=(int) ((red + green + blue)/3);
		return color;
	}

	
	
	
	/**
	 * this function calculate the array with the summing energies using dynamic programming
	 * @param image
	 * @return the energy array
	 */
	public static double[][] dynamicPrograming(int[][] image, int energyType, double[][] energy){
		double[][] energyMat = new double[image.length][image[0].length];
		double temp;
		for(int i=0;i<image[0].length;i++) {
			energyMat[0][i]=energy[0][i];
		}
		if(energyType!=2) {
			for(int i=1;i<image.length;i++) {
				for(int j=0;j<image[0].length;j++) {
					if(j-1>=0) {
						temp = Math.min(energyMat[i-1][j], energyMat[i-1][j-1]);
						if(j+1<image[0].length) {
							temp = Math.min(temp, energyMat[i-1][j+1]);
						}
					}
					else {
						if(j+1<image[0].length) {
							temp = Math.min(energyMat[i-1][j], energyMat[i-1][j+1]);
						}
						else {
							temp = energyMat[i-1][j];
						}
					}
					energyMat[i][j] = energy[i][j]+temp;
				}
			}
		}
		if(energyType==2) {
			double Cl;
			double Cu;
			double Cr;
			for(int i=1;i<image.length;i++) {
				for(int j=0;j<image[0].length;j++) {
					if(j-1>=0) {
						if(j+1<image[0].length) {
							Cl=Math.abs(energy[i][j+1]-energy[i][j-1])+Math.abs(energy[i-1][j]-energy[i][j-1]);
							Cu=Math.abs(energy[i][j+1]-energy[i][j-1]);
							Cr=Math.abs(energy[i][j+1]-energy[i][j-1])+Math.abs(energy[i-1][j]-energy[i][j+1]);
							Cl=energyMat[i-1][j-1]+Cl;
							Cu=energyMat[i-1][j]+Cu;
							Cr=energyMat[i-1][j+1]+Cr;
							temp=Math.min(Cl,Cu);
							temp=Math.min(temp, Cr);
						}
						else {
							Cl=Math.abs(energy[i][j-1]-energy[i-1][j]);
							temp=Cl+energyMat[i-1][j-1];
						}
						temp = Math.min(energyMat[i-1][j], energyMat[i-1][j-1]);
					}
					else {
						if(j+1<image[0].length) {
							Cr=Math.abs(energy[i][j+1]-energy[i-1][j]);
							temp = Cr+energyMat[i-1][j+1];
						}
						else {
							temp = energyMat[i-1][j];
						}
					}
					energyMat[i][j] = temp;
				}
			}
		}
		return energyMat;
	}

	
	

	/**
	 * create the seam using the dynamic energy matrix for the image
	 * @param image
	 * @param dynamicEnergy - the dynamic energy matrix
	 * @return
	 */
	public static int[] seamArray(int[][] image, double[][] dynamicEnergy, int bit) {
		int[] seam = new int[dynamicEnergy.length];
		double min = dynamicEnergy[dynamicEnergy.length-1][0];
		int index=0;
		for(int i=1; i<dynamicEnergy[0].length;i++) {
			if(dynamicEnergy[dynamicEnergy.length-1][i]<min) {
				min=dynamicEnergy[dynamicEnergy.length-1][i];
				index=i;
			}
		}
		seam[seam.length-1]=index;
		if(bit==0) {
			for(int i=seam.length-2;i>=0;i--) {
				if(seam[i+1]==0) {
					if(dynamicEnergy[i][seam[i+1]]<=dynamicEnergy[i][seam[i+1]+1])
						seam[i]=seam[i+1];
					else
						seam[i]=seam[i+1]+1;
				}
				else if(seam[i+1]==dynamicEnergy[0].length-1) {
					if(dynamicEnergy[i][seam[i+1]]<dynamicEnergy[i][seam[i+1]-1])
						seam[i]=seam[i+1];
					else
						seam[i]=seam[i+1]-1;
				}
				else {
					if(dynamicEnergy[i][seam[i+1]]<dynamicEnergy[i][seam[i+1]-1]) {
						if(dynamicEnergy[i][seam[i+1]]<=dynamicEnergy[i][seam[i+1]+1])
							seam[i]=seam[i+1];
						else
							seam[i]=seam[i+1]+1;
					}
					else {
						if(dynamicEnergy[i][seam[i+1]-1]<=dynamicEnergy[i][seam[i+1]+1]) {
							seam[i]=seam[i+1]-1;
						}
						else
							seam[i]=seam[i+1]+1;
					}
						
				}
			}
		}
		else {
			for(int i = 0;i<seam.length;i++) {
				seam[i]=index;
			}
		}
		return seam;
	}


	/**
	 * calculate the new energy matrix
	 * @param image after remove seam
	 * @param imageEnergy before remove seam
	 * @param seam
	 * @param energyType
	 * @return the new energy matrix
	 */
	public static double[][] energyMat(int[][] image,double[][] imageEnergy,int[] seam,int energyType){
		double[][] energy= new double[image.length][image[0].length];
		int[][] init = new int[image.length][image[0].length];
		if(energyType==0 || energyType==2) {
			for(int i =0;i<seam.length;i++) {
				for(int j=i-1;j<=i+1;j++) {
					if(j>=0 &&j<imageEnergy.length) {
						for(int k=seam[j]-1;k<=seam[j]+1;k++){
							if((j>=0 && j<imageEnergy.length) && (k>=0 && k<imageEnergy[0].length)) {
								if(k<seam[j] && init[j][k]!=-1) {
									energy[j][k]=EnergyFunction(image,energyType,j,k);
									init[j][k]=-1;
								}
								if(k>seam[j] && init[j][k-1]!=-1) {
									energy[j][k-1]=EnergyFunction(image,energyType,j,k-1);
									init[j][k-1]=-1;
								}
							}
							
						}
					}
					
				}
			}
			for(int i=0;i<imageEnergy.length;i++) {
				for(int j=0;j<imageEnergy[0].length;j++) {
					if(j<seam[i])
						if(init[i][j]!=-1)
							energy[i][j]=imageEnergy[i][j];
					if(j>seam[i])
						if(init[i][j-1]!=-1)
							energy[i][j-1]=imageEnergy[i][j];
				}
			}
		}
		if(energyType == 1) {
			for(int i =0;i<seam.length;i++) {
				for(int j=i-4;j<=i+4;j++) {
					if(j>=0 &&j<imageEnergy.length) {
						for(int k=seam[j]-4;k<=seam[j]+4;k++){
							if((j>=0 && j<imageEnergy.length) && (k>=0 && k<imageEnergy[0].length)) {
								if(k<seam[j] && init[j][k]!=-1) {
									energy[j][k]=EnergyFunction(image,energyType,j,k);
									init[j][k]=-1;
								}
								if(k>seam[j] && init[j][k-1]!=-1) {
									energy[j][k-1]=EnergyFunction(image,energyType,j,k-1);
									init[j][k-1]=-1;
								}
							}
							
						}
					}
					
				}
			}
			for(int i=0;i<imageEnergy.length;i++) {
				for(int j=0;j<imageEnergy[0].length;j++) {
					if(j<seam[i])
						if(init[i][j]!=-1)
							energy[i][j]=imageEnergy[i][j];
					if(j>seam[i])
						if(init[i][j-1]!=-1)
							energy[i][j-1]=imageEnergy[i][j];
				}
			}
		}
		return energy;
	}
	
	/**
	 * calculate the mapping matrix
	 * @param image after removing seam
	 * @param map before remove seam
	 * @param seam
	 * @param bit, if bit == 1 we add the seam, else we remove
	 * @return the new map
	 */
	public static int[][] mapMatrix(int[][] image, int[][] map, int[] seam, int bit){
		int[][] newMap=new int[map.length][image[0].length];
		if(bit!=1) {
			for(int i=0;i<map.length;i++) {
				for(int j=0;j<map[0].length;j++) {
					if(j<seam[i]) {
						newMap[i][j]=map[i][j];
					}
					if(j>seam[i]) {
						newMap[i][j-1]=map[i][j];
					}
				}
			}
		}
		else {
			for(int i=0;i<map.length;i++) {
				for(int j=0;j<map[0].length;j++) {
					if(j<seam[i]) {
						newMap[i][j]=map[i][j];
					}
					if(j>seam[i]) {
						newMap[i][j-1]=map[i][j]+1;
					}
				}
			}
		}
		return newMap;
	}
	
	/**
	 * calc the energy matrix
	 * @param image
	 * @param energyType
	 * @return energy matrix
	 */
	public static double[][] energyMat(int[][] image,int energyType){
		double[][] energy=new double[image.length][image[0].length];
		for(int i=0;i<image.length;i++) {
			for(int j=0;j<image[0].length;j++) {
				energy[i][j]=EnergyFunction(image, energyType, i, j);
			}
		}
		return energy;
	}
	
	
	/**
	 *  remove k seams
	 * @param image
	 * @param energyType
	 * @param k
	 * @return the new image
	 */
	public static int[][] removekSeams(int[][] image, int energyType, int k){
		double[][] energy =energyMat(image, energyType);
		double[][] dyn=null;
		int[] seam;
		for(int i=0;i<k;i++) {
			dyn=dynamicPrograming(image, energyType, energy);
			seam=seamArray(image, dyn, 0);
			image=ImageManipulation.RemoveSeam2(image, seam);
			energy=energyMat(image, energy, seam, energyType);
		}
		return image;
	}
	
	/**
	 * add k seams
	 * @param image
	 * @param energyType
	 * @param k
	 * @return the new image
	 */
	public static int[][] addkSeams(int[][] image, int energyType, int k){
		double[][] energy =energyMat(image, energyType);
		double[][] dyn=null;
		int[][] image2=new int[image.length][image[0].length];
		int[][] map=new int[image.length][image[0].length];
		int[] seam;
		int[] seam2;
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[0].length;j++) {
				map[i][j]=j;
				image2[i][j]=image[i][j];
			}
		}
		
		for(int i=0;i<k;i++) {
			dyn=dynamicPrograming(image2, energyType, energy);
			seam=seamArray(image2, dyn,0);
			image2=ImageManipulation.RemoveSeam2(image2, seam);
			energy=energyMat(image2, energy, seam, energyType);
			seam2=seamMaped(seam,map);
			image=ImageManipulation.addSeam(image, seam2);
			//for(int j=0;j<seam2.length;j++) {
			//	image[j][seam2[j]+1]=avgSeam(image,j,seam2[j]+1);
			//}
			map=mapMatrix(image2, map, seam, 0);
		}
		return image;
	}
	
	/**
	 * calc the mapped seam
	 * @param seam
	 * @param map
	 * @return the mapped seam
	 */
	public static int[] seamMaped(int[] seam,int[][] map){
		int[] seam2=new int[seam.length];
		for(int i=0;i<seam.length;i++) {
			seam2[i]=map[i][seam[i]];
		}
		return seam2;
	}

	/**
	 * calc the avg value of image[i][j]
	 * @param image
	 * @param i
	 * @param j
	 * @return the avg value of the image[i][j]
	 */
	public static int avgSeam(int[][] image,int i, int j) {
		int norm=0;
		int sum_red=0;
		int sum_blue=0;
		int sum_green=0;
		for(int k=i-1;k<=i+1;k++) {
			for(int w=j-1;w<=j+1;w++) {
				if(k!=i || w!=j) {
					if((k>=0 && k<image.length) && (w>=0 && w<image[0].length)) {
						sum_red+=getRed(image[k][w]);
						sum_blue+=getBlue(image[k][w]);
						sum_green+=getGreen(image[k][w]);
						norm++;
					}
				}
			}
		}
		return new Color(sum_red/norm,sum_green/norm,sum_blue/norm).getRGB();
	}
}



