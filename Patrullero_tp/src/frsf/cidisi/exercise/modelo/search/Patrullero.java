package frsf.cidisi.exercise.modelo.search;

import frsf.cidisi.exercise.modelo.search.actions.IrAnodoX;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.Problem;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgent;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.solver.search.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import domain.Esquina;
import domain.Mapa;

public class Patrullero extends SearchBasedAgent {
	
	private int searchStrategy;
	public  static final int PROFUNDIDAD = 1;
	public  static final int ANCHURA = 2;
	public  static final int COSTO_UNIFORME = 3;
	public static final int A_ASTERISCO = 4;

    public Patrullero(Mapa mapa) {
    	
    	//Se selecciona la estrategia
    	searchStrategy = 4;

        // The Agent Goal
        ObjetivoPatrullero agGoal = new ObjetivoPatrullero();

        // The Agent State
        EstadoPatrullero agState = new EstadoPatrullero(mapa);
        this.setAgentState(agState);

        // Create the operators
        Vector<SearchAction> operators = new Vector<SearchAction>();
        
        //iteraci�n de la lista de las esquinas del mapa y creaci�n de IrAnodoX(Esquina esquina) con cada una de las esquinas
        List<Esquina> esquinas = new ArrayList<Esquina>(mapa.getGrafo().getVertices());
        for(Esquina esquina : esquinas){
        	operators.addElement(new IrAnodoX(esquina));
        }
        	
        // Create the Problem which the agent will resolve
        Problem problem = new Problem(agGoal, agState, operators);
        this.setProblem(problem);
    }

    /**
     * This method is executed by the simulator to ask the agent for an action.
     */
    @Override
    public Action selectAction() {
    	
    	 // Create the search strategy
        Strategy strategy = null;
        switch(searchStrategy){
		     	case(PROFUNDIDAD): 
		     		/*if ( ((EstadoAgente) this.getAgentState()).getRamaExpandidaAObjetivo() != null){
		     			EstadoAgente estadoAgenteClonado = (EstadoAgente) ((EstadoAgente) this.getAgentState()).clone();
		     			Vector<NTree> vectorRama =((EstadoAgente) this.getAgentState()).getRamaExpandidaAObjetivo();
		     			SearchAction accionRamaExpandida = vectorRama.remove(0).getAction();
		     			if(accionRamaExpandida.execute(estadoAgenteClonado)!= null)
		     				
		     				return accionRamaExpandida;
		     		}*/
		     		strategy= new DepthFirstSearch();
		     		break;
		     	case(ANCHURA):
		     		strategy = new BreathFirstSearch();
		     		break;
		     	case(COSTO_UNIFORME):
		     		IStepCostFunction cost = new UniformCostFunction();
		     		strategy = new UniformCostSearch(cost);  
		     		break;
		     	case(A_ASTERISCO):
		     		strategy = new AStarSearch(new CostFunction(), new Heuristic());
		     		break;
     	}
        

        // Create a Search object with the strategy
        Search searchSolver = new Search(strategy);

        /* Generate an XML file with the search tree. It can also be generated
         * in other formats like PDF with PDF_TREE */
        searchSolver.setVisibleTree(Search.EFAIA_TREE);

        // Set the Search searchSolver.
        this.setSolver(searchSolver);

        // Ask the solver for the best action
        Action selectedAction = null;
        try {       	
            selectedAction = this.getSolver().solve(new Object[]{this.getProblem()});
            
        } catch (Exception ex) {
            Logger.getLogger(Patrullero.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Return the selected action
        return selectedAction;

    }

    /**
     * This method is executed by the simulator to give the agent a perception.
     * Then it updates its state.
     * @param p
     */
    @Override
    public void see(Perception p) {
        this.getAgentState().updateState(p);
    }
}
