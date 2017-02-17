/*************************************************************************
 *  Compilation:  javac MyLZW.java
 *  Execution:    java MyLZW - < input.txt   (compress)
 *  Execution:    java MyLZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static final int L = 4096;       // number of codewords = 2^W
    private static final int W = 12;         // codeword width
/*Do Nothing Mode*/
    public static void DoNothing()
    {
    	int MW = 9;
    	int ML = (int)Math.pow(2,MW);
    	String input = BinaryStdIn.readString();
    	TST<Integer> st = new TST<Integer>();
    	for (int i = 0; i < R; i++)
    		st.put("" + (char) i, i);
    	int code = R+1;
    	char[] ch = input.toCharArray();
    	int index = 0;
    	BinaryStdOut.write(R+1, MW);
    	while (ch.length > index)
    	{
    		if (code == ML && MW < 16)
    		{
    			MW++;
    			ML*=2;
    		}
    		String s = st.longestPrefixOf(ch,index);
    		int t = s.length();
    		BinaryStdOut.write(st.get(s), MW);
    		if (t < ch.length-index && code < ML)
    		{
    			s+=ch[index+t];
    			st.put(s, code++);
    		}
    		index+=t;
    	}
    	BinaryStdOut.write(R, MW);
        BinaryStdOut.close();
    }
/*Reset Mode*/
    public static void Reset()
    {
    	int MW = 9;
    	int ML = (int)Math.pow(2,MW);
    	String input = BinaryStdIn.readString();
    	TST<Integer> st = new TST<Integer>();
    	for (int i = 0; i < R; i++)
    		st.put("" + (char) i, i);
    	int code = R+1;
    	char[] ch = input.toCharArray();
    	int index = 0;
    	BinaryStdOut.write(R+2, MW);
    	while (ch.length > index)
    	{
    		if (code == ML && MW < 16)
    		{
    			MW++;
    			ML*=2;
    		}
    		if (code == ML && MW == 16)
    		{
    			MW = 9;
    			ML = (int)Math.pow(2,MW);
    			st = new TST<Integer>();
    	    	for (int i = 0; i < R; i++)
    	    		st.put("" + (char) i, i);
    			code = R+1;
    		}
    		String s = st.longestPrefixOf(ch,index);
    		int t = s.length();
    		BinaryStdOut.write(st.get(s), MW);
    		if (t < ch.length-index && code < ML)
    		{
    			s+=ch[index+t];
    			st.put(s, code++);
    		}
    		index+=t;
    	}
    	BinaryStdOut.write(R, MW);
        BinaryStdOut.close();
    }
