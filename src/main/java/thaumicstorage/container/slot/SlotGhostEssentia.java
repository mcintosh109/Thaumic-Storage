package thaumicenergistics.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import thaumcraft.api.aspects.Aspect;

import thaumicenergistics.util.AEUtil;
import thaumicenergistics.util.EssentiaFilter;

/**
 * @author BrockWS
 */
public class SlotGhostEssentia extends SlotGhost {

    private EssentiaFilter filter;

    public SlotGhostEssentia(EssentiaFilter filter, IInventory inventory, int index, int xPosition, int yPosition, int groupID) {
        super(inventory, index, xPosition, yPosition, groupID);
        this.filter = filter;
    }

    public SlotGhostEssentia(EssentiaFilter filter, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.filter = filter;
    }

    public EssentiaFilter getFilter() {
        return this.filter;
    }

    public Aspect getAspect() {
        return this.getFilter().getAspect(this.getSlotIndex());
    }

    @Override
    public ItemStack getStack() {
        if (this.getAspect() != null)
            return AEUtil.getAEStackFromAspect(this.getAspect(), 0).asItemStackRepresentation();
        return ItemStack.EMPTY;
    }
}
