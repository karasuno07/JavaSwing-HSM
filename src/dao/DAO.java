package dao;

import java.util.List;

public interface DAO<E> {
	
	boolean insert(E entity);

	boolean update(E oldEntity, E newEntity);

	boolean delete(E entity);

	List<E> selectAll();

	List<E> select(String SQL, Object key);

	List<E> selectBySQL(String sql, Object... args);
	
}
