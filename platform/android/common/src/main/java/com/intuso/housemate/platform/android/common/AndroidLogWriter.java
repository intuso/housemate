package com.intuso.housemate.platform.android.common;

import android.util.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 08:54
 * To change this template use File | Settings | File Templates.
 */
public class AndroidLogWriter extends LogWriter {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final String tag;

    /**
     * Create a new log writer
     *
     * @param level the level to filter at
     */
    public AndroidLogWriter(LogLevel level, String tag) {
        super(level);
        this.tag = tag;
    }

    @Override
    protected void _write(LogLevel level, String message, Throwable t) {
        switch (level) {
            case DEBUG:
                Log.d(tag, DATE_FORMAT.format(new Date()) + SEPARATOR + level + SEPARATOR + message, t);
                break;
            case WARN:
                Log.w(tag, DATE_FORMAT.format(new Date()) + SEPARATOR + level + SEPARATOR + message, t);
                break;
            case ERROR:
                Log.e(tag, DATE_FORMAT.format(new Date()) + SEPARATOR + level + SEPARATOR + message, t);
                break;
            case FATAL:
                Log.e(tag, DATE_FORMAT.format(new Date()) + SEPARATOR + level + SEPARATOR + message, t);
                break;
            case INFO:
                Log.i(tag, DATE_FORMAT.format(new Date()) + SEPARATOR + level + SEPARATOR + message, t);
                break;
        }
    }
}
