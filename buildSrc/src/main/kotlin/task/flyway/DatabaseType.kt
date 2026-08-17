package task.flyway

enum class DatabaseType {
    NONE,
    APP,
    BATCH;

    companion object {
        private const val DB_TYPE_KEY = "db.type"

        fun init(): DatabaseType {
            return System.getProperty(DB_TYPE_KEY)?.let {
                valueOf(it.uppercase())
            } ?: NONE
        }
    }
}