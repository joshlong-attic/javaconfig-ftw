
package io.spring.presentations.jcmm;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomService {

	private DataSource dataSource;

	private String message;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void run() {
		try {
			System.out.println("\n\n"
					+ this.message
					+ " : "
					+ (this.dataSource == null ? "No DataSource"
							: this.dataSource.getConnection().getMetaData().getDatabaseProductName())
					+ "\n\n");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
