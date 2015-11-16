package keyphraseextraction;
import java.util.*;
import java.io.*;
class IrProject
{
	public static TreeMap tmap=new TreeMap();
	public static TreeMap tree1=new TreeMap();
	public static Hashtable stopword = new Hashtable();
        public static int maxcount=0;
        public String dir;
        Porter stem=new Porter();
	public static void stopwords(String path)throws Exception
	{
	    BufferedReader file = new BufferedReader(new FileReader(path));
		String text1;
		while(file.ready())
		{
			text1=file.readLine();
			stopword.put(text1, new Integer(1));
		}
	}

	public IrProject() throws Exception
	{
		tmap=new TreeMap();
                tree1=new TreeMap();
	}


	public void writetotree(String str)
	{
		if(str.length()>0)
		{
			if(!stopword.containsKey(str))
			{
				String st3=stem.stripAffixes(str.toString());
                                if(!tmap.containsKey(st3))
				tmap.put(st3,new Integer(1));

                                else
                                {
                                      int p1=(Integer)tmap.get(st3);
                                      tmap.put(st3, p1+1);
                                      if(p1+1>maxcount)
                                          maxcount=p1+1;

                                }
			}
		}
	}

	public void writetotreemap(String str)
	{
		tree1.put(str,new Integer(200));
	}


	public static void writetofile(String name,File dir)throws Exception
	{
		ArrayList b=new ArrayList(tree1.keySet());
		int count=0;
		ArrayList a=new ArrayList(tmap.keySet());
		File ff=new File(dir+"/"+name);
		FileOutputStream out=new FileOutputStream(ff);
		PrintStream p = new PrintStream(out);

		for(int i=0;i<b.size();i++)
		{
			p.print(b.get(i)+" ");
                         p.print(tree1.get(b.get(i)));
			p.print("\n");
		}
                int temp=0;
                for(int j=maxcount;j>=0;j--)
                {
		for(int i=0;i<a.size();i++)
			{

				//p.print(a.get(i)+" ");
                    Object obj=a.get(i);
                     int p1=(Integer)tmap.get(obj);
                            if(p1==j)
                            {
                                p.print(a.get(i)+" ");
                                p.print(tmap.get(a.get(i)));
				p.print("\n");
                                count++;
                            }
                     //if(count==50)
                        // temp=1;
			}
                if(temp==1)
                    break;
            }
		p.close();
		}

	public void parsedocument(String name) throws Exception
	{
		int ch;
		FileInputStream fin=new FileInputStream(name);
		DataInputStream in = new DataInputStream(fin);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String text1;
		int j=0;
		while((text1=br.readLine())!=null)
		{
			//if(j==0)
			/*{
				j++;
				String st1[]=text1.split("\\s+");
				for(int i=0;i<st1.length;i++)
					{
					String temp3=st1[i].trim();
					temp3 = temp3.replaceAll("['][^\\p{ASCII}]","");
					temp3 = temp3.replaceAll( "[/:\\.[-]`,?\\\\\\*\";!$^()@#&|+=%<>{}\\[\\]_]"," ");
                                        temp3=temp3.replaceAll("[0-9]","");
					temp3=temp3.trim();
					temp3=temp3.toLowerCase();
					writetotreemap(temp3);

					}

			//}*/
			//else
			//{
				String st[] = text1.split("\\s+");
				for(int i=0;i<st.length;i++)
					{
					 String temp2=st[i].trim();
					 temp2 = temp2.replaceAll("[']","");
					 temp2 = temp2.replaceAll("[^\\p{ASCII}]","");
					 temp2 = temp2.replaceAll( "[/:\\.[-]`,?\\\\\\*\";!$^()@#&|+=%<>{}\\[\\]_]"," ");
					 temp2=temp2.replaceAll("[0-9]","");
                                         temp2=temp2.trim();
					 temp2=temp2.toLowerCase();
					 writetotree(temp2);
					}
			//}
		}


	}
	public void mainFunc() throws Exception
	{
		File f1=new File("indexfiles");
		f1.mkdir();
		IrProject spe=new IrProject();
		stopwords("stopwords.txt");
                BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter path of file directory:");
		String dirname=br.readLine();
                dir=dirname;
		long tm1=new Date().getTime();
		File f2=new File(dirname);
		String files[]=f2.list();
		String temp="index_"+0;
		for(int i=0;i<files.length;i++)
		{
			spe=new IrProject();
                        maxcount=0;

			spe.parsedocument(dirname+"/"+files[i]);
			temp=files[i];

			writetofile(temp,f1);
			ArrayList c=new ArrayList(tree1.keySet());


		}
	}
}
