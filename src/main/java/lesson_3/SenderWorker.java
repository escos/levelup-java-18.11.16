package lesson_3;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class SenderWorker extends Thread {
    private PriorityQueue<String> queue;
    private PrintWriter writer;
    private boolean alive;

    public SenderWorker(PrintWriter writer) {
        this.writer = writer;
        queue = new PriorityQueue<String>();
    }

    @Override
    public void run() {
        alive = true;
        while (alive) {
            if (!queue.isEmpty()) {
                String message = queue.poll();
                writer.println(message);
                writer.flush();
            } else {
                Thread.yield();
            }
        }
    }

    public void addMessage(String message) {
        queue.add(message);
    }

    public void stopWorker() {
        alive = false;
    }
}