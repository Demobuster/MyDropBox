package com.dropbox.upload;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -7565763869637704265L;
	private final static Logger LOGGER = Logger.getLogger(FileUploadServlet.class.getCanonicalName());

    /**
     *
     * @param user
     * @param pathsToConvert
     * @return
     */
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);
        String subPath = ((request.getParameter("subPath") != null) ? request.getParameter("subPath") : "");

        //=============================== Create path components to save the file
        final String path = "F:/DataStorage/" + session.getAttribute("user") + '/' + subPath;
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);

        session.setAttribute("fileName", fileName);

        //==============================MAKING PERSONAL DIRS IS THEY DOESN'T EXIST
        File f = new File(path);
        if (!f.exists()) {                      
            f.mkdirs();
        }
        
        //==============================

        Date date = new Date(0);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        PathDAO.persist((String) session.getAttribute("user"), subPath + "/" + fileName, date);
     
        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try {
            out = new FileOutputStream(new File(path + '/' + fileName));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            
            RequestDispatcher rd = getServletContext().getRequestDispatcher("Service"); 
            rd.include(request, response);
            //================================================================================

            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", new Object[]{fileName, path});
        } catch (FileNotFoundException fne) {
            writer.println("You either did not specify a file to upload or are trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());
            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", new Object[]{fne.getMessage()});
        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            if (writer != null) {
                writer.close();
            } 
        }
        
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String path = "F:/DataStorage" + '/' + session.getAttribute("user") + '/' + request.getParameter("fName");

        if (path == null) {
            throw new ServletException("File Name can't be null or empty.");
        }

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

        System.out.println("File downloaded at client successfully");
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);

        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
