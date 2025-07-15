package com.baghdad.domain.exception

open class AppException() : Exception()
open class NetworkException() : AppException()
open class LocalDataBaseException() : AppException()


class NoInternetException() : NetworkException()
class UnKnownNetworkException() : NetworkException()
class StorageFullException() : LocalDataBaseException()
