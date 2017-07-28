package iot.pi.queue.util;

import java.io.*;
import java.util.Arrays;

public class WriteText {

    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void File(int row[]) throws IOException {
      
        String stuff = Arrays.toString(row);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream("src/data/test.txt");
			osw = new OutputStreamWriter(fos);
			for (int i = 0; i < stuff.length(); i++) {
	            osw.write(stuff.charAt(i));
	        }
		} finally {
			osw.close();
		}
    }
    
}