# J.ini
A simple, light .ini library for Java

(Work in progress)

Meant to be very light, simple, and easy to use for storing file-based settings and preferences.

## Usage

Sample code:

```
import foy.util.JINI;

public class test {
  public static void main(String[] args) {
    // open a .ini file, the false flag specifies to not create it if it does not exist, 
    // but if it does not exist it will throw a FileNotFoundException
    JINI ini = new JINI("mySettings.ini", false);
    
    // add the "[TEST]" section to our ini file
    // it will throw an IOException if the section already exists
    ini.addSection("TEST");
    
    // add the Key-Value-Pair "x=0" under the "[TEST]" section
		ini.addKVP("TEST", "x", "0");
  }
}
```
