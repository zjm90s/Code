package z.model;

public class Pair<T,E> {

	public T first;
	public E second;
	
	public Pair() {}
	
	public Pair(T first, E second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}
	
}
