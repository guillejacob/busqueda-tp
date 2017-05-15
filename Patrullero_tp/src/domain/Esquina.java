package domain;

/**
 * @author Leandro
 *
 */
public class Esquina {
	
	String nombre;
	
	
	public Esquina(String nombre) {
		this.nombre = nombre;
	}
	
	public String toString() {
		return "Esq."+nombre;
	}
	
	public boolean Equals(Esquina obj){
		if( this.nombre.equals(obj.nombre)){
			return true;
		}
		return false;
	}
}
