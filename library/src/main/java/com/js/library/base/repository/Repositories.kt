package com.js.library.base.repository

open class BaseRepositoryBoth<T : IRemoteDataSource, R : ILocalDataSource>(
    val remote: T,
    val local: R
) : IRepository

open class BaseRepositoryLocal<T : ILocalDataSource>(val source: T) : IRepository

open class BaseRepositoryRemote<T : IRemoteDataSource>(val source: T) : IRepository

open class BaseRepositoryNothing : IRepository

interface IRepository
