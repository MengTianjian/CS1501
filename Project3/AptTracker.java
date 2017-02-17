//import java.io.*;
import java.util.*;

public class AptTracker
{
	static IndexPQ indexpq;
	static Scanner sc;
	
	public static void main(String[] args) {
		indexpq = new IndexPQ(10000);
		sc = new Scanner(System.in);
		boolean Do = true;
		String i;
		while (Do)
		{
			Menu();
			i = sc.nextLine();
			//sc.nextLine();
			switch (i)
			{
			case "1":
				Add();
				break;
			case "2":
				Update();
				break;
			case "3":
				Remove();
				break;
			case "4":
				RetrievePrice();
				break;
			case "5":
				RetrieveSquareFootage();
				break;
			case "6":
				RetrievePricebyCity();
				break;
			case "7":
				RetrieveSquareFootagebyCity();
				break;
			case "8":
				Do = false;
				break;
			default:
				System.out.println("Please choose a valid operation!");
			}
		}
		System.out.println("Exit successfully!");
		sc.close();
	}
	
	public static void Menu()
	{
		System.out.println("---------------------------------------------------------");
		System.out.println("Apartment Traker:");
		System.out.println("1. Add an apartment");
		System.out.println("2. Update an apartment");
		System.out.println("3. Remove a specific apartment from consideration");
		System.out.println("4. Retrieve the lowest price apartment");
		System.out.println("5. Retrieve the highest square footage apartment");
		System.out.println("6. Retrieve the lowest price apartment by city");
		System.out.println("7. Retrieve the highest square footage apartment by city");
		System.out.println("8. Exit the traker");
		System.out.println("---------------------------------------------------------");
	}
	
	public static void Add()
	{
		Apt apt = new Apt();
		System.out.println("Please input Street address:");
		apt.Address = sc.nextLine();
		System.out.println("Please input Apartment number:");
		apt.AptNum = sc.nextLine();
		System.out.println("Please input Apartment's ZIP code:");
		apt.ZIP = sc.nextLine();
		int i = indexpq.searchApt(apt.Address, apt.AptNum, apt.ZIP);
		if (i != -1)
		{
			System.out.println("This apartment already exists!");
			return;
		}
		System.out.println("Please input City the apartment is in:");
		apt.City = sc.nextLine();
		System.out.println("Please input Price to rent:");
		apt.Price = sc.nextInt();
		System.out.println("Please input Square footage of the apartment:");
		apt.SquareFootage = sc.nextInt();
		sc.nextLine();
		indexpq.insert(indexpq.size(), apt);
		indexpq.maxpq.insert(indexpq.size()-1);
		indexpq.minpq.insert(indexpq.size()-1);
		System.out.println("Add successfully!");
	}
	
	public static int Search()
	{
		System.out.println("Please input Street address:");
		String Address = sc.nextLine();
		System.out.println("Please input Apartment number:");
		String AptNum = sc.nextLine();
		System.out.println("Please input Apartment's ZIP code:");
		String ZIP = sc.nextLine();
		int i = indexpq.searchApt(Address, AptNum, ZIP);
		if (i == -1)
			System.out.println("This apartment doesn't exist");
		else
		{
			System.out.println("The search result:");
			indexpq.AptOf(i).Display();
		}
		return i;
	}
	
	public static void Update()
	{
		int i = Search();
		if (i == -1)
			return;
		//indexpq.AptOf(i).Display();
		System.out.println("Would you like to update the price?(y/n)");
		String c = sc.nextLine();
		if (c.equals("y"))
		{
			System.out.println("Please input new Price to rent:");
			indexpq.AptOf(i).Price = sc.nextInt();
			sc.nextLine();
			indexpq.minpq.update(i);
			System.out.println("Update successfully!");
		}
		else if (c.equals("n")) {}
		else
			System.out.println("Please choose a valid operation!");
	}
	
	public static void Remove()
	{
		int i = Search();
		if (i == -1)
			return;
		//indexpq.AptOf(i).Display();
		indexpq.delete(i);
		indexpq.maxpq.delete(i);
		indexpq.minpq.delete(i);
		System.out.println("Remove successfully!");
	}
	
