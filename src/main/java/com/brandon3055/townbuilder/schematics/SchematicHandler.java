package com.brandon3055.townbuilder.schematics;

import com.brandon3055.townbuilder.utills.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

/**
 * Created by Brandon on 21/02/2015.
 */
public class SchematicHandler {

    private static String savePath;
    private static File saveFolder;

    public static void init(FMLPreInitializationEvent event) {
        savePath = event.getModConfigurationDirectory().getParentFile().getAbsolutePath() + "/mods/townbuilder";
    }

    public static File getSaveFolder() {
        if (saveFolder == null) {
            saveFolder = new File(savePath);
        }
        if (!saveFolder.exists()) saveFolder.mkdir();

        return saveFolder;
    }

    public static NBTTagCompound loadCompoundFromFile(String fileName) throws SchematicException {
        File file = getFile(fileName);
        if (!file.exists()) throw new SchematicException("Schematic dose not exist");

        try {
            LogHelper.info("Reading file [" + file.length() + " bytes]");

            FileInputStream fis = new FileInputStream(file.getCanonicalFile());
            DataInputStream is = new DataInputStream(new GZIPInputStream(fis));

            int type = is.readByte();

            String name = is.readUTF();

            if (!name.equals("Schematic")) throw new SchematicException("Invalid Schematic [" + file.getName() + "]");

            fis.close();
            is.close();

            fis = new FileInputStream(file.getCanonicalFile());
            is = new DataInputStream(new GZIPInputStream(fis));

            NBTTagCompound c = CompressedStreamTools.read(is);

            fis.close();
            is.close();
            LogHelper.info("Read Complete");
            return c;
        }
        catch (IOException e) {
            if (e instanceof ZipException) {
                try {
                    NBTTagCompound c = CompressedStreamTools.read(file);
                    c.setBoolean("UseOldLoader", true);
                    return c;
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else e.printStackTrace();
        }
        throw new SchematicException("Failed to read schematic. Unknown error");
    }

    public static File getFile(String fileName) {
        File file = new File(getSaveFolder(), fileName + ".schematic");
        if (!file.exists()) return null;
        return file;
    }

    public static void saveCompoundToFile(NBTTagCompound compound, String fileName) {
        if (compound == null) return;

        File schematicFile = new File(getSaveFolder(), fileName + ".schematic");
        try {
            LogHelper.info("Writing schematic to file...");
            DataOutputStream os = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(schematicFile)));
            writeTag(compound, os);

            os.close();
            LogHelper.info("Write complete");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeTag(NBTBase nbtBase, DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(nbtBase.getId());

        if (nbtBase.getId() != 0) {
            dataOutput.writeUTF("Schematic");
            try {
                ReflectionHelper.findMethod(NBTBase.class, "write", "func_74734_a", DataOutput.class).invoke(nbtBase, dataOutput);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteCompoundFile(String fileName) {
        File schematicFile = new File(getSaveFolder(), fileName + ".schematic");
        if (schematicFile.exists()) schematicFile.delete();
    }

    public static NBTTagCompound getCompoundForArea(World world, int posX, int posY, int posZ, int width, int height, int length) {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setShort("Width", (short) width);
        compound.setShort("Height", (short) height);
        compound.setShort("Length", (short) length);

        NBTTagList tileList = new NBTTagList();
        NBTTagCompound idNameConversion = new NBTTagCompound();

        byte[] blocks = new byte[width * height * length];
        byte[] addBlocks = null;
        byte[] blockData = new byte[width * height * length];

        Map<Integer, String> idToNameMap = new HashMap<Integer, String>();

        int totalBlocks = width * height * length;

        LogHelper.info("Creating schematic containing " + totalBlocks + " Blocks");

        int i = totalBlocks / 10;
        int blocksCopied = 0;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    BlockPos pos = new BlockPos(x + posX, y + posY, z + posZ);
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    // Save 4096 IDs in an AddBlocks section
                    if (Block.getIdFromBlock(block) > 255) {
                        if (addBlocks == null) { // Lazily create section
                            addBlocks = new byte[(blocks.length >> 1) + 1];
                        }

                        addBlocks[index >> 1] = (byte) (((index & 1) != 0) ? addBlocks[index >> 1] & 0xF0 | (Block.getIdFromBlock(block) >> 8) & 0xF : addBlocks[index >> 1] & 0xF | ((Block.getIdFromBlock(block) >> 8) & 0xF) << 4);
                    }

                    blocks[index] = (byte) Block.getIdFromBlock(block);
                    blockData[index] = (byte) block.getMetaFromState(state);//world.getBlockMetadata(x + posX, y + posY, z + posZ);

                    if (world.getTileEntity(pos) != null) {
                        NBTTagCompound tileCompound = new NBTTagCompound();
                        world.getTileEntity(pos).writeToNBT(tileCompound);
                        tileCompound.setInteger("x", x);
                        tileCompound.setInteger("y", y);
                        tileCompound.setInteger("z", z);
                        tileList.appendTag(tileCompound);
                    }

                    if (!idToNameMap.containsKey(Block.getIdFromBlock(block))) {
                        idToNameMap.put(Block.getIdFromBlock(block), Block.REGISTRY.getNameForObject(block).toString());//GameData.getBlockRegistry().getNameForObject(world.getBlock(x + posX, y + posY, z + posZ)));
                        idNameConversion.setString(String.valueOf(Block.getIdFromBlock(block)), Block.REGISTRY.getNameForObject(block).toString());//GameData.getBlockRegistry().getNameForObject(world.getBlock(x + posX, y + posY, z + posZ)));
                    }

                    blocksCopied++;

                    if (width > 1 && height > 1 && length > 1 && blocksCopied % i == 0)
                        LogHelper.info("Progress: " + (((double) blocksCopied / (double) totalBlocks) * 100D) + "%%");
                }
            }
        }

        compound.setByteArray("Blocks", blocks);
        compound.setByteArray("Data", blockData);
        if (addBlocks != null) compound.setByteArray("AddBlocks", addBlocks);
        compound.setTag("TileEntities", tileList);
        compound.setTag("idConversions", idNameConversion);

        return compound;
    }

    public static void loadAreaFromCompound(NBTTagCompound compound, World world, int posX, int posY, int posZ, boolean copyAir) throws SchematicException {
        if (compound != null && compound.hasKey("UseOldLoader")) {
//            loadAreaFromCompoundOld(compound, world, posX, posY, posZ, copyAir);
            LogHelper.error("The old schematic loader is nolonger supported!");
            return;
        }

        if (!compound.hasKey("Blocks")) throw new SchematicException("Schematic file is missing a \"Blocks\" tag");

        short width = compound.getShort("Width");
        short height = compound.getShort("Height");
        short length = compound.getShort("Length");

        byte[] blockId = compound.getByteArray("Blocks");
        byte[] blockData = compound.getByteArray("Data");
        byte[] addId = new byte[0];
        short[] blocks = new short[blockId.length];

        if (compound.hasKey("AddBlocks")) addId = compound.getByteArray("AddBlocks");

        for (int index = 0; index < blockId.length; index++) {
            if ((index >> 1) >= addId.length) { // No corresponding AddBlocks index
                blocks[index] = (short) (blockId[index] & 0xFF);
            }
            else {
                if ((index & 1) != 0) {
                    blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (short) (blockId[index] & 0xFF));
                }
                else {
                    blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (short) (blockId[index] & 0xFF));
                }
            }
        }

        NBTTagCompound idNameConversion = null;
        if (compound.hasKey("idConversions")) idNameConversion = compound.getCompoundTag("idConversions");

        NBTTagList tileList = compound.getTagList("TileEntities", 10);

        Map<Integer, Block> blockMap = new HashMap<Integer, Block>();


        int totalBlocks = width * height * length;

        LogHelper.info("Pasting schematic containing " + totalBlocks + " Blocks");

        int ii = totalBlocks / 10;
        int blocksCopied = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    BlockPos pos = new BlockPos(posX + x, posY + y, posZ + z);
                    int index = y * width * length + z * width + x;
                    int id = blocks[index];

                    if (!blockMap.containsKey(id)) {
                        Block block = Block.REGISTRY.getObject(new ResourceLocation(idNameConversion.getString(String.valueOf(id))));
                        if (idNameConversion != null && block != null)//GameData.getBlockRegistry().getObject(idNameConversion.getString(String.valueOf(id))) != null)
                        {
                            blockMap.put(id, block);//GameData.getBlockRegistry().getObject(idNameConversion.getString(String.valueOf(id))));
                        }
                        else blockMap.put(id, Block.getBlockById(id));
                    }

                    if (!copyAir && id == 0) continue;

                    Chunk chunk = world.getChunkFromBlockCoords(pos);

                    BlockPos pos2 = new BlockPos((posX + x) & 15, (posY + y), (posZ + z) & 15);
                    if (chunk.getTileEntity(pos2, Chunk.EnumCreateEntityType.CHECK) != null) {
                        chunk.getTileEntity(pos2, Chunk.EnumCreateEntityType.CHECK).invalidate();
                    }
                    chunk.removeInvalidTileEntity(pos2);

                    IBlockState oldState = world.getBlockState(pos);
                    IBlockState newState = blockMap.get(id).getStateFromMeta(blockData[(y * length + z) * width + x]);
                    chunk.setBlockState(pos, newState);
//					chunk.setBlockMetadata((posX + x) & 15, (posY + y), (posZ + z) & 15, blockData[(y * length + z) * width + x]);
//					world.markBlockForUpdate(posX + x, posY + y, posZ + z);TODO This probably needs to happen

                    world.notifyBlockUpdate(pos, oldState, newState, 3);

                    if (world.getTileEntity(pos) != null) {
                        TileEntity tile = world.getTileEntity(pos);
                        if (tileList != null) for (int i = 0; i < tileList.tagCount(); i++) {
                            if (tileList.getCompoundTagAt(i).getInteger("x") == x && tileList.getCompoundTagAt(i).getInteger("y") == y && tileList.getCompoundTagAt(i).getInteger("z") == z) {
                                tile.readFromNBT(tileList.getCompoundTagAt(i));
                            }
                        }
                        tile.setPos(pos);
//						tile.xCoord = posX + x;
//						tile.yCoord = posY + y;
//						tile.zCoord = posZ + z;
                    }

                    blocksCopied++;

                    if (width > 1 && height > 1 && length > 1 && blocksCopied % ii == 0)
                        LogHelper.info("Progress: " + (((double) blocksCopied / (double) totalBlocks) * 100D) + "%%");
                }
            }
        }


    }

    public static String[] getSchematics() {
        String[] s = SchematicHandler.getSaveFolder().list();
        for (int i = 0; i < s.length; i++) {
            if (s[i].contains(".schematic")) s[i] = s[i].substring(0, s[i].lastIndexOf(".schematic"));
        }

        return s;
    }

    public static class SchematicException extends Exception {
        private String msg;

        public SchematicException(String msg) {
            this.msg = msg;
        }

        @Override
        public String getMessage() {
            return msg;
        }
    }
}
