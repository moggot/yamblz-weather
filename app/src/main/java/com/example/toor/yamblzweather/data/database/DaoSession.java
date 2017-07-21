package com.example.toor.yamblzweather.data.database;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig weatherDaoConfig;

    private final WeatherDao weatherDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        weatherDaoConfig = daoConfigMap.get(WeatherDao.class).clone();
        weatherDaoConfig.initIdentityScope(type);

        weatherDao = new WeatherDao(weatherDaoConfig, this);

        registerDao(Weather.class, weatherDao);
    }
    
    public void clear() {
        weatherDaoConfig.clearIdentityScope();
    }

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

}