/*Monitor Mode*/
    public static void Monitor()
    {
    	int MW = 9;
    	int ML = (int)Math.pow(2,MW);
    	String input = BinaryStdIn.readString();
    	int csize = 0;
    	int ucsize = 0;
    	double oldratio = 0;
    	double newratio = 1;
    	TST<Integer> st = new TST<Integer>();
    	for (int i = 0; i < R; i++)
    		st.put("" + (char) i, i);
    	int code = R+1;
    	char[] ch = input.toCharArray();
    	int index = 0;
    	BinaryStdOut.write(R+3, MW);
    	csize+=MW;
    	while (ch.length > index)
    	{
    		if (code == ML && MW < 16)
    		{
    			MW++;
    			ML*=2;
    		}
    		if (code == ML && MW == 16)
    		{
    			oldratio = newratio;
    		}
    		if ((oldratio/newratio) >= 1.1 && MW == 16)
    		{
    			MW = 9;
    			ML = (int)Math.pow(2,MW);
    			st = new TST<Integer>();
    	    	for (int i = 0; i < R; i++)
    	    		st.put("" + (char) i, i);
    			code = R+1;
    			oldratio = 0;
    		}
    		String s = st.longestPrefixOf(ch,index);
    		int t = s.length();
    		BinaryStdOut.write(st.get(s), MW);
    		csize+=MW;ucsize+=8*t;newratio = (double)(ucsize)/(csize);
    		if (t < ch.length-index && code < ML)
    		{
    			s+=ch[index+t];
    			st.put(s, code);
    		}
    		code++;
    		index+=t;
    	}
    	BinaryStdOut.write(R, MW);
        BinaryStdOut.close();
    }

    public static void Expand()
    {
    	int MW = 9;
    	int ML = (int)Math.pow(2,MW);
    	String[] st = new String[ML*128];
    	int i;
    	for (i = 0; i < R; i++)
    		st[i] = "" + (char) i;
    	st[i++] = "";
    	int codeword = BinaryStdIn.readInt(MW);
    	if (codeword == R)
    		return;
    	if (codeword == R+1)
    	{
    		codeword = BinaryStdIn.readInt(MW);
    		i = R+1;
    		String val = st[codeword];
        	while (true)
        	{
        		if (i == ML-1 && MW < 16)
        		{
        			MW++;
        			ML*=2;
        		}
        		BinaryStdOut.write(val);
        		codeword = BinaryStdIn.readInt(MW);
        		if (codeword == R)
        			break;
        		String s = st[codeword];
        		if (i == codeword)
        			s = val + val.charAt(0);
        		if (i < ML && s != "")
        			st[i++] = val + s.charAt(0);
        		val = s;
        	}
    	}
    	else if (codeword == R+2)
    	{
    		codeword = BinaryStdIn.readInt(MW);
    		i = R+1;
    		String val = st[codeword];
        	while (true)
        	{
        		if (i == ML-1 && MW < 16)
        		{
        			MW++;
        			ML*=2;
        		}
        		if (i == ML-1 && MW == 16)
        		{
        			MW = 9;
        			ML = (int)Math.pow(2,MW);
        			st = new String[ML*128];
        			for (i = 0; i < R; i++)
        	    		st[i] = "" + (char) i;
        	    	st[i++] = "";
        			i = R;
        		}
        		BinaryStdOut.write(val);
        		codeword = BinaryStdIn.readInt(MW);
        		if (codeword == R)
        			break;
        		String s = st[codeword];
        		if (i == codeword)
        			s = val + val.charAt(0);
        		if (i < ML && s != "")
        			st[i++] = val + s.charAt(0);
        		val = s;
        	}
    	}
    	else if (codeword == R+3)
    	{
    		int csize = 0;
        	int ucsize = 0;
        	double oldratio = 0;
        	double newratio = 1;
    		codeword = BinaryStdIn.readInt(MW);
    		csize+=MW;
    		i = R+1;
    		String val = st[codeword];
        	while (true)
        	{
        		csize+=MW;ucsize+=8*val.length();newratio = (double)(ucsize)/(csize);
        		if (i == ML-1 && MW < 16)
        		{
        			MW++;
        			ML*=2;
        		}
        		
        		if (i == ML-1 && MW == 16)
        		{
        			oldratio = newratio;
        		}
        		if ((oldratio/newratio) >= 1.1 && MW == 16)
        		{
        			MW = 9;
        			ML = (int)Math.pow(2,MW);
        			st = new String[ML*128];
        			for (i = 0; i < R; i++)
        	    		st[i] = "" + (char) i;
        	    	st[i++] = "";
        			i = R;
        			oldratio = 0;
        		}
        		BinaryStdOut.write(val);
        		codeword = BinaryStdIn.readInt(MW);
        		if (codeword == R)
        			break;
        		String s = st[codeword];
        		if (i == codeword)
        			s = val + val.charAt(0);
        		if (i < ML && s != "")
        			st[i] = val + s.charAt(0);
        		i++;
        		val = s;
        	}
    	}
    	BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if (args[0].equals("-"))
        {
        	if (args[1].equals("n"))
        		DoNothing();
        	else if (args[1].equals("r"))
        		Reset();
        	else if (args[1].equals("m"))
        		Monitor();
        	else
        		throw new IllegalArgumentException("Illegal command line argument");
        }
        else if (args[0].equals("+"))
        	Expand();
        else
        	throw new IllegalArgumentException("Illegal command line argument");
    }

}
