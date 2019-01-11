import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements a Directed Graph Data Structure
 *
 * @author Dorcas Ujiji
 * @version Dec 9th, 2016
 */
public class Graph<V, E> extends Object {
	/** The list of nodes */
	private LinkedList<Node> nodeList;
	
	/** The list of edges */
	public LinkedList<Edge> edgeList;

	/** Constructor for empty graph */
	public Graph() {
		this.nodeList = new LinkedList<Node>();
		this.edgeList = new LinkedList<Edge>();
	}

	/** Constructor for graph from another graph */
	public Graph(Graph<V, E> graph) {
		// add all nodes in passed in (param)graph to new graph
		for(Edge e: graph.edgeList){
			edgeList.add(e);
		}
		// add all nodes in passed in (param)graph to new graph
		for(Node n: graph.nodeList){
			nodeList.add(n);
		}
	}

	/** Adds node to a graph*/
	public boolean addNode(Node node) {
		nodeList.add(node);
		return true;
	}
	
	/** Accessor for node in graph
	 * @param number in list of nodes of particular graph
	 */
	public Node getNode(int i) {
		return nodeList.get(i);
	}

	/** Accessor for edge in graph
	 * @param number in list of edges of particular graph
	 */
	public Edge getEdge(int i) {
		return edgeList.get(i);
	}

	/** Acessor for list of nodes */
	public LinkedList<Node> getNodeList() {
		return nodeList;
	}
	
	/** Acessor for list of edges */
	public LinkedList<Edge> getEdgeList() {
		return edgeList;
	}

	/** Adds an edge to the graph
	 * @param head of edge(node), tail of edge(node) and data of edge */
	public Edge addEdge(E data, Node head, Node tail) throws IllegalArgumentException {
		// if head of edge is not in graph
		if (!nodeList.contains(head))
			throw new IllegalArgumentException("Head is not contained in graph");
		// if tail of edge is not in graph
		if (!nodeList.contains(tail))
			throw new IllegalArgumentException("Tail is not contained in graph");
		// creates new edge using parameters
		Edge edge = new Edge(data, head, tail);
//		if (tail.findEdge(head) != null) {
//			return null;
//		} else {
			head.addEdge(edge);
			tail.addEdge(edge);
			edgeList.add(edge);
			return edge;
	}
	
	/** Accessor for number of nodes in graph */
	public int numNodes() {
		return nodeList.size();
	}

	/** Accessor for number of edges in graph */
	public int numEdges() {
		return edgeList.size();
	}

	/** Boolean to tell if Graph is empty */
	public boolean isEmpty() {
		return nodeList.size() == 0;
	}
	
	/** Removes node from graph */
	public void removeNode(Node node) {
		// removes node from graph
		if(nodeList.contains(node)){
			nodeList.remove(node);
			// remove node all its edges
			LinkedList<Edge> nodesEdge = new LinkedList<Edge>();
			nodesEdge.addAll(node.getIncomingEdges());
			nodesEdge.addAll(node.getOutgoingEdges());
			// remove edges to and from node 
			for(Edge e : nodesEdge){
				//removes references to deleted edges from opposite nodes
				e.oppositeTo(node).removeEdgeRef(e);;
				edgeList.remove(e);
			}
			
		}
	}
	
	/** Removes edge from graph*/
	public void removeEdge(Edge edge) {
		//remove it's reference in nodes
		edge.getHead().removeEdgeRef(edge);
		edge.getTail().removeEdgeRef(edge);
		// remove from list of edges
		edgeList.remove(edge);
	}
	
	/** Traverses the graph using BFT */
	public LinkedList<Node> BFT(Node start) {
		Queue<Node> nodeQueue = new LinkedList<Node>();
		LinkedList<Node> visited = new LinkedList<Node>();
		LinkedList<Node> seen = new LinkedList<Node>();
		LinkedList<Node> returnNode = new LinkedList<Node>();
		// add starting point
		nodeQueue.add(start);
		while(!nodeQueue.isEmpty()){
			// take off queue 
			Node polledNode = nodeQueue.poll();
			returnNode.add(polledNode);
			//and mark visited
			visited.add(polledNode);
			// For unseen neighbours
			for(Node neighbour: polledNode.getOutgoingNeighbors()){
				//marks as seen
				seen.add(neighbour);
				//add to queue
				nodeQueue.add(neighbour);
			}
		}
		// return nodes in order
		return returnNode;	
	}


	/*
	 * Implements Graph.Node data structure
	 */
	public class Node {
		/** Data contained in node */
		private V data;
		
		/** List of incoming edges of the node */
		private LinkedList<Edge> incoming;
		
		/** List of outgoing edges of the node */
		private LinkedList<Edge> outgoing;
		
