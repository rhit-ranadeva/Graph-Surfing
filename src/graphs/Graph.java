package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Abstract class to represent the Graph ADT. It is assumed that every vertex contains some 
 * data of type T, which serves as the identity of that node and provides access to it.
 * 
 * @author Nate Chenette and Vineet Ranade
 *
 * @param <T>
 * 		Generic comparable type of Graph elements
 */
public abstract class Graph<T> 
{
	/**
	 * Number of vertices
	 */
	int size;
	
	/**
	 * Number of edges
	 */
	int numEdges;
	
	/**
	 * Returns the number of vertices in the graph.
	 * @return
	 * 		The number of vertices in the graph
	 */
	public int size()
	{
		// Simply use the field
		return this.size;
	}

	/**
	 * Returns the number of edges in the graph.
	 * @return
	 * 		The number of edges in the graph
	 */
	public int numEdges()
	{
		// Simply use the field
		return this.numEdges;
	}

	
	/**
	 * Adds a directed edge from the vertex containing "from" to the vertex containing "to". 
	 * @param from
	 * @param to
	 * @return true if the add is successful, false if the edge is already in the graph.
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public abstract boolean addEdge(T from, T to);

	
	/**
	 * Determines whether a vertex is in the graph.
	 * @param key
	 * @return true if the graph has a vertex containing key.
	 */
	public abstract boolean hasVertex(T key);
	
	
	/**
	 * Determines whether an edge is in the graph.
	 * @param from
	 * @param to
	 * @return true if the directed edge (from, to) is in the graph, otherwise false.
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public abstract boolean hasEdge(T from, T to) throws NoSuchElementException;
	
	
	/**
	 * Removes an edge from the graph.
	 * @param from
	 * @param to
	 * @return true if the remove is successful, false if the edge is not in the graph.
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public abstract boolean removeEdge(T from, T to) throws NoSuchElementException;
	
	/**
	 * Computes out-degree of a vertex.
	 * @param key
	 * @return the number of successors of the vertex containing key
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract int outDegree(T key) throws NoSuchElementException;

	
	/**
	 * Computes in-degree of a vertex.
	 * @param key
	 * @return the number of predecessors of the vertex containing key
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract int inDegree(T key) throws NoSuchElementException;
	
	
	/**
	 * Returns the Set of vertex keys in the graph. 
	 * @return
	 */
	public abstract Set<T> keySet();
	
	/**
	 * Returns a Set of keys that are successors of the given key.
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Set<T> successorSet(T key) throws NoSuchElementException;
	
	/**
	 * Returns a Set of keys that are predecessors of the given key.
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Set<T> predecessorSet(T key) throws NoSuchElementException;
	
	/**
	 * Returns an Iterator that traverses the keys who are successors of the given key.
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Iterator<T> successorIterator(T key) throws NoSuchElementException;
	
	/**
	 * Returns an Iterator that traverses the keys who are successors of the given key.
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Iterator<T> predecessorIterator(T key) throws NoSuchElementException;
	
	/**
	 * Finds the strongly-connected component of the provided key.
	 * @param key
	 * @return a set containing all data in the strongly connected component of the vertex
	 * containing key 
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public Set<T> stronglyConnectedComponent(T key) throws NoSuchElementException
	{
		// Vertex must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		/*
		 * Algorithm: do a DFS forwards and backwards (i.e. using
		 * successors and predecessors, respectively) starting
		 * from the input key/vertex. Vertices that emerge in BOTH searches
		 * are in the strongly connected component of the input vertex.
		 */
		
		/**
		 * HashSet to store items from the successor DFS
		 */
		Set<T> forwardsOnly = new HashSet<T>();
		
		/**
		 * HashSet to store Vertices already visited to avoid repetition
		 */
		Set<T> visited = new HashSet<T>();
		
		/**
		 * Use Stack for Vertices to visit since we want DEPTH-first
		 */
		Stack<T> toVisit = new Stack<T>();
		
