package plugin.oremining;


import java.io.InputStream;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import plugin.oremining.mapper.PlayerScoreMapper;
import plugin.oremining.mapper.data.PlayerScore;

/**
 * DB接続やそれに付随する登録や更新処理を行うクラスです。
 */
public class DBManager {

  private final SqlSessionFactory sqlSessionFactory;

  public DBManager() {
    try {
      InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * プレイヤースコアテーブルから一覧でスコア情報を取得する。
   *
   * @return スコア情報の一覧。
   */
  public List<PlayerScore> selectList() {
    try (SqlSession session = sqlSessionFactory.openSession(true)) {
      return session.getMapper(PlayerScoreMapper.class).selectList();
    }
  }

  /**
   * プレイヤーテーブルにスコア情報を登録する。
   *
   * @param playerScore プレイヤースコア
   */
  public void insert(PlayerScore playerScore) {
    try (SqlSession session = sqlSessionFactory.openSession(true)) {
      session.getMapper(PlayerScoreMapper.class).insert(playerScore);
    }
  }
}
