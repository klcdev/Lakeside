package juuxel.lakeside.api

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

@Suppress("UnstableApiUsage")
object MoreOverworldBiomes {
    private val smallVariants: Multimap<Biome, SmallVariantEntry> = MultimapBuilder.hashKeys().hashSetValues().build<Biome, SmallVariantEntry>()
    private val islands: Multimap<Biome, SmallVariantEntry> = MultimapBuilder.hashKeys().hashSetValues().build<Biome, SmallVariantEntry>()

    fun addSmallVariant(base: Biome, variant: Biome, chance: Int) {
        smallVariants.put(base, SmallVariantEntry(variant, chance))
    }

    fun transformSmallVariant(base: Biome, random: LayerRandomnessSource): Biome? =
        transform(smallVariants, base, random)

    fun addIsland(base: Biome, variant: Biome, chance: Int) {
        islands.put(base, SmallVariantEntry(variant, chance))
    }

    fun transformIsland(base: Biome, random: LayerRandomnessSource): Biome? = transform(islands, base, random)

    private fun transform(
        variants: Multimap<Biome, SmallVariantEntry>, base: Biome, random: LayerRandomnessSource
    ): Biome? {
        if (variants.containsKey(base)) {
            val entries = variants[base]
            val bound = entries.sumBy { it.chance }
            val i = random.nextInt(bound)
            var offset = 0

            for (entry in entries) {
                if (i == offset) return entry.variant
                offset += entry.chance
            }
        }

        return null
    }

    private data class SmallVariantEntry(val variant: Biome, val chance: Int)
}
