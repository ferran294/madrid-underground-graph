package EDGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


/** Implementation of interface Graph using adjacency lists
 * @param <T> The base type of the nodes
 * @param <W> The base type of the weights of the edges
 */
public class EDListGraph<T,W> implements EDGraph<T,W> {
	@SuppressWarnings("hiding")
	private class Node<T> {
		T data;
		List< EDEdge<W> > lEdges;
		
		Node (T data) {
			this.data = data;
			this.lEdges = new LinkedList< EDEdge<W> >();
		}
		public boolean equals (Object other) {
			if (this == other) return true;
			if (!(other instanceof Node)) return false;
			Node<T> anotherNode = (Node<T>) other;
			return data.equals(anotherNode.data);
		}
	}
	
	// Private data
	private ArrayList<Node<T>> nodes;
	private int size; //real number of nodes
	private boolean directed;
	
	/** Constructor
	 * @param direct <code>true</code> for directed edges; 
	 * <code>false</code> for non directed edges.
	 */

	public EDListGraph() {
		directed = false; //not directed
		nodes =  new ArrayList<Node<T>>();
		size =0;
	}
	
	public EDListGraph (boolean dir) {
		directed = dir;
		nodes =  new ArrayList<Node<T>>();
		size =0;
	}
	
	//number of nodes of the graph
	public int getSize() {
		return size;
	}

	//array size for storing nodes
	public int arraySize() {
		return nodes.size();
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public int insertNode(T item) {
			
	    int i = 0;
	    while (i<nodes.size() && nodes.get(i).data != null) i++;
				
	    Node<T> newNode = new Node<T>(item);
	    if (i<nodes.size()) nodes.set(i,newNode);
	    else nodes.add(newNode);
	    size++;
	    return i;
	}
	
	@Override
	public int getNodeIndex(T item) {
		Node<T> aux = new Node<T>(item);
		return nodes.indexOf(aux);
	}

	@Override
	public T getNodeValue(int index) throws IndexOutOfBoundsException{
		
		return nodes.get(index).data;
		
	}
	
	@Override
	public boolean insertEdge(EDEdge<W> edge) {
		int sourceIndex = edge.getSource();
		int targetIndex = edge.getTarget();
		if (sourceIndex >=0 && sourceIndex<nodes.size() && targetIndex >=0 && targetIndex<nodes.size()) {
			Node<T> nodeSr = nodes.get(sourceIndex);
			Node<T> nodeTa = nodes.get(targetIndex);
			if (nodeSr.data!=null && nodeTa.data != null) {
			   if (!nodeSr.lEdges.contains(edge)) {
				   nodeSr.lEdges.add(edge);
				   nodes.set(sourceIndex,nodeSr); 
				   if (!directed) {//no dirigido
					  EDEdge<W> reverse = new EDEdge<W>(targetIndex,sourceIndex,edge.getWeight());
					  nodeTa.lEdges.add(reverse);
					  nodes.set(targetIndex, nodeTa);
				   }
				   return true;
			    }
			   else System.out.println("The graph has already this edge: "+edge.toString());
			}
		}
		return false;
	}
	
	@Override
	public EDEdge<W> getEdge(int source, int dest, W weight) {	
		if (source <0 || source >= nodes.size()) return null;
		
		Node<T> node = nodes.get(source);
		if (node.data == null ) return null;
		for (EDEdge<W> edge: node.lEdges)
			if (weight != null) {
				if (edge.getTarget() == dest && edge.getWeight().equals(weight)) return edge;
			}
			else 
				if (edge.getTarget() == dest) return edge;
		
		return null;
	}
	
	@Override
	public EDEdge<W> removeEdge(int source, int target, W weight) {
		if (source <0 || source >= nodes.size() || target<0 || target >= nodes.size()) return null;
		if (nodes.get(source).data!=null && nodes.get(target).data!=null) {
			EDEdge<W> edge = new EDEdge<W>(source, target, weight);
			Node<T> node = nodes.get(source);
			int i = node.lEdges.indexOf(edge);
			if (i != -1) {
				edge = node.lEdges.remove(i);
				if (!directed) {
					EDEdge<W> reverse = new EDEdge<W>(target,source,weight);
					nodes.get(target).lEdges.remove(reverse);
				}
				return edge;
			}	
		}
		return null;	
	}

	@Override
	public T removeNode(int index) {
		if (index >=0 && index < nodes.size()){
			if (!directed) {
				Node<T> node = nodes.get(index);
				for (EDEdge<W> edge: node.lEdges ) {
					int target = edge.getTarget();
					W label = edge.getWeight();
					EDEdge<W> other = new EDEdge<W>(target,index,label);
					nodes.get(target).lEdges.remove(other);
				}
			}
			else { //directed
				for (int i=0; i<nodes.size(); i++) {
					if (i!=index && nodes.get(i).data !=null) {
						Node<T> node = nodes.get(i);
						for (EDEdge<W> edge: node.lEdges) {
							if (index == edge.getTarget()) //any weight/label
								node.lEdges.remove(edge);
						}
					}
				}
			}
			
			Node<T> node = nodes.get(index);
			node.lEdges.clear();
			T ret = node.data;
			node.data = null; //It is not remove, data is set to null
			nodes.set(index, node);
			size--;
			System.out.println("Borrada posicion: "+index);
			return ret;
		}
		return null;
	}
	
	public void printGraphStructure() {
		System.out.println("Vector size " + nodes.size());
		System.out.println("Nodes: "+ this.getSize());
		for (int i=0; i<nodes.size(); i++) {
			System.out.print("pos "+i+": ");
	        Node<T> node = nodes.get(i);
			System.out.print(node.data+" -- ");
			Iterator<EDEdge<W>> it = node.lEdges.listIterator();
			while (it.hasNext()) {
					EDEdge<W> e = it.next();
					System.out.print("("+e.getSource()+","+e.getTarget()+", "+e.getWeight()+")->" );
			}
			System.out.println();
		}
	}
	
	public List<EDEdge<W>> getIncidentArcs(int index){
		if(index<0 || index>=this.size){
			throw new IndexOutOfBoundsException();
		}
		return nodes.get(index).lEdges;
	}
	
	
	//Implementar en el ejercicio 3
	//public List<EDEdge<W>> getIncidentArcs2(int index) throws IndexOutOfBoundsException {
		
		//List<EDEdge<W>> listaArcosIncidentes=new LinkedList<EDEdge<W>>();
		//for(int i=0;i<this.size;i++){
			//List<EDEdge<W>> arcosNodo=getAdyacentArcs(i);
			//for(EDEdge<W> arco:arcosNodo){
				//if(arco.getTarget()==index){
					//listaArcosIncidentes.add(arco);
				//}
			//}
		//}
		
		//return listaArcosIncidentes;
		
	//}
}
