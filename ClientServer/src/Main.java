
public class Main
{
	public static void main(String[] args) 
	{
		FileManager.deleteAllFiles();
		Content c1 = new Content("nome1","tipo1","Teste1");
		Content c2 = new Content("nome2","tipo2","Teste2");
		Content c3 = new Content("nome3","tipo3","Teste3");
		FileManager.writeFile(c1,3);
		FileManager.writeFile(c2,5);
		FileManager.writeFile(c3,2);

		
		
	
	}

}
