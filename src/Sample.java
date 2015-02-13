import java.io.IOException;
import foy.util.JINI;

public class Sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			JINI ini = new JINI("test.ini", false);
			ini.printIni();
			System.out.println("===================");
			ini.addSection("TEST");
			ini.addKVP("TEST", "x", "0");
			
			System.out.println(ini.containsSection("SECTION 1"));

			//System.out.println(ini.getBoolean("TEST", "x"));

			for (String s : ini.getSectionNames()) {
				System.out.println("SECTION: " + s);
				for (String k : ini.getKeysInSection(s)) {
					System.out.println("\tKEY: " + k);
				}
			}
			System.out.println("===================");
			for (String s : ini.printIni()) {
				System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
