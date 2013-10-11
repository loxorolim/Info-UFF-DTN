import java.awt.image.BufferedImage;


public class Main
{
	public static void main(String[] args) 
	{
	
//		Content c1 = new Content("nome1","tipo1","Teste1");
//		Content c2 = new Content("nome2","tipo2","Teste2");
//		Content c3 = new Content("nome3","tipo3","Teste3");
//		FileManager.writeFile(c1,3);
//		FileManager.writeFile(c2,5);
//		FileManager.writeFile(c3,2);
		FileManager.deleteAllFiles();
		BufferedImage img1 = FileManager.readImageFromFile("C:\\Users\\Public\\Pictures\\Sample Pictures\\Crisântemo.jpg");
		FileManager.writeFile(img1, 3);

		
	
	}

}
