package linkstate;

public class Vertex {
	  final private int id;
	 // final private String name;
	  
	  
	  public Vertex(int id) {
	    this.id = id;
	   // this.name = name;
	  }
	  public int getId() {
	    return id;
	  }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id != other.id)
			return false;
		return true;
	}
	  
	  
/*
	  public String getName() {
	   // return name;
	  }
*/	 
	  
//	  @Override
//	  public int hashCode() {
//	    final int prime = 31;
//	    int result = 1;
//	    result = prime * result + ((id == null) ? 0 : id.hashCode());
//	    return result;
//	  }
//	  
//	  @Override
//	  public boolean equals(Object obj) {
//	    if (this == obj)
//	      return true;
//	    if (obj == null)
//	      return false;
//	    if (getClass() != obj.getClass())
//	      return false;
//	    Vertex other = (Vertex) obj;
//	    if (id == null) {
//	      if (other.id != null)
//	        return false;
//	    } else if (!id.equals(other.id))
//	      return false;
//	    return true;
//	  }

//	  @Override
//	  public String toString() {
//	    return name;
//	  }
//	  
	 
	  
}