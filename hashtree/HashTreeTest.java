

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/**
 * 
 * @author Liang Tang
 * @email tangl99@gmail.com
 * Updated by Jan 3, 2013 11:13:59 PM
 *
 */
public class HashTreeTest {
	
	@Test
	public void test1() {
		HashTree tree = new HashTree(3, 7, 1);
		int[][] sets = {{1,2,3},{2,5,6},{4,5,9},{2,3,4}};
		long i = 0;
		for (int[] set: sets) {
			tree.add(set, i++);
		}
		tree.print();
		int[] transaction = {1,2,3,4,5,8,9};
		Set<Long> coveredInsts = tree.searchCoveredInsts(new TreeSet<Integer>(CollectionUtil.asSet(transaction)));
		for (long index: coveredInsts) {
			System.out.println(CollectionUtil.asList(sets[(int)index]));
		}
	}
	
	@Test
	public void test2() {
		HashTree tree = new HashTree(3);
		int[][] sets = {{1,2,3},{2,5,6},{4,5,9},{2,3,4}};
		long i = 0;
		for (int[] set: sets) {
			tree.add(set, i++);
		}
		// tree.print();
		int[] transaction = {2,4};
		Set<Long> coveredInsts = tree.searchCoveredInsts(new TreeSet<Integer>(CollectionUtil.asSet(transaction)));
		for (long index: coveredInsts) {
			System.out.println(CollectionUtil.asList(sets[(int)index]));
		}
	}

}
