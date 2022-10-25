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

/**
 * Stores a Graph using adjacency lists
 * 
 * @author Vineet Ranade
 *
 * @param <T>
 * 		Generic comparable type of Graph elements
 */
public class AdjacencyListGraph<T> extends Graph<T>
{
	/**
	 * Maps keys to Vertex objects
	 */
	Map<T,Vertex> keyToVertex;
	
	/**
	 * Number of vertices
	 */
	int size;
	
	/**
	 * Number of edges
	 */
	int numEdges;
	
	/**
	 * Class representing a Vertex in the Graph
	 * 
	 * @author Vineet Ranade
	 *
	 */
	private class Vertex
	{
		/**
		 * Key corresponding to this Vertex
		 */
		T key;
		
		/**
		 * List of successors (outgoing edge)
		 */
		List<Vertex> successors;
		
		/**
		 * List of predecessors (incoming edge)
		 */
		List<Vertex> predecessors;
		
		/**
		 * Constructs a Vertex object
		 * 
		 * @param key
		 * 		The key corresponding to this Vertex
		 */
		Vertex(T key)
		{
			// Initialize key using parameter
			this.key = key;
			
			// Initialize successor and predecessor lists
			this.successors = new ArrayList<Vertex>();
			this.predecessors = new ArrayList<Vertex>();
		}
	}
	
	/**
	 * Constructs an AdjacencyListGraph
	 * 
	 * @param keys
	 * 		Set of keys for each Vertex in the Graph
	 */
	AdjacencyListGraph(Set<T> keys)
	{
		// Initialize numEdges to 0
		this.numEdges = 0;
		
		// Initialize HashMap for mapping key to Vertex
		this.keyToVertex = new HashMap<T,Vertex>();
		
		// For each key...
		for (T key : keys) 
		{
			// Make a new Vertex from that key
			Vertex v = new Vertex(key);
			
			// Add key-Vertex pair to HashMap
			this.keyToVertex.put(key, v);
			
			// Increment size since we added a Vertex
			this.size++;
		}
	}

	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		
		// Use field
		return this.size;
	}

	@Override
	public int numEdges()
	{
		// TODO Auto-generated method stub
		
		// Use field
		return this.numEdges;
	}

	@Override
	public boolean addEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Both Vertices must exist
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		// Obtain starting and ending Vertices
		Vertex fromVertex = this.keyToVertex.get(from);
		Vertex toVertex = this.keyToVertex.get(to);
		
		// If an edge already exists between the two...
		// NOTE: checking predecessors of the ending Vertex is redundant!
		if (fromVertex.successors.contains(toVertex))
		{
			// Return false...can't add the same edge
			return false;
		}
		
		// Otherwise, add the edge
		
		/*
		 * Add the ending Vertex to successors of starting Vertex
		 * Add the starting Vertex to predecessors of ending Vertex
		 */
		fromVertex.successors.add(toVertex);
		toVertex.predecessors.add(fromVertex);
		
		// Increment numEdges field and return true
		this.numEdges++;
		
		return true;
	}

	@Override
	public boolean hasVertex(T key)
	{
		// TODO Auto-generated method stub
		
		// Check if the HashMap has this key
		return this.keyToVertex.containsKey(key);
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Both Vertices must exist
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		// Obtain starting and ending Vertices
		Vertex fromVertex = this.keyToVertex.get(from);
		Vertex toVertex = this.keyToVertex.get(to);
		
		// Check if an edge exists between the two
		return fromVertex.successors.contains(toVertex);
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Both Vertices must exist
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		// Obtain starting and ending Vertices
		Vertex fromVertex = this.keyToVertex.get(from);
		Vertex toVertex = this.keyToVertex.get(to);
		
		// If an edge doesn't exists between the two...
		if (!fromVertex.successors.contains(toVertex))
		{
			// Return false, can't remove a nonexistent edge
			return false;
		}
		
		/*
		 * Remove the ending Vertex from successors of starting Vertex
		 * Remove the starting Vertex from predecessors of ending Vertex
		 */
		fromVertex.successors.remove(toVertex);
		toVertex.predecessors.remove(fromVertex);
		
		// Decrement numEdges field and return true
		this.numEdges--;
		
		return true;
	}

	@Override
	public int outDegree(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Both Vertices must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		// Return size of the Vertex's successors list
		return this.keyToVertex.get(key).successors.size();
	}

	@Override
	public int inDegree(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Vertex must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		// Return size of the Vertex's predecessors list
		return this.keyToVertex.get(key).predecessors.size();
	}

	@Override
	public Set<T> keySet()
	{
		// TODO Auto-generated method stub
		
		// Simply convert keys into HashMap to a Set!
		return this.keyToVertex.keySet();
	}

	@Override
	public Set<T> successorSet(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Vertex must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		// For each successor, add its key to the Set to return
		Set<T> retSet = new HashSet<T>();
		for (Vertex successor : this.keyToVertex.get(key).successors)
		{
			retSet.add(successor.key);
		}
		
		// Return after all keys have been added
		return retSet;
	}

	@Override
	public Set<T> predecessorSet(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Vertex must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		// For each predecessor, add its key to the Set to return
		Set<T> retSet = new HashSet<T>();
		for (Vertex predecessor : this.keyToVertex.get(key).predecessors)
		{
			retSet.add(predecessor.key);
		}
		
		// Return after all keys have been added
		return retSet;
	}

	@Override
	public Iterator<T> successorIterator(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Vertex must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		// Use helper class below
		return new SuccessorOrPredecessorIterator(key, true);
	}
	
	/**
	 * Class with a SuccessorOrPredecessorIterator
	 * 
	 * @author Vineet Ranade
	 *
	 */
	class SuccessorOrPredecessorIterator implements Iterator<T>
	{
		/**
		 * Current index in successor list
		 */
		private int index;
		
		/**
		 * List of Vertices (successors/predecessors) we care about
		 */
		private List<Vertex> verticesOfInterest;
		
		/**
		 * Constructs a PredecessorIterator
		 * 
		 * @param key
		 * 		The key of the Vertex we want predecessors for
		 */
		public SuccessorOrPredecessorIterator(T key, boolean successors)
		{		
			// Start at index 0 of the List
			this.index = 0;
			
			// Initialize verticesOfInterest list by accessing the right Vertex
			if (successors)
			{
				this.verticesOfInterest = keyToVertex.get(key).successors;				
			}
			else
			{
				this.verticesOfInterest = keyToVertex.get(key).predecessors;				
			}
		}
		
		@Override
		public boolean hasNext() 
		{
			// Make sure our index is in range for verticesOfInterest List
			return this.index < this.verticesOfInterest.size();
		}
		
		@Override
		public T next() throws NoSuchElementException, ConcurrentModificationException
		{
			// If there's no next item, throw NoSuchElementException
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			
			// Return the key at this index in verticesOfInterest
			// Increment index afterwards as bookkeeping for subsequent next() call		
			return this.verticesOfInterest.get(index++).key;
		}
	}

	@Override
	public Iterator<T> predecessorIterator(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Vertex must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		// Use helper class above
		return new SuccessorOrPredecessorIterator(key, false);
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
