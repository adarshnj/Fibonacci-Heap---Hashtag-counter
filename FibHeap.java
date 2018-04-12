import java.util.*;

//Max priority Fibonacci Heap
public final class FibHeap<T> {

	private Node<T> nMax = null;
	private int hSize = 0;

	// Add data into the fibonacci Heap - O(1) Complexity
	public Node<T> enqueue(Integer value) {
		Node<T> node = new Node<T>(value);

		// Merge the nodes into a list and get the max of the two nodes
		nMax = merge(nMax, node);

		// Increment size of the heap
		++hSize;

		// Return address of the newly created node.
		return node;
	}

	// Check if the heap is empty - O(1)
	public boolean isEmpty() {
		return nMax == null;
	}

	// Return the root node with max value - O(1)
	Node<T> max() {
		if (isEmpty())
			throw new NoSuchElementException("Heap is Empty");
		return nMax;
	}

	// Merge two given subtrees into a single list
	private static <T> Node<T> merge(Node<T> first, Node<T> second) {

		if (first == null && second == null) {
			return null;
		} else if (first != null && second == null) {
			return first;
		} else if (first == null && second != null) {
			return second;
		} else {
			Node<T> oneNext = first.nNext;
			first.nNext = second.nNext;
			first.nNext.nPrev = first;
			second.nNext = oneNext;
			second.nNext.nPrev = second;

			return first.nData > second.nData ? first : second;
		}
	}

	// Return size of the Max Fibonacci Heap
	public int Size() {
		return hSize;
	}

	// Update existing value : increment frequency - O(1)
	public void increaseKey(Node<T> entry, Integer newData) {
		if (newData < 0)
			throw new IllegalArgumentException("Negative frequency");

		// Set new frequency(Value)
		entry.nData = newData;

		// Check if the value at parent node is gerater than the updated value
		// if true cut the parent from the tree and add to root list
		if (entry.nParent != null && entry.nData >= entry.nParent.nData)
			cut(entry);

		if (entry.nData >= nMax.nData)
			nMax = entry;
	}

	// Remove the node's parent from the tree and add to rootlist
	private void cut(Node<T> entry) {

		// set childcut to false
		entry.nChildCut = false;

		if (entry.nParent == null)
			return;

		// while the node is not the only child
		if (entry.nNext != entry) {
			entry.nNext.nPrev = entry.nPrev;
			entry.nPrev.nNext = entry.nNext;
		}

		if (entry.nParent.nChild == entry) {
			if (entry.nNext != entry) {
				entry.nParent.nChild = entry.nNext;
			} else {
				entry.nParent.nChild = null;
			}
		}

		entry.nParent.setmDegree(entry.nParent.getmDegree() - 1);

		entry.nPrev = entry.nNext = entry;
		nMax = merge(nMax, entry);

		if (entry.nParent.nChildCut)
			cut(entry.nParent);
		else
			entry.nParent.nChildCut = true;

		entry.nParent = null;

	}

	// Remove max element node(root) and append children to the root list and
	// pairwise combine - O(log n)
	public Node<T> removeMax() {

		if (isEmpty())
			throw new NoSuchElementException("Heap is empty.");

		--hSize;

		Node<T> maxElem = nMax;

		// max is not the only node in the tree
		if (nMax.nNext != nMax) {
			nMax.nPrev.nNext = nMax.nNext;
			nMax.nNext.nPrev = nMax.nPrev;
			nMax = nMax.nNext;
		}
		// empty the tree if max is the only node
		else
			nMax = null;

		// if max node has children
		if (maxElem.nChild != null) {
			// save max node's child for iteration
			Node<T> maxChild = maxElem.nChild;
			do {
				// set the nParent field of all children of max to null
				maxChild.nParent = null;
				// Move to next sibling
				maxChild = maxChild.nNext;

			} while (maxChild != maxElem.nChild);
		}

		// merge all children of max node(to be deleted) to root list
		nMax = merge(maxElem.nChild, nMax);

		if (nMax == null)
			return maxElem;

		// lists to make a pairwise combine of nodes in root list
		List<Node<T>> pairCombine = new ArrayList<Node<T>>();
		List<Node<T>> rootList = new ArrayList<Node<T>>();

		// parse all nodes in the root list and add to the list 'rootList'
		for (Node<T> curr = nMax; rootList.isEmpty() || rootList.get(0) != curr; curr = curr.nNext)
			rootList.add(curr);

		// for each node in the rootList
		for (Node<T> curr : rootList) {

			while (true) {
				while (curr.getmDegree() >= pairCombine.size())
					pairCombine.add(null);

				// add the node with particular degree into another list for
				// pairwise combine
				if (pairCombine.get(curr.getmDegree()) == null) {
					pairCombine.set(curr.getmDegree(), curr);
					break;
				}

				// if a tree of same degree already exists in the list get the
				// node to merge
				Node<T> other = pairCombine.get(curr.getmDegree());

				// Clear the node from the list for next tree with same degree
				pairCombine.set(curr.getmDegree(), null);

				// get the min and max node of the two trees to be merged
				Node<T> min = (other.getValue() < curr.getValue()) ? other : curr;
				Node<T> max = (other.getValue() < curr.getValue()) ? curr : other;

				/*
				 * Break max out of the root list, then merge it into min's
				 * child list.
				 */
				min.nNext.nPrev = min.nPrev;
				min.nPrev.nNext = min.nNext;

				// Isolate the min node
				min.nNext = min.nPrev = min;

				// merge the max node with the root list
				max.nChild = merge(max.nChild, min);

				// Make min node a child of max node
				min.nParent = max;

				// Set min node's childcut to false
				min.nChildCut = false;

				// Increment the degree of the max node
				++max.nDegree;

				// make the max node the 'curr' node for next pass
				curr = max;
			}

			// Compare the curr node with max and update max if necessary
			if (curr.getValue() >= nMax.getValue())
				nMax = curr;
		}
		// return the element to be deleted - previous max node
		return maxElem;
	}

}
