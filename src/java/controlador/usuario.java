/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
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
                    verificar_login(request, response);
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

                default:
                    not_found(request, response);
                    break;

            }
        }
    }

    //Metodos auxiliares del controlador para ejecutarlos en servlet (arriba)
    public void verificar_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usuario = request.getParameter("correo");
        String clave = request.getParameter("clave");

        _usuario modelo = new _usuario();
        modelo.setCorreo(usuario);
        modelo.setClave(clave);

        HashMap data = modelo.verificar_login();
        HttpSession http = request.getSession();

        if (data.size() <= 1 && data.get("estado").equals(201)) {
            http.setAttribute("login_user_notfound", true);
            response.sendRedirect("./");
        } else if (data.size() <= 1 && data.get("estado").equals(202)) {
            http.setAttribute("login_pwd_error", true);
            response.sendRedirect("./");
        } else {
            http.setAttribute("user_data", data);
            response.sendRedirect("./vista/home/?action=home");
        }

    }

    public JsonObject guardar_usuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //instancias
        _usuario modelo = new _usuario();
        Gson gson = new Gson();
        HashMap map = new HashMap();

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
        String nombre_archivo = RandomString(5) + path.getFileName().toString();
        InputStream input = foto.getInputStream();
        String[] extension = {".ico", ".png", ".jpg", ".svg+xml", ".svg", ".jpeg"};

        boolean fileisvalid = false;
        boolean existe_usuario = modelo.buscarNombreApellidos(nombre, paterno, materno);
        JsonObject json;
        HashMap insert;

        //rutas a utilizar
        String url = request.getRequestURL().toString();
        String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
        String src_img_perfil = BASE_URL + "/public/res/profile-user.svg";
        String dir_usuario = "appData/uploads/" + nombre + "_" + paterno + "_" + materno + "/";
        //getServletContext().getRealPath("/").replace("plataforma_archivos/", "")
        String dir_webapps = "/usr/share/tomcat9/webapps/";
        String uploads_usuario = dir_webapps + dir_usuario;

        //si el usuario no existe entonces
        if (existe_usuario == false) {

            //ver que el archivo sea uno permitido
            if (input != null) {
                for (String ext : extension) {
                    if (nombre_archivo.toLowerCase().endsWith(ext)) {
                        src_img_perfil = BASE_URL + dir_usuario + nombre_archivo;
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
            insert = modelo.registrar_usuario();

            if (insert.get("estado").equals(200)) {
                //si inserta creamos el directorio y movemos la foto de perfil siempre y cuando sea valido y esxista
                File usuario = new File(uploads_usuario);
                if (!usuario.exists()) {
                    usuario.mkdirs();
                }

                if (fileisvalid == true) {
                    File archivo = new File(uploads_usuario, nombre_archivo);
                    Files.copy(input, archivo.toPath());
                }

            }

            json = gson.toJsonTree(insert).getAsJsonObject();

        } else {
            map.put("estado", 600);
            json = gson.toJsonTree(map).getAsJsonObject();
        }

        return json;
    }

    public JsonObject verificarCorreoCambioPwd(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String correo = request.getParameter("correo");
        JsonObject json;
        boolean existe;
        HashMap enviado;

        HashMap estado = new HashMap();
        _usuario modelo = new _usuario();
        Gson gson = new Gson();

        modelo.setCorreo(correo);
        existe = modelo.correoExiste();

        if (existe == true) {
            enviado = email(correo);
            estado.put("correoEnviado", enviado.get("estado"));
        }

        if (existe == false) {
            estado.put("correoEnviado", 201);
        }

        json = gson.toJsonTree(estado).getAsJsonObject();

        return json;
    }

    public HashMap email(String correo) {

        String destinatario = correo; 
        final String username = "pruebassoftware96@gmail.com";
        final String password  = "54321halo"; 
        String codigo = RandomString(5);
        HashMap estado = new HashMap();
        _usuario modelo = new _usuario();
        HashMap update;

        //Get the session object  
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,new javax.mail.Authenticator() {
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
            message.setText("Hola, tu codigo para reestablecer la contraseña es [ "+codigo+" ]");

            // Send message  
            
            //update codigo en la db
            modelo.setCorreo(correo);
            modelo.setCodigoSeguridad(codigo);
            update = modelo.asignarCodigo();
            
            //si se guardo el codigo en la db lo enviamos si no solo mandamos el estado de error que haya salido en la funcion del modelo
            if(update.get("estado").equals(200)){
                Transport.send(message);
                estado.put("estado",200);
            }
            else
            {
                estado.put("estado",update.get("estado"));
            }

        } catch (MessagingException mex) {
            estado.put("estado",mex.getMessage());
        }
        
        return estado;
    }

    public JsonObject verificarCodigoCambioPwd(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
  
        String codigo_enviado = request.getParameter("codigo");
        HashMap codigodb;
        JsonObject json;
        Gson gson = new Gson();
        
        _usuario modelo = new _usuario();
        modelo.setCodigoSeguridad(codigo_enviado);
        codigodb = modelo.verificarCodigodb();
        
        json = gson.toJsonTree(codigodb).getAsJsonObject();
        
        return json;
    }
    
    public JsonObject resetCodigo(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        String correo = request.getParameter("correo");
        String codigo = RandomString(5);
        JsonObject json;
        Gson gson = new Gson();
        
        _usuario modelo = new _usuario();
        modelo.setCorreo(correo);
        modelo.setCodigoSeguridad(codigo);
        HashMap asignarCodigo = modelo.asignarCodigo();
        
        json = gson.toJsonTree(asignarCodigo).getAsJsonObject();
        
        return json;
    }
    
    public JsonObject cambiarPwd(HttpServletRequest request, HttpServletResponse response) throws IOException{
        
        String correo = request.getParameter("correo");
        String clave = request.getParameter("pwd");
        String claveBcrypt = BCrypt.hashpw(clave, BCrypt.gensalt(10));
        HashMap cambiarPwd;
        JsonObject json;
        Gson gson = new Gson();
        
        _usuario modelo = new _usuario();
        modelo.setCorreo(correo);
        modelo.setClave(claveBcrypt);
        
        cambiarPwd = modelo.cambiarPwd();
        json = gson.toJsonTree(cambiarPwd).getAsJsonObject();
        
        return json;
        
    }

    public void form_registro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("./vista/usuario/form_usuario.jsp?action=crear_usuario");
    }

    public void form_password(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("./vista/usuario/form_password.jsp?action=cambiar_contraseña");
    }

    public void not_found(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("vista/error/404.jsp").include(request, response);
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
