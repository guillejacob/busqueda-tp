package frsf.cidisi.exercise.aspiradora.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domain.Habitacion;

import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

/**
 * Represent the internal state of the Agent.
 */
public class EstadoAspiradora extends SearchBasedAgentState {
	
    private List<Habitacion> habitacionesSucias;
    private Habitacion posicion;
    private List<Habitacion> mapaHabitaciones;
    private int energiaDisponible;

    public EstadoAspiradora() {

    	habitacionesSucias = new ArrayList<Habitacion>();
		posicion = new Habitacion();
		mapaHabitaciones = new ArrayList<Habitacion>();
		energiaDisponible = 0;
    	
        this.initState();
    }

    /**
     * This method clones the state of the agent. It's used in the search
     * process, when creating the search tree.
     */
    @Override
    public SearchBasedAgentState clone() {
    	
    	EstadoAspiradora newState = new EstadoAspiradora();
    	
    	//Los atributos de tipo primitvos se pasan por copia
    	newState.setenergiaDisponible(this.getenergiaDisponible());
		//Los atributos que son objetos (los arrays tambi�n son de tipo objeto) se pasan por
    	//referencia; luego, es necesario clonarlos
    	List<Habitacion> newMapaHabitaciones = new ArrayList<Habitacion>();
    	for(Habitacion h : this.getmapaHabitaciones())
    		newMapaHabitaciones.add(h.clone());
    	newState.setmapaHabitaciones(newMapaHabitaciones);
    	
    	//Buscamos en el nuevo mapa las habitaciones sucias para agregarlas a la nueva lista
    	//de habitaciones sucias
    	List<Habitacion> newHabitacionesSucias = new ArrayList<Habitacion>();
    	for(Habitacion h : newMapaHabitaciones)
    		for(Habitacion hs : this.gethabitacionesSucias())
    			if(h.getNombre().equals(hs.getNombre()))
    				newHabitacionesSucias.add(h);
    	newState.sethabitacionesSucias(newHabitacionesSucias);
    	
    	//Este ultimo atributo (la posicion) ya se encuentra en la lista de habitaciones que
    	//representa el mapa! Entonces debemos buscarlo en la lista (la NUEVA!)
    	for(Habitacion h : newMapaHabitaciones)
    		if(h.getNombre().equals(this.getposicion().getNombre()))
    			newState.setposicion(h);
    	
        return newState;
        
    }

    /**
     * This method is used to update the Agent State when a Perception is
     * received by the Simulator.
     */
    @Override
    public void updateState(Perception p) {
    	
    	AspiradoraPerception percepcion = (AspiradoraPerception) p;
    	
    	//En base a la suciedad percibida y la posicion actual, actualizamos (de ser necesario)
    	//la lista de habitaciones sucias.
    	Habitacion posicionActual = this.getposicion();
    	int suciedadPercibida = percepcion.getsuciedad();
    	
    	//Si percibimos suciedad, tenemos que agregar la posicion actual a la lista de
    	//habitaciones sucias (si es que no lo esta ya)
    	if(suciedadPercibida == 1){
    		boolean yaExiste = false;
    		for(Habitacion h : this.gethabitacionesSucias())
    			if(h.getNombre().equals(posicionActual.getNombre()))
    				yaExiste = true;
    		if(!yaExiste)
    			this.gethabitacionesSucias().add(this.getposicion());
    	}
    	//Si no percibimos suciedad, tenemos que remover la posicion actual de la lista de
    	//habitaciones sucias (si es que aun esta en la lista)
    	else{
    		Habitacion habActual = null;
    		for(Habitacion h : this.gethabitacionesSucias())
    			if(h.getNombre().equals(posicionActual.getNombre()))
    				habActual = h;
    		if(habActual != null)
    			this.gethabitacionesSucias().remove(habActual);
    	}
        
    }

