import javax.script.*;
import java.io.*;
import java.util.*;

public class Controller {
    private String modelName;
    private Object model;

    public Controller(String modelName) {
        this.modelName = modelName;
        switch (modelName) {
            case "Model1":
                this.model = new Model1();
                break;
            case "Model2":
                this.model = new Model2();
                break;
            case "MultiAgentSim":
                MultiAgentSim sim = new MultiAgentSim();
                sim.steps = 10;
                sim.addAgent(new Agent(1.0));
                sim.addAgent(new Agent(2.0));
                sim.addAgent(new Agent(3.0));
                this.model = sim;
                break;
            default:
                throw new IllegalArgumentException("Unknown model: " + modelName);
        }
    }

    public Controller readDataFrom(String fname) throws IOException {
        if (model instanceof MultiAgentSim) {
            throw new UnsupportedOperationException("Data reading is not supported for this model.");
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fname);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + fname);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Map<String, List<Double>> data = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            String key = parts[0];
            List<Double> values = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                values.add(Double.parseDouble(parts[i]));
            }
            data.put(key, values);
        }
        reader.close();

        if (data.containsKey("LATA")) {
            if (model instanceof Model1) {
                ((Model1) model).LL = data.get("LATA").size();
            } else if (model instanceof Model2) {
                ((Model2) model).LL = data.get("LATA").size();
            }
        } else {
            throw new IllegalArgumentException("Missing LATA definition in input file.");
        }

        if (model instanceof Model1) {
            Model1 m = (Model1) model;
            m.twKI = fillValues(data.get("twKI"), m.LL);
            m.twKS = fillValues(data.get("twKS"), m.LL);
            m.twINW = fillValues(data.get("twINW"), m.LL);
            m.twEKS = fillValues(data.get("twEKS"), m.LL);
            m.twIMP = fillValues(data.get("twIMP"), m.LL);
            m.KI = fillValues(data.get("KI"), m.LL);
            m.KS = fillValues(data.get("KS"), m.LL);
            m.INW = fillValues(data.get("INW"), m.LL);
            m.EKS = fillValues(data.get("EKS"), m.LL);
            m.IMP = fillValues(data.get("IMP"), m.LL);
        } else if (model instanceof Model2) {
            Model2 m = (Model2) model;
            m.twKI = fillValues(data.get("twKI"), m.LL);
            m.twKS = fillValues(data.get("twKS"), m.LL);
            m.KI = fillValues(data.get("KI"), m.LL);
            m.KS = fillValues(data.get("KS"), m.LL);
            m.INW = fillValues(data.get("INW"), m.LL);
            m.EKS = fillValues(data.get("EKS"), m.LL);
            m.IMP = fillValues(data.get("IMP"), m.LL);
        }

        return this;
    }

    private double[] fillValues(List<Double> values, int length) {
        double[] result = new double[length];
        if (values == null || values.isEmpty()) {
            Arrays.fill(result, 0.0);
            return result;
        }
        for (int i = 0; i < length; i++) {
            result[i] = i < values.size() ? values.get(i) : values.get(values.size() - 1);
        }
        return result;
    }

    public Controller runModel() {
        if (model instanceof MultiAgentSim) {
            ((MultiAgentSim) model).run();
        } else if (model instanceof Model1) {
            ((Model1) model).run();
        } else if (model instanceof Model2) {
            ((Model2) model).run();
        }
        return this;
    }

    public Controller runScriptFromFile(String fname) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("groovy");
        FileReader scriptReader = new FileReader(fname);
        bindVariablesToScript(engine);
        engine.eval(scriptReader);

        if (model instanceof Model1) {
            if (engine.get("GDPGrowth") != null) {
                ((Model1) model).GDPGrowth = (double[]) engine.get("GDPGrowth");
            }
            if (engine.get("ZDEKS") != null) {
                ((Model1) model).ZDEKS = (double[]) engine.get("ZDEKS");
            }
        }

        scriptReader.close();
        return this;
    }

    public Controller runScript(String script) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("groovy");
        bindVariablesToScript(engine);
        engine.eval(script);

        if (model instanceof Model1) {
            if (engine.get("GDPGrowth") != null) {
                ((Model1) model).GDPGrowth = (double[]) engine.get("GDPGrowth");
            }
            if (engine.get("ZDEKS") != null) {
                ((Model1) model).ZDEKS = (double[]) engine.get("ZDEKS");
            }
        }

        return this;
    }

    private void bindVariablesToScript(ScriptEngine engine) {
        if (model instanceof Model1) {
            Model1 m = (Model1) model;
            engine.put("LL", m.LL);
            engine.put("twKI", m.twKI);
            engine.put("twKS", m.twKS);
            engine.put("twINW", m.twINW);
            engine.put("twEKS", m.twEKS);
            engine.put("twIMP", m.twIMP);
            engine.put("KI", m.KI);
            engine.put("KS", m.KS);
            engine.put("INW", m.INW);
            engine.put("EKS", m.EKS);
            engine.put("IMP", m.IMP);
            engine.put("PKB", m.PKB);
        } else if (model instanceof Model2) {
            Model2 m = (Model2) model;
            engine.put("LL", m.LL);
            engine.put("twKI", m.twKI);
            engine.put("twKS", m.twKS);
            engine.put("KI", m.KI);
            engine.put("KS", m.KS);
            engine.put("INW", m.INW);
            engine.put("EKS", m.EKS);
            engine.put("IMP", m.IMP);
        }
    }

    public String getResultsAsTsv() {
        StringBuilder sb = new StringBuilder();

        if (model instanceof Model1) {
            Model1 m = (Model1) model;
            sb.append("LATA\t");
            for (int year = 2015; year < 2015 + m.LL; year++) sb.append(year).append("\t");
            sb.append("\n");
            appendArray(sb, "twKI", m.twKI);
            appendArray(sb, "twKS", m.twKS);
            appendArray(sb, "twINW", m.twINW);
            appendArray(sb, "twEKS", m.twEKS);
            appendArray(sb, "twIMP", m.twIMP);
            appendArray(sb, "KI", m.KI);
            appendArray(sb, "KS", m.KS);
            appendArray(sb, "INW", m.INW);
            appendArray(sb, "EKS", m.EKS);
            appendArray(sb, "IMP", m.IMP);
            appendArray(sb, "PKB", m.PKB);
            if (m.GDPGrowth != null) appendArray(sb, "GDPGrowth (%)", m.GDPGrowth);
            if (m.ZDEKS != null) appendArray(sb, "ZDEKS", m.ZDEKS);
        } else if (model instanceof Model2) {
            Model2 m = (Model2) model;
            appendArray(sb, "Results", m.results);
        } else if (model instanceof MultiAgentSim) {
            MultiAgentSim sim = (MultiAgentSim) model;
            sb.append("Agent States\t");
            for (double state : sim.getAgentStates()) {
                sb.append(String.format("%.2f\t", state));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private void appendArray(StringBuilder sb, String name, double[] array) {
        sb.append(name).append("\t");
        for (double value : array) sb.append(String.format("%.2f\t", value));
        sb.append("\n");
    }
}
