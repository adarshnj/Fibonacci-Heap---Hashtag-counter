
public final class Node<T> {
	int nDegree = 0;
	boolean nChildCut = false;
	Integer nData;

	Node<T> nNext;
	Node<T> nPrev;

	Node<T> nParent;
	Node<T> nChild;

	Node(Integer data) {
		nNext = nPrev = this;
		nData = data;
	}

	// Getter and Setter functions
	public Integer getValue() {
		return nData;
	}

	public void setValue(Integer nData) {
		this.nData = nData;
	}

	public int getmDegree() {
		return nDegree;
	}

	public void setmDegree(int mDegree) {
		this.nDegree = mDegree;
	}

}