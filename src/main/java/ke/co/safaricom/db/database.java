package ke.co.safaricom.db;

import org.sql2o.Sql2o;

public class database {
    private static final Sql2o connection = new Sql2o(

            "jdbc:postgresql://localhost:5432/hero_squad",
            "postgres",
            "IloveJesus2023"
    );
    public static Sql2o getConnect() { return connection; }
}
