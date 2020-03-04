package banca.logic;

public class Usuario  implements java.io.Serializable {


     private String cedula;
     private String clave;
     private Integer tipo;
     private String apellidos;
     private String telefono;


    public Usuario() {
    }
	
    public Usuario(String cedula) {
        this.cedula = cedula;
    }

    public Usuario(String cedula, String clave, Integer tipo) {
        this.cedula = cedula;
        this.clave = clave;
        this.tipo = tipo;
    }
    
    

    public Usuario(String cedula, String clave, Integer tipo, String apellidos, String telefono) {
        this.cedula = cedula;
        this.clave = clave;
        this.tipo = tipo;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }
    
    
   
    public String getCedula() {
        return this.cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    public Integer getTipo() {
        return this.tipo;
    }
    
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    

}


