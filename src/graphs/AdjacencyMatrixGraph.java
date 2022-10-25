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
 * Stores a Graph using adjacency matrix
 * 
 * @author Vineet Ranade
 *
 * @param <T>
 * 		Generic comparable type of Graph elements
 */
public class AdjacencyMatrixGraph<T> extends Graph<T>
{
	/**
	 * Maps keys to indices
	 */
	Map<T, Integer> keyToIndex;
	
	/**
	 * Maps indices to keys
	 */
	List<T> indexToKey;
	
	/**
	 * Adjacency matrix
	 */
	int[][] matrix;
	
	/**
	 * Constructs an AdjacencyMatrixGraph
	 * 
	 * @param keys
	 * 		Set of keys for each Vertex in the Graph
	 */
	AdjacencyMatrixGraph(Set<T> keys)
	{
		int size = keys.size();
		this.keyToIndex = new HashMap<T,Integer>();
		this.indexToKey = new ArrayList<T>();
		this.matrix = new int[size][size];
		// need to populate keyToIndex and indexToKey with info from keys
		
		// Initialize size to length of Set
		this.size = size;
		// Initialize numEdges to 0
		this.numEdges = 0;
		
		// Declare and initialize array index to 0
		int index = 0;
		
		// For each key...
		for (T key : keys)
		{
			// Map the key to the index
			keyToIndex.put(key, index);
			
			// Map the index to the key
			indexToKey.add(key);
			
			// Increment index for next key
			index++;
		}
	}

	@Override
	public boolean addEdge(T from, T to)
	{
		// TODO Auto-generated method stub
		
		// Both Vertices must exist
		if (!this.hasVertex(from) || !this.hasVertex(to))
		{
			throw new NoSuchElementException();
		}
		
		// Obtain starting and ending indices
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		// If an edge already exists between the two...
		if (this.matrix[fromIndex][toIndex] == 1)
		{
			// Return false...can't add the same edge
			return false;
		}
		
		// Otherwise add the edge
		
		// Set matrix value at [fromIndex][toIndex] to 1 to represent an edge
		this.matrix[fromIndex][toIndex] = 1;
		
		// Increment numEdges field and return true		
		this.numEdges++;
		
		return true;
	}

	@Override
	public boolean hasVertex(T key)
	{
		// TODO Auto-generated method stub
		
		// Check if the keyToIndex mapping has this key
		return this.keyToIndex.containsKey(key);
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
		
		// Obtain starting and ending indices		
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		// Check if an edge exists between the two
		return this.matrix[fromIndex][toIndex] == 1;
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
		
		// Obtain starting and ending indices
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		// If an edge doesn't exists between the two...
		if (this.matrix[fromIndex][toIndex] == 0)
		{
			// Return false, can't remove a nonexistent edge
			return false;
		}
		
		// Set matrix value at [fromIndex][toIndex] to 0 to represent no edge
		this.matrix[fromIndex][toIndex] = 0;
		
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
		
		// fromIndex = row. Map key to row index
		int row = this.keyToIndex.get(key);
		
		// Counter
		int outDegree = 0;
		
		// For each column/col/toIndex in this row
		for (int col = 0; col < this.size(); col++)
		{
			// Increment outDegree if an edge exists
			if (this.matrix[row][col] == 1)
			{
				outDegree++;
			}
		}
		
		// Return final counter
		return outDegree;
	}

	@Override
	public int inDegree(T key) throws NoSuchElementException
	{
		// TODO Auto-generated method stub
		
		// Both Vertices must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		// toIndex = column/col. Map key to column index
		int col = this.keyToIndex.get(key);
		
		// Counter
		int inDegree = 0;
		
		// For each row/fromIndex in this row
		for (int row = 0; row < this.size(); row++)
		{
			// Increment outDegree if an edge exists
			if (this.matrix[row][col] == 1)
			{
				inDegree++;
			}
		}
		
		// Return final counter
		return inDegree;
	}

	@Override
	public Set<T> keySet()
	{
		// TODO Auto-generated method stub
		
		// Simply convert keys into HashMap to a Set!
		return new HashSet<T>(this.indexToKey);
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
		
		// For each toIndex with an edge, add its key to the Set to return
		Set<T> retSet = new HashSet<T>();
		
		// The row/fromIndex is fixed since we want successors
		int row = this.keyToIndex.get(key);
		for (int col = 0; col < this.size(); col++)
		{
			if (this.matrix[row][col] == 1)
			{
				retSet.add(this.indexToKey.get(col));
			}
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
		
		// For each fromIndex with an edge, add its key to the Set to return
		Set<T> retSet = new HashSet<T>();
		
		// The column/col/toIndex is fixed since we want predecessors
		int col = this.keyToIndex.get(key);
		for (int row = 0; row < this.size(); row++)
		{
			if (this.matrix[row][col] == 1)
			{
				retSet.add(this.indexToKey.get(row));
			}
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
		 * Current index in successor list (column)
		 */
		private int index;
		
		/**
		 * Fixed index of the Vertex we want successors/predecessors for
		 */
		private int fixedIndex;
		
		/**
		 * Whether we want successors or predecessors
		 */
		private boolean successors;
		
		/**
		 * Constructs a SuccessorIterator
		 * 
		 * @param key
		 * 		The key of the Vertex we want successors for
		 */
		public SuccessorOrPredecessorIterator(T key, boolean successors)
		{
			this.successors = successors;
			
			// Fix the row at the right index
			this.fixedIndex = keyToIndex.get(key);
			
			// Start at index 0
			this.index = 0;
			
			// If no edge exists...
			if (this.noEdge())
			{
				// Move to the next index in matrix with an edge!
				this.index++;
				this.moveToNextValid();
			}
		}
		
		/**
		 * Checks if no edge exists at the current position in matrix
		 * 
		 * @return
		 * 		true if no edge exists
		 * 		false if an edge exists
		 */
		public boolean noEdge()
		{
			// If we want successors, the fixedIndex is the row
			if (this.successors)
			{
				return matrix[fixedIndex][this.index] == 0;
			}
			
			// If we want predecessors, the fixedIndex is the column
			else
			{
				return matrix[this.index][fixedIndex] == 0;
			}
		}
		
		/**
		 * Helper method to move to next position in matrix with an edge
		 */
		public void moveToNextValid()
		{
			// While we're in range AND no edge exists currently
			while (this.index < size() && this.noEdge())
			{
				// Move to the next position and try again
				this.index++;
			}
			
			// Either we're out of range or have found an edge
		}
		
		@Override
		public boolean hasNext() 
		{
			// Make sure our index is in range for variable indices
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
			
			// Obtain the key of the correct Vertex
			T retVal = indexToKey.get(index);
			
			// Do bookkeeping for subsequent next() call
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