	public static void RetrievePrice()
	{
		if (!indexpq.isEmpty())
		{
			System.out.println("The lowest price apartment:");
			indexpq.minpq.minApt().Display();
		}
		else
			System.out.println("No apartment is in the tracker!");
	}
	
	public static void RetrievePricebyCity()
	{
		String City;
		System.out.println("Please input City:");
		City = sc.nextLine();
		Iterator<Apt> it = indexpq.minpq.iterator();
		while (it.hasNext())
		{
			if (it.next().City.equals(City))
			{
				System.out.printf("The lowest price apartment in %s:\n", City);
				it.next().Display();
				return;
			}
			it.remove();
		}
		System.out.println("No apartment is in this city!");
	}
	
	public static void RetrieveSquareFootage()
	{
		if (!indexpq.isEmpty())
		{
			System.out.println("The highest square footage apartment:");
			indexpq.maxpq.maxApt().Display();
		}
		else
			System.out.println("No apartment is in the tracker!");
	}
	
	public static void RetrieveSquareFootagebyCity()
	{
		String City;
		System.out.println("Please input City:");
		City = sc.nextLine();
		Iterator<Apt> it = indexpq.maxpq.iterator();
		
		while (it.hasNext())
		{
			if (it.next().City.equals(City))
			{
				System.out.printf("The highest square footage apartment in %s:\n", City);
				it.next().Display();
				return;
			}
			it.remove();
		}
		System.out.println("No apartment is in this city!");
	}
}

class IndexPQ
{
	private Apt[] apts;
	private int n;
	private HashMap<String, Integer> hm;
	public MaxPQ maxpq;
	public MinPQ minpq;
	
	public IndexPQ(int maxN)
	{
		if (maxN < 0) throw new IllegalArgumentException();
		n = 0;
		apts = new Apt[maxN + 1];
		hm = new HashMap<String, Integer>();
		maxpq = new MaxPQ(maxN);
		minpq = new MinPQ(maxN);
	}
	
	public boolean isEmpty()
	{
		return n == 0;
	}
	
	public int size()
	{
		return n;
	}
	
	public Apt AptOf(int i)
	{
		//if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		//else 
		return apts[i];
	}
	
	public int searchApt(String Address, String AptNum, String ZIP)
	{
		int j = -1;
		if (hm.containsKey((Address+AptNum+ZIP).toLowerCase()))
			j = hm.get((Address+AptNum+ZIP).toLowerCase());
		return j;
	}
	
	public void insert(int i, Apt apt)
	{
		n++;
		apts[i] = apt;
		hm.put((apt.Address+apt.AptNum+apt.ZIP).toLowerCase(), i);
		//maxpq.insert(i);
		//minpq.insert(i);
	}
	
	public void delete(int i)
	{
		if (apts[i] == null) throw new NoSuchElementException("index is not in the priority queue");
		n--;
		//maxpq.delete(i);
		//minpq.delete(i);
		hm.remove((apts[i].Address+apts[i].AptNum+apts[i].ZIP).toLowerCase());
		apts[i] = null;
	}
	
	class MaxPQ// extends IndexPQ
	{
		private int[] pq;
		private int[] qp;
		
		public MaxPQ(int maxN) {
			//super(maxN);
			pq = new int[maxN + 1];
			qp = new int[maxN + 1];
			for (int i = 0; i <= maxN; i++)
				qp[i] = -1;
		}

		public void insert(int i)
		{
			qp[i] = n;
			pq[n] = i;
			swim(n);
		}
		
		public Apt maxApt()
		{
			if (n == 0) throw new NoSuchElementException("Priority queue underflow");
			return apts[pq[1]];
		}
		
		public void delete(int i)
		{
			int index = qp[i];
			exch(index, n+1);
			swim(index);
			sink(index);
			qp[i] = -1;
		}
		
		public void exch(int i, int j)
		{
			int swap = pq[i];
			pq[i] = pq[j];
			pq[j] = swap;
			qp[pq[i]] = i;
			qp[pq[j]] = j;
		}
		
		private boolean less(int i, int j)
		{
			return apts[pq[i]].SquareFootage < apts[pq[j]].SquareFootage;
		}
		
		
		private void swim(int k)
		{
			while (k > 1 && less(k/2, k))
			{
				exch(k, k/2);
				k = k/2;
			}
		}
		
