import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Constants.timeToException = new ConcurrentHashMap<>();
        int n =1;
        int x =5;

        ExecutorService pool = Executors.newFixedThreadPool(Math.min(x,n));
        String[] fileList = {"logs1.txt"};
        int size = Math.min(x,n);
        for(int j=0;j<n;j=j+x) {
            for (int i = 0; i < size; i++) {
                pool.execute(new ParseLogFiles(fileList[j++]));
            }
        }
        pool.shutdown();
        try {
            pool.awaitTermination(20, TimeUnit.SECONDS);

            for(ConcurrentHashMap.Entry<String,ConcurrentHashMap<String,Integer>> me: Constants.timeToException.entrySet())
            {
                for(ConcurrentHashMap.Entry<String,Integer> temp : me.getValue().entrySet())
                System.out.println(me.getKey() + " " + temp.getKey() + " " + temp.getValue() );

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

