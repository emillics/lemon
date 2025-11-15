import com.drew.imaging.avi.AviMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteExample {
//    public static void main(String[] args) {
//        // SQLite数据库文件路径
//        String dbFile = System.getProperty("user.dir") + File.separator + "db/test.db";
//
//        // 创建连接
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
//
//            // 创建Statement来执行SQL语句
//            Statement statement = connection.createStatement();
//
//            // 创建表
//            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS users (" +
//                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    "username TEXT NOT NULL, " +
//                    "email TEXT NOT NULL UNIQUE)";
//            statement.executeUpdate(sqlCreateTable);
//
//            System.out.println("Table users created successfully");
//
//            // 关闭Statement
//            statement.close();
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir") + File.separator + "update");

        File file = new File("/Users/whl/Downloads/下载 (1)");
        try {
            Metadata metadata = Mp4MetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag.getTagName() + "=" + tag.getDescription());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

