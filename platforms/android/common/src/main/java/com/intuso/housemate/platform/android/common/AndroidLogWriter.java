package com.intuso.housemate.platform.android.common;

import android.util.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 08:54
 * To change this template use File | Settings | File Templates.
 */
public class AndroidLogWriter extends LogWriter {

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
    protected void _write(LogLevel level, String message) {
        switch (level) {
            case DEBUG:
                Log.d(tag, message);
                break;
            case WARN:
                Log.w(tag, message);
                break;
            case ERROR:
                Log.e(tag, message);
                break;
            case FATAL:
                Log.e(tag, message);
                break;
            case INFO:
                Log.i(tag, message);
                break;
        }
    }
}
