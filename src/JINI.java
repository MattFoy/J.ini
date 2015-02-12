import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class JINI {

	private String path;
	private BufferedReader br;
	private HashMap<String, HashMap<String, String>> ini;

	public JINI(String path) throws IOException {
		this.path = path;
		br = new BufferedReader(new FileReader(path));
		load();
	}

	public void load() throws IOException {
		int lineNum = 0;
		ini = new HashMap<String, HashMap<String, String>>();
		String line;
		String currentSection = "";
		while ((line = br.readLine()) != null) {
			line = line.trim();
			lineNum++;
			if (line.length() == 0 || line.startsWith("#")) {
				continue; // comment or blank line
			} else if (line.startsWith("[") && line.endsWith("]")) {
				currentSection = line.substring(1, line.length() - 1);
				try {
					addSection(currentSection);
				} catch (IOException ioe) {
					throw new IOException("[Line " + lineNum + "] "
							+ ioe.getMessage());
				}
			} else if (line.contains("=")) {
				String[] kvp = line.split("=");
				if (kvp.length != 2 || kvp[0] == "" || kvp[1] == "") {
					throw new IOException(
							"[Line "
									+ lineNum
									+ "] Invalid Key-Value-Pair in file. Invalid syntax.");
				} else {
					try {
						addKVP(currentSection, kvp[0], kvp[1]);
					} catch (IOException ioe) {
						throw new IOException("[Line " + lineNum + "] "
								+ ioe.getMessage());
					}
				}
			} else {
				throw new IOException("[Line " + lineNum + "] Invalid line: "
						+ line);
			}
		}
	}

	public void addSection(String section) throws IOException {
		if (ini.containsKey(section)) {
			throw new IOException("Duplicate section header: " + section);
		} else {
			ini.put(section, new HashMap<String, String>());
		}
	}

	public void addKVP(String section, String key, String value)
			throws IOException {
		if (ini.containsKey(section)) {
			if (!ini.get(section).containsKey(key)) {
				ini.get(section).put(key.trim(), value.trim());
			} else {
				throw new IOException("Duplicate key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public void deleteSection(String section) throws IOException {
		if (ini.containsKey(section)) {
			ini.remove(section);
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public void deleteKVP(String section, String key) throws IOException {
		if (ini.containsKey(section)) {
			if (ini.get(section).containsKey(key)) {
				ini.get(section).remove(key);
			} else {
				throw new IOException("No such key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public void setKVP(String section, String key, String value)
			throws IOException {
		if (ini.containsKey(section)) {
			if (ini.get(section).containsKey(key)) {
				ini.get(section).put(key, value);
			} else {
				throw new IOException("No such key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public boolean containsSection(String section) {
		return ini.containsKey(section);
	}

	public boolean sectionContainsKey(String section, String key)
			throws IOException {
		if (ini.containsKey(section)) {
			if (ini.get(section).containsKey(key)) {
				return ini.get(section).containsKey(key);
			} else {
				throw new IOException("No such key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public String getString(String section, String key) throws IOException {
		if (ini.containsKey(section)) {
			if (ini.get(section).containsKey(key)) {
				return ini.get(section).get(key);
			} else {
				throw new IOException("No such key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public int getInt(String section, String key) throws NumberFormatException,
			IOException {
		return Integer.parseInt(getString(section, key));
	}

	public Float getFouble(String section, String key)
			throws NumberFormatException, IOException {
		return Float.parseFloat(getString(section, key));
	}

	public Double getDouble(String section, String key)
			throws NumberFormatException, IOException {
		return Double.parseDouble(getString(section, key));
	}

	public char getChar(String section, String key)
			throws NumberFormatException, IOException {
		return (getString(section, key).charAt(0));
	}

	public void printIni() {
		for (Entry<String, HashMap<String, String>> kvps : ini.entrySet()) {
			System.out.println("[" + kvps.getKey() + "]");
			for (Entry<String, String> kvp : kvps.getValue().entrySet()) {
				System.out.println("\t" + kvp.getKey() + "=" + kvp.getValue());
			}
		}
	}

	public void saveFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));
		for (Entry<String, HashMap<String, String>> kvps : ini.entrySet()) {
			writer.write("[" + kvps.getKey() + "]");
			writer.newLine();
			for (Entry<String, String> kvp : kvps.getValue().entrySet()) {
				writer.write("\t" + kvp.getKey() + "=" + kvp.getValue());
				writer.newLine();
			}
		}
		writer.flush();
		writer.close();
	}

	// for testing
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			JINI ini = new JINI("test.ini");
			ini.printIni();
			System.out.println("===================");
			ini.addSection("TEST");
			ini.addKVP("TEST", "x", "2");
			ini.printIni();
			//ini.saveFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
