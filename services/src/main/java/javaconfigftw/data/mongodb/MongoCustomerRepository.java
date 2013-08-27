package javaconfigftw.data.mongodb;


import java.io.InputStream;
import java.math.BigInteger;

public interface MongoCustomerRepository {
    void storeProfilePhoto(BigInteger customerId, InputStream bytes);

    InputStream readProfilePhoto(BigInteger customerId);
}
