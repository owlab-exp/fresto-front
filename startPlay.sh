JAVA_OPTS="$JAVA_OPTS -Xms512m"
JAVA_OPTS="$JAVA_OPTS -Xmx2048m"

# reduce the per-thread stack size
JAVA_OPTS="$JAVA_OPTS -Xss128k"

# Force the JVM to use IPv4 stack
 JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"

# Enable aggressive optimizations in the JVM
#    - Disabled by default as it might cause the JVM to crash
 JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"

JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
JAVA_OPTS="$JAVA_OPTS -XX:SurvivorRatio=8"
JAVA_OPTS="$JAVA_OPTS -XX:MaxTenuringThreshold=1"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"

# GC logging options -- uncomment to enable
# JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
# JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
# JAVA_OPTS="$JAVA_OPTS -XX:+PrintClassHistogram"
# JAVA_OPTS="$JAVA_OPTS -XX:+PrintTenuringDistribution"
# JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
 JAVA_OPTS="$JAVA_OPTS -Xloggc:logs/gc.log"

# Causes the JVM to dump its heap on OutOfMemory.
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
# The path to the heap dump location, note directory must exists and have enough
# space for a full heap dump.
#JAVA_OPTS="$JAVA_OPTS -XX:HeapDumpPath=logs/heapdump.hprof"

play "start -Dhttp.port=9999"
