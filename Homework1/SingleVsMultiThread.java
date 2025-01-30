package Homework1;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
public class SingleVsMultiThread {
    private static final int SIZE = 2_000_000;
    private static final int DIVISOR = 21;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        int[] numbers = generateRandomNumbers(SIZE);

        // Однопоточное решение
        long startTime = System.nanoTime();
        int singleThreadResult = countValidNumbers(numbers, 0, SIZE);
        long singleThreadTime = System.nanoTime() - startTime;
        System.out.println("Single thread result: " + singleThreadResult);
        System.out.println("Single thread time: " + singleThreadTime / 1_000_000 + "ms");

        // Двухпоточное решение
        AtomicInteger multiThreadResult = new AtomicInteger();
        int mid = SIZE / 2;

        Thread t1 = new Thread(() -> multiThreadResult.addAndGet(countValidNumbers(numbers, 0, mid)));
        Thread t2 = new Thread(() -> multiThreadResult.addAndGet(countValidNumbers(numbers, mid, SIZE)));

        startTime = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long multiThreadTime = System.nanoTime() - startTime;

        System.out.println("Multi thread result: " + multiThreadResult.get());
        System.out.println("Multi thread time: " + multiThreadTime / 1_000_000 + "ms");

        // Сравнение результатов
        System.out.println("Results match:" + (singleThreadResult == multiThreadResult.get()));
    }

    private static int[] generateRandomNumbers(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = RANDOM.nextInt(Integer.MAX_VALUE);
        }
        return arr;
    }

    private static int countValidNumbers(int[] numbers, int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (numbers[i] % DIVISOR == 0 && containsDigit(numbers[i], 3)) {
                count++;
            }
        }
        return count;
    }

    private static boolean containsDigit(int number, int digit) {
        while (number > 0) {
            if (number % 10 == digit) {
                return true;
            }
            number /= 10;
        }
        return false;
    }
}
