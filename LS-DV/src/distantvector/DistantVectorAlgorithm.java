package distantvector;
import java.util.HashMap;
import java.util.Map;

public class DistantVectorAlgorithm {
	public DistantVectorAlgorithm(int i) {
	id = i;
	}

	void intitialize(int n) {
		dv = new double[n+1];
		next = new int[n+1];
		for (int i = 1; i <= n; i++) {
			dv[i] = Integer.MAX_VALUE;
			next[i] = -1;
		}
		next[id] = id;
		dv[id] = 0;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the dv
	 */
	public double[] getDv() {
		return dv;
	}

	/**
	 * @param dv the dv to set
	 */
	public void setDv(double[] dv) {
		this.dv = dv;
	}

	/**
	 * @return the next
	 */
	public int[] getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(int[] next) {
		this.next = next;
	}

	/**
	 * @return the neighbour
	 */
	public Map<DistantVectorAlgorithm, Double> getNeighbour() {
		return neighbour;
	}

	/**
	 * @param neighbour the neighbour to set
	 */
	public void setNeighbour(Map<DistantVectorAlgorithm, Double> neighbour) {
		this.neighbour = neighbour;
	}
	
	public void setNeighbourNode(DistantVectorAlgorithm node, double wgt) {
		neighbour.put(node,wgt);
//		next[node.getId()] = node.getId();
//		dv[node.getId()] = wgt;
	}
	
	public void updateDV(DistantVectorAlgorithm node, double wgt) {
		next[node.getId()] = node.getId();
		dv[node.getId()] = wgt;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistantVectorAlgorithm other = (DistantVectorAlgorithm) obj;
		if (id != other.id)
			return false;
		return true;
	}


	int id;
	double[] dv;
	int[] next;
	Map<DistantVectorAlgorithm,Double> neighbour = new HashMap<DistantVectorAlgorithm,Double>();
	
}
