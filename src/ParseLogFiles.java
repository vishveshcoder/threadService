import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class ParseLogFiles  implements Runnable{

    String fileName;

    public ParseLogFiles(String fileName)
    {
        this.fileName = fileName;
    }

    public void run()
    {
        try{

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.readLine();

            while(br.readLine()!=null) {
                String line = br.readLine();
                String[] h = line.split(" ");
                Calendar nowGMT = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                Timestamp ts = new Timestamp(Long.parseLong(h[1]));

                int minutes = ts.getMinutes();
                int count =-15;
                while(minutes>=0) {
                    minutes -=15;
                    count+=15;
                    if(minutes<0) {
                        String s = Integer.toString(ts.getHours()) + ":" + Integer.toString(count);
                        String s1 = "-" + Integer.toString(ts.getHours()) + ":" + Integer.toString(count+15);
                        s +=s1;
                        if(Constants.timeToException.get(s)==null)
                            Constants.timeToException.put(s,new ConcurrentHashMap<>());

                        Constants.timeToException.get(s).compute(h[2],(key,val)->
                                val==null?1:val+1
                        );

                    }
                }
            }

        }catch(Exception e){System.out.println(e);}

    }
}
