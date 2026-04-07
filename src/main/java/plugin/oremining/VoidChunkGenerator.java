package plugin.oremining;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jspecify.annotations.NonNull;

public class VoidChunkGenerator extends ChunkGenerator {

  @Override
  public void generateNoise(@NonNull WorldInfo worldInfo, java.util.@NonNull Random random,
      int chunkX, int chunkZ, @NonNull ChunkData chunkData) {
  }
}
