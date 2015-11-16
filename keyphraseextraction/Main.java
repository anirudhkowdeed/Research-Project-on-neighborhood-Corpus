package keyphraseextraction;
import java.io.IOException;

public class Main {
public static void main(String args[]) throws IOException, Exception
    {
        System.out.println("Index creation started....");
      IrProject irp=new IrProject();
      irp.mainFunc();
      System.out.println("Index completed............");
        System.out.println("Neighbour hood calculation started....");
       neighbourcal nc=new neighbourcal();
        System.out.println("Neighbour hood calculation ended....");
          System.out.println("POS-Tagger started....");
         JTextProcessor jtp=new JTextProcessor(irp.dir);
          System.out.println("POS-Tagger ended...");
          System.out.println("Keyphrase extraction started...");
            buildaffinity ba=new buildaffinity();
            ba.mainFunc();

}
}
