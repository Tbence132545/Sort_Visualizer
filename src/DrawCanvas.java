import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;

class DrawCanvas extends JPanel implements ActionListener {
    private int[] array;
    private Timer timer;
    private int currentIndex = 0;
    private int currentSwap = 0;
    private boolean sortingFinished = false;
    private int currentBlueIndex = 0;
    private String selectedSort ="Bubble Sort";
    private int insertionIndex = 1;
    private int insertionJIndex;
    private boolean isSwapping;
    private int selectionMinIndex;
    public int[] getArray(){
        return this.array;
    }
    public DrawCanvas() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.WHITE);
        array = new int[]{5, 3, 8, 4, 2, 7, 1, 6, 9, 10, 7, 11, 9};
        timer = new Timer(250, this);
    }

    public void startAlgorithm(String selectedAlgorithm) {
        selectedSort = selectedAlgorithm;
        currentIndex = 0;
        currentSwap = 0;
        sortingFinished = false;
        insertionIndex = 1;
        insertionJIndex = insertionIndex - 1;
        isSwapping = false;
        selectionMinIndex = currentIndex;
        System.out.println("Algorithm started");
        if(!timer.isRunning()){
            timer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawArray(g);
    }


    private void drawArray(Graphics g) {
        int width = getWidth() / array.length;
        for (int i = 0; i < array.length; i++) {
            int height = array[i] * 50;
            if (sortingFinished ||
                    (selectedSort.equals("Insertion Sort") && i == insertionIndex) ||
                    (selectedSort.equals("Insertion Sort") && isSwapping && i == insertionJIndex + 1) ||
                    (selectedSort.equals("Bubble Sort") && i == currentSwap) ||
                    (selectedSort.equals("Selection Sort") && i == selectionMinIndex) ||
                    (selectedSort.equals("Selection Sort") && i == currentIndex)) {
                g.setColor(Color.BLUE);
                g.fillRect(i * width, getHeight() - height, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(i * width, getHeight() - height, width, height);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(i * width, getHeight() - height, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(i * width, getHeight() - height, width, height);
            }
            String number = Integer.toString(array[i]);
            int textX = i * width + (width - g.getFontMetrics().stringWidth(number)) / 2;
            int textY = getHeight() - height + height / 2 + g.getFontMetrics().getAscent() / 2;
            g.drawString(number, textX, textY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (selectedSort) {
            case "Insertion Sort":
                insertionSortStep();
                break;
            case "Bubble Sort":
                bubbleSortStep();
                break;
            case "Selection Sort":
                selectionSortStep();
                break;
            case "QuickSort":
                break;
            case "MergeSort":
                break;
        }
        repaint();
    }

    private void insertionSortStep() {
        if (insertionIndex < array.length) {
            if (isSwapping) {
                if (insertionJIndex >= 0 && array[insertionJIndex] > array[insertionJIndex + 1]) {
                    int temp = array[insertionJIndex + 1];
                    array[insertionJIndex + 1] = array[insertionJIndex];
                    array[insertionJIndex] = temp;
                    insertionJIndex--;
                } else {
                    isSwapping = false;
                    insertionIndex++;
                    insertionJIndex = insertionIndex - 1;
                }
            } else {
                isSwapping = true;
            }
        } else {
            timer.stop();
            System.out.println("Insertion Sort finished");
            sortingFinished = true;
            animateSortedArray();
        }
    }

    private void bubbleSortStep() {
        if (currentIndex < array.length - 1) {
            if (currentSwap < array.length - currentIndex - 1) {
                if (array[currentSwap] > array[currentSwap + 1]) {
                    int temp = array[currentSwap];
                    array[currentSwap] = array[currentSwap + 1];
                    array[currentSwap + 1] = temp;
                }
                currentSwap++;
            } else {
                currentSwap = 0;
                currentIndex++;
            }
            repaint();
        } else {
            timer.stop();
            sortingFinished = true;
            animateSortedArray();
        }
    }

    private void selectionSortStep() {
        if (currentIndex < array.length - 1) {
            if (currentSwap < array.length) {
                if (array[currentSwap] < array[selectionMinIndex]) {
                    selectionMinIndex = currentSwap;
                }
                currentSwap++;
            } else {
                // Swap the found minimum element with the first element
                int temp = array[selectionMinIndex];
                array[selectionMinIndex] = array[currentIndex];
                array[currentIndex] = temp;

                // Move to the next element in the array
                currentIndex++;
                currentSwap = currentIndex;
                selectionMinIndex = currentIndex;
            }
        } else {
            timer.stop();
            sortingFinished = true;
            animateSortedArray();
        }
        repaint();
    }

    private void animateSortedArray() {
        timer = new Timer(250, e -> {
            currentBlueIndex++;
            if (currentBlueIndex >= array.length) {
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
    }

}
