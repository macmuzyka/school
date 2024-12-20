package com.school.service.utils.filetype;

import com.itextpdf.kernel.colors.DeviceRgb;

enum CellColoring {
    ORDINAL_NUMBER_COLUMN(new DeviceRgb(163, 209, 245)),
    STUDENT_COLUMN(new DeviceRgb(208, 238, 17)),
    SUBJECT_COLUMN(new DeviceRgb(166, 215, 91)),
    GRADES_COLUMN_SWITCH_0(new DeviceRgb(25, 132, 197)),
    GRADES_COLUMN_SWITCH_1(new DeviceRgb(72, 181, 196)),
    AVERAGE_GRADE_COLUMN(new DeviceRgb(163, 209, 245));

    private final DeviceRgb rgbColor;

    CellColoring(DeviceRgb rgbColor) {
        this.rgbColor = rgbColor;
    }

    public DeviceRgb rgb() {
        return rgbColor;
    }
}
