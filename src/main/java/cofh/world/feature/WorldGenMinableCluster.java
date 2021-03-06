package cofh.world.feature;

import cofh.api.world.WeightedRandomBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMinableCluster extends WorldGenerator {

	public static final List<WeightedRandomBlock> fabricateList(WeightedRandomBlock resource) {

		List<WeightedRandomBlock> list = new ArrayList<WeightedRandomBlock>();
		list.add(resource);
		return list;
	}

	private final List<WeightedRandomBlock> cluster;
	private final int genClusterSize;
	private Block genBlock;

	public WorldGenMinableCluster(ItemStack ore, int clusterSize) {

		this(new WeightedRandomBlock(ore), clusterSize);
	}

	public WorldGenMinableCluster(WeightedRandomBlock resource, int clusterSize) {

		this(fabricateList(resource), clusterSize);
	}

	public WorldGenMinableCluster(List<WeightedRandomBlock> resource, int clusterSize) {

		this(resource, clusterSize, Blocks.stone);
	}

	public WorldGenMinableCluster(ItemStack ore, int clusterSize, Block block) {

		this(new WeightedRandomBlock(ore, 1), clusterSize, block);
	}

	public WorldGenMinableCluster(WeightedRandomBlock resource, int clusterSize, Block block) {

		this(fabricateList(resource), clusterSize, block);
	}

	public WorldGenMinableCluster(List<WeightedRandomBlock> resource, int clusterSize, Block block) {

		cluster = resource;
		genClusterSize = clusterSize;
		genBlock = block;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {

		float f = rand.nextFloat() * (float) Math.PI;
		double d0 = x + 8 + MathHelper.sin(f) * genClusterSize / 8.0F;
		double d1 = x + 8 - MathHelper.sin(f) * genClusterSize / 8.0F;
		double d2 = z + 8 + MathHelper.cos(f) * genClusterSize / 8.0F;
		double d3 = z + 8 - MathHelper.cos(f) * genClusterSize / 8.0F;
		double d4 = y + rand.nextInt(3) - 2;
		double d5 = y + rand.nextInt(3) - 2;

		for (int l = 0; l <= genClusterSize; ++l) {
			double d6 = d0 + (d1 - d0) * l / genClusterSize;
			double d7 = d4 + (d5 - d4) * l / genClusterSize;
			double d8 = d2 + (d3 - d2) * l / genClusterSize;
			double d9 = rand.nextDouble() * genClusterSize / 16.0D;
			double d10 = (MathHelper.sin(l * (float) Math.PI / genClusterSize) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.sin(l * (float) Math.PI / genClusterSize) + 1.0F) * d9 + 1.0D;
			int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
			int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
			int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
			int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
			int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
			int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

			for (int k2 = i1; k2 <= l1; ++k2) {
				double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);

				if (d12 * d12 < 1.0D) {
					for (int l2 = j1; l2 <= i2; ++l2) {
						double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);

						if (d12 * d12 + d13 * d13 < 1.0D) {
							for (int i3 = k1; i3 <= j2; ++i3) {
								double d14 = (i3 + 0.5D - d8) / (d10 / 2.0D);
								Block block = world.getBlock(k2, l2, i3);

								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && block != null && block.isReplaceableOreGen(world, k2, l2, i3, genBlock)) {
									WeightedRandomBlock ore = (WeightedRandomBlock) WeightedRandom.getRandomItem(world.rand, cluster);
									world.setBlock(k2, l2, i3, ore.block, ore.metadata, 0);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

}
