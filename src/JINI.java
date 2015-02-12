import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JINI {

	// for testing
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			JINI ini = new JINI("test.ini");
			ini.printIni();
			System.out.println("===================");
			ini.addSection("TEST");
			ini.addKVP("TEST", "x", "0");
			
			System.out.println(ini.getBoolean("TEST", "x"));
			
			//for (String s : ini.printIni()) { System.out.println(s); }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String path;
	private BufferedReader br;
	private Sections ini;
	private ArrayList<String> comments = new ArrayList<String>();

	public JINI(String path) throws IOException {
		this.path = path;
		br = new BufferedReader(new FileReader(path));
		load();

	}

	public void load() throws IOException {
		int lineNum = 0;
		ini = new Sections();
		String line;
		String currentSection = "";
		while ((line = br.readLine()) != null) {
			line = line.trim();
			lineNum++;
			if (line.length() == 0) {
				continue; // blank line, can be ignored
			} else if (line.startsWith("#")) {
				comments.add(line);
				continue; // comment or blank line
			} else if (line.startsWith("[") && line.endsWith("]")) {
				currentSection = line.substring(1, line.length() - 1);
				try {
					Section newSec = addSection(currentSection);
					newSec.comments = this.comments;
					this.comments = new ArrayList<String>();
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
						KVP newKvp = addKVP(currentSection, kvp[0], kvp[1]);
						newKvp.comments = this.comments;
						this.comments = new ArrayList<String>();
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

	public Section addSection(String section) throws IOException {
		if (ini.contains(section)) {
			throw new IOException("Duplicate section header: " + section);
		} else {
			Section sec = new Section(section);
			ini.add(sec);
			return sec;
		}
	}

	public KVP addKVP(String section, String key, String value)
			throws IOException {
		if (ini.contains(section)) {
			if (!ini.get(section).kvps.contains(key)) {
				KVP kvp = new KVP(key.trim(), value.trim());
				ini.get(section).kvps.add(kvp);
				return kvp;
			} else {
				throw new IOException("Duplicate key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public void deleteSection(String section) throws IOException {
		if (ini.contains(section)) {
			ini.remove(section);
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public void deleteKVP(String section, String key) throws IOException {
		if (ini.contains(section)) {
			if (ini.get(section).kvps.contains(key)) {
				ini.get(section).kvps.remove(key);
			} else {
				throw new IOException("No such key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public void setKVP(String section, String key, String value)
			throws IOException {
		if (ini.contains(section)) {
			if (ini.get(section).kvps.contains(key)) {
				ini.get(section).kvps.get(key).value = value;
			} else {
				throw new IOException("No such key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	public boolean containsSection(String section) {
		return ini.contains(section);
	}
	
	public boolean sectionContainsKey(String section, String key)
			throws IOException {
		if (ini.contains(section)) {
			if (ini.get(section).kvps.contains(key)) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}
	
	// Attempts to return the value of the key in the given section.
	public String getString(String section, String key) throws IOException {
		if (ini.contains(section)) {
			if (ini.get(section).kvps.contains(key)) {
				return ini.get(section).kvps.get(key).value;
			} else {
				throw new IOException("No such key: " + key);
			}
		} else {
			throw new IOException("No such section header: " + section);
		}
	}

	// Attempts to return the value as an int
	public int getInt(String section, String key) throws NumberFormatException,
			IOException {
		return Integer.parseInt(getString(section, key));
	}
	
	// Attempts to return the value as a float
	public Float getFloat(String section, String key)
			throws NumberFormatException, IOException {
		return Float.parseFloat(getString(section, key));
	}

	// Attempts to return the value as a double
	public Double getDouble(String section, String key)
			throws NumberFormatException, IOException {
		return Double.parseDouble(getString(section, key));
	}
	
	// Gets the first char of the value.
	public char getChar(String section, String key)
			throws NumberFormatException, IOException {
		return (getString(section, key).charAt(0));
	}
	
	// Attempts to derive a boolean from the value.
	public Boolean getBoolean(String section, String key)
			throws IOException {
		String value = getString(section, key);
		if ((value.toLowerCase() == "true") || (value == "1")) {
			return true;
		} else if ((value.toLowerCase() == "false") || (value == "0")) {
			return false;
		} else {
			throw new IOException("Invalid boolean format: " + value);
		}
	}

	// Returns the contents of the ini file as an ArraLisy of lines.
	public ArrayList<String> printIni() {
		ArrayList<String> result = new ArrayList<String>();
		for (Section sec : ini) {
			for (String s : sec.comments) {
				result.add(s);
			}
			result.add("[" + sec.name + "]");
			for (KVP kvp : sec.kvps) {
				for (String s : kvp.comments) {
					result.add("\t" + s);
				}
				result.add("\t" + kvp.key + "=" + kvp.value);
			}
		}
		return result;
	}

	// writes the contents of the new ini to memory.
	public void saveFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));
		for (String s : printIni()) {
			writer.write(s);
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	
	// The following are all private inner classes to act as data structures for the ini files content.
	
	private class Sections extends ArrayList<Section> {
		private static final long serialVersionUID = 3902113370537074117L;

		public boolean contains(String section) {
			for (Section sec : this) {
				if (sec.name == section) {
					return true;
				}
			}
			return false;
		}

		public Section get(String section) {
			for (Section sec : this) {
				if (sec.name == section) {
					return sec;
				}
			}
			return null;
		}

		public void remove(String section) {
			this.remove(this.get(section));
		}
	}

	private class KVPs extends ArrayList<KVP> {
		private static final long serialVersionUID = 5970703957372604023L;

		public boolean contains(String key) {
			for (KVP kvp : this) {
				if (kvp.key == key) {
					return true;
				}
			}
			return false;
		}

		public KVP get(String key) {
			for (KVP kvp : this) {
				if (kvp.key == key) {
					return kvp;
				}
			}
			return null;
		}

		public void remove(String key) {
			this.remove(this.get(key));
		}
	}

	private class Commentable {
		public ArrayList<String> comments = new ArrayList<String>();
	}

	private class Section extends Commentable implements Comparable<Section> {
		public String name;
		public KVPs kvps;

		public Section(String name) {
			this.name = name;
			kvps = new KVPs();
		}

		@Override
		public int compareTo(Section other) {
			return this.name.compareTo(other.name);
		}
	}

	private class KVP extends Commentable implements Comparable<KVP> {
		public String key;
		public String value;

		public KVP(String k, String v) {
			key = k;
			value = v;
		}

		@Override
		public int compareTo(KVP other) {
			return this.key.compareTo(other.key);
		}
	}

}
