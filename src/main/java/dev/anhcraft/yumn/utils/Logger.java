package dev.anhcraft.yumn.utils;

import dev.anhcraft.yumn.Yumn;
import org.bukkit.Material;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Logger {
    public static final List<String> logQueue = new ArrayList<>();
    public static final Object LOCK = new Object();
    private static final SimpleDateFormat full_date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
    private static final Calendar calendar = Calendar.getInstance();
    private static boolean bukkitSide = true;
    private static String lastDate;
    private static int lastVersion;

    static {
        try {
            Material.CHEST.createBlockData();
        } catch (Exception e) {
            bukkitSide = false;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void printQueue() {
        synchronized (LOCK) {
            if(logQueue.isEmpty()) return;
            Date dt = new Date();
            calendar.setTime(dt);
            File dir = new File(Yumn.getInstance().getDataFolder(), "logs" + File.separator + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR));
            dir.mkdirs();
            String date = Logger.date.format(dt);
            File file;
            if(date.equals(lastDate)) {
                file = new File(dir, lastVersion == 0 ? (date + ".txt") : (date + " ("+(lastVersion)+").txt"));
            } else {
                file = new File(dir, date + ".txt");
                lastVersion = 0;
            }
            int i = 0;
            while (true) {
                if(file.exists()) {
                    if(file.length() > 1000000) {
                        file = new File(dir, date + " ("+(++i)+").txt");
                        continue;
                    }
                } else {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            lastDate = date;
            lastVersion = i;
            try {
                FileWriter fw = new FileWriter(file, true);
                for (String s : logQueue) {
                    fw.write(s);
                    fw.write(System.lineSeparator());
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logQueue.clear();
        }
    }

    public static final List<Logger> loggers = new ArrayList<>();
    public String id;

    public Logger(String id){
        this.id = id;
        loggers.add(this);
    }

    public void log(String format, Object... objects){
        if(!bukkitSide) {
            System.out.println("[#"+id+" "+ full_date.format(new Date())+"] " + String.format(format, objects));
            return;
        }
        synchronized (LOCK) {
            logQueue.add("[#"+id+" "+ full_date.format(new Date())+"] " + String.format(format, objects));
        }
    }
}
