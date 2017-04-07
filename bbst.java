import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;

public class bbst {
	//main method
	//using file handling to read data
	public static void main(String[] args) throws IOException, NumberFormatException {
		bbst rbt = new bbst();
		String nameOfFile = args[0];
		File file = new File(nameOfFile);
		BufferedReader reader = null;
		int total;
		reader = new BufferedReader(new FileReader(file));
		total = Integer.parseInt(reader.readLine());

		try {
			String str = reader.readLine();
			while (str != null) {				
				String[] str2 = str.split(" ");
				int id = Integer.parseInt(str2[0]);
				int count = Integer.parseInt(str2[1]);
				rbt.insert(id, count);
				str = reader.readLine();
			}
			reader.close();

		} finally {
		}
		//fetching commands from I/P
		//reading various commands as defined in project description
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String command = scanner.nextLine();
			String[] arr = command.split(" ");
			if (arr[0].equals("quit")){
				System.exit(0);
			}else if (arr[0].equals("increase")) {
				int id = Integer.parseInt(arr[1]);
				int count = Integer.parseInt(arr[2]);
				rbt.increaseCount(id, count);

			}else if (arr[0].equals("reduce")) {
				int id = Integer.parseInt(arr[1]);
				int count = Integer.parseInt(arr[2]);
				rbt.decreaseCount(id, count);

			}else  if(arr[0].equals("count")){	
				int id=Integer.parseInt(arr[1]);
				rbt.findCount(id);
				
			}else if (arr[0].equals("inrange")) {
				int id1 = Integer.parseInt(arr[1]);
				int id2 = Integer.parseInt(arr[2]);
				rbt.checkInRange(id1, id2);

			}else if (arr[0].equals("next")) {
				int id = Integer.parseInt(arr[1]);
				rbt.lowestNext(id);

			}else if (arr[0].equals("previous")) {
				int id = Integer.parseInt(arr[1]);
				rbt.greatestPrev(id);
			}
		}
	}

	private RedBlackTreeNode nilNode = new RedBlackTreeNode();

	private RedBlackTreeNode root = nilNode;

	public bbst() {
		root.left = nilNode;
		root.right = nilNode;
		root.parent = nilNode;
	}

	public void insert(int id, int count) {
		insert(new RedBlackTreeNode(id, count));
	}

	//inserting the new node in RBT
	private void insert(RedBlackTreeNode nodeToInsert) {
		RedBlackTreeNode temp2 = root;
		RedBlackTreeNode temp1 = nilNode;

		while (!checkIfNodeIsNull(temp2)) {
			temp1 = temp2;

			//going left
			if (nodeToInsert.key - (temp2.key) < 0) {
				temp2.totalLeft++;
				temp2 = temp2.left;
			}

			else {
				temp2.totalRight++;
				temp2 = temp2.right;
			}
		}
		nodeToInsert.parent = temp1;

		//inserting in left or right
		if (checkIfNodeIsNull(temp1))
			root = nodeToInsert;
		else if (nodeToInsert.key - (temp1.key) < 0)
			temp1.left = nodeToInsert;
		else
			temp1.right = nodeToInsert;

		//new children to nilNode
		nodeToInsert.left = nilNode;
		nodeToInsert.right = nilNode;
		nodeToInsert.color = RedBlackTreeNode.RED;

		fixInsert(nodeToInsert);

	}


	//fixing after insertion
	private void fixInsert(RedBlackTreeNode nodeToFix) {
		RedBlackTreeNode temp1 = nilNode;
		while (nodeToFix.parent.color == RedBlackTreeNode.RED) {
			//nodeToFix's parent is left child of its parent
			if (nodeToFix.parent == nodeToFix.parent.parent.left) {

				//if temp1's color is red
				if (temp1.color == RedBlackTreeNode.RED) {
					nodeToFix.parent.color = RedBlackTreeNode.BLACK;
					temp1.color = RedBlackTreeNode.BLACK;
					nodeToFix.parent.parent.color = RedBlackTreeNode.RED;
					nodeToFix = nodeToFix.parent.parent;
				}
				// temp1 is black and nodeToFix is right child
				else if (nodeToFix == nodeToFix.parent.right) {
					nodeToFix = nodeToFix.parent;
					rotateLeft(nodeToFix);
				}

				// nodeToFix's parent is black & nodeToFix is a left child
				else {
					nodeToFix.parent.color = RedBlackTreeNode.BLACK;
					nodeToFix.parent.parent.color = RedBlackTreeNode.RED;
					rotateRight(nodeToFix.parent.parent);
				}
			}
			//nodeToFix's parent is right child of its parent
			else {
				temp1 = nodeToFix.parent.parent.left;
				// color of temp1=red
				if (temp1.color == RedBlackTreeNode.RED) {
					nodeToFix.parent.color = RedBlackTreeNode.BLACK;
					temp1.color = RedBlackTreeNode.BLACK;
					nodeToFix.parent.parent.color = RedBlackTreeNode.RED;
					nodeToFix = nodeToFix.parent.parent;
				}
				//color of temp1 is black and nodeToFix is left child
				else if (nodeToFix == nodeToFix.parent.left) {
					nodeToFix = nodeToFix.parent;
					rotateRight(nodeToFix);
				}
				//color of temp1 is black and nodeToFix is right child
				else {
					nodeToFix.parent.color = RedBlackTreeNode.BLACK;
					nodeToFix.parent.parent.color = RedBlackTreeNode.RED;
					rotateLeft(nodeToFix.parent.parent);
				}
			}
		}
		root.color = RedBlackTreeNode.BLACK;
	}
	//next node with smallest value
	public RedBlackTreeNode findSuccessor(RedBlackTreeNode x) {
		if (!checkIfNodeIsNull(x.left))
			return minimumInTree(x.right);

		RedBlackTreeNode y = x.parent;
		while (!checkIfNodeIsNull(y) && x == y.right) {
			x = y;
			y = y.parent;
		}
		return y;
	}

	//next node with lowest id
	public void lowestNext(int theID) {
		{

			RedBlackTreeNode tempClose = nilNode;
			int distMin = 0x7FFFFFFF;
			RedBlackTreeNode temp = root;
			while (temp != nilNode) {
				int distance = Math.abs(temp.key - theID);
				if ((distance < distMin) && (temp.key - theID) > 0) {
					distMin = distance;
					tempClose = temp;
				}
				if (temp.key > theID)
					temp = temp.left;
				else if (temp.key <= theID)
					temp = temp.right;
			}
			System.out.println(tempClose.key + " " + tempClose.count);
		}
	}

	//prev node with greatest id
	public void greatestPrev(int theID) {
		RedBlackTreeNode tempClose = nilNode;
		int distMin = 0x7FFFFFFF;
		RedBlackTreeNode tempMin = root;
		while (tempMin != nilNode) {
			int distance = Math.abs(tempMin.key - theID);
			if ((distance < distMin) && (theID - tempMin.key > 0)) {
				distMin = distance;
				tempClose = tempMin;
			}
			if (tempMin.key >= theID)
				tempMin = tempMin.left;
			else if (tempMin.key < theID)
				tempMin = tempMin.right;
		}
		System.out.println(tempClose.key + " " + tempClose.count);
	}

	//node removal
	public void deleteNode(int key) {
		RedBlackTreeNode tempc = findNodeInTree(key);
		RedBlackTreeNode tempb = nilNode;
		RedBlackTreeNode tempa = nilNode;
		if (checkIfNodeIsNull(tempc.left) || checkIfNodeIsNull(tempc.right))
			tempa = tempc;
		else
			tempa = findSuccessor(tempc);

		if (!checkIfNodeIsNull(tempa.left))
			tempb = tempa.left;
		else
			tempb = tempa.right;

		tempb.parent = tempa.parent;
		if (checkIfNodeIsNull(tempa.parent))
			root = tempb;
		else if (!checkIfNodeIsNull(tempa.parent.left) && tempa.parent.left == tempa)
			tempa.parent.left = tempb;

		else if (!checkIfNodeIsNull(tempa.parent.right) && tempa.parent.right == tempa)
			tempa.parent.right = tempb;

		if (tempa != tempc) {
			tempc.key = tempa.key;
		}
		RedBlackTreeNode currentTemp = nilNode;
		RedBlackTreeNode keeptrack = nilNode;

		if (checkIfNodeIsNull(tempb)) {
			currentTemp = tempa.parent;
			keeptrack = tempa;
		}else {
			currentTemp = tempb.parent;
			keeptrack = tempb;
		}
		while (!checkIfNodeIsNull(currentTemp)) {
			if (tempa.key != currentTemp.key) {

				if (tempa.key - (currentTemp.key) > 0)
					currentTemp.totalRight--;

				if (tempa.key - (currentTemp.key) < 0)
					currentTemp.totalLeft--;
			}

			else {
				if (checkIfNodeIsNull(currentTemp.left))
					currentTemp.totalLeft--;
				else if (checkIfNodeIsNull(currentTemp.right))
					currentTemp.totalRight--;
				else if (keeptrack == currentTemp.right)
					currentTemp.totalRight--;
				else if (keeptrack == currentTemp.left)
					currentTemp.totalLeft--;
			}
			keeptrack = currentTemp;
			currentTemp = currentTemp.parent;
		}
		if (tempa.color == RedBlackTreeNode.BLACK)
			fixAfterDeletion(tempb);
	}

	//find node from key in RBT
	public RedBlackTreeNode findNodeInTree(int key) {
		RedBlackTreeNode current = root;
		while (!checkIfNodeIsNull(current)) {
			if (current.key == (key))
				return current;
			else if (current.key - (key) < 0)
				current = current.right;
			else
				current = current.left;
		}
		return nilNode;
	}
	//find node with minimum key
	public RedBlackTreeNode minimumInTree(RedBlackTreeNode node) {
		while (!checkIfNodeIsNull(node.left))
			node = node.left;
		return node;
	}
	//find greater elements
	public List<Integer> findBiggerElement(int key, Integer maxReturned) {
		List<Integer> newList = new ArrayList<Integer>();
		findBiggerElement(root, key, newList);
		return newList.subList(0, Math.min(maxReturned, newList.size()));
	}

	private void findBiggerElement(RedBlackTreeNode node, int key, List<Integer> newList) {
		if (checkIfNodeIsNull(node)) {
			return;
		} else if (node.key - (key) >= 0) {
			findBiggerElement(node.left, key, newList);
			newList.add(node.key);
			findBiggerElement(node.right, key, newList);
		} else {
			findBiggerElement(node.right, key, newList);
		}
	}

	//checking for null
	private boolean checkIfNodeIsNull(RedBlackTreeNode node) {
		return node == nilNode;
	}

	//find node with maximumInTree key
	public RedBlackTreeNode maximumInTree(RedBlackTreeNode node) {
		RedBlackTreeNode currentNode = node;
		while (currentNode.right != nilNode && currentNode.right != null) {
			currentNode = currentNode.right;
		}
		return currentNode;
	}
	//perform fixup after deletion
	private void fixAfterDeletion(RedBlackTreeNode nodeToDelete) {
		RedBlackTreeNode nodeTemp;
		while (nodeToDelete != root && nodeToDelete.color == RedBlackTreeNode.BLACK) {
			if (nodeToDelete == nodeToDelete.parent.left) {
				nodeTemp = nodeToDelete.parent.right;
				//color of nodeTemp=red
				if (nodeTemp.color == RedBlackTreeNode.RED) {
					nodeTemp.color = RedBlackTreeNode.BLACK;
					nodeToDelete.parent.color = RedBlackTreeNode.RED;
					rotateLeft(nodeToDelete.parent);
					nodeTemp = nodeToDelete.parent.right;
				}
				//nodeTemp's left and right child color=black
				if (nodeTemp.left.color == RedBlackTreeNode.BLACK && nodeTemp.right.color == RedBlackTreeNode.BLACK) {
					nodeTemp.color = RedBlackTreeNode.RED;
					nodeToDelete = nodeToDelete.parent;
				} else {
					//nodeTemp's right child color=black
					if (nodeTemp.right.color == RedBlackTreeNode.BLACK) {
						nodeTemp.left.color = RedBlackTreeNode.BLACK;
						nodeTemp.color = RedBlackTreeNode.RED;
						rotateRight(nodeTemp);
						nodeTemp = nodeToDelete.parent.right;
					}
					nodeTemp.color = nodeToDelete.parent.color;
					nodeToDelete.parent.color = RedBlackTreeNode.BLACK;
					nodeTemp.right.color = RedBlackTreeNode.BLACK;
					rotateLeft(nodeToDelete.parent);
					nodeToDelete = root;
				}
			} else {
				nodeTemp = nodeToDelete.parent.left;
				//nodeTemp color=red
				if (nodeTemp.color == RedBlackTreeNode.RED) {
					nodeTemp.color = RedBlackTreeNode.BLACK;
					nodeToDelete.parent.color = RedBlackTreeNode.RED;
					rotateRight(nodeToDelete.parent);
					nodeTemp = nodeToDelete.parent.left;
				}
				//nodeTemp's both children color= black
				if (nodeTemp.right.color == RedBlackTreeNode.BLACK && nodeTemp.left.color == RedBlackTreeNode.BLACK) {
					nodeTemp.color = RedBlackTreeNode.RED;
					nodeToDelete = nodeToDelete.parent;
				} else {
					//nodeTemp's left child color=black
					if (nodeTemp.left.color == RedBlackTreeNode.BLACK) {
						nodeTemp.right.color = RedBlackTreeNode.BLACK;
						nodeTemp.color = RedBlackTreeNode.RED;
						rotateLeft(nodeTemp);
						nodeTemp = nodeToDelete.parent.left;
					}
					//nodeTemp=black and left child=red
					nodeTemp.color = nodeToDelete.parent.color;
					nodeToDelete.parent.color = RedBlackTreeNode.BLACK;
					nodeTemp.left.color = RedBlackTreeNode.BLACK;
					rotateRight(nodeToDelete.parent);
					nodeToDelete = root;
				}
			}
		}
		nodeToDelete.color = RedBlackTreeNode.BLACK;
	}

	//increase count of id by count
	public void increaseCount(int theID, int count)
	{
		RedBlackTreeNode redBlackTreeNode = findNodeInTree(theID);
		if (redBlackTreeNode.equals(nilNode)) {
			insert(theID, count);
			System.out.println(count);
		} else {
			redBlackTreeNode.count += count;
			System.out.println(redBlackTreeNode.count);
		}
	}

	//decrease count of id by count
	public void decreaseCount(int theID, int count)
	{
		RedBlackTreeNode treenode = findNodeInTree(theID);
		if (!treenode.equals(nilNode)) {
			treenode.count -= count;
			if (treenode.count <= 0) {
				deleteNode(theID);
				System.out.println("0");
			} else
				System.out.println(treenode.count);
		} else
			System.out.println("0");
	}

	//displaying count of id
	public void findCount(int theID)
	{
		RedBlackTreeNode treeNode = findNodeInTree(theID);
		if (!treeNode.equals(nilNode)) {
			System.out.println(treeNode.count);
		} else
			System.out.println("0");
	}

	//finding sum of count of id's b/w id1 and id2
	public void checkInRange(int ID1, int ID2)
	{
		int newcount = 0;
		if (ID1 > ID2)
			System.out.print("InRange: Wrong parameter");
		else {
			List<Integer> list = findBiggerElement(ID1, Integer.MAX_VALUE);
			List<Integer> newlist = new ArrayList<Integer>();
			for (int i = 0; i < list.size(); i++) {
				{
					if (list.get(i) <= ID2) {
						newlist.add(list.get(i));
						newcount += findNodeInTree(list.get(i)).count;
					}
				}
			}
			System.out.println(newcount);
		}
	}
	//performing left rotate
		private void rotateLeft(RedBlackTreeNode nodeToRotate) {

			//nodeToRotate left and right are not nilNode
			if ((nodeToRotate.left) == nilNode && (nodeToRotate.right.left) == nilNode) {
				nodeToRotate.totalLeft = 0;
				nodeToRotate.totalRight = 0;
				nodeToRotate.right.totalLeft = 1;
			}
			//nodeToRotate's right.left child exists
			else if ((nodeToRotate.left) == nilNode && (nodeToRotate.right.left) != nilNode) {
				nodeToRotate.totalLeft = 0;
				nodeToRotate.totalRight = 1 + nodeToRotate.right.left.totalLeft + nodeToRotate.right.left.totalRight;
				nodeToRotate.right.totalLeft = 2 + nodeToRotate.right.left.totalLeft + nodeToRotate.right.left.totalRight;
			}
			// nodeToRotate.left exists
			else if ((nodeToRotate.left) != nilNode && (nodeToRotate.right.left) == nilNode) {
				nodeToRotate.totalRight = 0;
				nodeToRotate.right.totalLeft = 2 + nodeToRotate.left.totalLeft + nodeToRotate.left.totalRight;
			}
			//else case
			else {
				nodeToRotate.totalRight = 1 + nodeToRotate.right.left.totalLeft + nodeToRotate.right.left.totalRight;
				nodeToRotate.right.totalLeft = 3 + nodeToRotate.left.totalLeft + nodeToRotate.left.totalRight
						+ nodeToRotate.right.left.totalLeft + nodeToRotate.right.left.totalRight;
			}
			RedBlackTreeNode y;
			y = nodeToRotate.right;
			nodeToRotate.right = y.left;
			//if y.left exists
			if (!checkIfNodeIsNull(y.left))
				y.left.parent = nodeToRotate;
			y.parent = nodeToRotate.parent;
			if (checkIfNodeIsNull(nodeToRotate.parent))
				root = y;
			//nodeToRotate is left child
			else if (nodeToRotate.parent.left == nodeToRotate)
				nodeToRotate.parent.left = y;

			//nodeToRotate is right child
			else
				nodeToRotate.parent.right = y;

			y.left = nodeToRotate;
			nodeToRotate.parent = y;
		}

		//performing right rotate
		private void rotateRight(RedBlackTreeNode nodeToRotate) {

			//nodeToRotate left and right exists
			if ((nodeToRotate.right) == nilNode && (nodeToRotate.left.right) == nilNode) {
				nodeToRotate.totalRight = 0;
				nodeToRotate.totalLeft = 0;
				nodeToRotate.left.totalRight = 1;
			}
			// nodeToRotate's left.right exists
			else if ((nodeToRotate.right) == nilNode && (nodeToRotate.left.right) != nilNode) {
				nodeToRotate.totalRight = 0;
				nodeToRotate.totalLeft = 1 + nodeToRotate.left.right.totalRight + nodeToRotate.left.right.totalLeft;
				nodeToRotate.left.totalRight = 2 + nodeToRotate.left.right.totalRight + nodeToRotate.left.right.totalLeft;
			}
			//nodeToRotate.right exists
			else if ((nodeToRotate.right) != nilNode && (nodeToRotate.left.right) == nilNode) {
				nodeToRotate.totalLeft = 0;
				nodeToRotate.left.totalRight = 2 + nodeToRotate.right.totalRight + nodeToRotate.right.totalLeft;

			}
			//else case
			else {
				nodeToRotate.totalLeft = 1 + nodeToRotate.left.right.totalRight + nodeToRotate.left.right.totalLeft;
				nodeToRotate.left.totalRight = 3 + nodeToRotate.right.totalRight + nodeToRotate.right.totalLeft
						+ nodeToRotate.left.right.totalRight + nodeToRotate.left.right.totalLeft;
			}
			RedBlackTreeNode tempNode = nodeToRotate.left;
			nodeToRotate.left = tempNode.right;

			// Check if tempNode.right is nilNode
			if ((tempNode.right) != nilNode)
				tempNode.right.parent = nodeToRotate;
			tempNode.parent = nodeToRotate.parent;

			if ((nodeToRotate.parent) == nilNode)
				root = tempNode;
			else if (nodeToRotate.parent.right == nodeToRotate)
				nodeToRotate.parent.right = tempNode;
			else
				nodeToRotate.parent.left = tempNode;
			
			tempNode.right = nodeToRotate;
			nodeToRotate.parent = tempNode;
		}
}

class RedBlackTreeNode {

	//parent node
	RedBlackTreeNode parent;

	// left child
	RedBlackTreeNode left;

	// right child
	RedBlackTreeNode right;

	//total elements on left
	public int totalLeft = 0;

	//total elements on left
	public int totalRight = 0;

	//key
	public int key;

	//value of color
	public int color;

	//count
	public int count = 0;

	public static final int BLACK = 0;

	public static final int RED = 1;

	RedBlackTreeNode() {
		parent = null;
		color = BLACK;
		left = null;
		right = null;
		totalLeft = 0;
		totalRight = 0;
	}

	//setting values of variables
	RedBlackTreeNode(int key, int count) {
		this();
		this.key = key;
		this.count = count;

	}
}
