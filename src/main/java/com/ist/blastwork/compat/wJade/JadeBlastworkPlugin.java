package com.ist.blastwork.compat.wJade;

import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeBlastworkPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ShowExplosiveDataProvider.INSTANCE, ExplosiveBarrelBlock.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ShowExplosiveDataProvider.INSTANCE, ExplosiveBarrelBlock.class);
    }
}
