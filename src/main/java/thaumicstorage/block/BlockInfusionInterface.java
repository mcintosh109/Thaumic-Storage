package thaumicenergistics.block;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import thaumicstorage.api.storage.IAEEssentiaStack;
import thaumicstorage.client.render.IThEModel;
import thaumicstorage.tile.TileInfusionProvider;



Public class InterfaceBlock extends NetworkNodeBlock {
    public InterfaceBlock() {
        super(BlockUtils.DEFAULT_ROCK_PROPERTIES);

        this.setRegistryName(RS.ID, "Essintia interface");
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InterfaceTile();
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote || hand != EnumHand.MAIN_HAND)
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileInfusionProvider) {
            TileInfusionProvider inf = (TileInfusionProvider) te;
            if (player.isSneaking()) {
                if (inf.getStoredAspects() != null && !inf.getStoredAspects().isEmpty()) {
                    player.sendMessage(new TextComponentString("Stored Aspects:"));
                    for (IAEEssentiaStack stack : inf.getStoredAspects())
                        player.sendMessage(new TextComponentString(stack.getAspect().getName() + " = " + stack.getStackSize()));
                } else {
                    player.sendMessage(new TextComponentString("No aspects found"));
                }
            }
    }