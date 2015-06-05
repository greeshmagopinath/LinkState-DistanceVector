package distantvector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulateDistantVectorAlgorithm {

	List<DistantVectorAlgorithm> updatedNodes = new ArrayList<DistantVectorAlgorithm>();
	DistantVectorAlgorithm initialNode;
	Map<Integer, DistantVectorAlgorithm> nodes = new HashMap<Integer, DistantVectorAlgorithm>();
	int noOfNodes = -1;
	String filename;
	int Source;
	int Destination;
	
	public SimulateDistantVectorAlgorithm(int intial, String filename, int sourceNode, int destNode) {
		this.filename = filename;
		Source = sourceNode;
		Destination = destNode;
		initialize();
		initialNode = nodes.get(intial);
		updateDVForInitialNode(initialNode);
	}

	private void updateDVForInitialNode(DistantVectorAlgorithm initialNode2) {
		Map<DistantVectorAlgorithm,Double> neighbours = initialNode.getNeighbour();
			for (Map.Entry<DistantVectorAlgorithm, Double> entry : neighbours.entrySet()) {
			    DistantVectorAlgorithm key = entry.getKey();
			    Double value = entry.getValue();
			    initialNode.updateDV(key, value);
			}
			
	}

	public void initialize() {
		BufferedReader br;
		File file = new File(filename);
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				{
					if (noOfNodes == -1) {
						noOfNodes = Integer.parseInt(line); // take first line
						// initializing nodes
						for (int i = 1; i <= noOfNodes; i++) {
							DistantVectorAlgorithm node = new DistantVectorAlgorithm(i);
							node.intitialize(noOfNodes);
							nodes.put(i, node);
						}
					} else {
						String[] parts = line.split(" ");
						double wgt = Double.parseDouble(parts[2]); // Weight
						int src = Integer.parseInt(parts[0]); // source
						int dstn = Integer.parseInt(parts[1]); // destination
						DistantVectorAlgorithm srcNode = nodes.get(src);
						DistantVectorAlgorithm destNode = nodes.get(dstn);
						updateNode(srcNode, destNode, wgt);
					}
				}

			}
		} catch (IOException e) {
			System.out.println("Error in processing file");
		}

	}

	private void updateNode(DistantVectorAlgorithm srcNode, DistantVectorAlgorithm destNode, double wgt) {
		srcNode.setNeighbourNode(destNode, wgt);
		destNode.setNeighbourNode(srcNode, wgt);
	}

	private void startOperation() {
		updatedNodes.add(initialNode);
		informNeighbours(initialNode);
	}

	private void informNeighbours(DistantVectorAlgorithm initialNode) {

		while (!updatedNodes.isEmpty()) {
			DistantVectorAlgorithm workingnode = updatedNodes.get(0);
			Map<DistantVectorAlgorithm,Double> NeigbhoursToInfom = workingnode.getNeighbour();
			for (DistantVectorAlgorithm entry : NeigbhoursToInfom.keySet()) {
				updateDistanceVector(entry,workingnode);
			}
			updatedNodes.remove(workingnode);
		}
		DistantVectorAlgorithm source = nodes.get(Source);
		DistantVectorAlgorithm Dest = nodes.get(Destination);
		System.out.println("Cost from "+Source +" to " + Destination + ":" + source.getDv()[Destination]);
		printRoutingTable(source);
		printRoutingTable(Dest);
		
	}

	private void printRoutingTable(DistantVectorAlgorithm node) {
		System.out.println("");
		System.out.println("Routing Table at " + node.getId());
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Destination  NextHop Cost");
		for (int i = 1; i <= noOfNodes; i++) {
			if(node.getId() != i){
			if (i > 9 && node.getNext()[i]>9) {
				System.out.println(i + "            " + node.getNext()[i]+ "     " + node.getDv()[i]);
			} else if(i>9) { 
				System.out.println(i + "            " + node.getNext()[i]+ "      " + node.getDv()[i]);
			} else if(node.getNext()[i]>9){
				System.out.println(i + "             " + node.getNext()[i]+ "     " + node.getDv()[i]);
			}else {
				System.out.println(i + "             " + node.getNext()[i]+ "      " + node.getDv()[i]);
			}
		}
	}
		System.out.println("-----------------------------------------------------------------------");
	}

	private void updateDistanceVector(DistantVectorAlgorithm node, DistantVectorAlgorithm originatingNode) {
		for (int i = 1; i <= noOfNodes; i++) {
		if(node.getDv()[i] > (node.getNeighbour().get(originatingNode) + originatingNode.getDv()[i])) {
			node.getDv()[i] = node.getNeighbour().get(originatingNode) + originatingNode.getDv()[i];
			node.getNext()[i] = originatingNode.getId();
			if (updatedNodes.indexOf(node) == -1) {
			     updatedNodes.add(node);
			 }
		}
		}
	}

	public static void main(String[] args) {
		int intialNode = Integer.parseInt(args[0]);
		String fileName = args[1];
		int sourceNode = Integer.parseInt(args[2]);
		int destNode = Integer.parseInt(args[3]);
		SimulateDistantVectorAlgorithm sim = new SimulateDistantVectorAlgorithm(intialNode, fileName,sourceNode,destNode);
		sim.startOperation();
	}

}