		Set<T> toVisitSet = new HashSet<T>();
		
		/**
		 * Push the input key to the stack and begin the search
		 */
		toVisit.push(key);
		toVisitSet.add(key);
		
		// While we still have Vertices to visit in the stack...
		while (!toVisit.isEmpty())
		{
			// Visit the Vertex on top of the stack
			T vertexToVisit = toVisit.pop();
			toVisitSet.remove(vertexToVisit);
			
			// Add this Vertex to both of the "tracking" HashSets
			forwardsOnly.add(vertexToVisit);
			visited.add(vertexToVisit);
			
			// For each successor of this Vertex...
			Iterator<T> successorIterator = successorIterator(vertexToVisit);
			while (successorIterator.hasNext())
			{
				/*
				 * Push the successor to the Stack if we haven't seen it before
				 * AND if it's not already in the Stack. This is why we use
				 * the toVisitSet--contains() is much faster on the Set than 
				 * it is on the Stack.
				 */
				T neighbor = successorIterator.next();
				if (!visited.contains(neighbor) && !toVisitSet.contains(neighbor))
				{
					toVisitSet.add(neighbor);
					toVisit.push(neighbor);
				}
			}
		}
		
		// Reset visited HashSet
		visited = new HashSet<T>();
		
		/*
		 * Return value HashSet: the strongly connected component
		 */
		Set<T> scc = new HashSet<T>();
		
		/**
		 * Push the input key to the stack and begin the search
		 */
		toVisit.push(key);
		toVisitSet.add(key);
		
		// While we still have Vertices to visit in the stack...
		while (!toVisit.isEmpty())
		{
			// Visit the Vertex on top of the stack
			T vertexToVisit = toVisit.pop();
			toVisitSet.remove(vertexToVisit);
			
			// If we saw this Vertex using the successors as well...
			if (forwardsOnly.contains(vertexToVisit))
			{
				/*
				 * This Vertex IS part of the strongly connected component!
				 * This is because we could reach this Vertex from the input
				 * Vertex, and we can reach the input Vertex from this Vertex!
				 * The same logic would apply for each combination of Vertices
				 * along the way.
				 */
				scc.add(vertexToVisit);
			}
			
			// Add this Vertex to the "tracking" HashSet
			visited.add(vertexToVisit);
			
			// For each predecessor of this Vertex...
			Iterator<T> predecessorIterator = predecessorIterator(vertexToVisit);
			while (predecessorIterator.hasNext())
			{
				/*
				 * Push the predecessor to the Stack if we haven't seen it before
				 * AND if it's not already in the Stack. This is why we use
				 * the toVisitSet--contains() is much faster on the Set than 
				 * it is on the Stack.
				 */
				T neighbor = predecessorIterator.next();
				if (!visited.contains(neighbor) && !toVisitSet.contains(neighbor))
				{
					toVisitSet.add(neighbor);
					toVisit.push(neighbor);
				}
			}
		}
		
