package javaconfigftw.xml;

import javax.sql.DataSource;

public class CustomerService {
    private DataSource dataSource;
    public CustomerService() {
        System.out.println("Starting customerService.");
    }
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }
}
