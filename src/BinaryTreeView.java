import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
class BinaryTreeView extends JPanel {

    private int[] array;
    private int maxLevels;
    private int nodeRadius = 20;// Radius of each node
    private int levelHeight = 70;// Radius of each node

    public BinaryTreeView(int[] array) {
        this.array = array;
        this.maxLevels = (int) (Math.log(array.length + 1) / Math.log(2));
    }

    public void updateArray(int[] newArray) {
        this.array = newArray;
        maxLevels = (int) (Math.log(newArray.length + 1) / Math.log(2));
        repaint(); // Ensure the panel is repainted when the array is updated
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        drawTree(g, array, 0, width / 2, 50, width / 4, 0);
    }

    private void drawTree(Graphics g, int[] array, int index, int x, int y, int xOffset, int level) {
        if (index >= array.length) {
            return;
        }

        g.setColor(Color.BLACK);
        g.drawOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius); // Draw oval representing node
        g.drawString(Integer.toString(array[index]), x - 5, y + 5); // Draw value of node

        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;

        if (leftChildIndex < array.length) {
            int leftX = x - xOffset;
            int leftY = y + levelHeight;
            g.drawLine(x, y + nodeRadius, leftX, leftY - nodeRadius); // Draw line to left child
            drawTree(g, array, leftChildIndex, leftX, leftY, xOffset / 2, level + 1);
        }

        if (rightChildIndex < array.length) {
            int rightX = x + xOffset;
            int rightY = y + levelHeight;
            g.drawLine(x, y + nodeRadius, rightX, rightY - nodeRadius); // Draw line to right child
            drawTree(g, array, rightChildIndex, rightX, rightY, xOffset / 2, level + 1);
        }
    }


}