    /**
     * This method is optional, and sets the initial state of the agent.
     */
    @Override
    public void initState() {
        //�ste m�todo tambi�n debe tomar los valores del escenario particulat
        
        Habitacion h1 = new Habitacion("h1");
        Habitacion h2 = new Habitacion("h2");
        
        //habitacionesSucias arranca vac�o
        mapaHabitaciones.add(h1);
        mapaHabitaciones.add(h2);
        posicion = h1;
        energiaDisponible = 10;

    }

    /**
     * This method returns the String representation of the agent state.
     */
    @Override
    public String toString() {
        
    	String str = "";
    	
    	str += "Habitaciones del mapa: {";
        for(Habitacion h : mapaHabitaciones)
        	str+= h.toString() + ", ";
        str = str.substring(0,str.length()-2);
        str += "}\n";
        
        str += "Habitaciones actual: "+posicion.toString()+".\n";
        str += "Energia actual: "+energiaDisponible+".\n";
    	
        str += "Habitaciones sucias: {";
        for(Habitacion h : habitacionesSucias)
        	str+= h.toString() + ", ";
        str = str.substring(0,str.length()-2);
        str += "}\n";

        return str;
    }

    /**
     * This method is used in the search process to verify if the node already
     * exists in the actual search.
     */
    @Override
    public boolean equals(Object obj) {
       
    	//Cu�ndo dos estados son iguales lo definimos nosotros
    	//En este caso definimos que dos estado son iguales cuando estamos en la misma
    	//habitaci�n y la lista de habitaciones limpias es la misma (y la lista de visitadas!!)
    	EstadoAspiradora estadoComparado = (EstadoAspiradora) obj;
        
    	boolean mismaPosicion = estadoComparado.getposicion().getNombre().equals(this.getposicion().getNombre());
    	boolean mismasHabitacionesSucias = true;
    	boolean mismasHabitacionesVisitadas = true;
    	mismasHabitacionesSucias = this.gethabitacionesSucias().size() == estadoComparado.gethabitacionesSucias().size();
    	if(mismasHabitacionesSucias){
    		String[] nombresActuales = getArrayOfNames(this.gethabitacionesSucias());
    		String[] nobresComparadas = getArrayOfNames(estadoComparado.gethabitacionesSucias());
    		Arrays.sort(nombresActuales);
    		Arrays.sort(nobresComparadas);
    		for(int i=0;i<nombresActuales.length;i++)
    			if(!(nombresActuales[i].equals(nobresComparadas[i])))
    				mismasHabitacionesSucias = false;
    	}
    	
    	for(int i=0;i < this.getmapaHabitaciones().size();i++){
    		mismasHabitacionesVisitadas = mismasHabitacionesVisitadas &&
    			(this.getmapaHabitaciones().get(i).isVisitada() ==
    				estadoComparado.getmapaHabitaciones().get(i).isVisitada());
    	}
    	
        return (mismaPosicion && mismasHabitacionesSucias && mismasHabitacionesVisitadas);
    }

    // The following methods are agent-specific:
     public List<Habitacion> gethabitacionesSucias(){
        return habitacionesSucias;
     }
     
     public void sethabitacionesSucias(List<Habitacion> arg){
        habitacionesSucias = arg;
     }
     
     public Habitacion getposicion(){
        return posicion;
     }
     
     public void setposicion(Habitacion arg){
        posicion = arg;
     }
     
     public List<Habitacion> getmapaHabitaciones(){
        return mapaHabitaciones;
     }
     
     public void setmapaHabitaciones(List<Habitacion> arg){
        mapaHabitaciones = arg;
     }
     
     public int getenergiaDisponible(){
        return energiaDisponible;
     }
     
     public void setenergiaDisponible(int arg){
        energiaDisponible = arg;
     }
	
     private String[] getArrayOfNames(List<Habitacion> habitaciones){
    	 String[] arrayOfNames = new String[habitaciones.size()];
    	 
    	 for(int i=0;i<habitaciones.size();i++)
    		 arrayOfNames[i] = habitaciones.get(i).getNombre();
    	 
    	 return arrayOfNames;
     }
     
}

