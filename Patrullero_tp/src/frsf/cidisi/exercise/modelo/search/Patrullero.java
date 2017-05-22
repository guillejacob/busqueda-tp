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
import java.util.List;
import java.util.Vector;

import domain.Esquina;

public class Patrullero extends SearchBasedAgent {

    public Patrullero() {

        // The Agent Goal
        ObjetivoPatrullero agGoal = new ObjetivoPatrullero();

        // The Agent State
        EstadoPatrullero agState = new EstadoPatrullero();
        this.setAgentState(agState);

        // Create the operators
        Vector<SearchAction> operators = new Vector<SearchAction>();
        //iteraci�n de la lista de las esquinas del mapa y creaci�n de IrAnodoX(Esquina esquina) con cada una de las esquinas
        List<Esquina> esquinas = (List<Esquina>) agState.getMapa().getGrafo().getVertices();
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
        IStepCostFunction cost = new UniformCostFunction();
        UniformCostSearch strategy = new UniformCostSearch(cost);          

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
            selectedAction =
                    this.getSolver().solve(new Object[]{this.getProblem()});
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
