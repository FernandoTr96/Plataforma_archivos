/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo._publicaciones;
import modelo._usuario;

/**
 *
 * @author fernando
 */
//cuando se mandan imagenes se debe poner el @multipartConfig
@MultipartConfig
@WebServlet(name = "publicaciones", urlPatterns = {"/publicaciones"})
public class publicaciones extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    final String nombre_proyecto = "plataforma_archivos/";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String action = request.getParameter("action");

            switch (action) {

                case "adminPublicaciones":
                    adminPublicaciones(request, response);
                    break;

                case "guardarPublicacion":
                    out.print(guardarPublicacion(request, response));
                    break;

                case "listarPublicaciones":
                    out.print(listarPublicaciones(request, response));
                    break;

                case "eliminarPublicacion":
                    out.print(eliminarPublicacion(request, response));
                    break;

                case "getPublicacion":
                    out.print(getPublicacion(request, response));
                    break;

                case "updatePublicacion":
                    out.print(updatePublicacion(request, response));
                    break;

                case "buscarPublicaciones":
                    out.print(buscarPublicaciones(request, response));
                    break;

                case "listarPublicacionesHome":
                    out.print(listarPublicacionesHome(request, response));
                    break;

                case "buscarPublicacionesHome":
                    out.print(buscarPublicacionesHome(request, response));
                    break;

                case "filtrarPublicacionesHome":
                    out.print(filtrarPublicacionesHome(request, response));
                    break;

                case "detalles":
                    detalles(request, response);
                    break;

                case "tablaDetalles":
                    out.print(tablaDetalles(request, response));
                    break;

                case "listarArchivos":
                    out.print(listarArchivos(request, response));
                    break;

                case "eliminarArchivo":
                    out.print(eliminarArchivo(request, response));
                    break;

                case "cerrarPublicacion":
                    out.print(cerrarPublicacion(request, response));
                    break;

                case "subirDocumento":
                    out.print(subirDocumento(request, response));
                    break;

                case "crearZip":
                    out.print(crearZip(request, response));
                    break;
                    
                case "eliminarZip":
                    out.print(eliminarZip(request, response));
                    break;
            }
        }
    }

    public JsonObject guardarPublicacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject insert = new JsonObject();
        HttpSession http = request.getSession();
        _usuario usuario = (_usuario) http.getAttribute("usuario");

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String prioridad = request.getParameter("prioridad");
        String fechaInicial = request.getParameter("fecha_inicial");
        String fechaFinal = request.getParameter("fecha_final");
        int id_usuario = usuario.getId();
        String estado = "abierta";
        boolean existe_titulo;

        String url = request.getRequestURL().toString();
        String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String dir_documentacion = "appData/documentos/" + titulo.toUpperCase();
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        //"/usr/share/tomcat9/webapps/"
        String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");
        String uploads = dir_webapps + dir_documentacion;
        File carpetaUploads = new File(uploads);
        String carpeta = BASE_URL.replace(nombre_proyecto, "") + dir_documentacion;

        LocalDate fechaactual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate fechaInicioDate = LocalDate.parse(fechaInicial, formato);
        LocalDate fechaFinalDate = LocalDate.parse(fechaFinal, formato);

        if (fechaInicioDate.isBefore(fechaactual) || fechaInicioDate.isAfter(fechaFinalDate) || fechaFinalDate.isBefore(fechaInicioDate) || fechaFinalDate.equals(fechaactual)) {
            insert.addProperty("estado", 500);
            return insert;
        }

        if (fechaInicioDate.isAfter(fechaactual) && fechaInicioDate.isBefore(fechaFinalDate)) {
            estado = "programada";
        }

        _publicaciones modelo = new _publicaciones();
        existe_titulo = modelo.buscarTitulo(titulo);

        if (existe_titulo == false) {

            modelo.setId_usuario(id_usuario);
            modelo.setTitulo(titulo);
            modelo.setDescripcion(descripcion);
            modelo.setFecha_inicio(fechaInicial);
            modelo.setFecha_final(fechaFinal);
            modelo.setPrioridad(prioridad);
            modelo.setCarpeta(carpeta);
            modelo.setEstado(estado);
            

            insert = modelo.guardarPublicacion();

            
            if (insert.get("estado").getAsInt() == 200) {
                if (!carpetaUploads.exists()) {
                    carpetaUploads.mkdirs();
                    carpetaUploads.canRead();
                    carpetaUploads.canWrite();
                    carpetaUploads.canExecute();
                }
                insert.addProperty("estado", 200);
            }
            else
            {
                insert.addProperty("estado", insert.get("estado").getAsString());
            }

        } else {
            insert.addProperty("estado", 600);
        }

        return insert;
    }

    public JsonArray listarPublicaciones(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonArray json = new JsonArray();
        _publicaciones modelo = new _publicaciones();
        json = modelo.listarPublicaciones();

        return json;

    }

    public JsonObject eliminarPublicacion(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject json = new JsonObject();
        _publicaciones modelo = new _publicaciones();

        String id = request.getParameter("id");
        String titulo = request.getParameter("titulo");

        String dir_documentacion = "appData/documentos/" + titulo.toUpperCase();
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        //"/usr/share/tomcat9/webapps/"
        String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");
        String uploads = dir_webapps + dir_documentacion;
        File carpeta = new File(uploads);

        modelo.setId(Integer.parseInt(id));
        json = modelo.eliminarPulicacion();

        if (json.get("estado").getAsInt() == 200) {
            borrarCarpetaPublicacion(carpeta);
        }

        return json;
    }

    public void borrarCarpetaPublicacion(File directorio) {
        File[] array = directorio.listFiles();

        if(directorio.exists()){
            
            if (array != null) {
                for (File archivo : array) {
                    archivo.delete();
                }
            }

            directorio.delete();
            
        }
    }

    public JsonObject getPublicacion(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject json = new JsonObject();
        _publicaciones modelo = new _publicaciones();

        String id = request.getParameter("id");

        modelo.setId(Integer.parseInt(id));
        json = modelo.getPublicacion();

        return json;
    }

    public JsonObject updatePublicacion(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject json = new JsonObject();
        _publicaciones modelo = new _publicaciones();
        JsonObject titulo_anterior;
        boolean existe;

        String id = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        String prioridad = request.getParameter("prioridad");
        String descripcion = request.getParameter("descripcion");
        String fecha_inicial = request.getParameter("fecha_inicial");
        String fecha_final = request.getParameter("fecha_final");
        titulo_anterior = modelo.obtenerTitulo(Integer.parseInt(id));

        String url = request.getRequestURL().toString();
        String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String dir_documentacion_nueva = "appData/documentos/" + titulo.toUpperCase();
        String dir_documentacion_anterior = "appData/documentos/" + titulo_anterior.get("titulo").getAsString().toUpperCase();
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        //"/usr/share/tomcat9/webapps/"
        String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");

        String uploads_nueva = dir_webapps + dir_documentacion_nueva;
        String carpeta_nueva = BASE_URL.replace(nombre_proyecto, "") + dir_documentacion_nueva;

        String uploads_anterior = dir_webapps + dir_documentacion_anterior;
        String carpeta_anterior = BASE_URL.replace(nombre_proyecto, "") + dir_documentacion_anterior;

        //carpetas
        File nueva = new File(uploads_nueva);
        File anterior = new File(uploads_anterior);

        if (titulo.equals(titulo_anterior.get("titulo").getAsString())) {
            modelo.setId(Integer.parseInt(id));
            modelo.setTitulo(titulo);
            modelo.setPrioridad(prioridad);
            modelo.setDescripcion(descripcion);
            modelo.setFecha_inicio(fecha_inicial);
            modelo.setFecha_final(fecha_final);
            modelo.setCarpeta(carpeta_anterior);
            json = modelo.updatePublicacion();
        }

        if (!titulo.equals(titulo_anterior.get("titulo").getAsString())) {

            existe = modelo.buscarTitulo(titulo);

            if (existe == false) {

                if (anterior.exists()) {
                    anterior.renameTo(nueva);
                }

                modelo.setId(Integer.parseInt(id));
                modelo.setTitulo(titulo);
                modelo.setPrioridad(prioridad);
                modelo.setDescripcion(descripcion);
                modelo.setFecha_inicio(fecha_inicial);
                modelo.setFecha_final(fecha_final);
                modelo.setCarpeta(carpeta_nueva);
                json = modelo.updatePublicacion();

            } else {
                json.addProperty("estado", 600);
            }
        }

        return json;
    }

    public JsonArray buscarPublicaciones(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonArray json = new JsonArray();
        String palabra = request.getParameter("palabra");

        _publicaciones modelo = new _publicaciones();
        json = modelo.buscadorPublicaciones(palabra);

        return json;

    }

    public JsonArray listarPublicacionesHome(HttpServletRequest request, HttpServletResponse response) throws IOException {

        _publicaciones modelo = new _publicaciones();
        JsonArray json = new JsonArray();
        HttpSession http = request.getSession();
        _usuario usuario = (_usuario) http.getAttribute("usuario");

        int id = usuario.getId();

        String registro_inicio = request.getParameter("index");
        int inicio = Integer.parseInt(registro_inicio);

        String numero_registros = request.getParameter("cantidad");
        int num = Integer.parseInt(numero_registros);

        json = modelo.listarPublicacionesHome(id, inicio, num);
        

        return json;
    }

    public JsonArray buscarPublicacionesHome(HttpServletRequest request, HttpServletResponse response) throws IOException {

        _publicaciones modelo = new _publicaciones();
        JsonArray json = new JsonArray();
        HttpSession http = request.getSession();
        _usuario usuario = (_usuario) http.getAttribute("usuario");

        int id = usuario.getId();
        String palabra = request.getParameter("palabra");
        json = modelo.buscarPublicacionesHome(id, palabra);

        return json;
    }

    public JsonArray filtrarPublicacionesHome(HttpServletRequest request, HttpServletResponse response) throws IOException {

        _publicaciones modelo = new _publicaciones();
        JsonArray json = new JsonArray();
        HttpSession http = request.getSession();
        _usuario usuario = (_usuario) http.getAttribute("usuario");

        int id = usuario.getId();
        String dato = request.getParameter("dato");
        json = modelo.filtrarPublicacionesHome(id, dato);

        return json;
    }

    public void adminPublicaciones(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("./vista/publicaciones/publicaciones.jsp?action=adminPublicaciones");
    }

    public void detalles(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String id = request.getParameter("id");

        request.setAttribute(id, id);
        RequestDispatcher rd = request.getRequestDispatcher("./vista/publicaciones/detalles.jsp?action=detalles");
        rd.forward(request, response);
    }

    public JsonObject tablaDetalles(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject publicacion = new JsonObject();
        JsonObject json = new JsonObject();
        _publicaciones modelo = new _publicaciones();

        String id = request.getParameter("id");
        String fecha_inicio_format = "error al recibir publicacion !!";
        String fecha_final_format = "";
        String fecha_inicio = "";
        String fecha_final = "";
        String prioridad = "";
        String estado = "";
        String titulo_pub = "";
        String prioridad_clase = "";
        String estado_clase = "";

        if (id != null) {

            modelo.setId(Integer.parseInt(id));
            publicacion = modelo.getPublicacion();

            titulo_pub = publicacion.get("titulo").getAsString();
            fecha_inicio_format = publicacion.get("fecha_inicio_format").getAsString();
            fecha_final_format = publicacion.get("fecha_final_format").getAsString();
            fecha_inicio = publicacion.get("fecha_inicio").getAsString();
            fecha_final = publicacion.get("fecha_final").getAsString();
            prioridad = publicacion.get("prioridad").getAsString();
            estado = publicacion.get("estado").getAsString();

            if (prioridad.equals("normal")) {
                prioridad_clase = "icon-neutral";
            }

            if (prioridad.equals("urgente")) {
                prioridad_clase = "icon-danger";
            }

            if (estado.equals("abierta")) {
                estado_clase = "icon-success";
            }

            if (estado.equals("cerrada")) {
                estado_clase = "icon-danger";
            }

            if (estado.equals("programada")) {
                estado_clase = "icon-alert";
            }

            json.addProperty("fecha_inicio_format", fecha_inicio_format);
            json.addProperty("fecha_final_format", fecha_final_format);
            json.addProperty("fecha_inicio", fecha_inicio);
            json.addProperty("fecha_final", fecha_final);
            json.addProperty("prioridad", prioridad);
            json.addProperty("estado", estado);
            json.addProperty("titulo_pub", titulo_pub);
            json.addProperty("prioridad_clase", prioridad_clase);
            json.addProperty("estado_clase", estado_clase);
        } else {
            json.addProperty("estado", 201);
        }

        return json;
    }

    public JsonArray listarArchivos(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonArray json = new JsonArray();
        String nombre_carpeta = request.getParameter("nombre").toUpperCase();

        String url = request.getRequestURL().toString();
        String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String dir_documentacion = "appData/documentos/";
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        //"/usr/share/tomcat9/webapps/"
        String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");

        File carpeta = new File(dir_webapps + dir_documentacion + nombre_carpeta);
        File[] archivos = carpeta.listFiles();

        for (File archivo : archivos) {

            JsonObject jsonArchivos = new JsonObject();

            String nombre_archivo = archivo.getName();
            String url_server = BASE_URL.replace(nombre_proyecto, "") + dir_documentacion + nombre_carpeta + "/" + archivo.getName();

            jsonArchivos.addProperty("nombre", nombre_archivo);
            jsonArchivos.addProperty("url", url_server);
            json.add(jsonArchivos);
        }

        return json;
    }

    public JsonObject eliminarArchivo(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject json = new JsonObject();
        String nombre_carpeta = request.getParameter("carpeta").toUpperCase();
        String nombre = request.getParameter("nombre");

        String url = request.getRequestURL().toString();
        String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String dir_documentacion = "appData/documentos/";
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        //"/usr/share/tomcat9/webapps/"
        String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");

        File archivo = new File(dir_webapps + dir_documentacion + nombre_carpeta + "/" + nombre);
        json.addProperty("estado", 500);

        if (archivo.isFile()) {
            archivo.delete();
            json.addProperty("estado", 200);
        }

        if (archivo.isDirectory()) {
            borrarCarpetaPublicacion(archivo);
            json.addProperty("estado", 200);
        }

        return json;
    }

    public JsonObject cerrarPublicacion(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");

        _publicaciones modelo = new _publicaciones();
        modelo.setId(Integer.parseInt(id));
        modelo.setEstado("cerrada");

        JsonObject update = new JsonObject();
        update = modelo.cerrarPublicacion();

        return update;
    }

    public JsonObject subirDocumento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        JsonObject json = new JsonObject();

        try {

            Part archivo = request.getPart("archivo");
            Path path = Paths.get(archivo.getSubmittedFileName());
            String nombre_archivo = path.getFileName().toString();
            InputStream input = archivo.getInputStream();

            String nombre_carpeta = request.getParameter("carpeta").toUpperCase();
            String dir_carpeta = "appData/documentos/" + nombre_carpeta + "/";
            //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
            //"/usr/share/tomcat9/webapps/"
            String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");
            String documentacion = dir_webapps + dir_carpeta;

            File subida = new File(documentacion, nombre_archivo);

            if (!subida.exists()) {
                Files.copy(input, subida.toPath());
                json.addProperty("estado", 200);
            }
            else
            {
                json.addProperty("estado", 201);
            }

        } catch (Exception e) {

            json.addProperty("estado", e.getMessage());
        }

        return json;
    }

    public JsonObject crearZip(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {        
        
        JsonObject json = new JsonObject();

        String nombre_carpeta = request.getParameter("carpeta").toUpperCase();
        String dir_carpeta = "appData/documentos/" + nombre_carpeta + "/";
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        //"/usr/share/tomcat9/webapps/"
        String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");
        String documentacion = dir_webapps + dir_carpeta;

        File directorio = new File(dir_webapps+dir_carpeta);
        File [] archivos = directorio.listFiles();
        
        
        byte[] buffer = new byte[4096];
      

        try {
            
            FileOutputStream fos = new FileOutputStream(documentacion+nombre_carpeta+".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
                            
            for(File archivo : archivos){
                
                ZipEntry ze = new ZipEntry(archivo.getName());
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(documentacion+archivo.getName());

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                
                in.close();
            }
            
            zos.closeEntry();
            zos.close();
            
            json.addProperty("estado", 200);
            
            
        } catch (IOException ex) {
            
            json.addProperty("estado", ex.getMessage());
        }

        return json;
        
    }
    
    public JsonObject eliminarZip(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {        
        
        JsonObject json = new JsonObject();
        
        String nombre_carpeta = request.getParameter("carpeta").toUpperCase();
        String dir_carpeta = "appData/documentos/" + nombre_carpeta + "/";
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        //"/usr/share/tomcat9/webapps/"
        String dir_webapps = getServletContext().getRealPath("/").replace(nombre_proyecto, "");
        String documentacion = dir_webapps + dir_carpeta;
        
        try {

            File zip = new File(documentacion + nombre_carpeta + ".zip");
            zip.delete();
            json.addProperty("estado", 200);

        } catch (Exception ex) {
            
            json.addProperty("estado", ex.getMessage());
            
        }
        
        return json;
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
