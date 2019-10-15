package echapackage;

import java.io.*;

public class Test {

  public static void main(String args[]) throws Exception {
    Process proc = Runtime.getRuntime().exec("ls -l echapackage/python-schoof/");
    BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
  }
}
