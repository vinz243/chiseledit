package org.vinz243.tesa.helpers;

import net.minecraft.util.text.TextComponentString;

public class StringComponent extends TextComponentString {
    public StringComponent(String msg, Object... objects) {
        super(String.format(msg, objects));
    }
}
