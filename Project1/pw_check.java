import java.io.*;
import java.util.*;
public class pw_check {
	static char[] letters = {/*'a',*/'b','c','d','e','f','g','h',/*'i',*/'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	static char[] numbers = {'0',/*'1',*/'2','3',/*'4',*/'5','6','7','8','9'};
	static char[] symbols = {'!','@','$','^','_','*'};
	static char[] characters = {/*'a',*/'b','c','d','e','f','g','h',/*'i',*/'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0',/*'1',*/'2','3',/*'4',*/'5','6','7','8','9','!','@','$','^','_','*'};
	static String ls = String.valueOf(letters);
	static String ns = String.valueOf(numbers);
	static String ss = String.valueOf(symbols);
	static String cs = String.valueOf(characters);
	static boolean[] changed = new boolean[5];
	
	
	public static void main(String[] args) {
		if (args.length == 0)
			System.out.println("No command line arguments.");
		else
		{
			if (args[0].equals("-find"))
			{
				DLB.Init();
				readDictionary();
				Find();
				System.out.println("All password are generated!");
			}
			else if (!args[0].equals("-check"))
				System.out.println("Unvaild command line.");
			else
			{
				File file = new File("all_passwords.txt");
				if (!file.exists())
					System.out.println("Haven't generated passwords!");
				else
					Check();
				
			}
		}
	}
	
	/**
	 * Read dictionary to DLB.
	 */
	public static void readDictionary()
	{
		File file = new File("dictionary.txt");
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null)
			{
				if (tempString.length() <= 5 && !(tempString.toLowerCase().contains("i")) && !(tempString.toLowerCase().contains("a")))
					DLB.Add(tempString);
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e1){}
			}
		}
	}
	
	/**
	 * Check whether the password user input is vaild.
	 */
	public static void Check()
	{
		double[][][][][] allpassword = new double[cs.length()][cs.length()][cs.length()][cs.length()][cs.length()];
		String password = "";
		File file = new File("all_passwords.txt");
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			System.out.println("Loading...");
			while ((tempString = reader.readLine()) != null)
			{
				password = tempString.split(",")[0];
				allpassword[cs.indexOf(password.charAt(0))][cs.indexOf(password.charAt(1))][cs.indexOf(password.charAt(2))][cs.indexOf(password.charAt(3))][cs.indexOf(password.charAt(4))] = Double.valueOf(tempString.split(",")[1]);
			}
			reader.close();
			System.out.println("All passwords loaded.");
			System.out.println("Please input a password or input \"exit\" to exit:");
			Scanner sc = new Scanner(System.in);
			String yourpassword = sc.nextLine();
			yourpassword = yourpassword.toLowerCase();
			if (yourpassword.equals("exit"))
			{
				sc.close();
				return;
			}
			while (true)
			{
				int count[] = {0,0,0,0,0};
				int state = 0;
				if (yourpassword.length() > 5)
				{
					yourpassword = yourpassword.substring(0, 5);
					state++;
				}
				else if (yourpassword.length() < 5)
				{
					int len = yourpassword.length();
					state++;
					for (int i = 0; i < (5 - len); i++)
					{
						yourpassword += "b";
					}
				}
				for (int i = 0; i < 5; i++)
				{
					changed[i] = false;
					if (!cs.contains(String.valueOf(yourpassword.charAt(i))))
					{
						state++;
						yourpassword = yourpassword.replace(yourpassword.charAt(i), 'b');
						changed[i] = true;
					}
					count[i] = cs.indexOf(yourpassword.charAt(i));
				}
				if (state == 0)
				{
					if (allpassword[count[0]][count[1]][count[2]][count[3]][count[4]]!=0)
					{
						System.out.printf("Vaild!\t\tNeed %f ms to generate.\n", allpassword[count[0]][count[1]][count[2]][count[3]][count[4]]);
						System.out.println("Please input a password or input \"exit\" to exit:");
						sc = new Scanner(System.in);
						yourpassword = sc.nextLine();
						yourpassword = yourpassword.toLowerCase();
						if (yourpassword.equals("exit"))
							break;
						continue;
					}
				}
				System.out.println("Unvaild!");
				for (int i = 0; i < 10; i++)
				{
					while (count[4]==cs.length()||changed[4]==false||allpassword[count[0]][count[1]][count[2]][count[3]][count[4]]==0)
					{
						count=New(count);
					}
					yourpassword = ""+characters[count[0]]+characters[count[1]]+characters[count[2]]+characters[count[3]]+characters[count[4]];
					if (i == 9)
						System.out.printf("%d.%s\tNeed %f ms to generate.\n",i+1,yourpassword,allpassword[count[0]][count[1]][count[2]][count[3]][count[4]]);
					else
						System.out.printf("%d. %s\tNeed %f ms to generate.\n",i+1,yourpassword,allpassword[count[0]][count[1]][count[2]][count[3]][count[4]]);
					count[4]++;
				}
				System.out.println("Please input a password or input \"exit\" to exit:");
				sc = new Scanner(System.in);
				yourpassword = sc.nextLine();
				yourpassword = yourpassword.toLowerCase();
				if (yourpassword.equals("exit"))
					break;
			}
			sc.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e1){}
			}
		}
	}
	
	/**
	 * Generate all vaild passwords.
	 */
	public static void Find()
	{
		//int p = 0;
		try
		{
			File f = new File("all_passwords.txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			long Time = System.nanoTime();
			char[] numandsym = new char[numbers.length + symbols.length];
			System.arraycopy(numbers, 0, numandsym, 0, numbers.length);
			System.arraycopy(symbols, 0, numandsym, numbers.length, symbols.length);
			for (char i1 : characters)
			{
				int[] count = {0,0,0};
				count = Add(count, i1);
				for (char i2 : characters)
				{
					count = Add(count, i2);
					for (char i3 : characters)
					{
						count = Add(count, i3);
						if (count[1]==3 || count[2]==3)
						{
							count = Minus(count, i3);
							continue;
						}
						for (char i4 : characters)
						{
							count = Add(count, i4);
							if (count[0]==4||count[1]==3 || count[2]==3)
							{
								count = Minus(count, i4);
								continue;
							}
							for (char i5 : characters)
							{
								count = Add(count, i5);
								if (count[0] > 3 || count[0] == 0 || count[1] > 2 || count[1] == 0 || count[2] > 2 || count[2] == 0)
								{
									count = Minus(count, i5);
									continue;
								}
								String password = new String();
								String testpassword = new String();
								password = "" + i1 + i2 + i3 + i4 + i5;
								testpassword = password.replace('$', 's').replace('7', 't').replace('0', 'o').replace('3', 'e');
								int s = 0;
								for (int i = 0; i < 4; i++)
								{
									for (int j = i+2; j < 6; j++)
									{
										if (DLB.Search(testpassword.substring(i, j)))
										{
											s++;
											break;
										}
									}
								}
								if (s==0)
								{
									//p++;
									output.write(password);
									output.write(',');
									output.write(String.valueOf((System.nanoTime()-Time)/(double)1000000));
									output.write('\n');
								}
								count = Minus(count, i5);
							}
							count = Minus(count, i4);
						}
						count = Minus(count, i3);
					}
					count = Minus(count, i2);
				}
				count = Minus(count, i1);
			}
			output.close();
			//System.out.printf("Generated %d passwords!", p);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Count the number of the characters.
	 */
	public static int[] Add(int[] count, char i)
	{
		if (ls.contains(String.valueOf(i)))
			count[0]++;
		if (ns.contains(String.valueOf(i)))
			count[1]++;
		if (ss.contains(String.valueOf(i)))
			count[2]++;
		return count;
	}
	
	/**
	 * Minus the number of the characters.
	 */
	public static int[] Minus(int[] count, char i)
	{
		if (ls.contains(String.valueOf(i)))
			count[0]--;
		if (ns.contains(String.valueOf(i)))
			count[1]--;
		if (ss.contains(String.valueOf(i)))
			count[2]--;
		return count;
	}
	
	/**
	 * Generate the password shared longest prefix with user input.
	 */
	public static int[] New(int[] count)
	{
		if (changed[4] == false)
		{
			count[4] = 0;
			changed[4] = true;
		}
		else
		{
			if (count[4] >= cs.length()-1)
			{
				count[4] = 0;
				if (changed[3] == false)
				{
					count[3] = 0;
					changed[3] = true;
				}
				else
				{
					if (count[3] >= cs.length()-1)
					{
						count[3] = 0;
						if (changed[2] == false)
						{
							count[2] = 0;
							changed[2] = true;
						}
						else
						{
							if (count[2] >= cs.length()-1)
							{
								count[2] = 0;
								if (changed[1] == false)
								{
									count[1] = 0;
									changed[1] = true;
								}
								else
								{
									if (count[1] >= cs.length()-1)
									{
										count[1] = 0;
										if (changed[0] == false)
										{
											count[0] = 0;
											changed[0] = true;
										}
										else
											count[0]++;
									}
									else
										count[1]++;
								}
							}
							else
								count[2]++;
						}
					}
					else
						count[3]++;
				}
			}
			else
				count[4]++;
		}
		return count;
	}
}

/**
 * The implementation of DLB.
 */
class DLB
{
	static Node rootNode;
	
	public static void Init()
	{
		rootNode = new Node();
	}
	
	public static void Add(String s)
	{
		Node curNode = rootNode;
		s += "*";
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			curNode = AddNode(c, curNode);
		}
	}
	
	public static Node AddNode(char c, Node curNode)
	{
		if (curNode.cNode == null)
		{
			curNode.cNode = new Node(c);
			return curNode.cNode;
		}
		else
		{
			if (curNode.cNode == null)
			{
				curNode.cNode = new Node(c);
				return curNode.cNode;
			}
			else
			{
				Node nextNode = curNode.cNode;
				while (nextNode.pNode != null)
				{
					if (nextNode.value == c)
						break;
					nextNode = nextNode.pNode;
				}
				if (nextNode.value == c)
					return nextNode;
				else
				{
					nextNode.pNode = new Node(c);
					return nextNode.pNode;
				}
			}
		}
	}
	
	public static boolean Search(String key)
	{
		Node curNode = rootNode;
		key += "*";
		for (int i = 0; i < key.length(); i++)
		{
			char c = key.charAt(i);
			curNode = SearchNode(c, curNode);
			if (curNode == null)
				return false;
		}
		return true;
	}
	
	public static Node SearchNode(char c, Node curNode)
	{
		Node nextNode = curNode.cNode;
		while (nextNode != null)
		{
			if (nextNode.value == c)
				break;
			nextNode = nextNode.pNode;
		}
		return nextNode;
	}
}

class Node
{
	Node pNode;
	Node cNode;
	char value;
	
	public Node() {}
	
	public Node(char value)
	{
		this(value, null, null);
	}
	
	public Node(char value, Node pNode, Node cNode)
	{
		this.value = value;
		this.pNode = pNode;
		this.cNode = cNode;
	}
}