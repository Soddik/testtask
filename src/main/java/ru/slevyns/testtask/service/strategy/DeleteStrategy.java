package ru.slevyns.testtask.service.strategy;

/**
 * Deletion strategy
 */
public interface DeleteStrategy {
    /**
     * Method for deletion rows from a table
     *
     * @param tableName table name
     * @param dateTime  date older than which rows will be deleted
     */
    void delete(String tableName, String dateTime);

    /**
     * Compares the percentage of records to be removed from the table with the optimal percentage for the selected strategy.
     *
     * @param percent percentage of records to be deleted
     * @return true if the strategy matches the parameters, otherwise false
     */
    boolean isOptimal(double percent);

    /**
     * Returns type of selected strategy
     * @return strategy type string value
     */
    String getType();
}
