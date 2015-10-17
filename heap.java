package code;

import java.util.Arrays;
import java.util.Collections;

public class heap 
{
	private int size;
	private heapTerm[] heap;
	
	public heap(int capacity)
	{
		size = 0;
		heap = new heapTerm[capacity];
		for(int i = 0 ; i < heap.length; i++)
		{
			heapTerm t = new heapTerm();
			heap[i] = t;
		}
		
	}
	
	
	public void add(Term k)
	{
		if(k.count < 0)
			return;
		heapTerm ht = new heapTerm(k);
		if(size == heap.length && ht.count > heap[0].count)
		{
			
			heap[0] = ht;
			ShiftDown();
		}
		else if(size < heap.length)
		{
			heap[size] = ht;
			size ++;
			ShiftUp();
			
		}
	
	}
	
	public void ShiftDown()
	{
		if(size != heap.length)
		{
			System.out.println("heap is not full. Why Shift down?");
			return;
		}
		int index = 0;
		
		while(index < heap.length)
		{
			int child1 = 2*index + 1;
			int child2 = 2*index + 2;
			if(child1 >= heap.length && child2 >= heap.length)
					break;
			else if(child1 >= heap.length && child2 < heap.length)
			{
				if(heap[index].count > heap[child2].count)
				{
					heapTerm temp = heap[index];
					heap[index] = heap[child2];
					heap[child2] = temp;
					index = child2;
				}
				else
					break;
				
			}
			else if(child1 < heap.length && child2 >= heap.length)
			{
				if(heap[index].count > heap[child1].count)
				{
					heapTerm temp = heap[index];
					heap[index] = heap[child1];
					heap[child1] = temp;
					index = child1;
				}
				else
					break;
				
			}
			else
			{
				if(heap[child1].count <= heap[child2].count && heap[child1].count < heap[index].count)
				{
					heapTerm temp = heap[index];
					heap[index] = heap[child1];
					heap[child1] = temp;
					index = child1;
				}
				else if(heap[child2].count < heap[child1].count && heap[child2].count < heap[index].count)
				{
					heapTerm temp = heap[index];
					heap[index] = heap[child2];
					heap[child2] = temp;
					index = child2;
				}
				else
					break;
				
			}
		
		}
		
		
		
	}
	
	public void ShiftUp()
	{
		int index = size-1;	
		
		while(index > 0)
		{
			int parent;
			if(index %2 == 0)
				parent = (index-2)/2;
			else
				parent = (index -1)/2;
			
			if(heap[parent].count > heap[index].count)
			{
				heapTerm temp = heap[index];
				heap[index] = heap[parent];
				heap[parent] = temp;
			}
			index = parent;
		}
		return;
		
	}
	
	public void print()
	{
		for(int i = 0; i < heap.length; i++)
		{
			System.out.print(heap[i].term_value );
			if(i != heap.length - 1)
				System.out.print(", ");
			
		}
		
	}


	public heapTerm[] getList() {
		// TODO Auto-generated method stub
		return heap;
	}


	public void sort() 
	{
		// TODO Auto-generated method stub
		Arrays.sort(heap);
	}
	
	

}

class heapTerm implements Comparable <heapTerm> // doing this to avoid passing the huge postings list for comparison in the heap.
{
	String term_value;
	int count;
	
	heapTerm()
	{
		term_value = "";
		count = -1;
	}
	
	heapTerm(Term A)
	{
		term_value = A.term_value;
		count = A.count;
	}
	 public int compareTo(heapTerm term2) 
	 {
        int compareCount= term2.count;
        return compareCount-this.count;
	 }
}
