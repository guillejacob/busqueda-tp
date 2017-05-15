package frsf.cidisi.exercise.modelo.search.actions;
import java.util.List;

import domain.Calle;
import domain.Esquina;
import frsf.cidisi.exercise.modelo.search.*;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class IrAnodoX extends SearchAction {

	private Esquina nodoX;
	
    public IrAnodoX(Esquina nodoX) {
		super();
		this.nodoX = nodoX;
	}
	/**
     * This method updates a tree node state when the search process is running.
     * It does not updates the real world state.
     */
	
    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {
        EstadoPatrullero agentState = (EstadoPatrullero) s;
        
        Esquina posicionAgente = agentState.getposicionAgente();
        List<Esquina> esquinasAdyacentes = agentState.getMapa().getAdyacentes(posicionAgente);
        List<Calle> callesCortadas = agentState.getCallesCortadas();
        
        if(esquinasAdyacentes.contains(nodoX))//Ver si anda bien el contains!
        {
        	//ver si la calle que conecta el nodo actual con el nodo x no esta cortada
        	Calle calleEntreEsquinas = null; //TODO: agentState.getCalle(posicionAgente, nodoX);
        	if(!callesCortadas.contains(calleEntreEsquinas)){
        		return null;
        	}
        }
        return null;
    }

    /**
     * This method updates the agent state and the real world state.
     */
    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {
        EstadoAmbiente environmentState = (EstadoAmbiente) est;
        EstadoPatrullero agState = ((EstadoPatrullero) ast);

        // TODO: Use this conditions
        // PreConditions: null
        // PostConditions: null
        
        if (true) {
            // Update the real world
            
            // Update the agent state
            
            return environmentState;
        }

        return null;
    }

    /**
     * This method returns the action cost.
     */
    @Override
    public Double getCost() {
        return new Double(0);
    }

    /**
     * This method is not important for a search based agent, but is essensial
     * when creating a calculus based one.
     */
    @Override
    public String toString() {
        return "IrAnodoX";
    }
}