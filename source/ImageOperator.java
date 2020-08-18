import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageOperator {
	BufferedImage img=null;
	public	ImageOperator(String s) throws Exception{
		this.img= getImage(s);
	}
	public int[][] GetMatrix(){
		System.out.println("width: "+Integer.toString(this.img.getWidth())+" Height: "+Integer.toString(this.img.getHeight()));
		int [][] matrix= new int [this.img.getHeight()][this.img.getWidth()];
		for(int y=0; y<this.img.getHeight();y++) {
			for(int x=0; x<this.img.getWidth();x++) {
				matrix[y][x]=this.img.getRGB(x,y);
			}
		}
		System.out.println("matrix returned");
		return matrix;
	}
	
	public static void copy(int [][] matrix_og, String fout) throws IOException {
		BufferedImage out= new BufferedImage(matrix_og[0].length,matrix_og.length,1);
		int [] p=null;
		for(int i=0;i<matrix_og.length;i++) {
			p=matrix_og[i];
			for(int j=0;j<p.length;j++) {
				out.setRGB(j, i,p[j]);
			}
		}
	File output= new File(fout);
	ImageIO.write(out,"jpg",output);
	}
	public BufferedImage getImage(String s) throws Exception {
	
		try {
			BufferedImage image;
			image = ImageIO.read(new File(s));
			System.out.println("file opened");
			return image;
		} catch (IOException e) {
			throw new IOException("file not found");
		}
	}
}
