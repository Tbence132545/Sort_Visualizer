import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
public class AlgorithmVisualizer extends JFrame {
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

        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort"};
        JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);
        controlPanel.add(algorithmComboBox);

        DrawCanvas canvas = new DrawCanvas();
        canvas.setBorder(new LineBorder(Color.BLACK, 2));  // Add a border to the canvas
        panel.add(canvas, BorderLayout.CENTER);
        resetButton.addActionListener(l -> {
            panel.remove(canvas);
            DrawCanvas canvas2 = new DrawCanvas();
            canvas2.setBorder(new LineBorder(Color.BLACK, 2));
            panel.add(canvas2, BorderLayout.CENTER);
            panel.revalidate(); // Update the UI
            panel.repaint(); // Repaint the panel


        });
        startButton.addActionListener(e -> {
            System.out.println("Start button clicked");
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            canvas.startAlgorithm(selectedAlgorithm);
        });
    }

}