package linkstate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulateLinkStateAlgorithm {
 
  BufferedReader br;
  private List<Vertex> nodes=new ArrayList<Vertex>(); 
  private List<Edge> edges=new ArrayList<Edge>();
  HashMap<Integer, Vertex> vertextMap=new HashMap<Integer,Vertex>();
  String filename;  
  int noOfNodes=-1;
  
  /**
	 * @return the nodes
	 */
	public List<Vertex> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<Vertex> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * @param edges
	 *            the edges to set
	 */
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	private int initialize() {
		try {
			File file= new File(filename);
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				{
					if (noOfNodes == -1) {
						noOfNodes = Integer.parseInt(line); // take first line from the file
						// initializing nodes
						for (int i = 1; i <= noOfNodes; i++) {
							nodes.add(new Vertex(i));
							vertextMap.put(i, new Vertex(i));
						}
					}
					else {
						String[] parts = line.split(" ");
						double wgt = Double.parseDouble(parts[2]); // Weight
						int src = Integer.parseInt(parts[0]); // source
						int dstn = Integer.parseInt(parts[1]); // destination
						Edge edge = new Edge(vertextMap.get(src),vertextMap.get(dstn), wgt);
						edges.add(edge);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error in processing file");
		}
     return noOfNodes;
	}
 
    public static void main(String args[])
    { 
    	String filename = args[0];
		int node1= Integer.parseInt(args[1]);
		int node2= Integer.parseInt(args[2]);
        SimulateLinkStateAlgorithm test=new SimulateLinkStateAlgorithm();
        test.setFilename(filename);
    	int noOfNode = test.initialize();
        
    	Graph graph = new Graph(test.getNodes(), test.getEdges());
        for (int i = 1; i <= noOfNode; i++) {
        	LinkStateAlgorithm dijkstra = new LinkStateAlgorithm(graph);
            dijkstra.execute(test.getVertextMap().get(i));
        	if(node1==i) {
            dijkstra.printRoutingTable( test.getVertextMap().get(node1), test.getVertextMap().get(node2));	
        	}
        	if(node2==i) {
                dijkstra.printRoutingTable( test.getVertextMap().get(node2), test.getVertextMap().get(node1));	
            }
        }
        
    
}

	/**
	 * @return the vertextMap
	 */
	public HashMap<Integer, Vertex> getVertextMap() {
		return vertextMap;
	}

	/**
	 * @param vertextMap the vertextMap to set
	 */
	public void setVertextMap(HashMap<Integer, Vertex> vertextMap) {
		this.vertextMap = vertextMap;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
    
}
