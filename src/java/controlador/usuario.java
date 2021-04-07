/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo._usuario;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author fernando
 */
//cuando se mandan imagenes se debe poner el @multipartConfig
@MultipartConfig
@WebServlet(name = "usuario", urlPatterns = {"/usuario"})
public class usuario extends HttpServlet {

    //router de los metodos
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

            switch (action) {

                case "inicio":
                    out.print(verificar_login(request, response));
                    break;

                case "cerrar_sesion":
                    cerrar_sesion(request, response);
                    break;

                case "registro":
                    form_registro(request, response);
                    break;

                case "guardar_usuario":
                    out.print(guardar_usuario(request, response));
                    break;

                case "cambiar_pwd":
                    form_password(request, response);
                    break;

                case "verificarCorreoCambioPwd":
                    out.print(verificarCorreoCambioPwd(request, response));
                    break;

                case "verificarCodigoCambioPwd":
                    out.print(verificarCodigoCambioPwd(request, response));
                    break;

                case "resetCodigo":
                    out.print(resetCodigo(request, response));
                    break;

                case "cambiarPwd":
                    out.print(cambiarPwd(request, response));
                    break;

                case "editarPerfil":
                    form_editar_perfil(request, response);
                    break;

                case "updatePerfil":
                    out.print(updatePerfil(request, response));
                    break;

                case "adminUsuarios":
                    adminUsuarios(request, response);
                    break;

                case "listarUsuarios":
                    out.print(listarUsuarios(request, response));
                    break;

                case "eliminarUsuario":
                    out.print(eliminarUsuario(request, response));
                    break;
                    
                case "buscarUsuario":
                    out.print(buscarUsuario(request, response));
                    break;

                default:
                    not_found(request, response);
                    break;

            }
        }
    }

    //Metodos auxiliares del controlador para ejecutarlos en servlet (arriba)
    public JsonObject verificar_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usuario = request.getParameter("correo");
        String clave = request.getParameter("clave");

        _usuario modelo = new _usuario();
        modelo.setCorreo(usuario);
        modelo.setClave(clave);

        JsonObject data = modelo.verificar_login();
        HttpSession http = request.getSession();;

        switch (data.get("estado").getAsInt()) {
            case 201:
                http.setAttribute("login_user_notfound", true);
                response.sendRedirect("./");
                break;
            case 202:
                http.setAttribute("login_pwd_error", true);
                response.sendRedirect("./");
                break;
            case 200:
                //objeto para la sesion
                _usuario objusuario = new _usuario();
                objusuario.setId(data.get("id").getAsInt());
                objusuario.setNombre(data.get("nombre").getAsString());
                objusuario.setPaterno(data.get("paterno").getAsString());
                objusuario.setMaterno(data.get("materno").getAsString());
                objusuario.setCorreo(data.get("correo").getAsString());
                objusuario.setImg(data.get("img").getAsString());
                objusuario.setRol(data.get("rol").getAsString());
                http.setAttribute("usuario", objusuario);
                response.sendRedirect("./vista/home/?action=home");
                break;
        }

        return data;

    }

    public JsonObject guardar_usuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //instancias
        _usuario modelo = new _usuario();
        JsonObject json = new JsonObject();

        //datos y variables
        //cuando se mandan imagenes se debe poner el @multipartConfig hasta arriba del servlet
        String nombre = request.getParameter("nombre").toUpperCase();
        String paterno = request.getParameter("paterno").toUpperCase();
        String materno = request.getParameter("materno").toUpperCase();
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");
        String rol = "usuario";
        String claveBcrypt = BCrypt.hashpw(clave, BCrypt.gensalt(10));

        Part foto = request.getPart("perfil");
        Path path = Paths.get(foto.getSubmittedFileName());
        String nombre_archivo = path.getFileName().toString();
        InputStream input = foto.getInputStream();
        String[] extension = {".ico", ".png", ".jpg", ".svg+xml", ".svg", ".jpeg"};

        boolean fileisvalid = false;
        boolean existe_usuario = modelo.buscarNombreApellidos(nombre, paterno, materno);

        //rutas a utilizar
        String url = request.getRequestURL().toString();
        String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String nombre_proyecto = "plataforma_archivos/";
        String src_img_perfil = BASE_URL + "public/res/profile-user.svg";
        String dir_usuario = "appData/uploads/" + nombre + "_" + paterno + "_" + materno + "/";
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        String dir_webapps = "/usr/share/tomcat9/webapps/";
        String uploads_usuario = dir_webapps + dir_usuario;

        //si el usuario no existe entonces
        if (existe_usuario == false) {

            //ver que el archivo sea uno permitido
            if (input != null) {
                for (String ext : extension) {
                    if (nombre_archivo.toLowerCase().endsWith(ext)) {
                        src_img_perfil = BASE_URL.replace(nombre_proyecto, "") + dir_usuario + nombre_archivo;
                        fileisvalid = true;
                        break;
                    }
                }
            }

            //si la clave contiene la palabra newadmin creara el usuario de tipo admin
            //esto en caso de que el usuario root principal sea olvidado y no haya forma de recuperarlo
            //ESTO SOLO LO DEBE SABER LOS SUPERVISORES
            if (clave.contains("newadmin")) {
                rol = "admin";
            }

            //guardar en el modelo y llamar la funcion para insertar 
            modelo.setNombre(nombre);
            modelo.setPaterno(paterno);
            modelo.setMaterno(materno);
            modelo.setCorreo(correo);
            modelo.setClave(claveBcrypt);
            modelo.setRol(rol);
            modelo.setImg(src_img_perfil);
            modelo.setCodigoSeguridad(RandomString(5));
            json = modelo.registrar_usuario();

            if (json.get("estado").getAsInt() == 200) {
                //si inserta creamos el directorio y movemos la foto de perfil siempre y cuando sea valido y esxista
                File usuario = new File(uploads_usuario);
                if (!usuario.exists()) {
                    usuario.mkdirs();
                    usuario.canRead();
                    usuario.canWrite();
                    usuario.canExecute();
                }

                if (fileisvalid == true) {
                    File archivo = new File(uploads_usuario, nombre_archivo);
                    Files.copy(input, archivo.toPath());
                }

            }

        } else {

            json.addProperty("estado", 600);
        }

        return json;
    }

    public JsonObject verificarCorreoCambioPwd(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String correo = request.getParameter("correo");
        boolean existe;
        JsonObject enviado;

        _usuario modelo = new _usuario();
        JsonObject json = new JsonObject();

        modelo.setCorreo(correo);
        existe = modelo.correoExiste();

        if (existe == true) {
            enviado = email(correo);
            json.addProperty("correoEnviado", enviado.get("estado").getAsInt());
        }

        if (existe == false) {
            json.addProperty("correoEnviado", 201);
        }

        return json;
    }

    public JsonObject email(String correo) {

        String destinatario = correo;
        final String username = "pruebassoftware96@gmail.com";
        final String password = "54321halo";
        String codigo = RandomString(5);
        _usuario modelo = new _usuario();
        JsonObject json = new JsonObject();
        JsonObject update;

        //Get the session object  
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);

        //compose the message  
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject("Sistema de archivos - depto juridico");
            message.setText("Hola, tu codigo para reestablecer la contraseña es [ " + codigo + " ]");

            // Send message  
            //update codigo en la db
            modelo.setCorreo(correo);
            modelo.setCodigoSeguridad(codigo);
            update = modelo.asignarCodigo();

            //si se guardo el codigo en la db lo enviamos si no solo mandamos el estado de error que haya salido en la funcion del modelo
            if (update.get("estado").getAsInt() == 200) {
                Transport.send(message);
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", update.get("estado").getAsInt());
            }

        } catch (MessagingException mex) {
            json.addProperty("estado", mex.getMessage());
        }

        return json;
    }

    public JsonObject verificarCodigoCambioPwd(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String codigo_enviado = request.getParameter("codigo");
        JsonObject codigodb;

        _usuario modelo = new _usuario();
        modelo.setCodigoSeguridad(codigo_enviado);
        codigodb = modelo.verificarCodigodb();

        return codigodb;
    }

    public JsonObject resetCodigo(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String correo = request.getParameter("correo");
        String codigo = RandomString(5);

        _usuario modelo = new _usuario();
        modelo.setCorreo(correo);
        modelo.setCodigoSeguridad(codigo);
        JsonObject asignarCodigo = modelo.asignarCodigo();

        return asignarCodigo;
    }

    public JsonObject cambiarPwd(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String correo = request.getParameter("correo");
        String clave = request.getParameter("pwd");
        String claveBcrypt = BCrypt.hashpw(clave, BCrypt.gensalt(10));
        JsonObject cambiarPwd;

        _usuario modelo = new _usuario();
        modelo.setCorreo(correo);
        modelo.setClave(claveBcrypt);

        cambiarPwd = modelo.cambiarPwd();

        return cambiarPwd;

    }

    public JsonObject updatePerfil(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //instancias
        _usuario modelo = new _usuario();

        //datos y variables
        //cuando se mandan imagenes se debe poner el @multipartConfig hasta arriba del servlet
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");
        String claveBcrypt = BCrypt.hashpw(clave, BCrypt.gensalt(10));
        Part foto = request.getPart("perfil");
        Path path = Paths.get(foto.getSubmittedFileName());
        String nombre_archivo = path.getFileName().toString();
        InputStream input = foto.getInputStream();
        String[] extension = {".ico", ".png", ".jpg", ".svg+xml", ".svg", ".jpeg"};
        JsonObject update;
        String src_img_perfil = "";
        boolean fileisvalid = false;

        //obtener datos de la sesion
        HttpSession http = request.getSession();
        _usuario sesion = (_usuario) http.getAttribute("usuario");

        int id_sesion = sesion.getId();
        String nombre_sesion = sesion.getNombre();
        String paterno_sesion = sesion.getPaterno();
        String materno_sesion = sesion.getMaterno();
        String img_sesion = sesion.getImg();

        //rutas a utilizar
        String url = request.getRequestURL().toString();
        String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String nombre_proyecto = "plataforma_archivos/";
        String dir_usuario = "appData/uploads/" + nombre_sesion.toUpperCase() + "_" + paterno_sesion.toUpperCase() + "_" + materno_sesion.toUpperCase() + "/";
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        String dir_webapps = "/usr/share/tomcat9/webapps/";
        String uploads_usuario = dir_webapps + dir_usuario;
        String nombre_foto_anterior = img_sesion.replace(BASE_URL.replace(nombre_proyecto, "") + dir_usuario, "");
        String ruta_foto_anterior = uploads_usuario + nombre_foto_anterior;
        File fotoAnterior = new File(ruta_foto_anterior);

        //ver que el archivo sea uno permitido
        if (input != null) {
            for (String ext : extension) {
                if (nombre_archivo.toLowerCase().endsWith(ext)) {
                    src_img_perfil = BASE_URL.replace(nombre_proyecto, "") + dir_usuario + nombre_archivo;
                    fileisvalid = true;
                    fotoAnterior.delete();
                    break;
                }
            }
        }

        //asignar datos del formulario y de la sesion para encontrar el usuario y actualizar
        modelo.setId(id_sesion);
        modelo.setCorreo(correo);
        modelo.setClave(claveBcrypt);
        if (src_img_perfil.equals("")) {
            modelo.setImg(img_sesion);
        } else {
            modelo.setImg(src_img_perfil);
        }
        update = modelo.updatePerfil();

        if (update.get("estado").getAsInt() == 200) {

            if (fileisvalid == true) {
                File archivo = new File(uploads_usuario, nombre_archivo);
                Files.copy(input, archivo.toPath());
            }

        }

        //refrescar la sesion con los nuevos datos
        sesion.setCorreo(correo);
        if (src_img_perfil.equals("")) {
            sesion.setImg(img_sesion);
        } else {
            sesion.setImg(src_img_perfil);
        }
        http.setAttribute("usuario", sesion);

        return update;
    }

    public JsonArray listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonArray json;
        _usuario modelo = new _usuario();
        json = modelo.listarUsuarios();

        return json;
    }

    public JsonObject eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {

        _usuario modelo = new _usuario();

        String id = request.getParameter("id");
        modelo.setId(Integer.parseInt(id));

        JsonObject getNombreyApellidos = modelo.getNombreyApellidos();

        String nombre = getNombreyApellidos.get("nombre").getAsString();
        String paterno = getNombreyApellidos.get("paterno").getAsString();
        String materno = getNombreyApellidos.get("materno").getAsString();

        String nombre_proyecto = "plataforma_archivos/";
        String dir_usuario = "appData/uploads/" + nombre + "_" + paterno + "_" + materno;
        //getServletContext().getRealPath("/").replace(nombre_proyecto, "")
        String dir_webapps = "/usr/share/tomcat9/webapps/";
        String uploads_usuario = dir_webapps + dir_usuario;
        JsonObject eliminar = modelo.eliminarUsuario();

        if (eliminar.get("estado").getAsInt() == 200) {
            File directorio = new File(uploads_usuario);
            borrarCarpetaUsuario(directorio);
        }

        return eliminar;
    }
    
    public void borrarCarpetaUsuario(File directorio)
    {
        File[] array = directorio.listFiles();
                
        if (array != null) {                   
            for (File archivo : array) {
                archivo.delete();
            }
        }

        directorio.delete();
    }
    
    public JsonArray buscarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String palabra = request.getParameter("usuario");
        JsonArray json;
        _usuario modelo = new _usuario();
        json = modelo.buscarUsuario(palabra);
        
        return json;
    }

    public void form_editar_perfil(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("./vista/usuario/form_editar_perfil.jsp?action=editar_perfil");
    }

    public void adminUsuarios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("./vista/usuario/adminUsuarios.jsp?action=adminUsuarios");
    }

    public void not_found(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("vista/error/404.jsp").include(request, response);
    }

    public void form_registro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("./vista/usuario/form_usuario.jsp?action=crear_usuario");
    }

    public void form_password(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("./vista/usuario/form_password.jsp?action=cambiar_contraseña");
    }

    public void cerrar_sesion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("./");
        HttpSession http = request.getSession();
        http.removeAttribute("usuario");
        http.invalidate();
    }

    public String RandomString(int length) {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();

        if (length < 1) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);
        }

        return sb.toString();
    }

    //metodos doget y dopost
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
