package org.vinz243.chiseledit;

import mod.chiselsandbits.api.ChiselsAndBitsAddon;
import mod.chiselsandbits.api.IChiselAndBitsAPI;
import mod.chiselsandbits.api.IChiselsAndBitsAddon;

@ChiselsAndBitsAddon
public class ChiselAPIAccess implements IChiselsAndBitsAddon {
    public static IChiselAndBitsAPI apiInstance;

    @Override
    public void onReadyChiselsAndBits(IChiselAndBitsAPI api) {
        apiInstance = api;
    }


}
