/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo._accesoDirecto;
import modelo._usuario;

/**
 *
 * @author fernando
 */
@MultipartConfig
@WebServlet(name = "accesoDirecto", urlPatterns = {"/accesoDirecto"})
public class accesoDirecto extends HttpServlet {

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
            
            String action = request.getParameter("action");
           
            switch(action){
               
               case "marcar":
                   out.print(marcar(request, response));
               break;
                   
               case "desmarcar":
                   out.print(desmarcar(request, response));
               break;
               
               case "accesoDirecto":
                   accesoDirecto(request, response);
               break;
               
               case "listarAccesos":
                   out.print(listarAccesos(request, response));
               break;
               
               case "borrarAccesoDirecto":
                   out.print(borrarAccesoDirecto(request, response));
               break;
            }
        }
    }
    
    
    public JsonObject marcar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {        
        
        String id_publicacion = request.getParameter("id_publicacion");
        String id_usuario = request.getParameter("id_usuario");
        JsonObject json = new JsonObject();
        
        _accesoDirecto modelo = new _accesoDirecto();
        modelo.setId_publicacion(Integer.parseInt(id_publicacion));
        modelo.setId_usuario(Integer.parseInt(id_usuario));
       
        json = modelo.marcar();
        
        return json;
    }
    
    public JsonObject desmarcar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {        
        
        String id_publicacion = request.getParameter("id_publicacion");
        String id_usuario = request.getParameter("id_usuario");
        JsonObject json = new JsonObject();
        
        _accesoDirecto modelo = new _accesoDirecto();
        modelo.setId_publicacion(Integer.parseInt(id_publicacion));
        modelo.setId_usuario(Integer.parseInt(id_usuario));
       
        json = modelo.desmarcar();
        
        return json;
    }
    
    
    public JsonArray listarAccesos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    
        JsonArray json = new JsonArray();
        HttpSession http = request.getSession();
        _accesoDirecto modelo = new _accesoDirecto();
        
        _usuario usuario = (_usuario) http.getAttribute("usuario");
        modelo.setId_usuario(usuario.getId());
        
        json = modelo.listarAccesos();
        
        return json;
        
    }
    
    public JsonObject borrarAccesoDirecto(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {        
        
        String id = request.getParameter("id");
        JsonObject json = new JsonObject();
        
        _accesoDirecto modelo = new _accesoDirecto();
        modelo.setId(Integer.parseInt(id));
       
        json = modelo.borrarAccesoDirecto();
        
        return json;
    }

    public void accesoDirecto(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendRedirect("./vista/accesoDirecto/accesos.jsp?action=accesos");
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
