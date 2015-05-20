package com.dropbox.upload;

import java.io.*;
import java.sql.Date;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.dropbox.DAOs.PathDAO;

/**
 * Servlet implementation class Uploading
 */
@WebServlet("/Uploading")
@MultipartConfig(location = "/var/lib/openshift/554642504382ec1745000091/app-root/data")
public class Uploading extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final int BUFFER_LENGTH = 4096;
	
	public static ArrayList<String> stringToList(String path) {
        ArrayList<String> list = new ArrayList<String>();
        
        for (StringTokenizer stringTokenizer = new StringTokenizer(path); stringTokenizer.hasMoreTokens();) {
            String token = stringTokenizer.nextToken("/\\");
            list.add(token);
        }
        
        return list;
    }
	
    public static Tree<String> makeATree(String user, List<String> pathsToConvert) {
        Tree<String> tree = new Tree<String>();
        tree.setRootElement(new Node<String>(user));

        for (int i = 0; i < pathsToConvert.size(); i++) {
            String s = pathsToConvert.get(i);

            for (String path : pathsToConvert) {
                ArrayList<String> list = stringToList(path);
                TreeTryout.check(tree.getRootElement(), list);
            }
        }
        
        return tree;
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String path = System.getenv("OPENSHIFT_DATA_DIR") + session.getAttribute("user") + "/" +
					  request.getParameter("fName");

		File file = new File(path);

		if (!file.exists()) {
			throw new ServletException("File doesn't exists on server.");
		}

		ServletContext ctx = getServletContext();
		InputStream fis = new FileInputStream(file);
		String mimeType = ctx.getMimeType(file.getAbsolutePath());

		response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getParameter("fName") + "\"");

		ServletOutputStream os = response.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read = 0;

		while ((read = fis.read(bufferData)) != -1) {
			os.write(bufferData, 0, read);
		}

		os.flush();
		os.close();
		fis.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		String subPaths = ((request.getParameter("subPaths") != null) ? request.getParameter("subPaths") : "");
		String resPath = System.getenv("OPENSHIFT_DATA_DIR") + session.getAttribute("user") + "/" + subPaths;

		File f = new File(resPath);
        if (!f.exists()) {                      
            f.mkdirs();
        }
		
		Collection<Part> parts = request.getParts();
		out.write("<h2> Total parts : " + parts.size() + "</h2>");
        
        for (Part part : parts) {
        	InputStream is = request.getPart(part.getName()).getInputStream();
            String fileName = getFileName(part);
            FileOutputStream os = new FileOutputStream(resPath + "/" + fileName);
            byte[] bytes = new byte[BUFFER_LENGTH];
            int read = 0;
            
            while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                os.write(bytes, 0, read);
            }
            
            os.flush();
            is.close();
            os.close();
        	out.println(fileName + " was uploaded to " + resPath);
        	out.println(System.getenv("OPENSHIFT_DATA_DIR"));
        	
        	Date date = new Date(34545345);
        	PathDAO.persist((String) session.getAttribute("user"), subPaths + "/" + fileName, date);
        	
        	break;
        }
		
        response.sendRedirect("Service");
	}
	
	private String getFileName(Part part) {
		String partHeader = part.getHeader("content-disposition");
		for (String cd : partHeader.split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}
}