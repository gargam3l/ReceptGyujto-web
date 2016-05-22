/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

/**
 *
 * @author 604772006
 */
public class DBServ extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DBServ</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DBServ at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

        if (request.getParameter("action").equals("test")) {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            //    out.println("teszt");
            out.println(DBLogic.kapcsolatTeszt());
        } else if (request.getParameter("action").equals("inic")) {
            DBLogic.inic();
        } else if (request.getParameter("action").equals("otevoMennyTipus")) {
            String contentType = "application/x-java-serialized-object";
            response.setContentType(contentType);
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            ArrayList<String> value = new ArrayList<>();
            value = DBLogic.otevoMennyTipusok();
            out.writeObject(value);

            out.flush();
        } else if (request.getParameter("action").equals("receptetMent")) {
            try {
                String contentType = "application/x-java-serialized-object";
                response.setContentType(contentType);

                ObjectInputStream in = new ObjectInputStream(request.getInputStream());
                //BufferedReader in= new BufferedReader(new InputStreamReader(request.getInputStream()));
                Recept recept = new Recept();
                recept = (Recept) in.readObject();
                DBLogic.receptetMent(recept);

                in.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBServ.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (request.getParameter("action").equals("receptetSzerkeszt")) {
            try {
                String contentType = "application/x-java-serialized-object";
                response.setContentType(contentType);
                
                ObjectInputStream in = new ObjectInputStream(request.getInputStream());
                String aktualis = in.readUTF();

                Recept recept = new Recept();
                recept = (Recept) in.readObject();

                DBLogic.receptetSzerkeszt(aktualis, recept);

                in.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBServ.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (request.getParameter("action").equals("keresMegnevezesre")) {
            try {
                String contentType = "application/x-java-serialized-object";
                response.setContentType(contentType);

                //BufferedReader in= new BufferedReader(new InputStreamReader(request.getInputStream()));
                ArrayList<Recept> receptTar = new ArrayList<>();

                String parameter = request.getParameter("kulcs");
                String dekodoltParameter = URLDecoder.decode(parameter, "UTF-8");

                ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());

                receptTar = DBLogic.keresMegnevezesre(dekodoltParameter);
                out.writeObject(receptTar);

                out.flush();
            } catch (Exception ex) {
                Logger.getLogger(DBServ.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (request.getParameter("action").equals("keresOsszetevoRecepthez")) {
            try {
                String contentType = "application/x-java-serialized-object";
                response.setContentType(contentType);
               
                ArrayList<Osszetevok> eredmeny = new ArrayList<>();

                String parameter = request.getParameter("kulcs");
                String dekodoltParameter = URLDecoder.decode(parameter, "UTF-8");

                ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());

                eredmeny = DBLogic.keresOsszetevoRecepthez(dekodoltParameter);
                out.writeObject(eredmeny);

                out.flush();
            } catch (Exception ex) {
                Logger.getLogger(DBServ.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (request.getParameter("action").equals("receptetTorol")) {
            try {
                String contentType = "application/x-java-serialized-object";
                response.setContentType(contentType);
                ObjectInputStream in = new ObjectInputStream(request.getInputStream());
                String aktualis = in.readUTF();
                in.close();
               DBLogic.receptetTorol(aktualis);
            } catch (Exception ex) {
                Logger.getLogger(DBServ.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            // ???
        }
        // processRequest(request, response);
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
        doGet(request, response);
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
