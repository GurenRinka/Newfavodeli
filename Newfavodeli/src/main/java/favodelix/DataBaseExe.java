package favodelix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.google.api.services.youtube.model.SearchResult;

@Service
public class DataBaseExe extends JdbcDaoSupport implements DataDao{

	//SQL文
	private final String selectSql ="SELECT number,channelid,channelname FROM channel";
	private final String searchSql = selectSql + " WHERE number = ?";
	private final String countSql ="SELECT count(*) FROM channel";
	private String deleteSql = "DELETE FROM channel WHERE number = ?";
	private final String insertSql = "INSERT INTO channel(channelid, channelname) VALUES(?,?)";
	private final String updateSql = "UPDATE channel SET channelid=?,channelname=? WHERE number = ?";


	@Autowired
	private DataSource  dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	//宣言
	YoutubeApp youtubeApp = new YoutubeApp();
	Data data = new Data();
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;

	public DataBaseExe() {
	}

	@Override
	public List<Data> select() throws DataAccessException {
		RowMapper<Data> rowMapper= (rs,rowNum)->{
			Data item = new Data(
					rs.getString("number"),
					rs.getString("channelid"),
					rs.getString("channelname"));
			return item;
		};
		return getJdbcTemplate().query(selectSql, rowMapper);
	}

	public String[] selectYTDT(String channelID){
		YoutubeApp YTAP = new YoutubeApp();
		String oneData[] = new String[3];
		List<SearchResult> channelIDList =YTAP.youtubelive(channelID);
		oneData =  youtubeApp.oneVideoData(channelIDList.iterator());

		return oneData;

	}

	@Override
	public int insert(Data data) throws DataAccessException {
		Object[] param = {data.getChannelid(),data.getChannelname()};
		return getJdbcTemplate().update(insertSql, param);
	}

	@Override
	public Data search(Data data) throws DataAccessException {

		RowMapper<Data> rowMapper= (rs,rowNum)->{
			Data item = new Data(
					rs.getString("number"),
					rs.getString("channelid"),
					rs.getString("channelname"));
			return item;
		};
		Object[] param = {data.getNumber()};
		return (Data) getJdbcTemplate().query(searchSql, param, rowMapper).get(0);

	}

	@Override
	public int update(Data data) throws DataAccessException {
		Object[] param = {
				data.getChannelid(),data.getChannelname(),data.getNumber()
		};
		return getJdbcTemplate().update(updateSql,param);
	}

	@Override
	public int delete(Data data) throws DataAccessException {
		Object[] param = {data.getNumber()};
		return getJdbcTemplate().update(deleteSql,param);
	}

	@Override
	public int count() throws DataAccessException {
		int YouSo = 0;
		YouSo = getJdbcTemplate().queryForObject(countSql,Integer.class);
		return YouSo;
	}//count

}