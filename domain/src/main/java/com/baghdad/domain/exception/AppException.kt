package com.baghdad.domain.exception

open class AppException() : Exception()
open class NetworkException() : AppException()
open class LocalDataBaseException() : AppException()

class UnknownException() : AppException()
class UnAuthorizedException() : NetworkException()
class NoInternetException() : NetworkException()
class UnKnownNetworkException() : NetworkException()
class StorageFullException() : LocalDataBaseException()
