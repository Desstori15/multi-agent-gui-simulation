import java.util.ArrayList;
import java.util.List;

class MultiAgentSim {
    @Bind public int steps;
    private List<Agent> agents;

    public MultiAgentSim() {
        this.agents = new ArrayList<>();
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void run() {
        for (int step = 0; step < steps; step++) {
            for (Agent agent : agents) {
                agent.act();
            }
        }
    }

    public List<Double> getAgentStates() {
        List<Double> states = new ArrayList<>();
        for (Agent agent : agents) {
            states.add(agent.getState());
        }
        return states;
    }
}

class Agent {
    private double state;

    public Agent(double initialState) {
        this.state = initialState;
    }

    public void act() {
        state += Math.random() - 0.5;
    }

    public double getState() {
        return state;
    }
}

