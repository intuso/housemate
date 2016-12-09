package com.intuso.housemate.client.real.api.internal.type;

import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;

/**
 * Enumeration of the days of the week
 */
@TypeInfo(id = "day", name = "Day", description = "Day of the week")
public enum Day {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;
}
