package favodelix;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface DataDao {

	public List<Data> select() throws DataAccessException;
	public int insert(Data data) throws DataAccessException;
	public Data search(Data data) throws DataAccessException;
	public int update(Data data) throws DataAccessException;
	public int delete(Data data) throws DataAccessException;
	public int count() throws DataAccessException;


}
