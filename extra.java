// FILE FOR EXTRA/UNNECESSARY CODE

/* import org.w3c.dom.Node;

public class extra {
  // need to set deleted node to deleted node in somewhere
  private Node findAndDelete(Node currentNode, K key) {
    // every node we enter has to be red
    // we need to recalculate compare each time so value is up to date

    // key to be deleted is in the left subtree
    if(key.compareTo(currentNode.key) < 0) {
      // if the left node and left's child is not red, color flip to bring redness with us
      if(!currentNode.left.isRed && !currentNode.left.left.isRed) {
        colorFlip(currentNode);

        if(currentNode.right.left.isRed) {
          currentNode.right = rotateRight(currentNode.right);
          currentNode = rotateLeft(currentNode);
          colorFlip(currentNode);
        }
      currentNode.left = findAndDelete(currentNode.left, key);
      }
    }

    // key to delete is in the right subtree
    else {
      if(currentNode.left.isRed) currentNode = rotateRight(currentNode);
      
      // the current node has the proper key and it has a left red leaf
      if(key.compareTo(currentNode.key) == 0 && currentNode.right == null) return null;
      
      if(!currentNode.right.isRed && !currentNode.right.left.isRed) {
        colorFlip(currentNode);
        if(currentNode.left.left.isRed) {
          currentNode = rotateRight(currentNode);
          colorFlip(currentNode);
        }
      }

      if(key.compareTo(currentNode.key) == 0) {
        Node min = findMinNode(currentNode.right);
        currentNode.key = min.key;
        currentNode.value = min.value;

        currentNode.right = deleteMin(currentNode.right);
      }

      else currentNode.right = findAndDelete(currentNode.right, key);
    }

    return fixForDelete(currentNode);
  }

  private Node deleteMin(Node node) {
    if (node.left == null) return null;

    if (!node.left.isRed && !node.left.left.isRed) {
      colorFlip(node);
      if(node.left.left.isRed) {
      node = rotateRight(node);
      colorFlip(node);
      }
    }
        
    node.left = deleteMin(node.left);
    return fixForDelete(node);
  }
}
 */