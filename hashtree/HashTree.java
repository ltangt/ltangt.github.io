import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 * @author Liang Tang
 * @email tangl99@gmail.com
 * Updated by Jan 3, 2013 11:05:17 PM
 *
 */
public class HashTree<K> {
	
	private int numBranches = 79;
	
	private int maxNodeInsts = 43;
	
	private int instLen = -1;
	
	HashTreeNode<K> root = null;
	
	public HashTree(int instLen) {
		this.instLen = instLen;
		root = new HashTreeNode<K>(0);
	}
	
	public HashTree(int instLen, int numBranches, int maxNodeInsts) {
		this.instLen = instLen;
		this.numBranches = numBranches;
		this.maxNodeInsts = maxNodeInsts;
		root = new HashTreeNode<K>(0);
	}
	
	public void add(int[] inst, K key) {
		this.add(CollectionUtil.asSet(inst), key);
	}
	
	public void add(Set<Integer> inst, K key) {
		root.add(new InstObj<K>(inst, key));
	}
		
	public Set<K> searchCoveredInsts(SortedSet<Integer> querySet) {
		Set<K> coveredInsts = new HashSet<K>();
		int[] querySeq = CollectionUtil.asIntegerArray(querySet);
		root.search(querySeq, 0, querySet, coveredInsts);
		return coveredInsts;
	}
	
	public Set<K> searchCoveredInsts(Set<Integer> set) {
		return searchCoveredInsts(new TreeSet<Integer>(set));
	}
	
	public void print() {
		root.print();
	}
	
	class InstObj<K> {
		public K key;
		public int[] seq;
		
		public InstObj(Set<Integer> s, K key) {
			this.seq = CollectionUtil.asIntegerArray(s);
			this.key = key;
			Arrays.sort(seq);
		}
		
		public String toString() {
			return key+":"+CollectionUtil.asList(seq);
		}
	}
		
	class HashTreeNode<K> {
		HashTreeNode<K>[] childNodes = new HashTreeNode[numBranches];
		int depth; // depth of the root node is 0, root node cannot be a leaf node
		boolean bIsLeaf = true; 
		// The key is the instance id, the value is the sequence
		List<InstObj<K>> insts = new ArrayList<InstObj<K>>();
				
		public HashTreeNode(int depth) {
			this.depth = depth;
			if (depth == 0) {
				bIsLeaf = false;
			}
		}
		
		public void add(InstObj<K> inst) {
			if (bIsLeaf) {
				insts.add(inst);
				if (insts.size() > maxNodeInsts && depth < inst.seq.length-1) {
					split();
				}
			}
			else {
				pushDown(inst);
			}
		}
		
		private void pushDown(InstObj<K> inst) {
			int[] seq = inst.seq;
			int elem = seq[depth];
			int childNodeIndex = elem % numBranches;
			HashTreeNode<K> childNode = childNodes[childNodeIndex];
			if (childNode == null) {
				childNode = new HashTreeNode<K>(depth+1);
				childNodes[childNodeIndex] = childNode;
			}
			childNode.add(inst);
		}
		
		private void split() {
			for (InstObj<K> inst: insts) {
				pushDown(inst);
			}
			// Clear the instance map
			insts = new ArrayList<InstObj<K>>();
			bIsLeaf = false;
		}
		
		public void search(int[] querySeq, int startIndex, Set<Integer> querySet, Set<K> coveredInstKeys) {
			if (bIsLeaf) {
				for (InstObj<K> inst: insts) {
					boolean bCovered = true;
					for (int elem: inst.seq) {
						if (querySet.contains(elem) == false) {
							bCovered = false;
							break;
						}
					}
					if (bCovered) {
						coveredInstKeys.add(inst.key);
					}
				}
			}
			else {
				if (querySet.size() < instLen) {
					return;
				}
				for (int queryStartIndex=startIndex; queryStartIndex<=querySeq.length-instLen; queryStartIndex++) {
					int firstElem = querySeq[queryStartIndex];
					int childNodeIndex = firstElem % numBranches;
					HashTreeNode<K> childNode = childNodes[childNodeIndex];
					if (childNode != null) {
						childNode.search(querySeq, queryStartIndex+1, querySet, coveredInstKeys);
					}
				}
			}
		}
		
		public void print() {
			StringBuffer indentBuf = new StringBuffer();
			for (int i=0; i<depth; i++) {
				indentBuf.append("  ");
			}
			String indent = indentBuf.toString();
			if (bIsLeaf) {
				System.out.println(indent+insts.toString());
			}
			else {
				for (int i=0; i<childNodes.length; i++) {
					if (childNodes[i] == null) {
						System.out.println(indent+i+" node: null");
					}
					else {
						System.out.println(indent+i+" node:");
						childNodes[i].print();
					}
				}
			}
		}
	}
	
	
	
}
