import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;

public class AlgorithmVisualizer extends JFrame {
    private DrawCanvas currentCanvas;
    private BinaryTreeView binaryTreeView;

    public AlgorithmVisualizer() {
        setTitle("Algorithm Visualizer");
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel);

        JPanel controlPanel = new JPanel();
        panel.add(controlPanel, BorderLayout.SOUTH);

        JButton startButton = new JButton("Start");
        controlPanel.add(startButton);
        JButton resetButton = new JButton("Reset");
        controlPanel.add(resetButton);

        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Heap Sort"};
        JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);
        controlPanel.add(algorithmComboBox);

        currentCanvas = new DrawCanvas();
        currentCanvas.setBorder(new LineBorder(Color.BLACK, 2));  // Add a border to the canvas
        panel.add(currentCanvas, BorderLayout.CENTER);

        binaryTreeView = new BinaryTreeView(currentCanvas.getArray()); // Initially empty array
        binaryTreeView.setPreferredSize(new Dimension(800, 800));
        binaryTreeView.setVisible(false); // Initially hide the Binary Tree View
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
                if (selectedAlgorithm.equals("Heap Sort")) {
                    panel.remove(currentCanvas);
                    panel.add(binaryTreeView, BorderLayout.CENTER); // Add Binary Tree View to the center
                    binaryTreeView.updateArray(currentCanvas.getArray()); // Update array in binary tree view
                    binaryTreeView.setVisible(true);
                    binaryTreeView.startSorting();// Show binary tree view when heap sort is selected
                } else {
                    panel.remove(binaryTreeView);
                    panel.add(currentCanvas, BorderLayout.CENTER); // Add canvas to the center
                    currentCanvas.setVisible(true); // Show canvas for other sorting algorithms
                    binaryTreeView.setVisible(false); // Hide binary tree view for other sorting algorithms
                }
                currentCanvas.startAlgorithm(selectedAlgorithm); // Start selected algorithm
                panel.revalidate(); // Update the UI
                panel.repaint(); // Repaint the panel
            }
        });
        resetButton.addActionListener(l -> {
            panel.remove(currentCanvas);
            currentCanvas = new DrawCanvas();
            currentCanvas.setVisible(true); // Show canvas
            binaryTreeView.setVisible(false); // Hide the Binary Tree View
            panel.revalidate(); // Update the UI
            panel.repaint(); // Repaint the panel
            panel.add(currentCanvas);
            binaryTreeView.updateArray(currentCanvas.getArray()); // Update array in binary tree view
        });

        algorithmComboBox.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            if (selectedAlgorithm.equals("Heap Sort")) {
                panel.remove(currentCanvas);
                panel.add(binaryTreeView, BorderLayout.CENTER); // Add Binary Tree View to the center
                binaryTreeView.updateArray(currentCanvas.getArray()); // Update array in binary tree view
                binaryTreeView.setVisible(true); // Show binary tree view when heap sort is selected
            } else {
                panel.remove(binaryTreeView);
                panel.add(currentCanvas, BorderLayout.CENTER); // Add canvas to the center
                currentCanvas.setVisible(true); // Show canvas for other sorting algorithms
                binaryTreeView.setVisible(false); // Hide binary tree view for other sorting algorithms
            }

            panel.revalidate(); // Update the UI
            panel.repaint(); // Repaint the panel
        });
    }
}
