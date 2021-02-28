package thaumicenergistics.integration.appeng.grid;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IItemList;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainer;

import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;
import thaumicenergistics.util.AEUtil;
import thaumicenergistics.util.EssentiaFilter;

/**
 * Wraps a IAspectContainer for use by a ME system
 * <p>
 * Used by Essentia Storage Bus
 *
 * @author BrockWS
 */
public class EssentiaContainerAdapter implements IMEInventoryHandler<IAEEssentiaStack> {

    private IAspectContainer container;
    private EssentiaFilter config;

    public EssentiaContainerAdapter(IAspectContainer container, EssentiaFilter config) {
        this.container = container;
        this.config = config;
    }

    @Override
    public IAEEssentiaStack injectItems(IAEEssentiaStack input, Actionable type, IActionSource src) {
        if (input == null || !input.isMeaningful())
            return input;

        if (this.container.doesContainerAccept(input.getAspect())) { // Check if container accepts it
            // Add to container to see how much it can store
            int notAdded = this.container.addToContainer(input.getAspect(), (int) input.getStackSize());
            if (type == Actionable.SIMULATE) // Annoying hack, maybe talk with Azanor about getting some type of simulation instead
                this.container.takeFromContainer(input.getAspect(), (int) input.getStackSize() - notAdded);
            if (notAdded > 0) // Didn't add it all
                return input.setStackSize(notAdded);
            return null;
        }

        return input;
    }

    @Override
    public IAEEssentiaStack extractItems(IAEEssentiaStack request, Actionable mode, IActionSource src) {
        if (request == null || !request.isMeaningful())
            return null;
        if (this.container.containerContains(request.getAspect()) <= 0) // Make sure the container actually contains it
            return null;

        Aspect aspect = request.getAspect();
        int max = (int) Math.min(this.container.containerContains(aspect), request.getStackSize());

        if (mode == Actionable.SIMULATE)
            return AEUtil.getAEStackFromAspect(aspect, max);

        boolean worked = this.container.takeFromContainer(aspect, max);
        if (!worked)
            return null;

        return request.setStackSize(max);
    }

    @Override
    public IItemList<IAEEssentiaStack> getAvailableItems(IItemList<IAEEssentiaStack> out) {
        if (this.container == null)
            return out;
        for (Aspect aspect : this.container.getAspects().getAspects())
            out.add(AEUtil.getAEStackFromAspect(aspect, this.container.containerContains(aspect)));
        return out;
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEEssentiaStack input) {
        return false; // Maybe check if container instanceof TileJar and check if jar has a label with same aspect
    }

    @Override
    public boolean canAccept(IAEEssentiaStack input) {
        if (this.container == null)
            return false;
        return this.container.doesContainerAccept(input.getAspect()) && this.config.isInFilter(input.getAspect());
    }

    @Override
    public int getPriority() {
        // TODO: StorageBus Priority
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return true; // TODO: Verify requirement
    }

    @Override
    public IStorageChannel<IAEEssentiaStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class);
    }
}
