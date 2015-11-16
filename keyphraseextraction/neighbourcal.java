package keyphraseextraction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;


public class neighbourcal {
    public static TreeMap tmap=new TreeMap();
    public static TreeMap tree1=new TreeMap();
    public static TreeMap tree2=new TreeMap();
    public static TreeMap tree3=new TreeMap();
        public neighbourcal() throws FileNotFoundException, IOException
    {
            tmap=new TreeMap();
            tree2=new TreeMap();
            tree3=new TreeMap();
            this.mainFunc();
    }

    public void mainFunc() throws FileNotFoundException, IOException
    {
             File f2=new File("indexfiles");
             System.out.println("enter the query document:");
             Scanner str=new Scanner(System.in);
             Float maxfreq=0.0f;

             String fname=str.nextLine();
             System.out.println("enter the size of neighborhood corpus:");
             int yy=str.nextInt();
	     String files[]=f2.list();
                  BufferedReader file = new BufferedReader(new FileReader("indexfiles"+"/"+fname));
                  String text1;
                  int sumdoc=0;
                  double aucsum1=0;
		while(file.ready())
		{
			text1=file.readLine();
			String st[]=text1.split("\\s+");
                        tree1.put(st[0], Integer.parseInt(st[st.length-1]));
                        sumdoc+=Integer.parseInt(st[st.length-1])*Integer.parseInt(st[st.length-1]);
                        aucsum1=Math.sqrt(sumdoc);


		}

		for(int i=0;i<files.length;i++)
		{
                    int sum=0;
                    tmap=new TreeMap();

                   //if(fname.compareTo(files[i])!=0)
                    //{
                        BufferedReader file1 = new BufferedReader(new FileReader("indexfiles"+"/"+files[i]));
                        String text2;
                  int sum1=0;
                  double aucsum=0;
		  while(file1.ready())
		 {
			text2=file1.readLine();
			String st[]=text2.split("\\s+");
                        tmap.put(st[0], Integer.parseInt(st[st.length-1]));
                        sum1+=Integer.parseInt(st[st.length-1])*Integer.parseInt(st[st.length-1]);
                        aucsum=Math.sqrt(sum1);
                 }


                  ArrayList b=new ArrayList(tree1.keySet());
                   int vectprod=0;
                  for(int ind=0;ind<b.size();ind++)
		{
                      int count=0;
                      int count1=0;

			String strin=b.get(ind).toString();
                         count=Integer.parseInt(tree1.get(b.get(ind)).toString());


                         if(tmap.containsKey(strin))
                         {

                            count1=Integer.parseInt(tmap.get(strin).toString());

                            vectprod+=count*count1;
                    }



		}

                   tree2.put("indexfiles/"+files[i], new Float(vectprod/(aucsum1*aucsum)));
                   tree3.put( new Float(vectprod/(aucsum1*aucsum)),files[i].split("_")[0]+".out");
                    //if(maxfreq<=(vectprod/(aucsum1*aucsum)))
                       // maxfreq=new Float(vectprod/(aucsum1*aucsum));
                    //}
                    }
                  ArrayList b1=new ArrayList(tree3.keySet());
                  File ff=new File("out");
		FileOutputStream out=new FileOutputStream(ff);
		PrintStream p = new PrintStream(out);
               int filecount=0;
                  for(int ind2=b1.size()-1;ind2>=0;ind2--)
		{
                  String stri1=b1.get(ind2).toString();

                  p.print(tree3.get(b1.get(ind2))+" ");
                 p.print(stri1);
                  p.print("\n");
                  filecount++;
                  if(filecount==yy)
                      break;
                }


               // System.out.println(maxfreq);
    }
}



