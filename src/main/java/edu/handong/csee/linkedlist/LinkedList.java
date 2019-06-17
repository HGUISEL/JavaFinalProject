package edu.handong.csee.linkedlist;

import java.util.ArrayList;

public class LinkedList {
	private Node head;
	private Node tail;
	private int size = 0;
	private class Node{
		private String studentid;
		private ArrayList<String> data;
		private Node next;
		public Node(ArrayList<String> input, String studentid) {
			this.data = input;
			this.studentid = studentid;
			this.next = null;
		}
		public String toString() {
			return String.valueOf(this.data);
		}
	}
	
	public void addFirst(ArrayList<String> input, String studentid) {
		Node newNode = new Node(input, studentid);
		newNode.next = head;
		head = newNode;
		size++;
		if(head.next == null) {
			tail = head;
		}
	}
	
	public void addLast(ArrayList<String> input, String studentid) {
		Node newNode = new Node(input, studentid);
		if(size==0) {
			addFirst(input, studentid);
		} else {
			tail.next = newNode;
			tail = newNode;
			size++;
		}
	}
	
	public Node node(int index) {
		Node x = head;
		for(int i = 0; i<index; i++) {
			x = x.next;
		}
		return x;
	}
	
	public int findNodeIndexWithStudentID(String studentid) {
		Node x = head;
		for(int i = 0; i< size; i++) {
			if(Integer.parseInt(x.studentid) < Integer.parseInt(studentid)) {
				x = x.next;
			} else {
				return i;
			}
		}
		return size;
		
		
		/**
		 * 만약 0을 넣으려고 하는데 3,4,5다그러면 
		 * size = 3 index 0,1,2로 감.
		 * return 0
		 * 
		 * 만약 5를 넣으려고 하는데 1,2,3다 그러면
		 * size = 3 index 0,1,2로 감
		 * 0,1,2
		 * return 3
		 * 
		 * 들어갈 곳을 return.
		 * 
		 * string을 int로 바꿔서 하기때문에
		 * 파일이름에 int가 아닌 글자가 들어가면 에러남.
		 * compareTo() 사용하면 좋음.
		 */
	}
	
	public void add(int k, ArrayList<String> input, String studentid) {
		if(k ==0) {
			addFirst(input, studentid);
		} else {
			Node temp1 = node(k-1);
			Node temp2 = temp1.next;
			Node newNode = new Node(input, studentid);
			temp1.next = newNode;
			newNode.next = temp2;
			size++;
			if(newNode.next == null) {
				tail = newNode;
			}
		}
	}
	
	public void add(ArrayList<String> input, String studentid) {
		int index = findNodeIndexWithStudentID(studentid);
		//index 자리에 넣으면 됨.
		add(index, input, studentid);
	}
	
	public String toString() {
		if(head==null) {
			return "nothing";
		}
		
		Node temp = head;
		String str = "[";
		
		while(temp.next != null) {
			str += temp.studentid + ",";
			temp = temp.next;
		}
		
		str += temp.studentid;
		return str + "]";
	}
	
	public int size() {
		return size;
	}
	
	public ArrayList<String> getdata(int k) {
		Node temp = node(k);
		return temp.data;
	}
	
	public String getstudentid(int k) {
		Node temp = node(k);
		return temp.studentid;
	}
}
