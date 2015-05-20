package com.dropbox.upload;

import java.util.*;

public class TreeTryout {
//    private static Node<String> temp;
//
//    private static final String root = "binaries";
    
    
    
    /**
     * Recursive procedure walks through the path folders and check the roots: if it contains folder, it's going next, otherwise it creates 
     * a new child and goes again 
     * 
     * @param localRoot new iteration local root to search childs for
     * @param pathElements an elements of path, such as folders or files
     */
    public static void check(Node<String> localRoot, ArrayList<String> pathElements) {
        List<Node<String>> childrens = localRoot.getChildren();
        
        if (pathElements.isEmpty()) {
            return;
        }
        
        if (!childrens.contains(new Node<String>(pathElements.get(0)))) {           // not empty, but target not exits            
            localRoot.addChild(new Node<String>(pathElements.get(0)));            
            
            List<Node<String>> temp = localRoot.getChildren();                  // get all of the childrens to get first or last

            if (!childrens.isEmpty()) {
                localRoot = temp.get(localRoot.getNumberOfChildren() - 1);      // last
            } else {
                localRoot = temp.get(0);                                        // first
            }
            
            pathElements.remove(0);                                                 // target shift

            check(localRoot, pathElements);
        } else {
            int targetIndex = childrens.indexOf(new Node<String>(pathElements.get(0)));
            localRoot = childrens.get(targetIndex);                                     // finding index of targets position in childrens list and getting obj
            pathElements.remove(0);                                                 // target shift
            
            check(localRoot, pathElements);
        }
    }
    
    private static ArrayList<String> nodesListToStringList(List<Node<String>> nodes) {
        ArrayList<String> resList = new ArrayList<String>();
        
        for (Node<String> node : nodes) {
            resList.add(node.getData());
        }

        return resList;
    }
}

