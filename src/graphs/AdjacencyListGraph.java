package graphs;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class AdjacencyListGraph<T> extends Graph<T>
{
	Map<T,Vertex> keyToVertex;
	
	int size;
	int numEdges;
	
	private class Vertex
	{
		T key;
		List<Vertex> successors;
		List<Vertex> predecessors;
		
		Vertex(T key)
		{
			this.key = key;
			this.successors = new ArrayList<Vertex>();
			this.predecessors = new ArrayList<Vertex>();
		}
	}
	
	AdjacencyListGraph(Set<T> keys)
	{
		this.numEdges = 0;
		this.keyToVertex = new HashMap<T,Vertex>();
		for (T key : keys) 
		{
			Vertex v = new Vertex(key);
			this.keyToVertex.put(key, v);
			this.size++;
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
	public boolean addEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		Vertex fromVertex = this.keyToVertex.get(from);
		Vertex toVertex = this.keyToVertex.get(to);
		
		if (fromVertex.successors.contains(toVertex))
		{
			return false;
		}
		
		fromVertex.successors.add(toVertex);
		toVertex.predecessors.add(fromVertex);
		
		this.numEdges++;
		
		return true;
	}

	@Override
	public boolean hasVertex(T key)
	{
		// TODO Auto-generated method stub
		return this.keyToVertex.containsKey(key);
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		Vertex fromVertex = this.keyToVertex.get(from);
		Vertex toVertex = this.keyToVertex.get(to);
		
		return fromVertex.successors.contains(toVertex);
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		Vertex fromVertex = this.keyToVertex.get(from);
		Vertex toVertex = this.keyToVertex.get(to);
		
		if (!fromVertex.successors.contains(toVertex))
		{
			return false;
		}
		
		fromVertex.successors.remove(toVertex);
		toVertex.predecessors.remove(fromVertex);
		
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
		return this.keyToVertex.get(key).successors.size();
	}

	@Override
	public int inDegree(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		return this.keyToVertex.get(key).predecessors.size();
	}

	@Override
	public Set<T> keySet()
	{
		// TODO Auto-generated method stub
		return this.keyToVertex.keySet();
	}

	@Override
	public Set<T> successorSet(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		Set<T> retSet = new HashSet<T>();
		for (Vertex successor : this.keyToVertex.get(key).successors)
		{
			retSet.add(successor.key);
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
		for (Vertex predecessor : this.keyToVertex.get(key).predecessors)
		{
			retSet.add(predecessor.key);
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
		}
		
		@Override
		public boolean hasNext() 
		{
			// By definition, we have a next item if curNode isn't null
			return this.index < AdjacencyListGraph.this.keyToVertex.get(key).successors.size();
		}
		
		@Override
		public T next() throws NoSuchElementException, ConcurrentModificationException
		{
			// If there's no next item, throw NoSuchElementException
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			
			this.index++;
			
			// Return the return value
			return AdjacencyListGraph.this.keyToVertex.get(key).successors.get(index - 1).key;
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
		}
		
		@Override
		public boolean hasNext() 
		{
			// By definition, we have a next item if curNode isn't null
			return this.index < AdjacencyListGraph.this.keyToVertex.get(key).predecessors.size();
		}
		
		@Override
		public T next() throws NoSuchElementException, ConcurrentModificationException
		{
			// If there's no next item, throw NoSuchElementException
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			
			this.index++;
			
			// Return the return value
			return AdjacencyListGraph.this.keyToVertex.get(key).predecessors.get(index - 1).key;
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
