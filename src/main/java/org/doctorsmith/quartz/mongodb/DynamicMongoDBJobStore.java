package org.doctorsmith.quartz.mongodb;


import com.mongodb.MongoClient;
import org.quartz.spi.JobStore;

public class DynamicMongoDBJobStore extends MongoDBJobStore implements JobStore {

    public DynamicMongoDBJobStore() {
        super();
    }

    public DynamicMongoDBJobStore(MongoClient mongo) {
        super(mongo);
    }

    public DynamicMongoDBJobStore(final String mongoUri, final String username, final String password) {
        super(mongoUri, username, password);
    }

    @Override
    protected ClassLoader getJobClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
