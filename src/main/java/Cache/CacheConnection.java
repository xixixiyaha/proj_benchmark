package Cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class CacheConnection {

    public static final String URL = "123";
    public static final String IP = "123";
    public static final Integer PORT = 1234;
    public static final String PASSWORD = "123";

    public static JedisPool pool = null;

    public static void initializePool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
        if (pool==null){
            synchronized (CacheConnection.class){
                if(pool==null){
                    pool = new JedisPool(config,IP,PORT);
                }
            }
        }
    }

    public Jedis getResource(){
        try {
            Jedis jedis = pool.getResource();
            return jedis;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void releaseResource(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

    public void releasePool(){
        if(pool!=null){
            synchronized (CacheConnection.class){
                if(pool!= null){
                    pool.close();
                }
            }
        }
    }



}
