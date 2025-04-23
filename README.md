# multi-agent-gui-simulation

##  Multi-Agent and Economic Model Simulator with Java GUI

An educational simulation framework for running and comparing multiple economic and agent-based models through a Java Swing graphical interface.  
Supports dynamic data input, script injection (Groovy), and multi-agent behavior tracking.

---

##  Features

-  **Switchable Models**: Run either `Model1`, `Model2`, or a `MultiAgentSim`  
-  **TSV Output**: Results presented in tabular view with exportable TSV format  
-  **Script Execution**: Load and run Groovy scripts (external or ad hoc)  
-  **Dynamic Inputs**: Read input `.txt` files (preformatted)  
-  **Multi-Agent Simulation**: Basic agent interaction with step-based behavior  
-  **Modern Swing GUI**: Model selection, data file selection, and table view

---

##  Project Structure

```
multi-agent-gui-simulation/
├── Main.java              # Application entry point
├── Controller.java        # Main controller for model and script logic
├── GUI.java               # Swing interface for user interaction
├── Model1.java            # Basic economic simulation model (GDP, exports, etc.)
├── Model2.java            # Simplified economic index calculator
├── MultiAgentSim.java     # Simulation with multiple agents
├── Agent.java             # Agent class with basic `act()` behavior
├── script1.groovy         # Example Groovy script
├── data1.txt              # Example input dataset 1
└── data2.txt              # Example input dataset 2
```


---

##  Technologies Used

- Java 8+  
- Java Swing (`JFrame`, `JPanel`, `JTable`, etc.)  
- Groovy (for scripting via `javax.script.ScriptEngine`)  
- MVC-like separation (GUI ↔ Controller ↔ Models)  
- Java IO (BufferedReader, FileReader, etc.)

---

##  Input Data Format

Expected format in `.txt` files:

LATA 2015 2016 2017 twKI 1.1 1.2 1.3 KI 100 110 120 ...


*Make sure the file contains all required variables depending on the selected model.*

---

##  How to Run

```bash
# 1. Clone the repo
git clone https://github.com/Desstori15/multi-agent-gui-simulation.git
cd multi-agent-gui-simulation

# 2. Compile the project
javac *.java

# 3. Run the app
java Main
```



 Example Usage
Select Model1, Model2, or MultiAgentSim from the left panel
Choose a dataset (data1.txt) — only for Model1 and Model2
Click Run model
(Optional) Load a script or write an ad hoc one to manipulate data
View results in the center table


 What I Learned
Swing-based GUI application design
Embedding scripting languages (Groovy) into Java
Designing extensible simulation frameworks
Multi-agent system principles
Reading structured input files in Java


👨‍💻 Author
Vladislav Dobriyan
GitHub: @Desstori15
