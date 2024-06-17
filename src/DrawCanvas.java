import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.border.LineBorder;
import java.awt.event.MouseEvent;
import java.util.Random;

class DrawCanvas extends JPanel implements ActionListener {
    private int[] array;
    private Timer timer;
    private int currentIndex = 0;
    private int currentSwap = 0;
    private boolean sortingFinished = false;
    private int currentBlueIndex = 0;
    private String selectedSort = "Bubble Sort";
    private int insertionIndex = 1;
    private int insertionJIndex;
    private boolean isSwapping;
    private int selectionMinIndex;
    private int[] stack;
    private int l = 0;
    private int h;
    private int pivot;
    private int top = -1;
    private int curr_size=1;
    private int left_start=0;
    private int mergeStartIndex = 0;
    private int mergeEndIndex = 0;
    public int[] getArray() {
        return this.array;
    }

    public DrawCanvas() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.WHITE);
        array = new int[50];
        h = array.length - 1;
        stack = new int[h - l + 1];
        Random rnd = new Random();
        for (int i = 0; i < 50; i++) {
            array[i] = rnd.nextInt(1, 100);
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int width = getWidth() / array.length;
                int clickedIndex = e.getX() / width;
                if (clickedIndex >= 0 && clickedIndex < array.length) {
                    String newValueStr = JOptionPane.showInputDialog("Enter new value:");
                    if (newValueStr != null) {
                        try {
                            int newValue = Integer.parseInt(newValueStr);
                            array[clickedIndex] = newValue;
                            repaint();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer.");
                        }
                    }
                }
            }
        });
    }

    public void startAlgorithm(String selectedAlgorithm) {
        if(selectedAlgorithm.equals("Quick Sort") || selectedAlgorithm.equals("Merge Sort")){
            timer = new Timer(150, this);
        }
        else{
            timer = new Timer(15, this);
        }

        selectedSort = selectedAlgorithm;
        currentIndex = 0;
        currentSwap = 0;
        sortingFinished = false;
        insertionIndex = 1;
        insertionJIndex = insertionIndex - 1;
        isSwapping = false;
        selectionMinIndex = currentIndex;
        l = 0;
        h = array.length - 1;
        System.out.println("Algorithm started");
        if (!timer.isRunning()) {
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
            int height = array[i] * 7;
            if (sortingFinished ||
                    (selectedSort.equals("Insertion Sort") && (i == insertionIndex)) ||
                    (selectedSort.equals("Insertion Sort") && isSwapping && (i == (insertionJIndex + 1))) ||
                    (selectedSort.equals("Bubble Sort") && (i == currentSwap)) ||
                    (selectedSort.equals("Selection Sort") && (i == selectionMinIndex)) ||
                    (selectedSort.equals("Selection Sort") && (i == currentIndex)) ||
                    (selectedSort.equals("Quick Sort")&&  ((i == h) || (i==l)))||
                    (selectedSort.equals("Merge Sort") && (i == left_start - 1 || i == left_start + curr_size - 1)))
             {
                g.setColor(Color.BLUE);
                g.fillRect(i * width, getHeight() - height, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(i * width, getHeight() - height, width, height);
            }
            else if(selectedSort.equals("Quick Sort") && i==pivot){
                g.setColor(Color.GREEN);
                g.fillRect(i * width, getHeight() - height, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(i * width, getHeight() - height, width, height);

            }
            else {
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
            case "Quick Sort":
                quickSortStep();
                break;
            case "Merge Sort":
                mergeSortStep();
                break;
        }
        repaint();
    }
    private void mergeSortStep() {
        if (curr_size <= array.length) {
            if (left_start <= array.length - curr_size) {
                int mid = Math.min(left_start + curr_size - 1, array.length - 1);
                int right_end = Math.min(left_start + 2 * curr_size - 1, array.length - 1);
                merge(left_start, mid, right_end);
                left_start += curr_size * 2;
            } else {
                curr_size = 2 * curr_size;
                left_start = 0; // Reset left_start for the next pass
            }
        } else {
            timer.stop();
            System.out.println("Merge Sort finished");
            sortingFinished = true;
            animateSortedArray();
        }
    }



    private void merge(int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int L[] = new int[n1];
        int R[] = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = array[l + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = array[m + 1 + j];
        }

        int i = 0, j = 0;
        int k = l;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }
        mergeStartIndex = l;
        mergeEndIndex = r;
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

    private void quickSortStep() {
        if (!sortingFinished) {
            if (top < 0) { // If the stack is empty, initialize it
                stack[++top] = l;
                stack[++top] = h;
            }
            if (top >= 0) { // If the stack is not empty, perform a single step of quicksort
                h = stack[top--];
                l = stack[top--];

                pivot = array[h];
                int i = (l - 1);

                for (int j = l; j <= h - 1; j++) {
                    if (array[j] <= pivot) {
                        i++;
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }

                int temp = array[i + 1];
                array[i + 1] = array[h];
                array[h] = temp;
                int p = i + 1;

                if (p - 1 > l) { // Push the left subarray boundaries to the stack
                    stack[++top] = l;
                    stack[++top] = p - 1;
                }

                if (p + 1 < h) { // Push the right subarray boundaries to the stack
                    stack[++top] = p + 1;
                    stack[++top] = h;
                }

                if (top < 0) { // If the stack is empty, sorting is finished
                    sortingFinished = true;
                    animateSortedArray(); // Initiate animation of the sorted array
                }
            }
        }
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
