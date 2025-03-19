package main.dao;


import java.sql.SQLException;  // Importing the SQLException class to handle potential database interaction issues.


public interface SystemConfigDAO
{
    int findConfigValue(String configKey) throws SQLException;
    void updateConfigValue(String configKey, int configValue) throws SQLException;
}
