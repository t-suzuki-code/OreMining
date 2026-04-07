package plugin.oremining.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import plugin.oremining.mapper.data.PlayerScore;

public interface PlayerScoreMapper {

  @Select("select * from oremining_player_score ORDER BY score DESC LIMIT 10")
  List<PlayerScore> selectList();


  @Insert("insert INTO oremining_player_score(player_name, score, registered_at) "
      + "values(#{playerName}, #{score}, now())")
  void insert(PlayerScore playerScore);
}