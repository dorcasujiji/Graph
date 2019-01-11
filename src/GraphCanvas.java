import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.omg.CORBA.PRIVATE_MEMBER;


public class GraphCanvas<V,E> extends JComponent
{
	Graph<V,E> graph;
	HashMap<Graph<V,E>.Node, Point> nodeMap = new HashMap<>();
	HashMap<Point, Graph<V,E>.Node> pointMap = new HashMap<>();
	
	public GraphCanvas(Graph<V, E> graph) {
		this.graph = graph;
	}

    public void paintComponent(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    	g2d.setFont(new Font("Ariel", Font.BOLD, 16));
    	g.setColor(Color.green);
    	for (Graph<V, E>.Edge edge : graph.edgeList)
    	{
    		Graph<V, E>.Node head = edge.getHead();
    		Graph<V, E>.Node tail = edge.getTail();
    		
    		Point p1 = nodeMap.get(head);
    		Point p2 = nodeMap.get(tail);
    		
    		g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    	}
    	
    	for (Map.Entry<Graph<V,E>.Node, Point> entry : nodeMap.entrySet())
    	{
    		Point point = entry.getValue();
    		Rectangle2D rectangle = rectangleForPoint(point);
    		
    		g.setColor(Color.red);
    		g2d.draw(rectangle);
    		
    		g.setColor(Color.black);
    		g.drawString(""+entry.getKey().getData(), point.x-35, point.y+4);
    	}
    }
    
    Graph<V,E>.Edge edgeForPoint(Point point)
    {
    	for (Graph<V,E>.Edge edge : graph.edgeList)
    	{
    		Graph<V,E>.Node start = edge.getHead();
    		Graph<V,E>.Node end = edge.getTail();
    		
    		Point point1 = nodeMap.get(start);
    		Point point2 = nodeMap.get(end);
    		
    		float x0 = point.x, y0 = point.y, x1 = point1.x, x2 = point2.x, y1 = point1.y, y2 = point2.y;
    		
    		double dist = Math.abs((y2 - y1)*x0 - (x2-x1)*y0 + x2*y1-y2*x1)/ Math.hypot(y2-y1, x2-x1);
    		if (dist <= 30.0)
    			return edge;
    	}
    	return null;
    }
    
    Graph<V,E>.Node nodeForPoint(Point point)
    {
    	for (Map.Entry<Graph<V,E>.Node, Point> entry : nodeMap.entrySet())
    	{
    		Point p = entry.getValue();
    		Rectangle2D rectangle2d = rectangleForPoint(p);
    		if (rectangle2d.contains(point.x, point.y))
    		{
    			return entry.getKey();
    		}
    	}
    	return null;
    }
    
    private Rectangle2D rectangleForPoint(Point point)
    {
    	return new Rectangle2D.Float(point.x-40,point.y-25,80,50);
    }
    
    @Override
    public Dimension preferredSize() {
    	return new Dimension(500, 500);
    }
}
