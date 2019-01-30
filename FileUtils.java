import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This is a class for file scanners
 * @author yolanda
 *
 */
public class FileUtils {
	File objectsFile;
	File synonymsFile;
	File roomsFile;
	public FileUtils (Scanner scan) {
		System.out.println("What is your game name? >");
		boolean filesExist = false;
		String gameName = "";
		while (!filesExist) {
			gameName = scan.next();
			gameName = gameName.toLowerCase();
			gameName = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);
			File folder = new File(".");
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					if (file.toString().contains(gameName)) {
						filesExist=true;
						break;
					}
				}
			}
			if(!filesExist) {
				System.out.println("Files not found. Please check your spelling and try again.");
				System.out.println("What is your game name? >");	
			}
		} 
		if(filesExist) {
			this.objectsFile = new File(gameName+"Objects.txt");
			this.synonymsFile = new File(gameName+"Synonyms.txt");
			this.roomsFile = new File(gameName+"Rooms.txt");
		}
	}
	/**
	 * Returns the scanner for each game text file.
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 */
	public Scanner getScanner(String name) throws FileNotFoundException {
		if(name.equals("Objects")) {
			return new Scanner(this.objectsFile);
		} else if(name.equals("Synonyms")) {
			return new Scanner(this.synonymsFile);
		} else if(name.equals("Rooms")){
			return new Scanner(this.roomsFile);
		} else {
			return null;
		}
	}
	
}
