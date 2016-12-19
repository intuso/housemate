package com.intuso.housemate.client.api.internal.type;

import com.intuso.housemate.client.api.internal.annotation.Id;

/**
 * Enumeration of the days of the week
 */
@Id(value = "day", name = "Day", description = "Day of the week")
public enum Day {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;
}
