package com.brandon3055.townbuilder.schematics;

import com.brandon3055.townbuilder.utills.LogHelper;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brandon on 21/02/2015.
 */
public class SchematicHandler {

	private static String savePath;
	private static File saveFolder;

	public static void init(FMLPreInitializationEvent event)
	{
		savePath = event.getModConfigurationDirectory().getParentFile().getAbsolutePath() + "/mods/townbuilder";
	}

	public static File getSaveFolder()
	{
		if (saveFolder == null) { saveFolder = new File(savePath); }
		if (!saveFolder.exists()) saveFolder.mkdir();

		return saveFolder;
	}

	public static NBTTagCompound loadCompoundFromFile(String fileName) {
		File file = new File(getSaveFolder(), fileName + ".schematic");
		if (!file.exists()) return null;

		try { return CompressedStreamTools.read(file); }
		catch (IOException e) { e.printStackTrace(); }
		return null;
	}

	public static File getFile(String fileName) {
		File file = new File(getSaveFolder(), fileName + ".schematic");
		if (!file.exists()) return null;
		return file;
	}

	public static void saveCompoundToFile(NBTTagCompound compound, String fileName)
	{
		if (compound == null) return;
		File schematicFile = new File(getSaveFolder(), fileName + ".schematic");
		try
		{
			LogHelper.info("Writing schematic to file. This may take a moment for large schematics...");
			CompressedStreamTools.write(compound, schematicFile);
			LogHelper.info("Write schematic");
		}
		catch (IOException e) {	e.printStackTrace(); }
	}

	public static void deleteCompoundFile(String fileName)
	{
		File schematicFile = new File(getSaveFolder(), fileName + ".schematic");
		if (schematicFile.exists()) schematicFile.delete();
	}

	public static NBTTagCompound getCompoundForArea(World world, int x, int y, int z, int xSize, int ySize, int zSize)
	{
		NBTTagCompound compound = new NBTTagCompound();

		compound.setShort("Width", (short)xSize);
		compound.setShort("Height", (short)ySize);
		compound.setShort("Length", (short)zSize);

		NBTTagList tileList = new NBTTagList();
		NBTTagCompound idNameConversion = new NBTTagCompound();

		int[] ids = new int[xSize * ySize * zSize];
		byte[] metta = new byte[xSize * ySize * zSize];

		Map<Integer, String> idToNameMap = new HashMap<Integer, String>();

		int totalBlocks = xSize * ySize * zSize;

		LogHelper.info("Creating schematic containing " + totalBlocks + " Blocks");

		int i = totalBlocks / 10;
		int blocksCopied = 0;

		for (int ry = 0; ry < ySize; ry++)
		{
			for (int rz = 0; rz < zSize; rz++)
			{
				for (int rx = 0; rx < xSize; rx++)
				{
					int id = Block.getIdFromBlock(world.getBlock(x + rx, y + ry, z + rz));

					ids[(ry * zSize + rz) * xSize + rx] = id;
					metta[(ry * zSize + rz) * xSize + rx] = (byte)world.getBlockMetadata(x + rx, y + ry, z + rz);

					if (world.getTileEntity(x + rx, y + ry, z + rz) != null) {
						NBTTagCompound tileCompound = new NBTTagCompound();
						world.getTileEntity(x + rx, y + ry, z + rz).writeToNBT(tileCompound);
						tileCompound.setInteger("x", rx);
						tileCompound.setInteger("y", ry);
						tileCompound.setInteger("z", rz);
						tileList.appendTag(tileCompound);
					}

					if (!idToNameMap.containsKey(id)) {
						idToNameMap.put(id, GameData.getBlockRegistry().getNameForObject(world.getBlock(x + rx, y + ry, z + rz)));
						idNameConversion.setString(String.valueOf(id), GameData.getBlockRegistry().getNameForObject(world.getBlock(x + rx, y + ry, z + rz)));
					}

					blocksCopied++;

					if (blocksCopied % i == 0) LogHelper.info("Progress: " + (((double) blocksCopied / (double) totalBlocks) * 100D) + "%%");

				}
			}
		}

		LogHelper.info("Schematic created");

		compound.setTag("TileEntities", tileList);
		compound.setTag("idConversions", idNameConversion);
		compound.setTag("Ids", new NBTTagIntArray(ids));
		compound.setTag("Metta", new NBTTagByteArray(metta));

		return compound;
	}

