package net.visualillusionsent.spawnersplus;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Collections;
import java.util.List;

/**
 * Copyright (C) 2015 Visual Illusions Entertainment
 * All Rights Reserved.
 *
 * @author Jason Jones (darkdiplomat)
 */
public class SetSpawnerCommand extends CommandBase {
    private final SpawnersPlus reference;

    SetSpawnerCommand(SpawnersPlus reference) {
        this.reference = reference;
    }

    @Override
    public String getName() {
        return "setspawner";
    }

    public int getRequiredPermissionLevel() {
        return 1;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "setspawner <Mob{data}...>";
    }

    @Override
    public List getAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Invalid arguments").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }
        doSpawnerSetting(sender, args);
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender sender) {
        // TODO: Actual Permissions Setup?
        return sender.getCommandSenderEntity() != null && sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? func_175762_a(args, reference.allowedMobSet()) : null;
    }

    private void doSpawnerSetting(ICommandSender sender, String[] args) {
        // mob{extra} mob{extra}... ???

        // Step 1) Find the Spawner
        MovingObjectPosition mop = rayTrace((EntityPlayer)sender.getCommandSenderEntity(), 200);
        IBlockState state = sender.getEntityWorld().getBlockState(mop.getBlockPos());
        if (state.getBlock() instanceof BlockMobSpawner) {
            // Spawner Found
            // Step 2) Instance the spawner
            TileEntityMobSpawner mobSpawner = (TileEntityMobSpawner)sender.getEntityWorld().getTileEntity(mop.getBlockPos());
            MobSpawnerBaseLogic spawnerLogic = mobSpawner.getSpawnerBaseLogic();

            if (args.length == 1) {
                // Just a simple change
                String mobName = WordUtils.capitalizeFully(args[0]); // Adjust case to match Mojang internal
                if (reference.allowedMobSet().contains(mobName)) {
                    spawnerLogic.setEntityName(mobName);
                    doUpdate(mobSpawner);
                    sender.addChatMessage(new ChatComponentText("Mob Spawner now set to " + args[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                    return;
                }
                sender.addChatMessage(new ChatComponentText("ACCESS DENIED!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                return;
            }
            // TODO data structure

            sender.addChatMessage(new ChatComponentText("This is a test, this is only a test. Looking at MobSpawner.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
            return;
        }
        sender.addChatMessage(new ChatComponentText("Must be looking at MobSpawner...").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }

    private MovingObjectPosition rayTrace(EntityPlayer entity, double distance) {
        Vec3 vec3 = new Vec3(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
        Vec3 vec31 = entity.getLook(1.0f);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        return entity.getEntityWorld().rayTraceBlocks(vec3, vec32, false, true, false);
    }

    private void doUpdate(TileEntityMobSpawner mobSpawner) {
        mobSpawner.getWorld().markBlockForUpdate(mobSpawner.getPos()); // Makes the server call getDescriptionPacket for a full data sync
        mobSpawner.markDirty(); // Marks the chunk as dirty, so that it is saved properly on changes. Not required for the sync specifically, but usually goes alongside the former.
    }
}
