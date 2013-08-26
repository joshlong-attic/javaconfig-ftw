package javaconfigftw.javaconfig;

import javax.sql.DataSource;


public class CustomerService {

    private DataSource dataSource;

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }
}
