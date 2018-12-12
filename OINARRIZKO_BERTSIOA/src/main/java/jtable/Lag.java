package jtable;

import java.util.ArrayList;
import java.util.Vector;

class Lag {
	String txat;
	String lastMessage;
	String id;
	ArrayList<Object> a=new ArrayList<Object>();

	public Lag(String pTxat,String pLast, String pId) {
		super();
		this.txat = pTxat;
		this.lastMessage = pLast;
		this.id = pId;
		a.add(this.txat);
		a.add(this.lastMessage);
		a.add(this.id);
	}

	public Object getBalioa(int i) {
		return a.get(i);
	}

	public void insertElementAt(Object value, int i){
		a.set(i, value);
	}
	
	public static void main(String[] args) {
		Vector<Lag> data = new Vector<Lag>();
	}

	@Override
	public String toString() {
		return "Lag [txat=" + txat + ", id=" + id + "]";
	}
	
 }
