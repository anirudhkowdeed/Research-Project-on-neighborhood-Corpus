package keyphraseextraction;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author ani
 */
public class buildaffinity {

    public static HashMap<String,HashMap<Integer,Integer> > position=new HashMap();
    public static HashMap<String,HashMap<String,Double>> globalgraph=new HashMap();
    public static HashMap<String,HashMap<String,Double>> graph=new HashMap();
    public static HashMap<String,Integer> candclause=new HashMap();
    public static HashMap<String,Double> graphscore=new HashMap();
    public static HashMap orggraphscore=new HashMap();
    public static HashMap<String,Double> graphscoretemp=new HashMap();
    public static TreeMap wordscore=new TreeMap(Collections.reverseOrder());
    public static Integer window=new Integer(0);
    public static Integer nodecount=new Integer(0);
    public static ArrayList doc=new ArrayList();
    public static ArrayList postags=new ArrayList();
    public static ArrayList orgdoc=new ArrayList();
    public static ArrayList orgpostags=new ArrayList();
    public static HashMap filepos=new HashMap();
    public static String filename=new String();
    public static Double simi=new Double(0);
    public static TreeMap keyphrases=new TreeMap(Collections.reverseOrder());

public buildaffinity() throws FileNotFoundException, IOException
    {
     doc=new ArrayList();
     postags=new ArrayList();
     //graphscore=new HashMap();
     graph=new HashMap();
     position=new HashMap();
     //graphscoretemp=new HashMap();
     postags=new ArrayList();
     wordscore=new TreeMap(Collections.reverseOrder());
     simi=new Double(0);
    }


public static Boolean isGoodPOS(String pos)
{
	if(pos.equals("NN") || pos.equals("NNS") || pos.equals("NNP") || pos.equals("NNPS") || pos.equals("JJ")||pos.equals("JJS")) return true;

	return false;
}
public static Porter stem=new Porter();
public static void readTxtFile(String path) throws FileNotFoundException, IOException
        {
    String line;
    Long doclength=0l;
    int count=0;


    int index3=0;
    BufferedReader file = new BufferedReader(new FileReader(path));
    while(file.ready())
    {
    line=file.readLine();
    String st[] = line.split("\\s+");
    for(int i=0;i<st.length;i++)
    {
        int index=-1;
        int index1=-1;
        String word="";
        String pos="";
        String stmword="";
        index=st[i].indexOf("/");
        if(index!=-1)
        word=st[i].substring(0, index);
        String word1=st[i].substring(index+1);
        index1=word1.indexOf("/");

         if(index1!=-1)
        pos=st[i].substring(index+1, index+index1+1);
        word=word.toLowerCase();
        word=word.trim();
        word = word.replaceAll("[^\\p{ASCII}]","");
	//word = word.replaceAll( "[/:[-]`?\\\\\\*\";!$^()@#&|+=%<>{}\\[\\]_]","");
        word=word.replaceAll("[0-9]","");
         if(word.length()>0 && word != null)
        {

        stmword=word;



        //}

      doc.add(index3++,stmword);
        postags.add(pos);
        if(!isGoodPOS(pos))
        {
            doclength++;
            count++;
        }
        else
        {
          doclength++;

					if(position.containsKey(stmword))
                                        {
                                            HashMap h=new HashMap();
                                            h=position.get(stmword);
                                            h.put(doclength, new Integer(1));
                                            position.remove(stmword);
                                            position.put(word, h);
                                        }
                                        else
                                        {

                                     HashMap m=new HashMap();
                                    m.put(doclength, new Integer(1));
                                 position.put(stmword, m);
                                        }
        }
        }
        }

            }

   if(path.equals("output/"+filename))
   {
       orgdoc=doc;
       orgpostags=postags;
   }



    }
public static void buildGraph()
{


		for(int i=0;i<doc.size();i++)
                {
                    String word=doc.get(i).toString();
			int index=i-1;
			double count=window;

			while(index>=0 && count>0)
			{
				String neighbor=doc.get(index).toString();
					//weighted
					if(graph.containsKey(word))
                                        {
                                            HashMap hm=new HashMap();

                                            hm=graph.get(word);
                                            if(hm.containsKey(neighbor))
                                            {
                                                Double f=(Double) hm.get(neighbor);
                                                f=f+1L;

                                                hm.remove(neighbor);
                                                hm.put(neighbor, f);
                                            }
                                            else
                                            {

                                                hm.put(neighbor, new Double(1));
                                                //nodecount++;

                                                graph.remove(word);
                                                graph.put(word, hm);
                                            }
                                        }
                                        else
                                        {
                                        HashMap ss=new HashMap();
                                        ss.put(neighbor, new Double(1));

                                        graph.put(word,ss);
                                        //nodecount++;
                                         }



				index--;
				count--;
			}

			index=i+1;
			count=window;

			while(index<doc.size()&& count>0)
			{
				String neighbor=doc.get(index).toString();


					//weighted
					if(graph.containsKey(word))
                                        {
                                            HashMap hm=new HashMap();
                                            hm=graph.get(word);
                                            if(hm.containsKey(neighbor))
                                            {
                                                Double f=(Double) hm.get(neighbor);
                                                f=f+1L;

                                                hm.remove(neighbor);
                                                hm.put(neighbor, f);
                                            }
                                            else
                                            {

                                                hm.put(neighbor, new Double(1));
                                                //nodecount++;

                                                graph.remove(word);
                                                graph.put(word, hm);
                                            }
                                        }
                                        else
                                        {
                                        HashMap ss=new HashMap();

                                        ss.put(neighbor, new Double(1));
                                        graph.put(word,ss);
                                        //nodecount++;
                                         }



				index++;
				count--;
			}

		}

	//normalize();
}
public static void normalize()
   {
    ArrayList al=new ArrayList(globalgraph.keySet());
    Double sum=0.0;
    for(int i=0;i<al.size();i++)
    {
        sum=0.0;
        String st=al.get(i).toString();
        HashMap hm=globalgraph.get(st);
        ArrayList al1=new ArrayList(hm.keySet());
        for(int j=0;j<al1.size();j++)
        {
             String st1=al1.get(j).toString();
             Double d=(Double) hm.get(st1);
             sum+=d;
        }

        for(int j1=0;j1<al1.size();j1++)
        {
            String st2=al1.get(j1).toString();
             Double d1=(Double) hm.get(st2);

            hm.remove(st2);
                 hm.put(st2, new Double(d1 / sum));
        }
            globalgraph.remove(st);
        globalgraph.put(st, hm);
    }
    ArrayList a=new ArrayList(globalgraph.keySet());

         for(int j=0;j<a.size();j++)
         {
             String st=a.get(j).toString();

             HashMap hm=globalgraph.get(st);
             ArrayList a1=new ArrayList(hm.keySet());
             for(int h=0;h<a1.size();h++)
             {
                String st1=a1.get(h).toString();
                Double d=(Double) hm.get(st1);



             }


         }

    }
    public static void intialize()
    {
        ArrayList a3=new ArrayList(globalgraph.keySet());
        for(int i=0;i<a3.size();i++)
        {
            String str=a3.get(i).toString();
            graphscore.put(str, new Double(1));
        }
    }
    public static void scorecal()
    {
        intialize();
        for(int i=0;i<20;i++)
        {
           ArrayList ai=new ArrayList(graphscore.keySet()) ;
           for(int j=0;j<ai.size();j++)
           {
               String word=ai.get(j).toString();
               HashMap hm=new HashMap();
               hm=globalgraph.get(word);
               Double score=0.0;

               ArrayList al=new ArrayList(hm.keySet());
               for(int k=0;k<al.size();k++)
               {
                   String neighbor=al.get(k).toString();
                   HashMap hm1=globalgraph.get(neighbor);
                   Double d=(Double) hm1.get(word);
                   Double d1=graphscore.get(neighbor);


                   score+=d*d1;
               }

               score*=0.85;
               score+=(0.15/(1.0*nodecount));
               graphscoretemp.remove(word);
               graphscoretemp.put(word, score);
           }
            ArrayList ai1=new ArrayList(graphscore.keySet()) ;
            for(int j=0;j<ai1.size();j++)
            {
             String word=ai1.get(j).toString();
             graphscore.put(word,graphscoretemp.get(word));
            }

        }

    }
public static void mergegraph()
 {
    ArrayList al=new ArrayList(graph.keySet());
    for(int i=0;i<al.size();i++)
    {
        String str=al.get(i).toString();
        HashMap hm=graph.get(str);
        ArrayList al1=new ArrayList(hm.keySet());
         if(globalgraph.containsKey(str))
        {
             HashMap hm1=globalgraph.get(str);
             for(int j=0;j<al1.size();j++)
             {
                 String str1=al1.get(j).toString();
                 if(hm1.containsKey(str1))
                 {
                     Double d1=(Double) hm1.get(str1);
                     Double d2=(Double) hm.get(str1);
                     Double sum=d2*simi;
                     d1+=sum;
                     hm1.remove(str1);
                     hm1.put(str1,d1);
                 }

                 else
                 {
                     Double d3=(Double) hm.get(str1);
                     nodecount++;
                     hm1.put(str1,d3*simi);

                 }

             }
             globalgraph.remove(str);
             globalgraph.put(str,hm1);
        }
         else
         {
            HashMap hnew=graph.get(str);
            for(Object key:hnew.keySet())
            {
                nodecount++;
                hnew.put(key,( Double)(hnew.get(key))*simi);
             }
            //graph.put(str,hnew);
            globalgraph.put(str,hnew);

         }
    }
}
public static long extractkeyphrases()
    {

        long count=0;
        int count1=0;
	ArrayList pattern=new ArrayList();
	ArrayList patternpos=new ArrayList();

	//selecting candidate patterns
	for(int i=0;i<orgpostags.size();i++)
	{

		if((isGoodPOS(orgpostags.get(i).toString())||orgpostags.get(i).toString().equals("IN")||orgpostags.get(i).toString().equals("DT")||orgpostags.get(i).toString().equals("CC")||orgpostags.get(i).toString().equals("TO")) && count1<=window-1)
		{
			pattern.add(orgdoc.get(i));
			patternpos.add(orgpostags.get(i));
                        if(isGoodPOS(orgpostags.get(i).toString()))
                        count1++;
		}

		else if(pattern.size()>0 && (!isGoodPOS(orgpostags.get(i).toString())||count1>window-1))
		{
			String s="";
                        count1=0;
			for(int j=0;j<pattern.size();j++)
			{

                                s+=pattern.get(j).toString();
				if(j+1<pattern.size()) s+=" ";
			}

			if(patternpos.get(patternpos.size()-1).toString().compareTo("JJ")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("JJS")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("CC")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("DT")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("IN")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("TO")!=0)
			{
				if(!candclause.containsKey(s))
                                {
                                    candclause.put(s, new Integer(0));
                                    count++;
                                 }

				int i1=candclause.get(s);
                                candclause.remove(s);
                                candclause.put(s, i1+1);
			}

			pattern=new ArrayList();
			patternpos=new ArrayList();
		}
	}

	if(pattern.size()>0)
	{
		String s="";

		for(int j=0;j<pattern.size();j++)
		{
			s+=pattern.get(j).toString();
			if(j+1<pattern.size()) s+=" ";
		}

		if(patternpos.get(patternpos.size()-1).toString().compareTo("JJ")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("JJS")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("CC")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("DT")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("IN")!=0&&patternpos.get(patternpos.size()-1).toString().compareTo("TO")!=0)
		{
                    if(!candclause.containsKey(s))
                                {
                                    candclause.put(s, new Integer(0));
                                    count++;
                                 }

				int i1=candclause.get(s);
                                candclause.remove(s);
                                candclause.put(s, i1+1);
		}

		pattern=new ArrayList();
                patternpos=new ArrayList();

	}

	return count;

}
public static Double gettotalscore(String str)
    {
    String st[]=str.split("\\s+");
    Double sum=0.0;
    for(int i=0;i<st.length;i++)
    {
        Double val=(Double)orggraphscore.get(st[i]);
        sum+=val;
    }
    return sum;

}
public static void score()
    {
    long h=extractkeyphrases();
    ArrayList arr=new ArrayList(candclause.keySet());
    for(int i=0;i<arr.size();i++)
    {
        String str=arr.get(i).toString();
        Double d=gettotalscore(str);
        keyphrases.put(d, str);
    }

}
 public  void mainFunc() throws FileNotFoundException, IOException
    {
        String line;

        int count=0;
        BufferedReader file1 = new BufferedReader(new FileReader("out"));
    while(file1.ready())
    {
    line=file1.readLine();
    String st[] = line.split("\\s+");
    if(count==0)
        filename=st[0];
    filepos.put(st[0], Double.parseDouble(st[1]));
    count++;
     }
        System.out.println("enter the window size:");
        Scanner s=new Scanner(System.in);
        window=s.nextInt();
       ArrayList al=new ArrayList(filepos.keySet());
       for(int i=0;i<al.size();i++)
       {
            buildaffinity buildaffinity = new buildaffinity();
         String str=al.get(i).toString();
         Double d=(Double) filepos.get(str);
         simi=d;

         readTxtFile("output/"+str);
         buildGraph();
         mergegraph();
        }
       normalize();
        scorecal();
     /* ArrayList arr=new ArrayList(globalgraph.keySet());
       for(int k=0;k<arr.size();k++)
       {
           String str=arr.get(k).toString();
           System.out.println(str+" ");
           HashMap hm=globalgraph.get(str);
           ArrayList arr1=new ArrayList(hm.keySet());
           for(int k1=0;k1<arr1.size();k1++)
           {
               String str1=arr1.get(k1).toString();
               System.out.print(str1+" "+hm.get(str1)+" ");

           }

           System.out.println();

       }*/
        for(int j=0;j<orgdoc.size();j++)
        {
            Double score1=0.0;
            String str=orgdoc.get(j).toString();
            if(graphscore.containsKey(str))
            {
                score1=(Double)graphscore.get(str);

            }
            orggraphscore.put(str, score1);
        }
     score();
     ArrayList arr=new ArrayList(keyphrases.keySet());
       for(int k=0;k<50;k++)
       {
           Double d=(Double)arr.get(k);
           String str=keyphrases.get(d).toString();
           System.out.println("KEYPHRASE:"+"   "+str+"   "+"SCORE:"+" "+d);
            }

    }



}
