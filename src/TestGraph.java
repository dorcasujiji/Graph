import java.util.LinkedList;

/*
 * A test class for Graph.java
 * @author Dorcas Ujiji
 * @version Dec 7th, 2016
 * **/

public class TestGraph {
	public static void main(String[] args) {
		Graph<String, Integer> myGraph = new Graph<>();
		Graph<String, Integer>.Node node1 = myGraph.new Node("Mammal");
		Graph<String, Integer>.Node node2 = myGraph.new Node("Cow");
		Graph<String, Integer>.Node node3 = myGraph.new Node("Calf");
		Graph<String, Integer>.Node node4 = myGraph.new Node("human");

		myGraph.addNode(node1);
		myGraph.addNode(node2);
		myGraph.addNode(node3);
		myGraph.addNode(node4);

		Graph<String, Integer>.Edge edge1 = myGraph.addEdge(10, node1, node2);
		Graph<String, Integer>.Edge edge2 = myGraph.addEdge(5, node2, node3);
		Graph<String, Integer>.Edge edge3 = myGraph.addEdge(10, node1, node4);
		
		// print out edges
		System.out.println("Edges are :");
		for (Graph<String, Integer>.Edge edge : myGraph.getEdgeList()) {
			System.out.println(edge.getData());
		}
		System.out.println("Nodes are :");
		// print out nodes
		for (Graph<String, Integer>.Node node : myGraph.getNodeList()) {
			System.out.println(node.getData());
		}
		System.out.println();
		System.out.println("Printed Graph using BFT ");
		LinkedList<Graph<String, Integer>.Node> nodeList = myGraph.BFT(node1);
		for(Graph<String, Integer>.Node node: nodeList){
			System.out.println(node.getData());
		}
		
//
//		// delete one node
//		System.out.println("node3 deleted ");
//		myGraph.removeNode(node3);
//		// print out edges
//		System.out.println("New edges are :");
//		for (Graph<String, Integer>.Edge edge : myGraph.getEdgeList()) {
//			System.out.println(edge.getData());
//		}
//		System.out.println("New nodes are :");
//		// print out nodes
//		for (Graph<String, Integer>.Node node : myGraph.getNodeList()) {
//			System.out.println(node.getData());
//		}

	}
}
