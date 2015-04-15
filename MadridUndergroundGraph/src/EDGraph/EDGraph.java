package EDGraph;

import java.util.ArrayList;
import java.util.List;

public interface EDGraph<E,W> {

	//returns number of nodes of the graph
	int getSize();
	
	//returns the size of the array that stores the nodes
	int arraySize();
	
	//true if the graph has no nodes; false in other case
	boolean isEmpty();
	
	//inserts a new node with the label item
	int insertNode(E item);
	
	//inserts a new edge
	boolean insertEdge(EDEdge<W> edge);
	
	//given the label of a node, returns the index of the position
	//in the array where is stored
	int getNodeIndex(E item);
	
	//given the position of a node, returns the node element
	//returns null if an empty or wrong position
	E getNodeValue(int index);
	
	//given the indices of two nodes, returns the Edge that joins the
	//two nodes, if any. If there is no edge, then returns null
	//The direction source --> dest is important if the graph is directed
	EDEdge<W> getEdge(int source, int dest, W weight);
	
	//removes a node
	E removeNode(int index);
	
	//removes an edge
	EDEdge<W> removeEdge(int source, int dest, W weight);

	//prints the structure
	void printGraphStructure();
	
}