		/** Constructor for node
		 * @param data contained in node */
		public Node(V data) {
			this.data = data;
			// no incoming or outgoing nodes
			incoming = new LinkedList<>();
			outgoing = new LinkedList<>();
		}
		
		/** Constructor for empty node (no data or edges) */
		public Node() {
			this(null);
		}
		
		/** Changes data of node */
		public void setData(V newData) {
			data = newData;
		}
		
		/** Accessor for data of node */
		public V getData() {
			return this.data;
		}
		
		private LinkedList<Edge> getIncomingEdges(){
			LinkedList<Edge> edges = new LinkedList<>();
			return incoming;
		}
		
		public LinkedList<Edge> getOutgoingEdges(){
			LinkedList<Edge> edges = new LinkedList<>();
			return outgoing;
		}
		
		/** Creates a copy of an already existing edge */
		public boolean addEdge(Edge edge) {
			if (edge.getHead() == this) {
				outgoing.add(edge);
			} else if (edge.getTail() == this) {
				incoming.add(edge);
			} else {
				return false;
			}
			return true;
		}
		
		/** Deletes a node from the graph */
		public void removeEdgeRef(Edge edge){
			if(incoming.contains(edge)){
					incoming.remove(edge);
				}
			if(outgoing.contains(edge)){
				outgoing.remove(edge);
			}
		}
		
		
		/** Finds edge that connects two nodes */
		public Edge findEdge(Node dest) {
			for (Edge e : outgoing) {
				if (e.getHead() == dest)
					// returns connecting edge
					return e;
			}
			// returns null if no edge between them
			return null;
		}
		
		/** return edge to another node */
		public Edge edgeTo(Node neighbor){
			for(Edge edge: incoming){
				if(edge.getHead() == neighbor || edge.getTail() == neighbor){
					return edge;
				}
			}
			for(Edge edge: outgoing){
				if(edge.getHead() == neighbor || edge.getTail() == neighbor){
					return edge;
				}
			}
			return null;
		}
		
		/** Returns outgoing neighbors */
		public LinkedList<Node> getOutgoingNeighbors(){
			LinkedList<Node> outNeighbors = new LinkedList<>();
			//test printing
			for(Edge out : outgoing ){
				outNeighbors.add(out.getTail());
			}
			return outNeighbors;
		}
		
		
		/** Returns incoming neighbors */
		public LinkedList<Node> getIncomingNeighbors(){
			LinkedList<Node> outNeighbors = new LinkedList<>();
			for(Edge in:incoming ){
				outNeighbors.add(in.getTail());
			}
			return outNeighbors;
		}
		
		/** Returns true if node is an outgoing neighbor and vice versa */
		
		/** Returns true if node is an incoming neighbor and vice versa */
	
			
	}

	
/***************************************************************************/


	/*
	 * Implements Graph.Edge data structure
	 */
	public class Edge {
		
		/** Head of edge */
		private Node head;
		
		/** Tail of edge */
		private Node tail;
		
		/** Data contained in edge */
		private E data;

		/** Contructor for edge with head, tail and data */
		public Edge(E data, Node head, Node tail ) {
			this.head = head;
			this.tail = tail;
			this.data = data;
		}

		/** Constructor for edge with null data */
		public Edge(Node head, Node tail) {
			this( null, head, tail);
		}

		/** Changes data of edge */
		public void setData(E newData) {
			data = newData;
		}
		
		/** Returns data of edge */
		public E getData() {
			return data;
		}

		/** Returns head of edge */
		public Node getHead() {
			return this.head;
		}

		/** Returns tail of edge */
		public Node getTail() {
			return this.tail;
		}
		
		/** Overrides .equals() */
		public boolean equals(Edge edge) {
			if (head == edge.getHead() && tail == edge.getTail()) {
				return true;
			}
			return false;
		}
		
		/** Redefines hashCode */
		public int hashCode() {
			return head.hashCode() + tail.hashCode();
		}
		
//		/** removes head/tail of an edge */
//		public void removeNodeRef(Node node) {
//			if(this.head == node){
//				this.head = null;
//			}
//			if(this.tail == node){
//				this.tail = null;
//			}
//		}
		
		/** Returns opposite node of parameter node */
		public Node oppositeTo(Node node){
			if(this.head.equals(node)){
				return this.tail;
			}
			if(this.tail.equals(node)){
				return this.head;
			}
			return null;
		}
		//end of edge
	}
}

//////////////////////////*** To do ***//////////////////////////////////
/**
 * check()
 * DFT(Graph.Node start)
 * getEdgeRef(Graph.Node head, Graph.Node tail)
 * print()
 * removeEdge(Graph.Node head, Graph.Node tail)
 * distances(Graph.Node start)
 * endpoints(java.util.HashSet<Graph.Edge> edges)
 * **/

