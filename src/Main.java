import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {AlgorithmVisualizer visualizer = new AlgorithmVisualizer(); visualizer.setVisible(true);});

    }

}