	public static void loadAreaFromCompound(NBTTagCompound compound, World world, int x, int y, int z, boolean copyAir)
	{
		int xSize = compound.getShort("Width");
		int ySize = compound.getShort("Height");
		int zSize = compound.getShort("Length");

		NBTTagCompound idNameConversion = compound.getCompoundTag("idConversions");
		if (idNameConversion == null) return;

		NBTTagList tileList = compound.getTagList("TileEntities", 10);

		Map<Integer, Block> blockMap = new HashMap<Integer, Block>();

		int[] ids = compound.getIntArray("Ids");
		if (ids.length < xSize * ySize + zSize) {
			LogHelper.error("invalid id array " + ids.length);
			return;
		}

		byte[] metta = compound.getByteArray("Metta");
		if (metta.length < xSize * ySize + zSize) {
			LogHelper.error("invalid metta array " + metta.length);
			return;
		}

		for (int ry = 0; ry < ySize; ry++)
		{
			for (int rz = 0; rz < zSize; rz++)
			{
				for (int rx = 0; rx < xSize; rx++)
				{
					int id = ids[(ry * zSize + rz) * xSize + rx];
					if (!blockMap.containsKey(id))
					{
						if (GameData.getBlockRegistry().getObject(idNameConversion.getString(String.valueOf(id))) != null)
						{
							blockMap.put(id, GameData.getBlockRegistry().getObject(idNameConversion.getString(String.valueOf(id))));
						}
						else continue;
					}

					if (!copyAir && id == 0) continue;

					//if (world.getTileEntity(x + rx, y + ry, z + rz) != null) world.removeTileEntity(x + rx, y + ry, z + rz);

					Chunk chunk = world.getChunkFromBlockCoords(x + rx, z + rz);

					if (chunk.getTileEntityUnsafe((x + rx) & 15, (y + ry) , (z + rz) & 15) != null)
					{
						chunk.getTileEntityUnsafe((x + rx) & 15, (y + ry) , (z + rz) & 15).invalidate();
					}
					chunk.removeInvalidTileEntity((x + rx) & 15, (y + ry) , (z + rz) & 15);

					chunk.func_150807_a((x + rx) & 15, (y + ry) , (z + rz) & 15, blockMap.get(id), metta[(ry * zSize + rz) * xSize + rx]);
					chunk.setBlockMetadata((x + rx) & 15, (y + ry), (z + rz) & 15, metta[(ry * zSize + rz) * xSize + rx]);
					world.markBlockForUpdate(x + rx, y + ry, z + rz);

					if (world.getTileEntity(x + rx, y + ry, z + rz) != null)
					{
						TileEntity tile = world.getTileEntity(x + rx, y + ry, z + rz);
						if (tileList != null)
						for (int i = 0; i < tileList.tagCount(); i ++)
						{
							if (tileList.getCompoundTagAt(i).getInteger("x") == rx && tileList.getCompoundTagAt(i).getInteger("y") == ry && tileList.getCompoundTagAt(i).getInteger("z") == rz) {
								tile.readFromNBT(tileList.getCompoundTagAt(i));
							}
						}
						tile.xCoord = x + rx;
						tile.yCoord = y + ry;
						tile.zCoord = z + rz;
					}
				}
			}
		}
	}

	public static String[] getSchematics()
	{
		String[] s = SchematicHandler.getSaveFolder().list();
		for (int i = 0; i < s.length; i ++)
		{
			if (s[i].contains(".schematic")) s[i] = s[i].substring(0, s[i].lastIndexOf(".schematic"));
		}

		return s;
	}
}
