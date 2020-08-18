
public class Main {

	public static void main(String[] args) throws Exception {
		ImageOperator image = new ImageOperator(args[0]);
		int width=Integer.parseInt(args[1]);
		int rows=Integer.parseInt(args[2]);
		int energyType=Integer.parseInt(args[3]);
			if(energyType!=0 && energyType!=1 && energyType!=2)
				throw new Exception("Energy type is invalid");
		int k;
		int[][] img=image.GetMatrix();
		if(img[0].length>width) {
			img=SeamCarving.removekSeams(img, energyType,img[0].length-width);
		}
		else if(img[0].length<width) {
			while(img[0].length-width!=0) {
				k=width-img[0].length;
				if(k>img[0].length/4) {
					k=img[0].length/4;
					img=SeamCarving.addkSeams(img, energyType, k);
				}
				else
					img=SeamCarving.addkSeams(img,energyType,k);
			}
		}
		img=ImageManipulation.transpose(img);
		if(img[0].length>rows) {
			img=SeamCarving.removekSeams(img, energyType,img[0].length-rows);
		}
		else if(img[0].length<rows) {
			while(img[0].length-rows!=0) {
				k=rows-img[0].length;
				if(k>img[0].length/4) {
					k=img[0].length/4;
					img=SeamCarving.addkSeams(img, energyType, k);
				}
				else
					img=SeamCarving.addkSeams(img,energyType,k);
			}
		}
		img=ImageManipulation.transpose(img);
		
		ImageOperator.copy(img,args[4]);
	}

}
