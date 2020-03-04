<%@page import="banca.logic.Usuario"%>
<% Usuario usuario=  new Usuario("111","111",1); /* null;*/  %>

<header>
    <div class="logo">
        <span>Banco Santander</span>
        <img src="/Proyecto 1 Javier Amador Carlos - Gutierrez/images/logo.png">
    </div> 
    <div class="menu">
        <ul> 
              <li>
                <a href="/Proyecto 1 Javier Amador Carlos - Gutierrez/presentation/Index.jsp">Inicio</a>
              </li>
                        <% if (usuario!=null){ %>
                <li>
                  <a href="/Proyecto 1 Javier Amador Carlos - Gutierrez/presentation/cliente/cuentas/View.jsp">Cuentas</a>
                  <ul>  <!--submenu --> </ul>
                </li>                        
                <li >
                  <a  href="#">User:<%=usuario.getCedula()%></a>
                  <ul>  <!--submenu --> </ul>
                </li> 
                        <% } %>
                        <% if (usuario==null){%>
                <li>
                  <a href="/Proyecto 1 Javier Amador Carlos - Gutierrez/presentation/login/show">Login</a>
                </li>            
                        <% }%>             
            </ul>
    </div>
  </header>          

