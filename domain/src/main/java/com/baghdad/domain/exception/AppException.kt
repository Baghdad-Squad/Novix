package com.baghdad.domain.exception

open class AppException() : Exception()
open class NetworkException() : AppException()
open class LocalDataBaseException() : AppException()


class NoInternetException() : NetworkException()
class SerializationException() : NetworkException()
class RequestTimeoutException() : NetworkException()
class TooManyRequestsException() : NetworkException()
class ServerException() : NetworkException()
class NotFoundSearchException() : NetworkException()

class DataBaseCorruptException() : LocalDataBaseException()
class StorageFullException() : LocalDataBaseException()
class NotFoundException() : LocalDataBaseException()
