import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class JINI {
	
	private String path;	
	private BufferedReader br;
	private HashMap<String, HashMap<String,String>> ini;
	
	
	public JINI(String path) throws IOException {
		this.path = path;
		br = new BufferedReader(new FileReader(path));
		load();
	}
	
	public void load() throws IOException {
		ini = new HashMap<String, HashMap<String,String>>();
		String line;
		line = br.readLine();
	}
	
	
	//for testing
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
