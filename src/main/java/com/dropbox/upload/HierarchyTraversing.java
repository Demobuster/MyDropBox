package com.dropbox.upload;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.dropbox.DAOs.PathDAO;

/**
 * Servlet implementation class HierarchyTraversing
 */
@WebServlet("/HierarchyTraversing")
public class HierarchyTraversing extends HttpServlet {

	private static final long serialVersionUID = 5287659044356320615L;
	private static Node<String> temp;

    private static ArrayList<String> nodesListToStringList(List<Node<String>> nodes) {
        ArrayList<String> resList = new ArrayList<String>();

        for (Node<String> node : nodes) {
            resList.add(node.getData());
        }

        return resList;
    }

    public static void find(Node<String> element, List<Node<String>> list, String elementToFind) {
        list.add(element);
        for (Node<String> data : element.getChildren()) {
            if (elementToFind.equals(data.getData())) {
                temp = data;
                break;
            } else {
                find(data, list, elementToFind);
            }
        }
    }

    private static String getFilePath(String user, String fName) {
        ArrayList<String> path = PathDAO.getAllPathsByUser(user);
        
        for (Iterator iterator = path.iterator(); iterator.hasNext();) {
            String current = (String) iterator.next();
            if (current.contains(fName)) {
                return current;
            }
        }
        
        return null;
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String redirectPage = "";

        ArrayList<String> paths = PathDAO.getAllPathsByUser((String) session.getAttribute("user"));

        Tree<String> tree = FileUploadServlet.makeATree((String) session.getAttribute("user"), paths);
        session.setAttribute("currentUserTree", tree);
        Tree<String> currentUserTree = (Tree<String>) session.getAttribute("currentUserTree");

        String elementToFind;
        if (request.getParameter("elementToFind") != null) {
        	session.setAttribute("travelPath", 
        			(String) session.getAttribute("travelPath") + request.getParameter("elementToFind") + "/");
        	
            if (request.getParameter("elementToFind").contains(".")) {
                redirectPage = "Uploading?fName=" + getFilePath((String) session.getAttribute("user"), 
                                                                                request.getParameter("elementToFind"));
            } else {
                elementToFind = request.getParameter("elementToFind");

                List<Node<String>> list = new ArrayList<Node<String>>();
                find(currentUserTree.getRootElement(), list, elementToFind);

                session.setAttribute("currentFoldersToDisplay", nodesListToStringList(temp.getChildren()));
                redirectPage = "travel.jsp";
            }
        } else {
            session.setAttribute("currentFoldersToDisplay", nodesListToStringList(currentUserTree.getRootElement().getChildren()));
            redirectPage = "travel.jsp";
        }

        response.sendRedirect(redirectPage);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    

}