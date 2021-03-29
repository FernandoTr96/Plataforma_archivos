<%

String action = request.getParameter("action");
String title = "";

if(action != null){ title = action; } else { title = "Iniciar sesión"; };

%>