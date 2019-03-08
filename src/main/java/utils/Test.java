package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
  public static void main(String[] args) throws InterruptedException {
//    for (int i = 0; i < 10; i++) {
//      Thread.sleep(1000);
//      System.out.println(i);
//    }

//    if (Pattern.matches("A.*.a", "A.asav.acdc.a"))
//      System.out.println("Yes");

    File file = new File("data/items.txt");
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;
      int cnt = 0, oneInvoCnt = 0;
      while ((line = reader.readLine()) != null) {
        String[] apis = line.split(" ");
        if (apis.length == 1) oneInvoCnt++;
        cnt++;
      }
      System.out.println(cnt + " " + oneInvoCnt);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
