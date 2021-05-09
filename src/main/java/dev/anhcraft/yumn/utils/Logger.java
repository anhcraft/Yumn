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
    public static final List<String> LOG_QUEUE = new ArrayList<>();
    public static final Object LOCK = new Object();
    public static final List<Logger> loggers = new ArrayList<>();
    private static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat DATE = new SimpleDateFormat("dd-MM-yyyy");
    private static final Calendar CALENDAR = Calendar.getInstance();
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

    public String id;
    public Logger(String id) {
        this.id = id;
        loggers.add(this);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void printQueue() {
        synchronized (LOCK) {
            if (LOG_QUEUE.isEmpty()) return;
            Date dt = new Date();
            CALENDAR.setTime(dt);
            File dir = new File(Yumn.getInstance().getDataFolder(), "logs" + File.separator + CALENDAR.get(Calendar.MONTH) + "-" + CALENDAR.get(Calendar.YEAR));
            dir.mkdirs();
            String date = Logger.DATE.format(dt);
            File file;
            if (date.equals(lastDate)) {
                file = new File(dir, lastVersion == 0 ? (date + ".txt") : (date + " (" + (lastVersion) + ").txt"));
            } else {
                file = new File(dir, date + ".txt");
                lastVersion = 0;
            }
            int i = 0;
            while (true) {
                if (file.exists()) {
                    if (file.length() > 1000000) {
                        file = new File(dir, date + " (" + (++i) + ").txt");
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
                for (String s : LOG_QUEUE) {
                    fw.write(s);
                    fw.write(System.lineSeparator());
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOG_QUEUE.clear();
        }
    }

    public void log(String str) {
        if (!bukkitSide) {
            System.out.println("[#" + id + " " + FULL_DATE.format(new Date()) + "] " + str);
            return;
        }
        synchronized (LOCK) {
            LOG_QUEUE.add("[#" + id + " " + FULL_DATE.format(new Date()) + "] " + str);
        }
    }

    public void logf(String format, Object... objects) {
        if (!bukkitSide) {
            System.out.println("[#" + id + " " + FULL_DATE.format(new Date()) + "] " + String.format(format, objects));
            return;
        }
        synchronized (LOCK) {
            LOG_QUEUE.add("[#" + id + " " + FULL_DATE.format(new Date()) + "] " + String.format(format, objects));
        }
    }
}
