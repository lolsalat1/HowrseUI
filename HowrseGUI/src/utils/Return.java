package utils;

public class Return<E> {

	public Return(E data, boolean sucess) {
		this.data = data;
		this.sucess = sucess;
	}
	
	public E data;
	public boolean sucess;
	
}
