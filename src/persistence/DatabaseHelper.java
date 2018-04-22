package persistence;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.postgresql.Driver;

public class DatabaseHelper<T> {
	private String jdbcURL;
	private String username;
	private String password;

	public static final String CAR_FACILITY_DB_URL = "jdbc:postgresql://localhost:5432/car_facility_system";
	public static final String POSTGRES_USERNAME = "postgres";
	public static final String POSTGRES_PASSWORD = "password";

	public DatabaseHelper(String jdbcURL, String username, String password) throws RemoteException {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			throw new RemoteException("No JDBC driver", e);
		}
	}

	public DatabaseHelper(String jdbcURL) throws RemoteException {
		this(jdbcURL, null, null);
	}

	protected Connection getConnection() throws SQLException {
		if (username == null) {
			return DriverManager.getConnection(jdbcURL);
		} else {
			return DriverManager.getConnection(jdbcURL, username, password);
		}
	}

	private PreparedStatement prepare(Connection connection, String sql, Object[] parameters) throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sql);
		for (int i = 0; i < parameters.length; i++) {
			stat.setObject(i + 1, parameters[i]);
		}
		return stat;
	}

	public int executeUpdate(String sql, Object... parameters) throws RemoteException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			return stat.executeUpdate();
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	private PreparedStatement prepareReturningId(Connection connection, String sql, Object[] parameters)
			throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		for (int i = 0; i < parameters.length; i++) {
			stat.setObject(i + 1, parameters[i]);
		}
		return stat;
	}

	public int executeUpdateReturningId(String sql, Object... parameters) throws RemoteException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepareReturningId(connection, sql, parameters);

			stat.executeUpdate();

			// retrieve the generated primary key
			try (ResultSet output = stat.getGeneratedKeys()) {

				if (!output.next()) return -1; // nothing is returned

				return output.getInt(1);
			}
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public T mapSingle(DataMapper<T> mapper, String sql, Object... parameters) throws RemoteException {
		try (Connection con = getConnection()) {
			PreparedStatement stat = prepare(con, sql, parameters);
			ResultSet rs = stat.executeQuery();
			if (rs.next()) {
				return mapper.create(rs);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public List<T> map(DataMapper<T> mapper, String sql, Object... parameters) throws RemoteException {
		try (Connection con = getConnection()) {
			PreparedStatement stat = prepare(con, sql, parameters);
			ResultSet rs = stat.executeQuery();
			LinkedList<T> allObjects = new LinkedList<>();
			while (rs.next())
				allObjects.add(mapper.create(rs));
			rs.close();
			return allObjects;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
