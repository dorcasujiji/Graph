import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * Implements a Directed Graph Data GUI
 *
 * @author Dorcas Ujiji
 * @version Dec 15th, 2016
 */
public class GraphGUI extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
	Graph<String, Integer> myGraph = new Graph<>();
	GraphCanvas<String, Integer> canvas = new GraphCanvas<>(myGraph);
	ButtonGroup buttonGroup = new ButtonGroup();
	JToggleButton addEdge = new JToggleButton("Add edge");
	JToggleButton addNode = new JToggleButton("Add node");
	JToggleButton deleteEdge = new JToggleButton("Delete edge");
	JToggleButton deleteNode = new JToggleButton("Delete node");
	JToggleButton editNode = new JToggleButton("Edit node");

	Graph<String, Integer>.Node draggedNode = null;
	Point startPoint = new Point(-10, -10), endPoint = new Point(-10, -10);

	boolean didDrag = false;

	public GraphGUI() {
		super("Graph GUI");

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

		canvas.nodeMap.put(node1, new Point(100, 100));
		canvas.nodeMap.put(node2, new Point(300, 100));
		canvas.nodeMap.put(node3, new Point(100, 300));
		canvas.nodeMap.put(node4, new Point(300, 300));
	}

	InputMode mode = InputMode.ADD_NODE;

	static final GraphGUI GUI = new GraphGUI();;

	/**
	 * Schedules a job for the event-dispatching thread creating and showing
	 * this application's GUI.
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI.createAndShowGUI();
			}
		});
	}

	/** Sets up the GUI window */
	public void createAndShowGUI() {
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add components
		createComponents();

		// Display the window.
		pack();
		setVisible(true);
	}

	/** Puts content in the GUI window */
	public void createComponents() {
		// graph display
		Container pane = getContentPane();
		pane.setLayout(new FlowLayout());
		pane.addMouseListener(this);
		pane.addMouseMotionListener(this);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		// PointMouseListener pml = new PointMouseListener();
		// canvas.addMouseListener(pml);
		// canvas.addMouseMotionListener(pml);
		panel1.add(canvas);
		JLabel instr = new JLabel("Click to add new points; drag to move.");
		panel1.add(instr, BorderLayout.NORTH);
		pane.add(panel1);
		addNode.setSelected(true);

		addEdge.setActionCommand("addEdge");
		addNode.setActionCommand("addNode");
		deleteEdge.setActionCommand("deleteEdge");
		deleteNode.setActionCommand("deleteNode");
		editNode.setActionCommand("editNode");
		addEdge.addActionListener(this);
		addNode.addActionListener(this);
		deleteEdge.addActionListener(this);
		deleteNode.addActionListener(this);
		editNode.addActionListener(this);

		buttonGroup.add(addEdge);
		buttonGroup.add(addNode);
		buttonGroup.add(deleteEdge);
		buttonGroup.add(deleteNode);
		buttonGroup.add(editNode);

		// controls
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(2, 1));
		panel2.add(addEdge);
		panel2.add(addNode);
		panel2.add(deleteEdge);
		panel2.add(deleteNode);
		panel2.add(editNode);
		pane.add(panel2);
	}

	private void addNode(String data, Point point) {
		Graph<String, Integer>.Node node = myGraph.new Node(data);
		myGraph.addNode(node);
		canvas.nodeMap.put(node, point);
		canvas.pointMap.put(point, node);
		GUI.repaint();
	}

	private void addEdge() {
		Graph<String, Integer>.Node startNode = canvas.nodeForPoint(startPoint);
		Graph<String, Integer>.Node endNode = canvas.nodeForPoint(endPoint);
		if (startNode != null && endNode != null && startNode != endNode)
			myGraph.addEdge(null, startNode, endNode);
		startPoint = new Point(-10, -10);
		endPoint = new Point(-10, -10);
		GUI.repaint();
	}

	private void deleteNode(Point p) {
		Graph<String, Integer>.Node node = canvas.nodeForPoint(p);
		if (node != null) {
			myGraph.removeNode(node);
			Point point = canvas.nodeMap.get(node);
			canvas.nodeMap.remove(node);
			canvas.pointMap.remove(point);
			GUI.repaint();
		}
	}

	private void editNode(Point p) {
		Graph<String, Integer>.Node node = canvas.nodeForPoint(p);
		if (node != null) {
			node.setData(promptForData());
			GUI.repaint();
		}
	}

	private String promptForData() {
		return JOptionPane.showInputDialog("Please input a value for the node");
	}

	private void deleteEdge(Point point) {
		Graph<String, Integer>.Edge edge = canvas.edgeForPoint(point);
		if (edge != null) {
			myGraph.removeEdge(edge);
			GUI.repaint();
		}
	}

	private void dragNode(Point p) {
		if (draggedNode == null)
			return;
		didDrag = true;

		Point point = canvas.nodeMap.get(draggedNode);
		canvas.nodeMap.put(draggedNode, p);
		canvas.pointMap.remove(point);
		canvas.pointMap.put(p, draggedNode);
		GUI.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addNode")) {
			mode = InputMode.ADD_NODE;
		} else if (e.getActionCommand().equals("addEdge")) {
			mode = InputMode.ADD_EDGE;
		} else if (e.getActionCommand().equals("deleteNode")) {
			mode = InputMode.DELETE_NODE;
		} else if (e.getActionCommand().equals("deleteEdge")) {
			mode = InputMode.DELETE_EDGE;
		} else if (e.getActionCommand().equals("editNode")) {
			mode = InputMode.EDIT_NODE;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (didDrag) {
			didDrag = false;
			return;
		}
		switch (mode) {
		case ADD_EDGE:
			break;
		case ADD_NODE:
			addNode(promptForData(), new Point(e.getX(), e.getY()));
			break;
		case DELETE_EDGE:
			deleteEdge(new Point(e.getX(), e.getY()));
			break;
		case DELETE_NODE:
			deleteNode(new Point(e.getX(), e.getY()));
			break;
		case EDIT_NODE:
			editNode(new Point(e.getX(), e.getY()));
			break;
		default:
			break;

		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (mode) {
		case ADD_EDGE:
			startPoint = new Point(e.getX(), e.getY());
			break;
		case EDIT_NODE:
		case ADD_NODE:
			draggedNode = canvas.nodeForPoint(new Point(e.getX(), e.getY()));
			break;
		default:
			break;

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (mode) {
		case ADD_EDGE:
			endPoint = new Point(e.getX(), e.getY());
			addEdge();
			break;
		case EDIT_NODE:
		case ADD_NODE:
			didDrag = false;
			draggedNode = null;
			break;
		default:
			break;

		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch (mode) {
		case ADD_EDGE:
			endPoint = new Point(e.getX(), e.getY());
			repaint();
			break;
		case EDIT_NODE:
		case ADD_NODE:
			dragNode(new Point(e.getX(), e.getY()));
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		getContentPane().getGraphics().drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

	enum InputMode {
		ADD_EDGE, ADD_NODE, DELETE_EDGE, DELETE_NODE, EDIT_NODE
	}
}
