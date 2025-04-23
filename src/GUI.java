
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GUI {
    private JFrame frame;
    private JTable table;
    private Controller controller;
    private JList<String> modelList;
    private JList<String> dataList;
    private Map<String, String> dataFiles;

    public GUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        controller = new Controller("Model1");
        frame = new JFrame("Modelling framework sample");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        dataFiles = new HashMap<>();
        dataFiles.put("data1.txt", "data1.txt");
        dataFiles.put("data2.txt", "data2.txt");

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Left Panel with model and data selection
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel selectionPanel = new JPanel(new GridLayout(1, 2));

        // Model Selection Column
        JPanel modelPanel = new JPanel(new BorderLayout());
        modelPanel.add(new JLabel("Select model"), BorderLayout.NORTH);
        modelList = new JList<>(new String[]{"Model1", "Model2", "MultiAgentSim"});
        JScrollPane modelScrollPane = new JScrollPane(modelList);
        modelScrollPane.setPreferredSize(new Dimension(150, 300));
        modelPanel.add(modelScrollPane, BorderLayout.CENTER);

        // Data Selection Column
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.add(new JLabel("Select data"), BorderLayout.NORTH);
        dataList = new JList<>(dataFiles.keySet().toArray(new String[0]));
        JScrollPane dataScrollPane = new JScrollPane(dataList);
        dataScrollPane.setPreferredSize(new Dimension(150, 300));
        dataPanel.add(dataScrollPane, BorderLayout.CENTER);

        selectionPanel.add(modelPanel);
        selectionPanel.add(dataPanel);
        leftPanel.add(selectionPanel, BorderLayout.CENTER);

        // Add Run Model Button
        JButton runModelButton = new JButton("Run model");
        runModelButton.setBackground(new Color(255, 182, 193));
        runModelButton.addActionListener(e -> runModel());
        leftPanel.add(runModelButton, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Table for displaying results
        table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel for additional buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton runScriptButton = new JButton("Run script from file");
        runScriptButton.setBackground(new Color(64, 224, 208));
        runScriptButton.addActionListener(e -> runScriptFromFile());
        buttonsPanel.add(runScriptButton);

        JButton adhocScriptButton = new JButton("Create and run ad hoc script");
        adhocScriptButton.setBackground(new Color(147, 112, 219));
        adhocScriptButton.addActionListener(e -> createAndRunScript());
        buttonsPanel.add(adhocScriptButton);

        // Add buttons panel below the table
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void runModel() {
        try {
            String modelName = modelList.getSelectedValue();
            String dataFile = dataList.getSelectedValue();

            if (modelName == null) {
                JOptionPane.showMessageDialog(frame, "Please select a model.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            controller = new Controller(modelName);

            // If MultiAgentSim is selected, skip reading data
            if (!"MultiAgentSim".equals(modelName) && dataFile == null) {
                JOptionPane.showMessageDialog(frame, "Please select a data file.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!"MultiAgentSim".equals(modelName)) {
                controller.readDataFrom(dataFile).runModel();
            } else {
                controller.runModel();
            }

            updateTable(controller.getResultsAsTsv());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runScriptFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.runScriptFromFile(fileChooser.getSelectedFile().getAbsolutePath());
                updateTable(controller.getResultsAsTsv());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createAndRunScript() {
        JTextArea textArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        int result = JOptionPane.showConfirmDialog(frame, scrollPane, "Ad hoc script", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String script = textArea.getText();
            if (script != null && !script.trim().isEmpty()) {
                try {
                    controller.runScript(script);
                    updateTable(controller.getResultsAsTsv());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void updateTable(String tsvData) {
        String[] lines = tsvData.split("\n");
        String[] columns = lines[0].split("\t");
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split("\t");
            tableModel.addRow(values);
        }
        table.setModel(tableModel);
    }
}
