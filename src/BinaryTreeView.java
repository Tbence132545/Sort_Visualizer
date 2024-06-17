import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BinaryTreeView extends JPanel implements ActionListener {

    private int[] array;
    private int maxLevels;
    private int nodeRadius = 20; // Radius of each node
    private int levelHeight = 70; // Height between levels
    private Timer timer;
    private int heapSize;
    private boolean isHeapBuilt = false;
    private int swapIndex1 = -1; // Indices of nodes being swapped
    private int swapIndex2 = -1; // Indices of nodes being swapped
    private int heapifyIndex = -1; // Index of the node being heapified
    private boolean swapped = false; // Indicates if nodes were swapped in the last step
    private boolean heapifying = false; // Indicates if heapify process is ongoing
    private boolean sortEnded = false; // Indicates if sorting is complete

    public BinaryTreeView(int[] array) {
        this.array = array;
        this.maxLevels = (int) (Math.log(array.length + 1) / Math.log(2));
        this.heapSize = array.length;

        // Timer to perform sorting steps
        timer = new Timer(15, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (heapSize > 0) {
            if (heapifying) {
                // Perform heapify step
                heapifyStep(heapifyIndex);
            } else if (swapped) {
                // After a swap, start heapifying from the root
                heapifying = true;
                heapifyIndex = 0;
            } else {
                // Perform swap and prepare for heapify
                heapSortStep();
                swapped = true;
            }
        } else {
            sortEnded = true;
            timer.stop();
        }
        repaint();
    }

    public void updateArray(int[] newArray) {
        this.array = newArray;
        maxLevels = (int) (Math.log(newArray.length + 1) / Math.log(2));
        this.heapSize = newArray.length;
        isHeapBuilt = false;
        sortEnded = false;
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

        // Set color based on the node's current state
        if (sortEnded) {
            g.setColor(Color.BLUE); // Highlight all nodes when sorting is complete
            g.fillOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
        } else if (index == swapIndex1 || index == swapIndex2) {
            g.setColor(Color.BLUE); // Highlight the nodes being swapped
            g.fillOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
        } else if (index == heapifyIndex) {
            g.setColor(Color.GREEN); // Highlight the node being heapified
            g.fillOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
        }
        g.setColor(Color.BLACK);
        g.drawOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
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

    private void heapSortStep() {
        if (!isHeapBuilt) {
            buildMaxHeap();
            isHeapBuilt = true;
        } else {
            swapIndex1 = 0;
            swapIndex2 = heapSize - 1;
            swap(swapIndex1, swapIndex2);
            heapSize--;
        }
    }

    private void buildMaxHeap() {
        for (int i = heapSize / 2 - 1; i >= 0; i--) {
            maxHeapify(i);
        }
    }

    private void maxHeapify(int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < heapSize && array[left] > array[largest]) {
            largest = left;
        }

        if (right < heapSize && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(i, largest);
            maxHeapify(largest);
        }
    }

    private void heapifyStep(int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < heapSize && array[left] > array[largest]) {
            largest = left;
        }

        if (right < heapSize && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != i) {
            swapIndex1 = i;
            swapIndex2 = largest;
            swap(i, largest);
            heapifyIndex = largest; // Move to the next node to heapify
        } else {
            // Heapify process for this node is complete
            swapIndex1 = -1;
            swapIndex2 = -1;
            heapifyIndex = -1;
            heapifying = false;
            swapped = false;
        }
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public void startSorting() {
        timer.start();
    }

    public void stopSorting() {
        timer.stop();
    }
}