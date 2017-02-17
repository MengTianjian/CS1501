package airline;
public class Route
{
	private int v;
	private int w;
	private int distance;
	private double price;
	
	public Route(int v, int w, int distance, double price)
	{
		this.v = v;
		this.w = w;
		this.distance = distance;
		this.price = price;
	}
	
	public int distance()
	{
		return distance;
	}
	
	public double price()
	{
		return price;
	}
	
	public int either()
	{
		return v;
	}
	
	public int other(int vertex)
	{
		if (vertex == v) return w;
		else if (vertex == w) return v;
		else throw new IllegalArgumentException("Illegal endpoint");
	}
}