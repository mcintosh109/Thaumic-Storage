package thaumicenergistics.client.gui.crafting;

import java.io.IOException;
import java.util.Objects;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import appeng.client.gui.implementations.GuiCraftConfirm;
import appeng.core.localization.GuiText;

import thaumicenergistics.init.ModGUIs;
import thaumicenergistics.network.PacketHandler;
import thaumicenergistics.network.packets.PacketOpenGUI;
import thaumicenergistics.part.PartArcaneTerminal;

/**
 * @author BrockWS
 */
public class GuiCraftConfirmBridge extends GuiCraftConfirm {

    private PartArcaneTerminal part;

    public GuiCraftConfirmBridge(InventoryPlayer inventoryPlayer, PartArcaneTerminal part) {
        super(inventoryPlayer, part);
        this.part = part;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.removeIf(Objects::isNull);
        this.buttonList.add(new GuiButton(0, this.guiLeft + 6, this.guiTop + this.ySize - 25, 50, 20, GuiText.Cancel.getLocal()));
    }

    @Override
    protected void actionPerformed(GuiButton btn) throws IOException {
        if (btn.displayString.equals(GuiText.Cancel.getLocal())) {
            PacketHandler.sendToServer(new PacketOpenGUI(ModGUIs.ARCANE_TERMINAL, this.part.getLocation().getPos(), this.part.side));
            return;
        }

        super.actionPerformed(btn);

        if (btn.displayString.equals(GuiText.Start.getLocal())) {
            PacketHandler.sendToServer(new PacketOpenGUI(ModGUIs.ARCANE_TERMINAL, this.part.getLocation().getPos(), this.part.side));
        }
    }
}
