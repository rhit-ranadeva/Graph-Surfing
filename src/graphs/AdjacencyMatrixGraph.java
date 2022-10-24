package graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import graphs.AdjacencyListGraph.PredecessorIterator;
import graphs.AdjacencyListGraph.SuccessorIterator;

public class AdjacencyMatrixGraph<T> extends Graph<T>
{
	Map<T,Integer> keyToIndex;
	List<T> indexToKey;
	int[][] matrix;
	
	int size;
	int numEdges;
	
	AdjacencyMatrixGraph(Set<T> keys)
	{
		int size = keys.size();
		this.keyToIndex = new HashMap<T,Integer>();
		this.indexToKey = new ArrayList<T>();
		this.matrix = new int[size][size];
		// need to populate keyToIndex and indexToKey with info from keys
		this.size = size;
		this.numEdges = 0;
		int index = 0;
		for (T key : keys)
		{
			keyToIndex.put(key, index);
			indexToKey.add(key);
			index++;
		}
	}
	
	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return this.size;
	}

	@Override
	public int numEdges()
	{
		// TODO Auto-generated method stub
		return this.numEdges;
	}

	@Override
	public boolean addEdge(T from, T to)
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		if (this.matrix[fromIndex][toIndex] == 1)
		{
			return false;
		}
		
		this.matrix[fromIndex][toIndex] = 1;
		
		this.numEdges++;
		
		return true;
	}

	@Override
	public boolean hasVertex(T key)
	{
		// TODO Auto-generated method stub
		return this.keyToIndex.containsKey(key);
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		return this.matrix[fromIndex][toIndex] == 1;
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		if (this.matrix[fromIndex][toIndex] == 0)
		{
			return false;
		}
		
		this.matrix[fromIndex][toIndex] = 0;
		
		this.numEdges--;
		
		return true;
	}

	@Override
	public int outDegree(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		int row = this.keyToIndex.get(key);
		int outDegree = 0;
		
		for (int col = 0; col < this.size(); col++)
		{
			if (this.matrix[row][col] == 1)
			{
				outDegree++;
			}
		}
		
		return outDegree;
	}

	@Override
	public int inDegree(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		int col = this.keyToIndex.get(key);
		int inDegree = 0;
		
		for (int row = 0; row < this.size(); row++)
		{
			if (this.matrix[row][col] == 1)
			{
				inDegree++;
			}
		}
		
		return inDegree;
	}

	@Override
	public Set<T> keySet()
	{
		// TODO Auto-generated method stub
		return new HashSet<T>(this.indexToKey);
	}

	@Override
	public Set<T> successorSet(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		Set<T> retSet = new HashSet<T>();
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		int row = this.keyToIndex.get(key);
		for (int col = 0; col < this.size(); col++)
		{
			if (this.matrix[row][col] == 1)
			{
				retSet.add(this.indexToKey.get(col));
			}
		}
		return retSet;
	}

	@Override
	public Set<T> predecessorSet(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		Set<T> retSet = new HashSet<T>();
		int col = this.keyToIndex.get(key);
		for (int row = 0; row < this.size(); row++)
		{
			if (this.matrix[row][col] == 1)
			{
				retSet.add(this.indexToKey.get(row));
			}
		}
		return retSet;
	}

	@Override
	public Iterator<T> successorIterator(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		return new SuccessorIterator(key);
	}
	
	class SuccessorIterator implements Iterator<T>
	{
		private int index;
		private T key;
		
		public SuccessorIterator(T key)
		{
			this.key = key;
			this.index = 0;
			if (matrix[keyToIndex.get(key)][this.index] == 0)
			{
				this.index++;
				this.moveToNextValid();
			}
		}
		
		public void moveToNextValid()
		{
			while (this.index < size() && matrix[keyToIndex.get(key)][this.index] == 0)
			{
				this.index++;
			}
			return;
		}
		
		@Override
		public boolean hasNext() 
		{
			// By definition, we have a next item if curNode isn't null
			return this.index < size();
		}
		
		@Override
		public T next() throws NoSuchElementException, ConcurrentModificationException
		{
			// If there's no next item, throw NoSuchElementException
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			
			T retVal = indexToKey.get(index);
			
			this.index++;
			this.moveToNextValid();
			
			// Return the return value
			return retVal;
		}
	}

	@Override
	public Iterator<T> predecessorIterator(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		return new PredecessorIterator(key);
	}
	
	class PredecessorIterator implements Iterator<T>
	{
		private int index;
		private T key;
		
		public PredecessorIterator(T key)
		{
			this.key = key;
			this.index = 0;
			if (matrix[this.index][keyToIndex.get(key)] == 0)
			{
				this.index++;
				this.moveToNextValid();
			}
		}
		
		public void moveToNextValid()
		{
			while (this.index < size() && matrix[this.index][keyToIndex.get(key)] == 0)
			{
				this.index++;
			}
			return;
		}
		
		@Override
		public boolean hasNext() 
		{
			// By definition, we have a next item if curNode isn't null
			return this.index < size();
		}
		
		@Override
		public T next() throws NoSuchElementException, ConcurrentModificationException
		{
			// If there's no next item, throw NoSuchElementException
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			
			T retVal = indexToKey.get(index);
			
			this.index++;
			this.moveToNextValid();
			
			// Return the return value
			return retVal;
		}
	}

	@Override
	public Set<T> stronglyConnectedComponent(T key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> shortestPath(T startLabel, T endLabel)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
