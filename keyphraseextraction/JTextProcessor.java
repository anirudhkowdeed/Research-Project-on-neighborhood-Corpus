package keyphraseextraction;

import java.io.*;
import java.util.*;
import jtextpro.JTextPro;

public class JTextProcessor {
    public static Hashtable stopword = new Hashtable();
        public static int maxcount=0;
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

    public JTextProcessor( String dir) throws FileNotFoundException, IOException, Exception {

        String str="models";
        stopwords("stopwords.txt");
	// assign paths to trained models
	String senSegmentationModelDir = str + File.separator + "SenSegmenter";
	String posTaggingModelDir = str + File.separator + "CRFTagger";
	String phraseChunkingModelDir = str + File.separator + "CRFChunker";
        //File f2=new File("indexfiles");
        //String files[]=f2.list();
        JTextPro textPro = new JTextPro();
         BufferedReader file1 = new BufferedReader(new FileReader("out"));
	// assign path to sentence segmentation model and load it to memory
	textPro.setSenSegmenterModelDir(senSegmentationModelDir);
	//System.out.println();
	//System.out.println("Loading sentence segmentation model ...");
	textPro.initSenSegmenter();
	//System.out.println("Loading sentence segmentation model completed!");

	// initialize sentence tokenizer
	textPro.initSenTokenizer();

	// assign path to POS tagging model and load it to memory
	textPro.setPosTaggerModelDir(posTaggingModelDir);
	//System.out.println();
	//System.out.println("Loading POS tagging model ...");
	textPro.initPosTagger();
	//System.out.println("Loading POS tagging model completed!");

	// assign path to phrase chunking model and load it to memory
	textPro.setPhraseChunkerModelDir(phraseChunkingModelDir);
	//System.out.println();
	//System.out.println("Loading phrase chunking model ...");
	textPro.initPhraseChunker();
	//System.out.println("Loading phrase chunking model completed!");
	//System.out.println();

		//String temp="index_"+0;
		while(file1.ready())//for(int i1=0;i1<files.length;i1++)
		{
                    String filenames=file1.readLine();
                    filenames=filenames.replaceAll(".out"," ");
                    //String filenam[]=filenames.split("/+");
                    String files[]=filenames.split("\\s+");
	String dataFile = dir+"/"+files[0];

	// create a JTextPro object

	BufferedReader fin = null;
	PrintWriter fout = null;
        File f=new File("output");
f.mkdir();
	try {
	    fin = new BufferedReader(new FileReader(dataFile));
	    fout = new PrintWriter(new FileWriter("output/"+files[0]+".out"));

	    String line;
	    while ((line = fin.readLine()) != null) {

		List sentences = textPro.doSenSegmentation(line);

		String sentence;
		for (int i = 0; i < sentences.size(); i++) {
		    sentence = (String)sentences.get(i);

		    List senToks = JTextPro.tokenize(textPro.doSenTokenization(sentence));
		    List posTags = textPro.doPosTagging(senToks);
		    List chunkTags = textPro.doPhraseChunking(senToks, posTags);

		    for (int j = 0; j < senToks.size(); j++) {
                        if(!stopword.containsKey((String)senToks.get(j).toString().toLowerCase()))
			fout.print((String)senToks.get(j) + "/" + (String)posTags.get(j) + "/" +
				    (String)chunkTags.get(j) + " ");
                       // else
                            //System.out.println((String)senToks.get(j));
		    }
		    fout.println();
		}
	    }

	    fin.close();
	    fout.close();

	} catch(IOException e) {
	    System.out.println(e.toString());
	    return;
	}
    }
    }
} // end of class JTextProcessor