		private void sink(int k)
		{
			while (2*k <= n)
			{
				int j = 2*k;
				if (j < n && less(j, j+1)) j++;
				if (!less(k,j)) break;
				exch(k, j);
				k = j;
			}
		}
		
		public Iterator<Apt> iterator()
		{
			return new HeapIterator();
		}
		
		class HeapIterator implements Iterator<Apt>
		{
			private IndexPQ copy;
			
			public HeapIterator()
			{
				copy = new IndexPQ(n+1);
				for (int i = 1; i <= n; i++)
				{
					copy.insert(pq[i], apts[pq[i]]);
					copy.maxpq.insert(pq[i]);
				}
			}
			
			public boolean hasNext()
			{
				return !copy.isEmpty();
			}
			
			public void remove()
			{
				copy.delete(copy.maxpq.pq[1]);
				copy.maxpq.delete(copy.maxpq.pq[1]);
			}
			
			public Apt next() {
				if (!hasNext()) throw new NoSuchElementException();
				return copy.maxpq.maxApt();
			}
		}
	}
	
	class MinPQ// extends IndexPQ
	{
		private int[] pq;
		private int[] qp;
		
		public MinPQ(int maxN) {
			//super(maxN);
			pq = new int[maxN + 1];
			qp = new int[maxN + 1];
			for (int i = 0; i <= maxN; i++)
				qp[i] = -1;
		}
		
		public void insert(int i)
		{
			qp[i] = n;
			pq[n] = i;
			swim(n);
		}
		
		public Apt minApt()
		{
			if (n == 0) throw new NoSuchElementException("Priority queue underflow");
			return apts[pq[1]];
		}
		
		public void update(int i)
		{
			swim(qp[i]);
			sink(qp[i]);
		}
		
		public void delete(int i)
		{
			int index = qp[i];
			exch(index, n+1);
			swim(index);
			sink(index);
			qp[i] = -1;
		}
		
		private boolean greater(int i, int j)
		{
			return apts[pq[i]].Price > apts[pq[j]].Price;
		}
		
		public void exch(int i, int j)
		{
			int swap = pq[i];
			pq[i] = pq[j];
			pq[j] = swap;
			qp[pq[i]] = i;
			qp[pq[j]] = j;
		}
		
		private void swim(int k)
		{
			while (k > 1 && greater(k/2, k))
			{
				exch(k, k/2);
				k = k/2;
			}
		}
		
		private void sink(int k)
		{
			while (2*k <= n)
			{
				int j = 2*k;
				if (j < n && greater(j, j+1)) j++;
				if (!greater(k,j)) break;
				exch(k, j);
				k = j;
			}
		}
		
		public Iterator<Apt> iterator()
		{
			return new HeapIterator();
		}
		
		class HeapIterator implements Iterator<Apt>
		{
			private IndexPQ copy;
			
			public HeapIterator()
			{
				copy = new IndexPQ(n+1);
				for (int i = 1; i <= n; i++)
				{
					copy.insert(pq[i], apts[pq[i]]);
					copy.minpq.insert(pq[i]);
				}
			}
			
			public boolean hasNext()
			{
				return !copy.isEmpty();
			}
			
			public void remove()
			{
				copy.delete(copy.minpq.pq[1]);
				copy.minpq.delete(copy.minpq.pq[1]);
			}
			
			public Apt next() {
				if (!hasNext()) throw new NoSuchElementException();
				return copy.minpq.minApt();
			}
		}
	}
}



class Apt
{
	public String Address;
	public String AptNum;
	public String City;
	public String ZIP;
	public int Price;
	public int SquareFootage;
	
	public Apt() {}
	
	public void Display()
	{
		System.out.printf("Street address:\t\t\t%s\n", Address);
		System.out.printf("Apartment number:\t\t%s\n", AptNum);
		System.out.printf("City the apartment is in:\t%s\n", City);
		System.out.printf("Apartment's ZIP code:\t\t%s\n", ZIP);
		System.out.printf("Price to rent:\t\t\t%d\n", Price);
		System.out.printf("Square footage:\t\t\t%d\n", SquareFootage);
	}
}