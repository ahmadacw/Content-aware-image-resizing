import java.util.Arrays;
/*this file contains the following functions :-
RemoveSeam: removes the seam given as the first argument, seam[0] will decide whether it is a vertical
of a horizontal seam, see the comment at the start of the function
AddsSeam: basically the same as RemoveSeam.
concat: concatenate two arrays.
greyScale: returns a greyScaled Image.
transpose: transposes the given matrix
*/
public class ImageManipulation {
	
	public static int[][] RemoveSeam2(int[][] image, int[] seam){
		int[][] image2 = new int[image.length][image[0].length-1];
		int [] p=null;
		int [] start=null;
		int [] end=null;
		for(int i=0;i<image.length;i++) {
			start=Arrays.copyOfRange(image[i],0,Math.min(seam[i],image[i].length));
			end=Arrays.copyOfRange(image[i],seam[i]+1, image[i].length);
			p=concat(start,end);
			image2[i]=p;
		}
		return image2;
	}
	public static int[][] addSeam(int[][] image, int[] seam){
		int[][] image2 = new int[image.length][image[0].length+1];
		int [] p=null;
		int [] start=null;
		int [] end=null;
		for(int i=0;i<image.length;i++) {
			int [] calculatedValue= {image[i][seam[i]]};
			start=Arrays.copyOfRange(image[i],0,Math.min(seam[i],image[i].length));
			end=Arrays.copyOfRange(image[i],seam[i], image[i].length);
			p=concat(start,calculatedValue);
			p=concat(p,end);
			image2[i]=p;
		}
		return image2;
	}
	
	public static  int[] concat(int [] arr1,int [] arr2) {
		int [] x= new int [arr1.length+arr2.length];
		for(int i =0;i<arr1.length;i++) {
			x[i]=arr1[i];		
		}
		for(int i=arr1.length;i<x.length;i++) {
			x[i]=arr2[i-arr1.length];
		}
		return x;
	}
		
	public static int[][] transpose(int[][] matrix){
		int [] [] mat_o=new int [matrix[0].length][matrix.length];
		for(int i=0;i<matrix.length;i++) {
			for(int j=0;j<matrix[0].length;j++) {
				mat_o[j][i]=matrix[i][j];
			}
		}
		return mat_o;
	}
}

	