		// Return the strongly-connected component
		return scc;
	}
	
	/**
	 * Searches for the shortest path between start and end points in the graph.
	 * @param startLabel
	 * @param endLabel
	 * @return a list of data, starting with start and ending with end, that gives the path through
	 * the graph, or null if no such path is found.  
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public List<T> shortestPath(T startLabel, T endLabel) throws NoSuchElementException
	{
		// Both Vertices must exist
		if (!this.hasVertex(startLabel) || !this.hasVertex(endLabel))
		{
			throw new NoSuchElementException();
		}
		
		// If we get a degenerate case...return a List with startLabel
		if (startLabel.equals(endLabel))
		{
			List<T> retList = new ArrayList<T>();
			retList.add(startLabel);
			return retList;
		}
		
		/**
		 * Tracks visited Vertices
		 */
		Set<T> visited = new HashSet<T>();
		
		/**
		 * Queue of Vertices to visit for breadth-first search
		 */
		Queue<T> toVisit = new LinkedList<>();
		
		/**
		 * Set of Vertices to visit.
		 * Seems redundant but was needed for 
		 */
		Set<T> toVisitSet = new HashSet<T>();
		
		/**
		 * Maps a Vertex to the predecessor which added it to toVisit
		 */
		Map<T, T> childToParent = new HashMap<T, T>();
		
		// Begin with startLabel
		toVisit.add(startLabel);
		toVisitSet.add(startLabel);
		
		// While we have Vertices to visit...
		while (!toVisit.isEmpty())
		{
			// Dequeue to find next Vertex to visit
			T vertexToVisit = toVisit.poll();
			toVisitSet.remove(vertexToVisit);
			
			// Update visited
			visited.add(vertexToVisit);
			
			// For each successor of this Vertex...
			Iterator<T> successorIterator = successorIterator(vertexToVisit);
			while (successorIterator.hasNext())
			{
				T neighbor = successorIterator.next();
				
				// If this successor hasn't been visited and isn't already in the queue...
				if (!visited.contains(neighbor) && !toVisitSet.contains(neighbor))
				{
					// Update the mapping
					childToParent.put(neighbor, vertexToVisit);
					
					// Enqueue this successor
					toVisit.offer(neighbor);
					toVisitSet.add(neighbor);
					
					// If we've found our destination...
					if (neighbor.equals(endLabel))
					{
						// Create empty ArrayList
						ArrayList<T> retList = new ArrayList<T>();
						
						// Call recursive helper to populate retList and return
						this.populateList(startLabel, neighbor, childToParent, retList);
						return retList;
					}
				}
			}
		}
		
		// If we couldn't find a path, return null
		return null;
	}
	
	/**
	 * Populates a List with shortest path
	 * 
	 * @param startLabel
	 * 		The label of the staring Vertex
	 * @param curVertex
	 * 		The current Vertex
	 * @param childToParent
	 * 		Maps a Vertex to its predecessor in the shortest path
	 * @param retList
	 * 		The List to populate
	 */
	private void populateList(T startLabel, T curVertex, Map<T, T> childToParent, List<T> retList)
	{
		// Base case: if we're at the starting Vertex...
		if (curVertex.equals(startLabel))
		{
			// Add to List
			retList.add(curVertex);
		}
		
		// Recursive case: otherwise...
		else
		{
			// Add everything coming before curVertex in the shortest Path, then add curVertex
			populateList(startLabel, childToParent.get(curVertex), childToParent, retList);
			retList.add(curVertex);
		}
	}

	/********************************************/
	/***EVERYTHING*BELOW*HERE*IS*FOR*THE*BONUS***/
	/********************************************/
	
	/**
	 * Simple wrapper class with 2 Vertices' data
	 * 
	 * @author Vineet Ranade
	 *
	 */
	public class StartEnd
	{
		/**
		 * Start Vertex data
		 */
		T start;
		
		/**
		 * End Vertex data
		 */
		T end;
		
		/**
		 * Constructs a StartEnd object with empty data
		 */
		public StartEnd()
		{
			this.start = null;
			this.end = null;
		}
		
		@Override
		public String toString()
		{
			return this.start.toString() + " and " + this.end.toString();
		}
	}
	
	/**
	 * Finds the longest shortest path between two vertices in a Graph
	 * 
	 * @return
	 * 		Wrapper object containing the start and end vertices yielding
	 * 		the longest shortest path
	 */
	public StartEnd longestShortestPath()
	{
		/*
		 * The final return value
		 */
		StartEnd retVal = new StartEnd();
		
		/*
		 * The length of the longest shortest path so far
		 */
		int maxLength = 0;
		
		/*
		 * The current shortest path
		 */
		List<T> path;
		
		/*
		 * The longest shortest path so far
		 */
		List<T> maxPath = new ArrayList<T>();
		
		/*
		 * The length of the current shortest path
		 */
		int pathLength;
		
		/*
		 * Set of all Vertices in the Graph
		 */
		Set<T> searchSet = keySet();
		
		/*
		 * The component (Set of reachable Vertices)
		 * of the current Vertex
		 */
		Set<T> component;
		
		/*
		 * The next 2 variables were used because I never actually
		 * got this method to finish running! I simply printed
		 * the longest shortest path every now and then using
		 * these variables
		 */
		
		/*
		 * Number of Vertices traversed in the inner for loop
		 */
		int numInside;
		
		/*
		 * Which Vertex we're on in the outer for loop
		 */
		int numOutside = 1;
		
		// For each Vertex in the Graph...
		for (T startVertex : searchSet)
		{
			numInside = 0;
			
			// For each other Vertex reachable from this Vertex...
			component = connectedComponent(startVertex);
			for (T endVertex : component)
			{
				// Compute shortest path
				path = shortestPath(startVertex, endVertex);
				
				// Compute length
				pathLength = path.size();
				
				// If this is the longest shortest path, update the variables
				if (pathLength > maxLength)
				{
					retVal.start = startVertex;
					retVal.end = endVertex;
					maxPath = path;
					maxLength = pathLength;
					System.out.println("New max length: " + maxLength + "--" + retVal.toString());
				}
				numInside++;
				System.out.println("Done with " + numInside + " out of " + component.size() + " for Vertex " + numOutside);
			}
			
			numOutside++;
		}
		
		// Return the start and end Vertices yielding longest shortest path
		System.out.println(maxPath);
		return retVal;
	}
	
	/**
	 * Finds the (NOT strongly) connected component of the provided key.
	 * @param key
	 * @return a set containing all Vertices reachable from the Vertex containing key
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public Set<T> connectedComponent(T key) throws NoSuchElementException
	{
		// Vertex must exist
		if (!this.hasVertex(key))
		{
			throw new NoSuchElementException();
		}
		
		/*
		 * Algorithm: do a DFS forwards and backwards (i.e. using
		 * successors and predecessors, respectively) starting
		 * from the input key/vertex. Vertices that emerge in BOTH searches
		 * are in the strongly connected component of the input vertex.
		 */
		
		/**
		 * HashSet to store items from the successor DFS
		 */
		Set<T> forwardsOnly = new HashSet<T>();
		
		/**
		 * HashSet to store Vertices already visited to avoid repetition
		 */
		Set<T> visited = new HashSet<T>();
		
		/**
		 * Use Stack for Vertices to visit since we want DEPTH-first
		 */
		Stack<T> toVisit = new Stack<T>();
		
		Set<T> toVisitSet = new HashSet<T>();
		
		/**
		 * Push the input key to the stack and begin the search
		 */
		toVisit.push(key);
		toVisitSet.add(key);
		
		// While we still have Vertices to visit in the stack...
		while (!toVisit.isEmpty())
		{
			// Visit the Vertex on top of the stack
			T vertexToVisit = toVisit.pop();
			toVisitSet.remove(vertexToVisit);
			
			// Add this Vertex to both of the "tracking" HashSets
			forwardsOnly.add(vertexToVisit);
			visited.add(vertexToVisit);
			
			// For each successor of this Vertex...
			Iterator<T> successorIterator = successorIterator(vertexToVisit);
			while (successorIterator.hasNext())
			{
				/*
				 * Push the successor to the Stack if we haven't seen it before
				 * AND if it's not already in the Stack. This is why we use
				 * the toVisitSet--contains() is much faster on the Set than 
				 * it is on the Stack.
				 */
				T neighbor = successorIterator.next();
				if (!visited.contains(neighbor) && !toVisitSet.contains(neighbor))
				{
					toVisitSet.add(neighbor);
					toVisit.push(neighbor);
				}
			}
		}
		
		// Return all reachable Vertices
		return forwardsOnly;
	}
	
}

