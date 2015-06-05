package linkstate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.List;
import java.util.Map;
import java.util.Set;

//import Edge;
//import Graph;
//import Vertex;

public class LinkStateAlgorithm {

  private final List<Vertex> nodes;
  private final List<Edge> edges;
  private Set<Vertex> settledNodes;
  private Set<Vertex> unSettledNodes;
  private Map<Vertex, Vertex> predecessors;
  private Map<Vertex, Double> distance;

  public LinkStateAlgorithm(Graph graph) {
    // create a copy of the array so that we can operate on this array
    this.nodes = new ArrayList<Vertex>(graph.getVertexes());
    this.edges = new ArrayList<Edge>(graph.getEdges());
  }

  public void execute(Vertex source) {
    settledNodes = new HashSet<Vertex>();
    unSettledNodes = new HashSet<Vertex>();
    distance = new HashMap<Vertex, Double>();
   predecessors = new HashMap<Vertex, Vertex>();
    distance.put(source, 0.0);
    unSettledNodes.add(source);
    while (unSettledNodes.size() > 0) {
      Vertex node = getMinimum(unSettledNodes);
      settledNodes.add(node);
      unSettledNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  private void findMinimalDistances(Vertex node) {
    List<Vertex> adjacentNodes = getNeighbors(node);
    for (Vertex target : adjacentNodes) {
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) {
        distance.put(target, getShortestDistance(node)
            + getDistance(node, target));
        predecessors.put(target, node);
        unSettledNodes.add(target);
      }
    }

  }

  private double getDistance(Vertex node, Vertex target) {
    for (Edge edge : edges) {
      if ((edge.getSource().equals(node)
          && edge.getDestination().equals(target))|| edge.getSource().equals(target)
          && edge.getDestination().equals(node)){
        return edge.getWeight();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private List<Vertex> getNeighbors(Vertex node) {
    List<Vertex> neighbors = new ArrayList<Vertex>();
    for (Edge edge : edges) {
      if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) 
        neighbors.add(edge.getDestination());
        else if(edge.getDestination().equals(node) && !isSettled(edge.getSource()))
        neighbors.add(edge.getSource());
      }
    
    return neighbors;
  }

  private Vertex getMinimum(Set<Vertex> vertexes) {
    Vertex minimum = null;
    for (Vertex vertex : vertexes) {
      if (minimum == null) {
        minimum = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
          minimum = vertex;
        }
      }
    }
    return minimum;
  }

  private boolean isSettled(Vertex vertex) {
    return settledNodes.contains(vertex);
  }

  private double getShortestDistance(Vertex destination) {
    Double d = distance.get(destination);
    if (d == null) {
      return Double.MAX_VALUE;
    } else {
      return d;
    }
  }
  

  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  private LinkedList<Vertex> getPath(Vertex target) {
    LinkedList<Vertex> path = new LinkedList<Vertex>();
    Vertex step = target;
    // check if a path exists
    if (predecessors.get(step) == null) {
      return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    return path;
  }
 
  public Vertex getNext(Vertex source,Vertex target) {
	    Vertex step = target;
	    Vertex prev = null;
	    
	    // check if a path exists
	    if (predecessors.get(step) == null) {
	      return null;
	    }
	    while (predecessors.get(step) != null) {
	    	prev = step;
	    	step = predecessors.get(step);
	    }
	    return prev;  
  }
	 
  public void printRoutingTable(Vertex source, Vertex target) {
	  System.out.println("Distance from " +source.getId() +" to " + target.getId() +" is:"+ distance.get(target));
	  String path="";
	  for (Iterator<Vertex> iterator = getPath(target).iterator(); iterator.hasNext();) {
		Vertex type = (Vertex) iterator.next();
		if(type.equals(target)) {
		path = path + type.getId();
		} else {
		path = path + type.getId() + "--->";
		}
	}
	    System.out.println("Path taken is: "+path);
	    System.out.println("");
		System.out.println("Routing Table at " + source.getId());
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Destination  NextHop Cost");
		
		for (Iterator<Vertex> iterator = nodes.iterator(); iterator.hasNext();) {
			Vertex type = (Vertex) iterator.next();
			if(getNext(source,type) != null){
			int next = getNext(source,type).getId();
			if (type.getId() >9 && next>9) {
				System.out.println(type.getId() + "            " + next+ "     " + distance.get(type));
			} else if(type.getId()> 9) { 
				System.out.println(type.getId() + "            " + next+ "      " + distance.get(type));
			} else if(next > 9){
				System.out.println(type.getId() + "             " + next+ "     " + distance.get(type));
			}else {
				System.out.println(type.getId() + "             " + next+ "      " + distance.get(type));
			}
		}
		}
		System.out.println("-----------------------------------------------------------------------");
  }
} 