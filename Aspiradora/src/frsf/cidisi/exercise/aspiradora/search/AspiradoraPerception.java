package frsf.cidisi.exercise.aspiradora.search;

import domain.Habitacion;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;

public class AspiradoraPerception extends Perception {

    public static int UNKNOWN_PERCEPTION = -1;   
	private int suciedad;

    public  AspiradoraPerception() {
    	//Aqu� crearimos los objetos, listas, arrays que componen la percepci�n
    	suciedad = UNKNOWN_PERCEPTION;
    }

    public AspiradoraPerception(Agent agent, Environment environment) {
        super(agent, environment);
    }

    /**
     * This method initializa a perception of the agent.
     */
    @Override
    public void initPerception(Agent agentIn, Environment environmentIn) {
    	
        Aspiradora agent = (Aspiradora) agentIn;
        AmbienteAspiradora environment = (AmbienteAspiradora) environmentIn;
        EstadoAmbiente environmentState = environment.getEnvironmentState();
       
        //Aqu� creamos la percepci�n inicial del agente
        //Pimero chequeamos que la habitaci�n en la que est� el agente est� sucia
        Habitacion h = environmentState.getposicionAspiradora();
        boolean estaSucia = environmentState.gethabitacionesSucias().contains(h);
        
        //Si lo est�, el valor de la percepci�n ser� 1; en caso contrario ser� 0
        if(estaSucia)
        	suciedad = 1;
        else
        	suciedad = 0;
        
    }
    
    @Override
    public String toString() {
        String str = "";
        
        str += "La habitaci�n est� ";
        str += (suciedad == 1)?"sucia.":"limpia.";

        return str.toString();
    }

    // The following methods are agent-specific:	
     public int getsuciedad(){
        return suciedad;
     }
     
     public void setsuciedad(int arg){
        this.suciedad = arg;
     }
	
   
}
