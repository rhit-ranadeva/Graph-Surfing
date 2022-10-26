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
	

//	/** Finds the strongly-connected component of the provided key.
//	 * @param key
//	 * @return a set containing all data in the strongly connected component of the vertex
//	 * containing key 
//	 * @throws NoSuchElementException if the key is not found in the graph
//	 */
//	public Set<T> stronglyConnectedComponent(T key) throws NoSuchElementException
//	{
//		// Vertex must exist
//		if (!this.hasVertex(key))
//		{
//			throw new NoSuchElementException();
//		}
//		
//		/**
//		 * Final set to return
//		 */
//		Set<T> scc = new HashSet<T>();
//		
//		/**
//		 * Set of visited Vertices
//		 */
//		Set<T> visited = new HashSet<T>();
//		
//		/**
//		 * Stack of Vertices to visit for depth-first search
//		 */
//		Stack<T> toVisit = new Stack<T>();
//		
//		toVisit.add(key);
//		
//		while (!toVisit.isEmpty())
//		{
//			T vertexToVisit = toVisit.pop();
//			if (shortestPath(vertexToVisit, key) != null)
//			{
//				scc.add(vertexToVisit);
//			}
//			visited.add(vertexToVisit);
//			
//			Iterator<T> successorIterator = successorIterator(vertexToVisit);
//			while (successorIterator.hasNext())
//			{
//				T neighbor = successorIterator.next();
//				if (!visited.contains(neighbor))
//				{
//					toVisit.push(neighbor);
//				}
//			}
//		}
//		
//		
//		// Return the final Set
//		return scc;
//	}	
	
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
		
		/**
		 * Final set to return
		 */
		Set<T> scc = new HashSet<>();
		
		/**
		 * Set of visited Vertices
		 */
		Set<T> visited = new HashSet<>();
		
		/**
		 * Stack of Vertices to visit for depth-first search
		 */
		Stack<T> toVisit = new Stack<>();
		
		/**
		 * Maps successor to its predecessor
		 */
		Map<T, T> childToParent = new HashMap<T, T>();
		
		/**
		 * Maps the end of a cycle to all Vertices before it
		 */
		Map<T, Set<T>> cycleMap = new HashMap<T, Set<T>>();

		// Add the key, obviously, to scc first
		scc.add(key);
		
		// Begin with the key
		toVisit.add(key);
		
		// While we have elements in the toVisit stack...
		while (!toVisit.isEmpty())
		{
			// Pop the stack and add to visited
			T vertexToVisit = toVisit.pop();
			visited.add(vertexToVisit);

			// For each successor of this Vertex...
			Iterator<T> successorIterator = successorIterator(vertexToVisit);
			while (successorIterator.hasNext())
			{
				// If we haven't seen this neighbor before...
				T neighbor = successorIterator.next();
				if (!visited.contains(neighbor))
				{
					// Update mapping and push to the toVisit stack
					childToParent.put(neighbor, vertexToVisit);
					toVisit.push(neighbor);
				}
				
				// Otherwise, we've seen this neighbor before...
				else
				{
					// If we found the root or another node which we know goes to the root...
					if (neighbor.equals(key) || scc.contains(neighbor))
					{
						// Add this Vertex and everything before it to scc
						T curVertex = vertexToVisit;
						while (!curVertex.equals(key))
						{
							if (scc.add(curVertex) && cycleMap.containsKey(curVertex))
							{
								for (T directPredecessor : cycleMap.get(curVertex))
								{
									T curCycleVertex = directPredecessor;
									while (!scc.contains(curCycleVertex))
									{
										scc.add(curCycleVertex);
										curCycleVertex = childToParent.get(curCycleVertex);
									}									
								}
							}
							curVertex = childToParent.get(curVertex);
						}
						cycleMap.clear();
					}
					
					// Otherwise, we don't know if this seen root will be part of scc...
					else
					{
						// So track this detected cycle and move on
						Set<T> newPreds = new HashSet<T>();
						if (cycleMap.get(neighbor) == null)
						{
							newPreds.add(vertexToVisit);
						}
						else
						{
							newPreds = cycleMap.get(neighbor);
							newPreds.add(vertexToVisit);
						}
						cycleMap.put(neighbor, newPreds);
					}

				}
			}
		}

		// Return the final Set
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
}

